package pathfinder;

import java.util.PriorityQueue;

/**
 *
 * @author Paweł
 */
public class PathFinder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Vertex one = new Vertex("A");
        Vertex two = new Vertex("B");
        Vertex three = new Vertex("C");

        Edge[] oneNeighbours = new Edge[2];
        oneNeighbours[0] = new Edge(two, 2);
        oneNeighbours[1] = new Edge(three, 5);
        
        one.neighbours = oneNeighbours;

        Edge[] twoNeighbours = new Edge[1];
        twoNeighbours[0] = new Edge(three, 4);
        
        two.neighbours = twoNeighbours;
        
        System.out.println("Available paths from A:");
        calculatePathsFrom(one);
        
        System.out.println("\nAvailable paths from B:");
        calculatePathsFrom(two);

    }

    public static void calculatePathsFrom(Vertex source) {

        source.minDist = 0;
        PriorityQueue<Vertex> visited = new PriorityQueue<>();

        visited.add(source);

        while (!visited.isEmpty()) {

            Vertex currentStart = visited.poll();

            for (Edge e : currentStart.neighbours) {

                Vertex currentEnd = e.end;
                double weight = e.weight;
                
                System.out.println("From: " + source.toString() + " to: " + currentEnd.toString() + " - " + weight);

                double currentPathCost = currentStart.minDist + weight;

                if (currentPathCost < currentStart.minDist) {

                    visited.remove(currentEnd);
                    currentEnd.minDist = currentPathCost;
                    currentEnd.previous = currentStart;
                    visited.add(currentEnd);
                }
            }

        }

    }

}

class Vertex implements Comparable<Vertex> {

    public String name;
    public Edge[] neighbours;
    public double minDist = Double.POSITIVE_INFINITY;
    public Vertex previous;

    public Vertex(String newName) {
        this.name = newName;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public int compareTo(Vertex second) {
        return Double.compare(this.minDist, second.minDist);
    }

}

class Edge {

    public Vertex end;
    public double weight;

    public Edge(Vertex destination, double w) {
        this.end = destination;
        this.weight = w;
    }
}
