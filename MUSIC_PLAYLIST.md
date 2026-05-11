# Music Playlist Feature

## Overview

The plugin now supports sequential music playback. Instead of looping a single track, you can add multiple music files that will play in order.

## How to Use

1. Place your music files in `src/main/resources/audio/music/` with the naming pattern:
   - `background_1.wav`
   - `background_2.wav`
   - `background_3.wav`
   - etc.

2. The plugin will automatically detect all numbered tracks starting from 1.

3. Tracks play sequentially without gaps: 1 → 2 → 3 → ... → back to 1

## Backward Compatibility

If no numbered tracks are found, the plugin falls back to `background.wav` (legacy single-track mode).

## Example Setup

```
src/main/resources/audio/music/
├── background_1.wav  (intro theme)
├── background_2.wav  (main theme)
├── background_3.wav  (ambient loop)
└── background_4.wav  (outro theme)
```

After track 4 finishes, the playlist loops back to track 1.

## Testing

To test the playlist feature:

1. Add multiple `background_N.wav` files to `src/main/resources/audio/music/`
2. Build the plugin: `task build`
3. Install the plugin in your IDE
4. Enable background music in Settings → Minecraft Theme Audio
5. The tracks should play in sequence

## Implementation Details

- The playlist is loaded on plugin startup and when music is restarted
- Track transitions happen automatically when a track finishes
- Volume settings apply to all tracks
- Muting stops the current track immediately
- Unmuting starts from the beginning of the playlist