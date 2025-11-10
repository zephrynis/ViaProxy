/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: Minimal stub for ViaLegacy
 */
package net.raphimc.viaproxy.stubs;

import java.util.logging.Logger;

/**
 * Stub for ViaLegacy main class
 */
public class ViaLegacy {
    private static final StubPlatform PLATFORM = new StubPlatform();
    
    public static ViaLegacyConfig getConfig() {
        return new ViaLegacyConfig();
    }
    
    public static StubPlatform getPlatform() {
        return PLATFORM;
    }
    
    public static class StubPlatform {
        private final Logger logger = Logger.getLogger("ViaLegacy-Stub");
        
        public Logger getLogger() {
            return logger;
        }
    }
}
