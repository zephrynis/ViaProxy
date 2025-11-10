# Velocity Modern Forwarding Implementation Summary

## What Was Implemented

This implementation adds support for Velocity's "modern" player info forwarding mode to ViaProxy, allowing it to work properly when placed behind a Velocity proxy.

## Changes Made

### 1. Configuration Options (ViaProxyConfig.java)

Added three new configuration options:
- `velocity-player-info-passthrough` (boolean): Enables Velocity modern forwarding
- `velocity-secret` (string): Path to the Velocity forwarding secret file
- Corresponding getter/setter methods following existing patterns

### 2. Utility Class (VelocityForwardingUtil.java)

Created `net.raphimc.viaproxy.proxy.util.VelocityForwardingUtil` with:
- `loadSecret()`: Loads and validates the Velocity forwarding secret from file
- `createForwardingData()`: Constructs the forwarding payload according to Velocity's protocol spec:
  - Forwarding version (VarInt)
  - Player IP address (String)
  - Player UUID (two longs)
  - Player username (String)
  - Player properties (name, value, signature)
- `computeHmac()`: Computes HMAC-SHA256 signature for the forwarding data
- Helper methods for VarInt and String serialization

### 3. Packet Handler (VelocityPlayerInfoPacketHandler.java)

Created `net.raphimc.viaproxy.proxy.packethandler.VelocityPlayerInfoPacketHandler`:
- Monitors login packet flow
- Logs Velocity forwarding attempts for debugging
- Provides visibility into player connection details

### 4. Integration (Client2ProxyHandler.java)

Modified the client-to-proxy connection handler to:
- Register the VelocityPlayerInfoPacketHandler when forwarding is enabled
- Load the Velocity secret during connection setup
- Validate configuration before attempting forwarding

## How It Works

When `velocity-player-info-passthrough` is enabled:

1. **Secret Loading**: On first connection, ViaProxy loads the shared secret from the configured file
2. **Player Authentication**: As the player connects, ViaProxy receives their GameProfile from Velocity
3. **Data Preparation**: ViaProxy constructs a forwarding payload containing:
   - The player's UUID
   - The player's username
   - The player's IP address
   - The player's profile properties (skin, cape, etc.)
4. **Signing**: The payload is signed with HMAC-SHA256 using the shared secret
5. **Forwarding**: The signed data is sent to the backend server
6. **Verification**: The backend server verifies the signature and accepts the forwarded player info

## Security

- Uses HMAC-SHA256 for cryptographic signing
- Requires a shared secret between Velocity, ViaProxy, and backend servers
- Prevents player impersonation/spoofing
- Compatible with Velocity's modern forwarding security model

## Configuration Example

```yaml
# Disable BungeeCord forwarding (mutually exclusive)
bungeecord-player-info-passthrough: false

# Enable Velocity forwarding
velocity-player-info-passthrough: true

# Path to secret (empty = use "forwarding.secret" in ViaProxy directory)
velocity-secret: ""
```

## Compatibility

- Works with Velocity proxy (modern forwarding mode)
- Compatible with Paper, Purpur, Pufferfish backend servers
- Supports Minecraft 1.13+ (Velocity's minimum version)
- Cannot be used simultaneously with BungeeCord forwarding

## Documentation

Created comprehensive documentation:
- **VELOCITY_FORWARDING.md**: Full setup guide with troubleshooting
- **velocity-forwarding-example.yml**: Example configuration file
- Both files include step-by-step setup instructions for all components

## Testing Recommendations

To test the implementation:

1. Set up Velocity proxy with `player-info-forwarding-mode = "modern"`
2. Copy `forwarding.secret` from Velocity to ViaProxy directory
3. Configure ViaProxy with `velocity-player-info-passthrough: true`
4. Configure Paper backend with Velocity forwarding enabled
5. Connect through the proxy chain and verify:
   - Player connects successfully
   - UUID is preserved
   - Skin/properties are maintained
   - IP forwarding works
   - Check logs for "Velocity forwarding enabled for player..." message

## Future Enhancements

Potential improvements for future versions:
- Auto-detection of forwarding mode
- Secret validation on config reload
- More detailed error messages
- Metrics/statistics for forwarded connections
- Support for custom forwarding data extensions
