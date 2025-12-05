import java.util.List;

public class BoykovKolmogorov {
    Integer numNodes;
    List<List<Integer>> capacities;
    List<List<Integer>> flows;
    List<List<Integer>> adjList;
    BoykovKolmogorov(Integer numNodes) {
        this.numNodes = numNodes;
        this.capacities = new java.util.ArrayList<>(numNodes);
        this.flows = new java.util.ArrayList<>(numNodes);
        adjList = new java.util.ArrayList<>(numNodes);
        for (int i = 0; i < numNodes; i++) {
            this.capacities.add(new java.util.ArrayList<>());
            this.flows.add(new java.util.ArrayList<>());
            this.adjList.add(new java.util.ArrayList<>());
            for (int j = 0; j < numNodes; j++) {
                this.capacities.get(i).add(0);
                this.flows.get(i).add(0);
            }
        }
    }
    public void addEdge(int from, int to, int capacity) {
        this.capacities.get(from).set(to, capacity);
        this.adjList.get(from).add(to);
        this.adjList.get(to).add(from);
    }
}