# Automated import replacement script for ViaProxy minimal fork
# This replaces all Via* library imports with stub implementations

import os
import re

# Define replacements
REPLACEMENTS = {
    'import com.viaversion.viabackwards.protocol.v1_20_5to1_20_3.storage.CookieStorage;': 
        'import net.raphimc.viaproxy.stubs.CookieStorage;',
    'import net.raphimc.viabedrock.api.BedrockProtocolVersion;': 
        'import net.raphimc.viaproxy.stubs.BedrockProtocolVersion;',
    'import net.raphimc.vialegacy.api.LegacyProtocolVersion;': 
        'import net.raphimc.viaproxy.stubs.LegacyProtocolVersion;',
    'import net.raphimc.vialegacy.api.util.GameProfileUtil;':
        'import net.raphimc.viaproxy.stubs.GameProfileUtil;',
    'import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.model.ClassicLevel;':
        'import net.raphimc.viaproxy.stubs.ClassicLevel;',
    'import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.storage.ClassicLevelStorage;':
        'import net.raphimc.viaproxy.stubs.ClassicLevelStorage;',
    'import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.storage.ProtocolMetadataStorage;':
        'import net.raphimc.viaproxy.stubs.ProtocolMetadataStorage;',
    'import net.raphimc.viabedrock.protocol.data.ProtocolConstants;':
        'import net.raphimc.viaproxy.stubs.ProtocolConstants;',
    'import net.raphimc.viabedrock.protocol.storage.AuthChainData;':
        'import net.raphimc.viaproxy.stubs.AuthChainData;',
    'import net.raphimc.viabedrock.protocol.data.enums.java.ResourcePackAction;':
        'import net.raphimc.viaproxy.stubs.ResourcePackAction;',
    'import net.raphimc.viabedrock.api.util.FileSystemUtil;':
        'import net.raphimc.viaproxy.stubs.FileSystemUtil;',
    'import com.vdurmont.semver4j.Semver;':
        'import net.raphimc.viaproxy.stubs.Semver;',
    'import net.raphimc.vialegacy.ViaLegacyConfig;':
        'import net.raphimc.viaproxy.stubs.ViaLegacyConfig;',
    'import net.raphimc.vialegacy.ViaLegacy;':
        'import net.raphimc.viaproxy.stubs.ViaLegacy;',
    'import net.raphimc.vialegacy.protocol.alpha.a1_0_16_2toa1_0_17_1_0_17_4.storage.TimeLockStorage;':
        'import net.raphimc.viaproxy.stubs.TimeLockStorage;',
    # Provider base classes
    'import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.provider.ClassicCustomCommandProvider;':
        'import net.raphimc.viaproxy.stubs.ViaLegacyProviders.ClassicCustomCommandProvider;',
    'import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.provider.ClassicMPPassProvider;':
        'import net.raphimc.viaproxy.stubs.ViaLegacyProviders.ClassicMPPassProvider;',
    'import net.raphimc.vialegacy.protocol.classic.c0_28_30toa1_0_15.provider.ClassicWorldHeightProvider;':
        'import net.raphimc.viaproxy.stubs.ViaLegacyProviders.ClassicWorldHeightProvider;',
    'import net.raphimc.vialegacy.protocol.release.r1_6_4tor1_7_2_5.provider.EncryptionProvider;':
        'import net.raphimc.viaproxy.stubs.ViaLegacyProviders.EncryptionProvider;',
    'import net.raphimc.vialegacy.protocol.release.r1_7_6_10tor1_8.provider.GameProfileFetcher;':
        'import net.raphimc.viaproxy.stubs.ViaLegacyProviders.GameProfileFetcher;',
    'import net.raphimc.vialegacy.protocol.release.r1_2_4_5tor1_3_1_2.provider.OldAuthProvider;':
        'import net.raphimc.viaproxy.stubs.ViaLegacyProviders.OldAuthProvider;',
    'import net.raphimc.viabedrock.protocol.provider.NettyPipelineProvider;':
        'import net.raphimc.viaproxy.stubs.ViaBedrockProviders.NettyPipelineProvider;',
    'import com.viaversion.viabackwards.protocol.v1_20_5to1_20_3.provider.TransferProvider;':
        'import net.raphimc.viaproxy.stubs.ViaBackwardsProviders.TransferProvider;',
    'import net.raphimc.viabedrock.api.minecraft.data.BedrockRealmsService;':
        'import net.raphimc.viaproxy.stubs.BedrockRealmsService;',
    'import net.raphimc.minecraftauth.service.realms.BedrockRealmsService;':
        'import net.raphimc.viaproxy.stubs.BedrockRealmsService;',
}

def replace_in_file(filepath):
    """Replace Via* imports in a single file"""
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original = content
        for old, new in REPLACEMENTS.items():
            content = content.replace(old, new)
        
        if content != original:
            with open(filepath, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"Updated: {filepath}")
            return True
        return False
    except Exception as e:
        print(f"Error processing {filepath}: {e}")
        return False

def main():
    src_root = r"E:\Git\ViaProxy\src\main\java"
    updated_count = 0
    
    for root, dirs, files in os.walk(src_root):
        for file in files:
            if file.endswith('.java'):
                filepath = os.path.join(root, file)
                if replace_in_file(filepath):
                    updated_count += 1
    
    print(f"\nTotal files updated: {updated_count}")

if __name__ == "__main__":
    main()
