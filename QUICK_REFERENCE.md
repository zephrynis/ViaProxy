# Velocity Forwarding Quick Reference

## Configuration (viaproxy.yml)

```yaml
bungeecord-player-info-passthrough: false
velocity-player-info-passthrough: true
velocity-secret: ""  # or "/path/to/forwarding.secret"
```

## Setup Steps

1. **Copy Secret File**
   ```bash
   cp velocity/forwarding.secret viaproxy/
   ```

2. **Enable in Config**
   ```yaml
   velocity-player-info-passthrough: true
   ```

3. **Configure Backend**
   ```yaml
   # Paper: config/paper-global.yml
   proxies:
     velocity:
       enabled: true
       secret: "<secret-from-file>"
   ```

4. **Restart All**

## Log Messages

✅ **Success**:
```
[VELOCITY] Velocity forwarding enabled for player Steve (uuid) from 192.168.1.100
```

❌ **Errors**:
```
Velocity forwarding secret file not found: /path/to/forwarding.secret
```

## Troubleshooting

| Error | Solution |
|-------|----------|
| "secret file not found" | Copy `forwarding.secret` to ViaProxy dir |
| "Invalid signature" | Ensure all components use same secret |
| "did not send forwarding request" | Enable `velocity-player-info-passthrough: true` |

## Architecture

```
Player → Velocity → ViaProxy → Backend
         ↓           ↓           ↓
      Authenticate  Forward   Verify
                    Signed     & Accept
                    Data
```

## Files Modified

- ✏️ `ViaProxyConfig.java` - Config options
- ✏️ `Client2ProxyHandler.java` - Integration
- ➕ `VelocityForwardingUtil.java` - Protocol impl
- ➕ `VelocityPlayerInfoPacketHandler.java` - Handler

## Key Features

- 🔐 HMAC-SHA256 signing
- 👤 Full player data (UUID, IP, properties)
- 🚫 BungeeCord incompatible (by design)
- 📝 Debug logging
- ⚙️ Easy configuration

## Documentation

- 📖 `VELOCITY_FORWARDING.md` - Complete guide
- 📄 `velocity-forwarding-example.yml` - Config template
- 🔧 `BUILD_GUIDE.md` - Build instructions
- 📦 `DELIVERY_SUMMARY.md` - Full delivery summary

## Support

For help, check:
1. `VELOCITY_FORWARDING.md` troubleshooting section
2. ViaProxy logs with `log-ips: true`
3. Verify secret matches on all components

---

**Status**: ✅ Ready to use  
**Version**: Compatible with ViaProxy current version  
**Date**: November 9, 2025
