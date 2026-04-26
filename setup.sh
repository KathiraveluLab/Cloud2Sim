#!/bin/bash

# Cloud2Sim Setup and Runner Script
# This script builds the project and provides a convenient way to run simulations.

PROJECT_ROOT=$(pwd)

echo "--- Initializing Cloud2Sim Setup ---"

# 2. Build the project
echo "Cleaning lib directory..."
rm -rf lib/*

echo "Building Cloud2Sim with Maven..."
mvn clean install -DskipTests

if [ $? -ne 0 ]; then
    echo "Error: Maven build failed. Please check the logs."
    exit 1
fi

echo "Build successful."

# 3. Create a runner script for convenient execution
cat <<'EOF' > run_sim.sh
#!/bin/bash
# Cloud2Sim Runner
# Usage: ./run_sim.sh <MainClassName> [Args]

JAVA_VER=$(java -version 2>&1 | head -n 1 | awk -F '"' '{print $2}' | awk -F '.' '{print $1}')

JVM_FLAGS=""
if [ "$JAVA_VER" -ge 9 ]; then
    JVM_FLAGS="--add-modules java.se --add-exports java.base/jdk.internal.ref=ALL-UNNAMED --add-opens jdk.management/com.sun.management.internal=ALL-UNNAMED --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.util=ALL-UNNAMED --add-opens java.base/java.util.concurrent=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED --add-opens java.base/java.nio=ALL-UNNAMED --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.management/sun.management=ALL-UNNAMED -Dhazelcast.nio.string.creator.optimized=false"
fi

CP="lib/*:."

if [ -z "$1" ]; then
    echo "Usage: ./run_sim.sh <MainClassName> [Args]"
    echo "Example: ./run_sim.sh pt.inesc_id.gsd.cloud2sim.applications.main.statics.Simulator"
    exit 1
fi

MAIN_CLASS=$1
shift

java $JVM_FLAGS -cp "$CP" "$MAIN_CLASS" "$@"
EOF

chmod +x run_sim.sh

echo "--- Setup Complete ---"
echo "You can now run simulations using the './run_sim.sh' wrapper."
echo "Example:"
echo "  ./run_sim.sh pt.inesc_id.gsd.cloud2sim.applications.main.statics.Simulator"
