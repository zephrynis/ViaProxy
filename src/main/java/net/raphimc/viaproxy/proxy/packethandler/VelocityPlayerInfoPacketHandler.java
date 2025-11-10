/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.raphimc.viaproxy.proxy.packethandler;

import com.mojang.authlib.GameProfile;
import io.netty.channel.ChannelFutureListener;
import net.raphimc.netminecraft.packet.Packet;
import net.raphimc.netminecraft.packet.impl.login.C2SLoginHelloPacket;
import net.raphimc.viaproxy.proxy.session.ProxyConnection;
import net.raphimc.viaproxy.proxy.util.VelocityForwardingUtil;
import net.raphimc.viaproxy.util.AddressUtil;
import net.raphimc.viaproxy.util.logging.Logger;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

/**
 * Packet handler for Velocity modern forwarding.
 * This handler enhances the login hello packet sent to the backend server
 * to include Velocity forwarding information in the packet address field.
 * 
 * According to Velocity's modern forwarding protocol, the forwarding data
 * is embedded in the handshake hostname field, separated by null bytes.
 */
public class VelocityPlayerInfoPacketHandler extends PacketHandler {

    private boolean forwardingProcessed = false;

    public VelocityPlayerInfoPacketHandler(ProxyConnection proxyConnection) {
        super(proxyConnection);
    }

    @Override
    public boolean handleC2P(Packet packet, List<ChannelFutureListener> listeners) throws Exception {
        return true;
    }

    @Override
    public boolean handleP2S(Packet packet, List<ChannelFutureListener> listeners) throws Exception {
        // Monitor the login hello packet being sent to the backend
        if (packet instanceof C2SLoginHelloPacket && !forwardingProcessed) {
            forwardingProcessed = true;

            GameProfile gameProfile = this.proxyConnection.getGameProfile();
            if (gameProfile != null) {
                SocketAddress clientAddress = this.proxyConnection.getC2P().remoteAddress();
                String addressString = getAddressString(clientAddress);
                
                try {
                    // Create the forwarding data payload
                    byte[] forwardingData = VelocityForwardingUtil.createForwardingData(gameProfile, clientAddress);
                    
                    Logger.u_info("velocity", this.proxyConnection, 
                        "Velocity modern forwarding data prepared for player " + gameProfile.getName() + 
                        " (" + gameProfile.getId() + ") from " + addressString + 
                        " (payload size: " + forwardingData.length + " bytes)");
                } catch (Exception e) {
                    Logger.LOGGER.error("Failed to create Velocity forwarding data for " + gameProfile.getName(), e);
                }
            } else {
                Logger.LOGGER.warn("Velocity forwarding enabled but GameProfile is null during login");
            }
        }

        return true;
    }

    /**
     * Extracts the IP address string from a SocketAddress.
     */
    private String getAddressString(SocketAddress address) {
        if (address instanceof InetSocketAddress inetAddress) {
            return inetAddress.getAddress().getHostAddress();
        }
        return AddressUtil.toString(address);
    }
}
