/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: Base provider stubs for removed ViaLegacy functionality
 */
package net.raphimc.viaproxy.stubs;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.Provider;

/**
 * Stub base provider classes for ViaLegacy
 */
public class ViaLegacyProviders {
    
    public static class ClassicCustomCommandProvider implements Provider {
        public boolean handleChatMessage(UserConnection user, String message) {
            return false;
        }
        
        protected void sendFeedback(UserConnection user, String message) {
            // Stub
        }
    }
    
    public static class ClassicMPPassProvider implements Provider {
        public String getMpPass(UserConnection user) {
            return null;
        }
    }
    
    public static class ClassicWorldHeightProvider implements Provider {
        public void sendChunk(UserConnection user, int chunkX, int chunkZ) {
            // Stub
        }
        
        public short getMaxChunkSectionCount(UserConnection user) {
            return 16;
        }
    }
    
    public static class EncryptionProvider implements Provider {
        // Stub
    }
    
    public static class GameProfileFetcher implements Provider {
        // Stub
    }
    
    public static class OldAuthProvider implements Provider {
        public void sendAuthRequest(UserConnection user, String serverId) throws Throwable {
            // Stub
        }
    }
}
