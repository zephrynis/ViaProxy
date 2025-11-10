/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: Minimal stub for Semver4j library
 */
package net.raphimc.viaproxy.stubs;

/**
 * Stub for Semver - semantic versioning
 */
public class Semver {
    private final String version;
    
    public Semver(String version) {
        this.version = version;
    }
    
    public boolean isGreaterThan(String other) {
        // Stub implementation - always returns false to disable version checks
        return false;
    }
    
    public boolean isGreaterThan(Semver other) {
        return isGreaterThan(other.version);
    }
    
    public boolean isLowerThan(String other) {
        return false;
    }
    
    public boolean isLowerThan(Semver other) {
        return isLowerThan(other.version);
    }
    
    @Override
    public String toString() {
        return version;
    }
}
