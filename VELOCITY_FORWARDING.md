# Velocity Modern Forwarding Support in ViaProxy

This document describes how to configure ViaProxy to work with Velocity's modern player info forwarding mode.

## Overview

ViaProxy now supports Velocity's "modern" forwarding mode, which allows you to run ViaProxy behind a Velocity proxy while properly forwarding player information (UUID, username, IP address, and properties) to the backend server.

## Architecture

```
Player → Velocity Proxy → ViaProxy → Backend Server (Paper/Spigot)
```

When properly configured, this setup allows:
- Player authentication at the Velocity proxy level
- Protocol translation by ViaProxy
- Secure player info forwarding to the backend server
- Maintaining player UUIDs, skins, and properties throughout the chain

## Configuration

### Step 1: Configure Velocity

1. In your Velocity `velocity.toml`, enable modern forwarding:
```toml
[servers]
# Your server configuration
lobby = "viaproxy-address:25565"

[advanced]
# Enable modern forwarding
player-info-forwarding-mode = "modern"
```

2. Velocity will generate a `forwarding.secret` file. Copy this file to your ViaProxy directory.

### Step 2: Configure ViaProxy

Edit `viaproxy.yml` and add/modify the following options:

```yaml
# Disable BungeeCord forwarding (cannot be used with Velocity forwarding)
bungeecord-player-info-passthrough: false

# Enable Velocity modern forwarding
velocity-player-info-passthrough: true

# Path to the Velocity forwarding secret file
# Leave empty to use "forwarding.secret" in the ViaProxy directory
velocity-secret: ""

# Optional: Specify custom path to the secret file
# velocity-secret: "/path/to/forwarding.secret"
```

### Step 3: Configure Backend Server (Paper/Spigot)

In your backend server's `config/paper-global.yml` (Paper) or equivalent:

```yaml
proxies:
  velocity:
    enabled: true
    online-mode: true
    secret: "<same-secret-from-forwarding.secret>"
```

For Paper 1.19+, the configuration is in `config/paper-global.yml`:
```yaml
proxies:
  velocity:
    enabled: true
    online-mode: true
    secret: "<contents-of-forwarding.secret>"
```

**Important**: The secret must match exactly between Velocity, ViaProxy, and your backend server.

## How It Works

1. Player connects to Velocity proxy
2. Velocity authenticates the player with Mojang
3. Player connects through ViaProxy
4. ViaProxy reads the player's GameProfile (UUID, username, properties)
5. ViaProxy creates a signed forwarding payload using the shared secret
6. ViaProxy forwards this data to the backend server during the handshake
7. Backend server verifies the signature and accepts the forwarded player info

The forwarding payload includes:
- Forwarding protocol version
- Player's IP address
- Player's UUID
- Player's username
- Player's properties (skin, cape, etc.) with signatures

All data is signed with HMAC-SHA256 using the shared secret to prevent spoofing.

## Security Considerations

1. **Keep the secret file secure**: The `forwarding.secret` file must be kept private and should have appropriate file permissions
2. **Firewall your backend**: Ensure your backend server is not directly accessible from the internet
3. **Use the same secret everywhere**: The secret must match on Velocity, ViaProxy, and all backend servers
4. **Do not use both forwarding modes**: Only enable either BungeeCord or Velocity forwarding, not both

## Troubleshooting

### "Your server did not send a forwarding request to the proxy"

This error means the backend server didn't receive proper forwarding data. Check:
- `velocity-player-info-passthrough` is set to `true` in ViaProxy config
- The `forwarding.secret` file exists and is readable by ViaProxy
- The secret matches between all components
- The backend server has Velocity forwarding enabled

### "Velocity forwarding secret file not found"

Solution:
- Copy the `forwarding.secret` file from your Velocity proxy to the ViaProxy directory
- Or specify the path using the `velocity-secret` config option
- Ensure the file has proper read permissions

### "Invalid signature" or "Cannot verify player"

This means the HMAC signature validation failed. Verify:
- All components are using the exact same secret
- The secret file hasn't been modified
- There are no extra whitespace characters in the secret

### Players connecting with wrong UUID

Ensure:
- The Velocity proxy is in online mode
- ViaProxy's `proxy-online-mode` matches your setup
- The backend server has `online-mode: true` in the Velocity forwarding config

## Example Setup

Complete example configuration for a Velocity → ViaProxy → Paper 1.20 setup:

**Velocity (velocity.toml)**:
```toml
[servers]
survival = "127.0.0.1:25565"  # ViaProxy address

[advanced]
player-info-forwarding-mode = "modern"
```

**ViaProxy (viaproxy.yml)**:
```yaml
bind-address: "0.0.0.0:25565"
target-address: "127.0.0.1:25566"  # Paper server
target-version: "1.20.4"
bungeecord-player-info-passthrough: false
velocity-player-info-passthrough: true
velocity-secret: ""  # Uses forwarding.secret in ViaProxy directory
```

**Paper (config/paper-global.yml)**:
```yaml
proxies:
  velocity:
    enabled: true
    online-mode: true
    secret: "abcdef123456..."  # Content from forwarding.secret
  bungee-cord:
    online-mode: false
```

## Compatibility

- Velocity: All versions with modern forwarding support
- Backend Servers: Paper, Purpur, Pufferfish (with Velocity forwarding enabled)
- Minecraft Versions: 1.13+ (Velocity requirement)

## Migration from BungeeCord Forwarding

If you're currently using BungeeCord forwarding:

1. Set `bungeecord-player-info-passthrough: false`
2. Set `velocity-player-info-passthrough: true`
3. Add the `forwarding.secret` file
4. Update your backend server configuration to use Velocity forwarding instead of BungeeCord forwarding
5. Restart all components

## Additional Notes

- Velocity forwarding is more secure than BungeeCord forwarding
- The forwarding data is cryptographically signed to prevent player impersonation
- ViaProxy will log forwarding attempts for debugging: `[VELOCITY] Velocity forwarding enabled for player <name> (<uuid>) from <ip>`
- Both forwarding modes cannot be active simultaneously
