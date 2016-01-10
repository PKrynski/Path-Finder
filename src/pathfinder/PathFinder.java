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

            if (v.minDist == Double.POSITIVE_INFINITY) {
                System.out.println("Brak drogi do " + v);
            } else {
                System.out.println("Najlżejsza droga do " + v + " - waga: " + v.minDist);
                List<Vertex> path = getShortestPathTo(v);
                System.out.println("Droga: " + path);
            }

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

    }

    public void calculatePathsFrom(Vertex source) {

        source.minDist = 0;
        PriorityQueue<Vertex> visited = new PriorityQueue<>();

        visited.add(source);

        while (!visited.isEmpty()) {

            Vertex currentStart = visited.poll();

            for (Edge e : currentStart.neighbours) {

                Vertex currentEnd = e.end;
                double weight = e.weight;

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

    public List<Vertex> getShortestPathTo(Vertex destination) {

        List<Vertex> path = new ArrayList<>();

        Vertex current;

        for (current = destination; current != null; current = current.previous) {
            path.add(current);
        }

        Collections.reverse(path);

        return path;
    }

    public void showInput() {

        System.out.println("Wszystkie dostępne bezpośrednie drogi:");

        for (Vertex v : allNodes) {

            for (Edge e : v.neighbours) {
                System.out.println("Z: " + v.toString() + " do: " + e.end.toString() + " - " + e.weight);
            }
        }
    }

    public static void main(String[] args) {

        if (args.length > 0) {

            PathFinder pathFinder = new PathFinder();

            String filename = args[0];

            try {
                pathFinder.readFileData(filename);
                System.out.println("Plik wejściowy: " + filename);
                pathFinder.showInput();
            } catch (FileNotFoundException ex) {
                System.err.println("Podany plik nie istnieje: " + filename);
                return;
            } catch (Exception ex) {
                System.err.println("Dane w podanym pliku mają nieprawidłowy format: " + filename);
                return;
            }

            if (args.length > 1) {

                String source = args[1];

                pathFinder.calculatePathsFrom(pathFinder.getVertex(source));
                System.out.println("");

                if (args.length == 2) {

                    System.out.println("Drogi z " + source + ":");
                    pathFinder.displayPaths();
                }

                if (args.length == 3) {

                    String destination = args[2];
                    System.out.println("Najlżejsza droga z " + source + " do " + destination + ":");

                    Vertex end = pathFinder.getVertex(destination);

                    if (end.minDist == Double.POSITIVE_INFINITY) {
                        System.out.println("Tych punktów nie łączy żadna droga!");
                    } else {

                        for (Vertex v : pathFinder.getShortestPathTo(end)) {
                            System.out.print(" -> ");
                            System.out.print(v);
                        }
                        System.out.println("");

                        System.out.println("waga drogi: " + end.minDist);
                    }

                }

            } else {
                System.out.println("\nWczytano dane wejściowe, jednak nie podałeś punktu początkowego ani końcowego.");
                System.out.println("Punkt początkowy podaj jako drugi argument");
                System.out.println("Punkt końcowy podaj jako trzeci argument.");
                System.out.println("Jeśli nie podasz punktu końcowego wypisane zostaną najkorzystniejsze"
                                   + " ścieżki do wszystkich pozostałych wierzchołków. ");
            }

        } else {
            System.out.println("Podano za mało argumentów.");
            System.out.println("Jako pierwszy argument podaj nazwę pliku zawierającego dane wejściowe.");
            System.out.println("Drugi argument: punkt początkowy.");
            System.out.println("Trzeci argument: punkt końcowy.");
            System.out.println("Jeśli nie podasz punktu końcowego wypisane zostaną najkorzystniejsze"
                               + " ścieżki do wszystkich pozostałych wierzchołków. ");
        }

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
