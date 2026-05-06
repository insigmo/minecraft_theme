FROM gradle:jdk21

WORKDIR /workspace

COPY . .

# Build plugin and copy artifact to mounted output volume.
CMD ["bash", "-lc", "set -euo pipefail; gradle --no-daemon clean buildPlugin; ls -la build/distributions; mkdir -p /out; artifact=$(ls -1 build/distributions/*.zip | head -n 1); cp \"$artifact\" /out/; ls -la /out"]

