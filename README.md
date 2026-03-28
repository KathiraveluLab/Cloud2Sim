# Cloud2Sim

An Adaptive and Distributed Architecture for Cloud and MapReduce Algorithms and Simulations.

Cloud2Sim proposes a distributed concurrent architecture to CloudSim simulations.

## Building Cloud2Sim

To build Cloud2Sim from source using Maven:

```bash
mvn clean install
```

To skip tests during the build:

```bash
mvn clean install -DskipTests
```

The built distribution can be found in `distribution/target/`.

## Running Simulations

To run an existing simulation example:

```bash
java -classpath cloudsim-3.1-SNAPSHOT.jar:cloudsim-examples-3.1-SNAPSHOT.jar:lib/hazelcast-3.2.jar:cloud2sim-1.0-SNAPSHOT.jar pt.inesc_id.gsd.cloud2sim.applications.main.statics.Simulator
```

### Adaptive Scaling

Initiate the `ScalableSimulator` on the master node:

```bash
java -classpath cloudsim-3.1-SNAPSHOT.jar:lib/hazelcast-3.2.jar:cloud2sim-1.0-SNAPSHOT.jar pt.inesc_id.gsd.cloud2sim.applications.main.dynamics.ScalableSimulator
```

Initiate the `IntelligentAdaptiveScaler` on the worker nodes:

```bash
java -classpath cloudsim-3.1-SNAPSHOT.jar:lib/hazelcast-3.2.jar:cloud2sim-1.0-SNAPSHOT.jar pt.inesc_id.gsd.cloud2sim.scale.ias.IntelligentAdaptiveScaler
```

### Map-Reduce Simulations

Workload files are located in `conf/mapreduce/`.

#### Using Hazelcast

Set the map-reduce workload size in `conf/cloud2sim.properties`:

```properties
mapReduceSize=10
```

Run the simulator:

```bash
java -classpath cloudsim-3.1-SNAPSHOT.jar:lib/hazelcast-3.2.jar:cloud2sim-1.0-SNAPSHOT.jar pt.inesc_id.gsd.cloud2sim.mapreduce.hazelcast.HzMapReduceSimulator
```

#### Using Infinispan

Set the map-reduce workload size in `conf/infinispan.properties`:

```properties
mapReduceSize=10
```

Run the initiator:

```bash
java -classpath cloudsim-3.1-SNAPSHOT.jar:lib/*:cloud2sim-1.0-SNAPSHOT.jar pt.inesc_id.gsd.cloud2sim.infinispan.Initiator
```

Run the simulator:

```bash
java -classpath cloudsim-3.1-SNAPSHOT.jar:lib/*:cloud2sim-1.0-SNAPSHOT.jar pt.inesc_id.gsd.cloud2sim.mapreduce.infinispan.InfMapReduceSimulator
```

## Citing Cloud2Sim

If you use Cloud2Sim in your research, please cite the following papers: 

* Kathiravelu, P. & L. Veiga (2014). **An Adaptive Distributed Simulator for Cloud and MapReduce Algorithms and Architectures.** In IEEE/ACM 7th International Conference on Utility and Cloud Computing (UCC 2014), London, UK. pp. 79 – 88. IEEE Computer Society.

* Kathiravelu, P. & L. Veiga (2014). **Concurrent and Distributed CloudSim Simulations.** In IEEE 22nd International Symposium on Modeling, Analysis and Simulation of Computer and Telecommunication Systems (MASCOTS'14), Paris, France. pp. 490–493. IEEE Computer Society.
