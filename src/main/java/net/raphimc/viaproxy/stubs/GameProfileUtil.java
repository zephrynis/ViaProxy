/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: This is a minimal stub for GameProfileUtil
 */
package net.raphimc.viaproxy.stubs;

import com.mojang.authlib.GameProfile;

import java.util.UUID;

/**
 * Stub class replacing net.raphimc.vialegacy.api.util.GameProfileUtil
 */
public class GameProfileUtil {
    public static GameProfile makeOfflineGameProfile(String username) {
        return new GameProfile(getOfflinePlayerUuid(username), username);
    }
    
    public static UUID getOfflinePlayerUuid(String username) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes());
    }
}
