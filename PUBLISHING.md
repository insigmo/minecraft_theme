# Packaging and Publishing

## Build Artifact

1. Ensure Gradle is installed.
2. Generate wrapper once:
   - `gradle wrapper`
3. Build plugin:
   - `./gradlew buildPlugin`
4. Zip output:
   - `build/distributions/*.zip`

## Marketplace Preparation

- Verify `id`, `name`, `description`, and vendor fields in `src/main/resources/META-INF/plugin.xml`.
- Add screenshots and feature list in Marketplace listing.
- Mention required audio files and fallback behavior.
- Upload plugin zip from `build/distributions`.

