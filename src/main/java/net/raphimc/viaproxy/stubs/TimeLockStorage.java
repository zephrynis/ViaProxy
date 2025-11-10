/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: Minimal stub for ViaLegacy time lock storage
 */
package net.raphimc.viaproxy.stubs;

import com.viaversion.viaversion.api.connection.StorableObject;

/**
 * Stub for TimeLockStorage - used for alpha protocol time locking
 */
public class TimeLockStorage implements StorableObject {
    public void setTime(long time) {
        // Stub
    }
    
    public long getTime() {
        return 0;
    }
}
