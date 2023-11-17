import edu.princeton.cs.algs4.DijkstraSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;

import java.util.ArrayList;
import java.util.List;


class Flood {
    public Flood() {};
    //return which village is the latest one flooded
    public int village(int villages, List<int[]> road) {
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(villages);

        for (int[] r: road) {
            G.addEdge(new DirectedEdge(r[0], r[1], r[2]));
        }

        DijkstraSP sp = new DijkstraSP(G, 0);

        double distance = 0;
        int ans = 0;
        for (int v = 0; v < villages; v++) {
            if (!sp.hasPathTo(v)) continue;

            if (sp.distTo(v) > distance) {
                distance = sp.distTo(v);
                ans = v;
            }
        }


        return ans;
    }
}