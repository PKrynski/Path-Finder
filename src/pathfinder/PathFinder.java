package pathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        oneNeighbours[1] = new Edge(three, 8);

        one.neighbours = oneNeighbours;

        Edge[] twoNeighbours = new Edge[1];
        twoNeighbours[0] = new Edge(three, 4);

        two.neighbours = twoNeighbours;

        Edge[] threeNeighbours = new Edge[1];
        threeNeighbours[0] = new Edge(one, 1);

        three.neighbours = threeNeighbours;

        Vertex[] all = {one, two, three};

        System.out.println("Dostępne drogi:");
        calculatePathsFrom(one);
        System.out.println("");

        //System.out.println("\nAvailable paths from B:");
        //calculatePathsFrom(two);
        
        for (Vertex v : all) {
            System.out.println("Najlżejsza droga do " + v + " - " + v.minDist);
            List<Vertex> path = getShortestPathTo(v);
            System.out.println("Droga: " + path);
        }

        System.out.println("\nNajlżejsza droga z A do C:");
        
         for( Vertex v : getShortestPathTo(three) ) {
         System.out.print(" -> ");
         System.out.print(v);
         }
         System.out.println("");
         
        //System.out.println( getShortestPathTo(three) );
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

                System.out.println("Z: " + currentStart.toString() + " do: " + currentEnd.toString() + " - " + weight);

                double currentPathCost = currentStart.minDist + weight;
                
                if (currentPathCost < currentEnd.minDist) {

                    visited.remove(currentEnd);
                    currentEnd.minDist = currentPathCost;
                    currentEnd.previous = currentStart;
                    visited.add(currentEnd);
                }
            }

        }

    }

    public static List<Vertex> getShortestPathTo(Vertex destination) {

        List<Vertex> path = new ArrayList<>();

        Vertex current;

        for (current = destination; current != null; current = current.previous) {
            path.add(current);
        }

        Collections.reverse(path);

        return path;
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
