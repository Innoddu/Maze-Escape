package byow.Core;

import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @SOURCE from lecture silde spanning tree.
 */
public class KruskalMST {

    public List<Edge> mst = new ArrayList<Edge>();
    public HashMap<Integer, Integer> roomMap = new HashMap<>();

    public KruskalMST(EdgeWeightedGraph G) {
        MinPQ<Edge> pq = new MinPQ<Edge>();
        for (Edge e : G.edges()) {
            pq.insert(e);
        }
        WeightedQuickUnionUF uf = new WeightedQuickUnionUF(G.V());

        while (!pq.isEmpty() && mst.size() < G.V() - 1) {
            Edge e = pq.delMin();
//            System.out.println("pq.del" + e);
//            StdOut.println("either:" + e.either());
//            StdOut.println("ohter:" + e.other(e.either()));
            int v = e.either();
            int w = e.other(v);
            if (!uf.connected(v, w)) {
                uf.union(v, w);
                mst.add(e);
                roomMap.put(v, w);
            }
        }
        System.out.println("mst: " + mst);
        System.out.println(uf);
    }
}
