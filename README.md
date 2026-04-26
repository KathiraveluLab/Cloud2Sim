# Cloud2Sim

An Adaptive and Distributed Architecture for Cloud and MapReduce Algorithms and Simulations.

Cloud2Sim proposes a distributed concurrent architecture to CloudSim simulations.

## Building and Running Cloud2Sim

The easiest way to build and run the simulation is using the automated setup script.

### 1. Setup and Build
Run the setup script from the project root. This will compile the project with Maven and generate a `run_sim.sh` wrapper that handles all Java version-specific settings (like `--add-opens` for modern JDKs).

```bash
./setup.sh
```

### 2. Running Simulations
Use the generated `run_sim.sh` script to launch any simulation class. The script automatically manages the classpath and JVM arguments.

**Static Simulation:**
```bash
./run_sim.sh pt.inesc_id.gsd.cloud2sim.applications.main.statics.Simulator
```

**Adaptive Scaling:**
```bash
# Master node
./run_sim.sh pt.inesc_id.gsd.cloud2sim.applications.main.dynamics.ScalableSimulator

# Worker nodes
./run_sim.sh pt.inesc_id.gsd.cloud2sim.scale.ias.IntelligentAdaptiveScaler
```

**Map-Reduce Simulations:**
```bash
# Hazelcast
./run_sim.sh pt.inesc_id.gsd.cloud2sim.mapreduce.hazelcast.HzMapReduceSimulator

# Infinispan
./run_sim.sh pt.inesc_id.gsd.cloud2sim.infinispan.Initiator
./run_sim.sh pt.inesc_id.gsd.cloud2sim.mapreduce.infinispan.InfMapReduceSimulator
```

### Adaptive Scaling

Initiate the `ScalableSimulator` on the master node:

```bash
java $JAVA_OPTS -classpath "lib/*:." pt.inesc_id.gsd.cloud2sim.applications.main.dynamics.ScalableSimulator
```

Initiate the `IntelligentAdaptiveScaler` on the worker nodes:

```bash
java $JAVA_OPTS -classpath "lib/*:." pt.inesc_id.gsd.cloud2sim.scale.ias.IntelligentAdaptiveScaler
```

### Distributed Infinispan Simulation

Cloud2Sim leverages Infinispan's distributed data grid capabilities. You can run multiple instances of the Infinispan nodes to simulate a distributed environment.

1. **Initiator**: Acts as a cluster node. It can be used to hold data or serve as a persistent member of the grid.
   ```bash
   ./run_sim.sh pt.inesc_id.gsd.cloud2sim.infinispan.Initiator
   ```

2. **Simulator**: Joins the Infinispan cluster formed by the Initiator(s) and triggers the MapReduce tasks across all available nodes.
   ```bash
   ./run_sim.sh pt.inesc_id.gsd.cloud2sim.mapreduce.infinispan.InfMapReduceSimulator
   ```

*Note: You can run one Initiator and one Simulator on the same machine, or distribute them across different machines by updating the JGroups configuration in `conf/jgroups-tcp-config.xml` with actual IP addresses.*

### Map-Reduce Configuration

Workload files and dataset configurations are managed in `conf/cloud2sim.properties`:

*   `loadFolder`: Path to the input text files (e.g., `conf/mapreduce/load`).
*   `mapReduceSize`: The number of lines to process per file.
*   `isVerbose`: Set to `true` to see detailed word count results.

#### Using Hazelcast

Hazelcast supports distributed execution using an Initiator/Simulator pattern. You can form a cluster by running one or more Initiator nodes.

1. **Initiator**: Starts a standalone Hazelcast node to join the cluster.
   ```bash
   ./run_sim.sh pt.inesc_id.gsd.cloud2sim.hazelcast.HzInitiator
   ```

2. **Simulator**: Joins the Hazelcast cluster and executes the MapReduce simulation across all nodes.
   ```bash
   ./run_sim.sh pt.inesc_id.gsd.cloud2sim.mapreduce.hazelcast.HzMapReduceSimulator
   ```

### Java Compatibility
Cloud2Sim is configured for **Java 11** and newer. The `run_sim.sh` script automatically applies the necessary `--add-opens` flags required for Hazelcast and Infinispan to work on modern JDKs (9, 11, 17, 21+).

#### Using Infinispan
Follow the Initiator/Simulator pattern described above.

## Citing Cloud2Sim

If you use Cloud2Sim in your research, please cite the following papers: 

* Kathiravelu, P. & L. Veiga (2014). **An Adaptive Distributed Simulator for Cloud and MapReduce Algorithms and Architectures.** In IEEE/ACM 7th International Conference on Utility and Cloud Computing (UCC 2014), London, UK. pp. 79 – 88. IEEE Computer Society.

* Kathiravelu, P. & L. Veiga (2014). **Concurrent and Distributed CloudSim Simulations.** In IEEE 22nd International Symposium on Modeling, Analysis and Simulation of Computer and Telecommunication Systems (MASCOTS'14), Paris, France. pp. 490–493. IEEE Computer Society.
