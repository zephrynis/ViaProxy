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
package net.raphimc.viaproxy.proxy.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.raphimc.viaproxy.ViaProxy;
import net.raphimc.viaproxy.util.logging.Logger;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Utility class for Velocity modern forwarding protocol implementation.
 * Based on the Velocity forwarding specification:
 * https://docs.papermc.io/velocity/player-info-forwarding
 */
public class VelocityForwardingUtil {

    private static final int FORWARDING_VERSION = 1;
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private static byte[] secretKey = null;

    /**
     * Loads the Velocity forwarding secret from the configured file path.
     * If no path is configured, it tries to load from "forwarding.secret" in the current directory.
     * Supports both plain text files and TOML config files (extracts secret = "..." pattern).
     *
     * @return true if the secret was loaded successfully, false otherwise
     */
    public static boolean loadSecret() {
        String secretPath = ViaProxy.getConfig().getVelocitySecret();
        if (secretPath == null || secretPath.isEmpty()) {
            secretPath = "forwarding.secret";
        }

        File secretFile = new File(secretPath);
        if (!secretFile.isAbsolute()) {
            secretFile = new File(ViaProxy.getCwd(), secretPath);
        }
        
        if (!secretFile.exists()) {
            Logger.LOGGER.error("Velocity forwarding secret file not found: " + secretFile.getAbsolutePath());
            Logger.LOGGER.error("Please create the file or disable velocity-player-info-passthrough.");
            return false;
        }

        try {
            String fileContent = new String(Files.readAllBytes(secretFile.toPath()), StandardCharsets.UTF_8);
            String secretString = extractSecret(fileContent);
            
            if (secretString.isEmpty()) {
                Logger.LOGGER.error("Velocity forwarding secret is empty or could not be parsed from: " + secretFile.getAbsolutePath());
                return false;
            }
            
            secretKey = secretString.getBytes(StandardCharsets.UTF_8);
            Logger.LOGGER.info("Loaded Velocity forwarding secret from: " + secretFile.getAbsolutePath());
            return true;
        } catch (IOException e) {
            Logger.LOGGER.error("Failed to read Velocity forwarding secret file: " + secretFile.getAbsolutePath(), e);
            return false;
        }
    }

    /**
     * Extracts the secret from the file content.
     * Supports both plain text format and TOML format (secret = "...").
     *
     * @param content The file content
     * @return The extracted secret
     */
    private static String extractSecret(String content) {
        // Try to parse as TOML first (look for secret = "...")
        String tomlPattern = "secret\\s*=\\s*\"([^\"]+)\"";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(tomlPattern);
        java.util.regex.Matcher matcher = pattern.matcher(content);
        
        if (matcher.find()) {
            return matcher.group(1);
        }
        
        // Fallback: treat as plain text (trim whitespace and newlines)
        return content.trim();
    }

    /**
     * Creates a Velocity modern forwarding plugin message payload.
     * The message format is:
     * - Forwarding version (VarInt)
     * - Player address (String)
     * - Player UUID (UUID)
     * - Player username (String)
     * - Properties count (VarInt)
     * - For each property:
     *   - Name (String)
     *   - Value (String)
     *   - Is signed (Boolean)
     *   - If signed: Signature (String)
     *
     * @param gameProfile The player's game profile with UUID, username, and properties
     * @param clientAddress The player's IP address
     * @return The complete plugin message data with HMAC signature
     */
    public static byte[] createForwardingData(GameProfile gameProfile, SocketAddress clientAddress) {
        if (secretKey == null) {
            throw new IllegalStateException("Velocity forwarding secret not loaded!");
        }

        String addressString = getAddressString(clientAddress);

        // Create the forwarding data
        ByteArrayDataOutput dataOutput = ByteStreams.newDataOutput();
        
        // Write forwarding version
        writeVarInt(dataOutput, FORWARDING_VERSION);
        
        // Write player address
        writeString(dataOutput, addressString);
        
        // Write player UUID
        UUID uuid = gameProfile.getId();
        dataOutput.writeLong(uuid.getMostSignificantBits());
        dataOutput.writeLong(uuid.getLeastSignificantBits());
        
        // Write player username
        writeString(dataOutput, gameProfile.getName());
        
        // Write properties
        writeVarInt(dataOutput, gameProfile.getProperties().size());
        for (Property property : gameProfile.getProperties().values()) {
            writeString(dataOutput, property.name());
            writeString(dataOutput, property.value());
            
            String signature = property.signature();
            if (signature != null) {
                dataOutput.writeBoolean(true);
                writeString(dataOutput, signature);
            } else {
                dataOutput.writeBoolean(false);
            }
        }

        byte[] data = dataOutput.toByteArray();

        // Compute HMAC-SHA256 signature
        byte[] signature = computeHmac(data);

        // Combine signature + data
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.write(signature);
        output.write(data);

        return output.toByteArray();
    }

    /**
     * Computes the HMAC-SHA256 signature of the given data using the loaded secret key.
     *
     * @param data The data to sign
     * @return The HMAC signature
     */
    private static byte[] computeHmac(byte[] data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey, HMAC_ALGORITHM);
            mac.init(keySpec);
            return mac.doFinal(data);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to compute HMAC signature for Velocity forwarding", e);
        }
    }

    /**
     * Extracts the IP address string from a SocketAddress.
     *
     * @param address The socket address
     * @return The IP address as a string (without port)
     */
    private static String getAddressString(SocketAddress address) {
        if (address instanceof InetSocketAddress inetAddress) {
            return inetAddress.getAddress().getHostAddress();
        }
        return "127.0.0.1"; // Fallback
    }

    /**
     * Writes a VarInt to the data output.
     *
     * @param output The data output
     * @param value The value to write
     */
    private static void writeVarInt(ByteArrayDataOutput output, int value) {
        while ((value & -128) != 0) {
            output.writeByte(value & 127 | 128);
            value >>>= 7;
        }
        output.writeByte(value);
    }

    /**
     * Writes a string to the data output in Minecraft's format (VarInt length + UTF-8 bytes).
     *
     * @param output The data output
     * @param value The string to write
     */
    private static void writeString(ByteArrayDataOutput output, String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeVarInt(output, bytes.length);
        output.write(bytes);
    }

    /**
     * Checks if the Velocity secret is loaded.
     *
     * @return true if the secret is loaded, false otherwise
     */
    public static boolean isSecretLoaded() {
        return secretKey != null;
    }
}
