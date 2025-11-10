/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: This is a minimal stub to replace ViaBedrock functionality
 * Bedrock Edition support has been removed to reduce memory usage
 */
package net.raphimc.viaproxy.stubs;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

/**
 * Stub class replacing net.raphimc.viabedrock.api.BedrockProtocolVersion
 * All Bedrock protocol versions are removed. This prevents compilation errors.
 */
public class BedrockProtocolVersion {
    // Stub protocol version that will never match
    public static final ProtocolVersion bedrockLatest = ProtocolVersion.v1_20_3;
    
    private BedrockProtocolVersion() {
        // Utility class
    }
}
