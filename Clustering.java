import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;

public class Clustering {
    private int[] clusterId; // Array to store the cluster ID for each point
    private int k; // num of clusters

    // Constructor to perform clustering
    public Clustering(Point2D[] locations, int k) {
        if (locations == null || k < 1 || k > locations.length) {
            throw new IllegalArgumentException();
        }
        this.k = k;  // Store the number of clusters

        int m = locations.length;
        clusterId = new int[m];
        EdgeWeightedGraph graph = new EdgeWeightedGraph(m);

        // Build a complete graph
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                double weight = locations[i].distanceTo(locations[j]);
                Edge edge = new Edge(i, j, weight);
                graph.addEdge(edge);
            }
        }

        // Compute the minimum spanning tree
        KruskalMST mst = new KruskalMST(graph);

        // Create a new graph using the MST but removing the largest m-k edges
        EdgeWeightedGraph clusterGraph = new EdgeWeightedGraph(m);
        int edgesAdded = 0;
        for (Edge e : mst.edges()) {
            if (edgesAdded < m - k) {
                clusterGraph.addEdge(e);
                edgesAdded++;
            }
            else {
                break;
            }
        }

        // Determine the connected components
        CC cc = new CC(clusterGraph);
        for (int i = 0; i < m; i++) {
            clusterId[i] = cc.id(i);
        }
    }

    // Returns the cluster of the i-th point
    public int clusterOf(int i) {
        if (i < 0 || i >= clusterId.length) {
            throw new IllegalArgumentException();
        }
        return clusterId[i];
    }

    // Reduces dimensions of input based on clusters
    // Reduces dimensions of input based on clusters
    public int[] reduceDimensions(int[] input) {
        if (input == null || input.length != clusterId.length) {
            throw new IllegalArgumentException(
                    "Input array is null or does not match the number of locations.");
        }

        // Initialize the reduced array with size k, since k clusters are expected
        int[] reduced = new int[k];  // Use k directly

        // Aggregate the inputs based on their cluster IDs
        for (int i = 0; i < input.length; i++) {
            reduced[clusterId[i]] += input[i];
        }
        return reduced;
    }


    // Unit testing
    public static void main(String[] args) {
        // Example usage
        Point2D[] locations = new Point2D[] {
                new Point2D(0.1, 0.2), new Point2D(0.4, 0.2),
                new Point2D(0.5, 0.8), new Point2D(0.9, 0.9)
        };
        Clustering clustering = new Clustering(locations, 2);
        for (int i = 0; i < locations.length; i++) {
            System.out.println("Point " + i + " is in cluster " +
                                       clustering.clusterOf(i));
        }

        int[] input = { 100, 150, 200, 250 };
        int[] reduced = clustering.reduceDimensions(input);
        for (int val : reduced) {
            System.out.println("Reduced dimension value: " + val);
        }
    }
}
