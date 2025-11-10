/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: This is a minimal stub to replace ViaLegacy functionality
 * Legacy protocol support (pre-1.7) has been removed to reduce memory usage
 */
package net.raphimc.viaproxy.stubs;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;

/**
 * Stub class replacing net.raphimc.vialegacy.api.LegacyProtocolVersion
 * All legacy protocol versions are removed. This prevents compilation errors.
 */
public class LegacyProtocolVersion {
    // Stub protocol versions that will never match
    public static final ProtocolVersion r1_3_1tor1_3_2 = ProtocolVersion.v1_20_3;
    public static final ProtocolVersion r1_6_4 = ProtocolVersion.v1_20_3;
    public static final ProtocolVersion r1_7_2_5 = ProtocolVersion.v1_20_3;
    public static final ProtocolVersion b1_7tob1_7_3 = ProtocolVersion.v1_20_3;
    public static final ProtocolVersion b1_8tob1_8_1 = ProtocolVersion.v1_20_3;
    public static final ProtocolVersion r1_0_0tor1_0_1 = ProtocolVersion.v1_20_3;
    public static final ProtocolVersion c0_28toc0_30 = ProtocolVersion.v1_20_3;
    
    private LegacyProtocolVersion() {
        // Utility class
    }
}
