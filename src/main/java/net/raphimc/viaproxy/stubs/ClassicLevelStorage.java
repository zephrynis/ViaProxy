/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: Minimal stub for ViaLegacy
 */
package net.raphimc.viaproxy.stubs;

import com.viaversion.viaversion.api.connection.StorableObject;

/**
 * Stub for ClassicLevelStorage
 */
public class ClassicLevelStorage implements StorableObject {
    private final ClassicLevel classicLevel = new ClassicLevel();
    
    public ClassicLevel getClassicLevel() {
        return classicLevel;
    }
}
