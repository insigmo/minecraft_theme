# Test Matrix

## IDE Coverage

- IntelliJ IDEA 2025.3+
- PyCharm 2025.3+
- WebStorm 2025.3+

## Functional Checks

1. Install plugin from disk.
2. Select `Minecraft Dark` theme.
3. Open multiple files and switch tabs rapidly:
   - Expected: short click SFX (or beep fallback if WAV is missing).
4. Start a Run configuration:
   - Expected: Run start SFX.
5. Stop/finish Run:
   - Expected: Run stop SFX.
6. Start Debug:
   - Expected: Debug start SFX.
7. Stop Debug:
   - Expected: Debug stop SFX.
8. Verify status bar widget:
   - `MC: On` toggles to `MC: Muted` on click and back.
9. Open Settings > `Minecraft Theme Audio`:
   - Toggle SFX/music options and volume sliders.
   - Apply and verify behavior changes without restart.

## Notes

- When `audio/*.wav` files are absent, SFX fallback is a system beep.
- Background music requires `audio/music/background.wav`.

