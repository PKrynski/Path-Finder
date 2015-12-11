package pathfinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 *
 * @author Paweł
 */
public class PathFinder {

    private ArrayList<Vertex> allNodes = new ArrayList<>();

    public void addVertex(Vertex newVertex) {

        this.allNodes.add(newVertex);
    }

    public Vertex getVertex(String vertexName) {

        for (Vertex v : allNodes) {
            if (v.name.equals(vertexName)) {
                return v;
            }
        }

        Vertex theOne = new Vertex(vertexName);

        this.addVertex(theOne);

        return theOne;
    }

    public void displayPaths() {

        for (Vertex v : allNodes) {
            System.out.println("Najlżejsza droga do " + v + " - " + v.minDist);
            List<Vertex> path = getShortestPathTo(v);
            System.out.println("Droga: " + path);
        }
    }

    public void readFileData(String filename) throws FileNotFoundException {

        File myFile = new File(filename);
        Scanner fileInput = new Scanner(myFile);

        while (fileInput.hasNext()) {

            String vertexName = fileInput.next();
            Vertex inputVertex = getVertex(vertexName);

            int numberOfEdges = fileInput.nextInt();
            fileInput.next();

            for (int i = 0; i < numberOfEdges; i++) {

                String targetName = fileInput.next();
                double edgeWeight = Double.parseDouble(fileInput.next());

                Vertex targetVertex = this.getVertex(targetName);

                inputVertex.neighbours.add(new Edge(targetVertex, edgeWeight));
            }

        }

        /*
         Vertex one = new Vertex("A");
         Vertex two = new Vertex("B");
         Vertex three = new Vertex("C");

         ArrayList<Edge> oneNeighbours = new ArrayList<>();
         oneNeighbours.add(new Edge(two, 2));
         oneNeighbours.add(new Edge(three, 8));

         one.neighbours = oneNeighbours;

         ArrayList<Edge> twoNeighbours = new ArrayList<>();
         twoNeighbours.add(new Edge(three, 4));

         two.neighbours = twoNeighbours;

         ArrayList<Edge> threeNeighbours = new ArrayList<>();
         threeNeighbours.add(new Edge(one, 1));

         three.neighbours = threeNeighbours;

         this.addVertex(one);
         this.addVertex(two);
         this.addVertex(three);
         */
    }

    public static void main(String[] args) {

        PathFinder pathFinder = new PathFinder();

        String filename = args[0];

        try {
            pathFinder.readFileData(filename);
        } catch (FileNotFoundException ex) {
            System.out.println("Podany plik nie istnieje: " + filename);
            return;
        }

        System.out.println("Dostępne drogi:");
        calculatePathsFrom(pathFinder.allNodes.get(0));
        System.out.println("");

        pathFinder.displayPaths();

        //System.out.println("\nAvailable paths from B:");
        //calculatePathsFrom(two);
        System.out.println("\nNajlżejsza droga z A do C:");

        for (Vertex v : getShortestPathTo(pathFinder.allNodes.get(2))) {
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
    public ArrayList<Edge> neighbours = new ArrayList<>();
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
