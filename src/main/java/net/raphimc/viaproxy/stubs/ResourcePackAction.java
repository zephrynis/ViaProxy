/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: Minimal stub for ViaBedrock resource pack handling
 */
package net.raphimc.viaproxy.stubs;

/**
 * Stub for ResourcePackAction enum
 */
public enum ResourcePackAction {
    SUCCESSFULLY_LOADED(3),
    INVALID_URL(1),
    FAILED_DOWNLOAD(2),
    ACCEPTED(0);
    
    private final int id;
    
    ResourcePackAction(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;
    }
}
