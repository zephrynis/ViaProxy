/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: Minimal stub for ViaBedrock
 */
package net.raphimc.viaproxy.stubs;

import com.viaversion.viaversion.api.connection.StorableObject;

/**
 * Stub for AuthChainData - Bedrock authentication chain
 */
public class AuthChainData implements StorableObject {
    private final String displayName;
    
    public AuthChainData(String mojangJwt, String identityJwt, Object publicKey, Object privateKey, Object deviceId) {
        // Constructor stub - store minimal data
        this.displayName = "BedrockPlayer";
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
