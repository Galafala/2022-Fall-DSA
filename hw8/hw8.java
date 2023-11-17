import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.PrimMST;
import java.util.List;



class Budget {
    public Budget() {}

    public int plan(int island, List<int[]> bridge) {
        EdgeWeightedGraph G = new EdgeWeightedGraph(island);
        for (int[] b: bridge) {
            Edge e = new Edge(b[0], b[1], b[2]);
            G.addEdge(e);
        }
        PrimMST mst = new PrimMST(G);
        return (int) mst.weight();
    }
}