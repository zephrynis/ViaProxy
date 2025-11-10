/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: This is a minimal stub to replace ViaBackwards functionality
 */
package net.raphimc.viaproxy.stubs;

import com.viaversion.viaversion.api.connection.StorableObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Stub class replacing com.viaversion.viabackwards.protocol.v1_20_5to1_20_3.storage.CookieStorage
 * Transfer packet cookie storage has been stubbed out.
 */
public class CookieStorage implements StorableObject {
    private final Map<String, byte[]> cookies = new HashMap<>();
    
    public Map<String, byte[]> cookies() {
        return cookies;
    }
}
