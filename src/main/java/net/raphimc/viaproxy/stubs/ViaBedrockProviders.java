/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: Base provider stubs for removed ViaBedrock functionality
 */
package net.raphimc.viaproxy.stubs;

import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.platform.providers.Provider;
import javax.crypto.SecretKey;

/**
 * Stub base provider classes for ViaBedrock
 */
public class ViaBedrockProviders {
    
    public static class NettyPipelineProvider implements Provider {
        public void enableCompression(UserConnection user, Object protocolCompression) {
            // Stub
        }
        
        public void enableEncryption(UserConnection user, SecretKey key) {
            // Stub
        }
    }
}
