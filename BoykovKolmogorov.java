import java.util.List;
import java.util.Queue;
import java.util.ArrayDeque;

public class BoykovKolmogorov {
    Integer numNodes;
    List<List<Integer>> capacities;
    List<List<Integer>> flows;
    List<List<Integer>> adjList;
    static final int FREE = 0;
    static final int S_TREE = 1;
    static final int T_TREE = -1;
    Integer finalMaxFlow = -1;

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
    public Integer getCapacity(int from, int to) {
        return this.capacities.get(from).get(to);
    }
    public Integer residual(int from, int to) {
        return this.capacities.get(from).get(to) - this.flows.get(from).get(to);
    }
    public Integer maxFlow(int source, int sink) {
        Integer[] tree = new Integer[this.numNodes];
        Integer[] parent = new Integer[this.numNodes];
        Queue<Integer> active = new ArrayDeque<>();
        Queue<Integer> orphans = new ArrayDeque<>();
        tree[source] = S_TREE;
        tree[sink] = T_TREE;
        active.add(source);
        active.add(sink);
        Integer totalflow = 0;
        while(true) {
            int metP = -1, metQ = -1;
            while(!active.isEmpty() && metP == -1) {
                int p = active.poll();
                for (int q : this.adjList.get(p)) {
                    if (residual(p, q) > 0) {
                        if (tree[q] == FREE) {
                            tree[q] = tree[p];
                            parent[q] = p;
                            active.add(q);
                        } else if (tree[q] != tree[p]) {
                            metQ = q;
                            metP = p;
                            break;
                        }
                    }
                }
            }
            if (metQ == -1) {
                break;
            }
            int bottleneck = Integer.MAX_VALUE;
            int p = metP;
            int q = metQ;

            int v = p;
            while (v != source && v != sink) {
                int u = parent[v];
                bottleneck = Math.min(bottleneck, residual(u, v));
                v = u;
            }
            if(v!=source)
                bottleneck = Math.min(bottleneck, residual(v, sink));

            v = q;
            while (v != source && v != sink) {
                int u = parent[v];
                bottleneck = Math.min(bottleneck, residual(u, v));
                v = u;
            }
            if(v!=sink)
                bottleneck = Math.min(bottleneck, residual(source, v));

            v = p;
            while (v != source && v != sink) {
                int u = parent[v];
                this.flows.get(u).set(v, this.flows.get(u).get(v) + bottleneck);
                this.flows.get(v).set(u, this.flows.get(v).get(u) - bottleneck);
                if (residual(u, v) == 0) {
                    orphans.add(v);
                }
                v = u;
            }
            v = q;
            while (v != source && v != sink) {
                int u = parent[v];
                this.flows.get(v).set(u, this.flows.get(v).get(u) + bottleneck);
                this.flows.get(u).set(v, this.flows.get(u).get(v) - bottleneck);
                if (residual(u, v) == 0) {
                    orphans.add(v);
                }
                v = u;
            }
            totalflow += bottleneck;

            while (!orphans.isEmpty()) {
                int orphan = orphans.poll();
                parent[orphan] = -1;

                boolean foundNewParent = false;

                for(int u : this.adjList.get(orphan)) {
                    if (tree[u] == tree[orphan] && residual(u, orphan) > 0) {
                        parent[orphan] = u;
                        foundNewParent = true;
                        break;
                    }
                }
                if (!foundNewParent){
                    tree[orphan] = FREE;
                    for (int u : this.adjList.get(orphan)) {
                        if (tree[u] == tree[orphan]) {
                            if (parent[u] != null && parent[u] == orphan) {
                                orphans.add(u);
                            }
                        } else if (tree[u] != FREE) {
                            active.add(u);
                        }
                    }
                }
            }
        }
        this.finalMaxFlow = totalflow;
        return totalflow;
    }

    public Integer previousFlow() {
        return this.finalMaxFlow;
    }

    public void resetFlow(){
        this.finalMaxFlow = -1;
        for (int i = 0; i < numNodes; i++) {
            for (int j = 0; j < numNodes; j++) {
                flows.get(i).set(j, 0);
            }
        }
    }
}