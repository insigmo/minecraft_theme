# Minecraft Theme & Sounds Plugin

JetBrains plugin that delivers:
- Minecraft-inspired dark UI + editor color scheme
- SFX for tab switches and Run/Debug start/stop events
- Background music loop
- Status bar mute toggle (`MC: On` / `MC: Muted`)
- Settings page for volume and feature toggles

## Features

- Theme file: `src/main/resources/themes/MinecraftDark.theme.json`
- Editor scheme: `src/main/resources/editor/MinecraftDark.icls`
- Listeners:
  - `FileEditorManagerListener` for tab switch sounds
  - `ExecutionListener` for Run/Debug sounds
- Audio engine:
  - App-level persistent settings (`minecraft-audio.xml`)
  - WAV playback via `javax.sound.sampled`
  - Fallback to system beep when SFX files are absent

## Audio Files

Put your own WAV files into:
- `src/main/resources/audio/sfx/tab-switch.wav`
- `src/main/resources/audio/sfx/run-start.wav`
- `src/main/resources/audio/sfx/debug-start.wav`
- `src/main/resources/audio/sfx/run-stop.wav`
- `src/main/resources/audio/sfx/debug-stop.wav`
- `src/main/resources/audio/sfx/error.wav`
- `src/main/resources/audio/music/background.wav` (or use playlist format below)

### Music Playlist

The plugin supports sequential music playback. You can add multiple tracks:
- `src/main/resources/audio/music/background_1.wav`
- `src/main/resources/audio/music/background_2.wav`
- `src/main/resources/audio/music/background_3.wav`
- etc.

Tracks will play in order (1, 2, 3...) and loop back to the first track when finished.
If no numbered tracks are found, the plugin falls back to `background.wav`.

## Build

This repository currently does not include a Gradle wrapper. To build:
1. Install Gradle locally.
2. Run `gradle wrapper`.
3. Run `./gradlew buildPlugin`.

The plugin zip will be generated under `build/distributions/`.

## Build With Docker (One Command)

Run:

`docker compose up --build plugin-builder`

What happens:
- Container builds the plugin with Gradle.
- Result zip is copied into `./dist` on your machine.
- Container exits after completion.
- Build image uses `gradle:jdk21` (tracks the latest Gradle release with JDK 21).

## Build With Task

If you use [Task](https://taskfile.dev), run:

`task build`

It runs the same Docker-based build and writes zip artifacts to `./dist`.

If `./dist` is empty, inspect logs:

`docker compose logs plugin-builder`

