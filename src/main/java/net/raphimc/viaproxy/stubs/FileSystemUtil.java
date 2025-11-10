/*
 * This file is part of ViaProxy - https://github.com/RaphiMC/ViaProxy
 * Copyright (C) 2021-2025 RK_01/RaphiMC and contributors
 *
 * STUB: Minimal stub for ViaBedrock file system utilities
 */
package net.raphimc.viaproxy.stubs;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

/**
 * Stub for FileSystemUtil
 */
public class FileSystemUtil {
    public static FileSystem getJarFileSystem(Class<?> clazz) {
        return null;
    }
    
    public static Map<Path, byte[]> getFilesInDirectory(String directory) {
        // Return empty map - no legacy language files needed
        return Collections.emptyMap();
    }
}
