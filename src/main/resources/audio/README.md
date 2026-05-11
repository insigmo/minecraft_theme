Place your custom WAV files here.

## Sound Effects

Expected files:
- `sfx/tab-switch.wav`
- `sfx/run-start.wav`
- `sfx/debug-start.wav`
- `sfx/run-stop.wav`
- `sfx/debug-stop.wav`
- `sfx/error.wav`

## Background Music

You can use either a single track or a playlist:

### Single Track (legacy)
- `music/background.wav` - plays in a continuous loop

### Playlist (recommended)
- `music/background_1.wav`
- `music/background_2.wav`
- `music/background_3.wav`
- etc.

Tracks will play sequentially (1 → 2 → 3 → ... → 1) without gaps.
The plugin automatically detects all `background_N.wav` files where N starts from 1.

If no numbered tracks are found, the plugin falls back to `background.wav`.

## Notes

Current repository contains placeholder files. Replace them with valid WAV audio keeping the same filenames.

If files are missing or invalid, the plugin falls back to a system beep for short SFX, and background music stays disabled.

