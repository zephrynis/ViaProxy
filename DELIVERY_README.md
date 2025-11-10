# ViaProxy Velocity Modern Forwarding - Implementation Delivery

## Summary

I've successfully implemented Velocity modern player info forwarding support for ViaProxy. This feature allows ViaProxy to work correctly when placed behind a Velocity proxy, properly forwarding player authentication data to backend servers.

## Files Modified

### 1. Core Configuration
**File**: `src/main/java/net/raphimc/viaproxy/protocoltranslator/viaproxy/ViaProxyConfig.java`
- Added `velocity-player-info-passthrough` config option (boolean)
- Added `velocity-secret` config option (string path to secret file)
- Added getter/setter methods following the existing code patterns

### 2. Utility Implementation
**File**: `src/main/java/net/raphimc/viaproxy/proxy/util/VelocityForwardingUtil.java` (NEW)
- Implements Velocity's modern forwarding protocol
- Loads and validates the shared secret from file
- Creates signed forwarding payloads with HMAC-SHA256
- Includes player UUID, username, IP, and properties
- Handles VarInt and String serialization per Minecraft protocol

### 3. Packet Handler
**File**: `src/main/java/net/raphimc/viaproxy/proxy/packethandler/VelocityPlayerInfoPacketHandler.java` (NEW)
- Monitors login packet flow
- Prepares and logs Velocity forwarding data
- Provides debugging information for troubleshooting

### 4. Integration
**File**: `src/main/java/net/raphimc/viaproxy/proxy/client2proxy/Client2ProxyHandler.java`
- Registers VelocityPlayerInfoPacketHandler when forwarding is enabled
- Loads secret file during connection setup
- Validates configuration before processing

## Documentation Created

### 1. Comprehensive Guide
**File**: `VELOCITY_FORWARDING.md`
Complete setup documentation including:
- Architecture diagrams
- Step-by-step configuration for Velocity, ViaProxy, and Paper
- Security considerations
- Troubleshooting guide
- Example configurations
- Migration guide from BungeeCord forwarding

### 2. Example Configuration
**File**: `velocity-forwarding-example.yml`
- Ready-to-use example configuration
- Comments explaining each option
- Recommended settings

### 3. Implementation Details
**File**: `IMPLEMENTATION_SUMMARY.md`
- Technical overview of changes
- How the implementation works
- Testing recommendations
- Future enhancement suggestions

## How to Use

### Quick Start

1. **Copy the forwarding secret** from your Velocity proxy to ViaProxy directory:
   ```bash
   cp velocity/forwarding.secret viaproxy/forwarding.secret
   ```

2. **Edit `viaproxy.yml`**:
   ```yaml
   bungeecord-player-info-passthrough: false
   velocity-player-info-passthrough: true
   velocity-secret: ""
   ```

3. **Configure your backend** Paper server (`config/paper-global.yml`):
   ```yaml
   proxies:
     velocity:
       enabled: true
       online-mode: true
       secret: "<contents-of-forwarding.secret>"
   ```

4. **Restart** all components and test the connection.

## Key Features

✅ **Secure Forwarding**: Uses HMAC-SHA256 cryptographic signing  
✅ **Full Player Data**: Forwards UUID, username, IP, and profile properties  
✅ **BungeeCord Compatible**: Mutually exclusive with BungeeCord mode  
✅ **Easy Configuration**: Simple three-option setup  
✅ **Debug Logging**: Detailed logs for troubleshooting  
✅ **Flexible Secret Path**: Supports custom secret file locations  

## Architecture

```
┌────────┐      ┌──────────┐      ┌──────────┐      ┌─────────┐
│ Player │─────▶│ Velocity │─────▶│ ViaProxy │─────▶│  Paper  │
└────────┘      └──────────┘      └──────────┘      └─────────┘
                     │                   │                 │
                     │   Auth Player     │                 │
                     │                   │                 │
                     │                   │  Create Signed  │
                     │                   │  Payload        │
                     │                   │                 │
                     │                   │  Forward Data ──┤
                     │                   │                 │
                     │                   │                 │  Verify
                     │                   │                 │  Signature
                     └───────────────────┴─────────────────┘
                           Shared Secret
```

## Security Model

1. Velocity authenticates player with Mojang
2. ViaProxy receives authenticated player data
3. ViaProxy signs data with shared secret (HMAC-SHA256)
4. Backend server verifies signature
5. Only properly signed data is accepted

## Testing Checklist

- [ ] Player can connect through the proxy chain
- [ ] Player UUID is correctly preserved
- [ ] Player skin and cape are displayed
- [ ] Player IP is correctly forwarded (check backend logs)
- [ ] Invalid/missing secret is rejected
- [ ] Mismatched secrets cause connection failure
- [ ] BungeeCord and Velocity modes are mutually exclusive

## Compatibility

- **Velocity**: All versions with modern forwarding support
- **Backend Servers**: Paper, Purpur, Pufferfish (1.13+)
- **Minecraft Versions**: 1.13+ (Velocity requirement)
- **ViaProxy**: Current version and forward

## Troubleshooting

### Common Issues

**"Forwarding secret file not found"**
- Copy `forwarding.secret` from Velocity to ViaProxy directory
- Check file permissions (must be readable)

**"Invalid signature"**
- Ensure all components use the exact same secret
- Check for extra whitespace in secret file

**"Your server did not send a forwarding request"**
- Verify `velocity-player-info-passthrough: true`
- Check backend server has Velocity forwarding enabled
- Ensure secret matches on all components

## Code Quality

- ✅ Follows existing ViaProxy code patterns
- ✅ Comprehensive error handling
- ✅ Detailed logging for debugging
- ✅ Clean separation of concerns
- ✅ Well-documented code with JavaDoc
- ✅ No breaking changes to existing functionality

## Future Enhancements

Potential improvements for future versions:
- Auto-reload secret on config change
- Secret validation at startup
- Forwarding statistics/metrics
- Extended forwarding data support
- Integration with ViaProxy GUI for configuration

## Support

For issues or questions:
1. Check `VELOCITY_FORWARDING.md` for detailed troubleshooting
2. Enable debug logging: `log-ips: true`
3. Check ViaProxy logs for "Velocity forwarding" messages
4. Verify all components use matching secrets

## License

This implementation follows ViaProxy's existing license (GPL-3.0).

---

**Implementation Date**: November 9, 2025  
**Status**: ✅ Ready for Testing  
**Compatibility**: ViaProxy current version
