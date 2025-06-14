# Tiertaggerbramnet

A Minecraft Spigot plugin that fetches and displays player tiers, with support for PlaceholderAPI.

## Features

- Fetches player tiers from an external source.
- `/tier` command to check your own or another player's tier.
- `/tier reload` command to reload the tier list (requires permission).
- PlaceholderAPI integration: use `%tier%` to display a player's tier in compatible plugins.

## Installation

1. Download the plugin JAR and place it in your server's `plugins` folder.
2. (Optional) Install [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) for placeholder support.
3. Restart or reload your server.

## Commands

- `/tier` — Shows your tier.
- `/tier <player>` — Shows the specified player's tier.
- `/tier reload` — Reloads the tier list. Requires `tiertagger.reload` permission.

## Permissions

- `tiertagger.reload` — Allows reloading the tier list.

## Placeholders

- `%tier%` — Displays the player's tier (e.g., `HT1`, `LT2`, or `unknown`).

## Configuration

No configuration is required. The plugin fetches the tier list automatically and refreshes it every 60 seconds.

## License

MIT