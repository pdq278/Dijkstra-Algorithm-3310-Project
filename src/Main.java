import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

// The following is a vertex class to go with our graph class.
class Vertex {
    private String name;
    private int x;
    private int y;

    // Constructor
    public Vertex(String name, int x, int y){
        this.name = name;
        this.x = x;
        this.y = y;
    }

    // Another constructor where x and y are random numbers between 20 and 480. However, no vertex can be within 20 pixels of another vertex.
    public Vertex(String name, Vertex[] vertices){
        this.name = name;
        this.x = (int)(Math.random() * 430) + 50;
        this.y = (int)(Math.random() * 430) + 50;
        for(Vertex v : vertices){
            if (v != null) {
                if (Math.abs(v.getX() - this.x) < 50 || Math.abs(v.getY() - this.y) < 50) {
                    this.x = (int) (Math.random() * 430) + 50;
                    this.y = (int) (Math.random() * 430) + 50;
                }
            }
        }
    }

    // Getters
    public String getName(){
        return name;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    // Setters
    public void setName(String name){
        this.name = name;
    }
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
}

// The following is a fully functional graph class.
class Graph {
    private int[][] adjMatrix; // The adjacency matrix
    public Vertex[] vertices; // The vertices
    private int[][] weights; // The weights

    private int vertexCount; // The number of vertices
    private int edgeCount; // The number of edges
    private int maxVertexCount; // The maximum number of vertices
    private int maxEdgeCount; // The maximum number of edges

    private String stringPath; // The string path

    public Graph(int maxVertices, int maxEdges) {
        this.vertices = new Vertex[maxVertices];
        this.adjMatrix = new int[maxVertices][maxVertices];
        this.maxVertexCount = maxVertices;
        this.maxEdgeCount = maxEdges;
        this.weights = new int[maxVertices][maxVertices];
        this.stringPath = "";
    }

    // Gets the number of vertices.
    public int getVertexCount() {
        return vertexCount;
    }

    // Gets the vertices of our graph.
    public Vertex[] getVertices() {
        return vertices;
    }

    // Adds a vertex to our graph.
    public void addVertex(Vertex vertex) {
        if (vertexCount == maxVertexCount) { // If the graph is full, we cannot add any more vertices.
            System.out.println("Graph is full");
            return;
        }
        vertices[vertexCount] = vertex; // Add the vertex to the graph.
        vertexCount++; // Increment the number of vertices.
    }

    public void addEdge(int from, int to, int weight) {
        if (edgeCount == maxEdgeCount) { // If the graph is full, we cannot add any more edges.
            System.out.println("Graph is full");
            return;
        }
        adjMatrix[from][to] = 1;
        adjMatrix[to][from] = 1;
        weights[from][to] = weight;
        edgeCount++;
        makeSymmetric(); // Ensures that if the edge from A to B is x, then the edge from B to A is also x.
    }

    // Another version of addEdge that randomizes the weight.
    public void addEdge(int from, int to) {
        if (edgeCount == maxEdgeCount) {
            System.out.println("Graph is full");
            return;
        }
        adjMatrix[from][to] = 1;
        adjMatrix[to][from] = 1;
        weights[from][to] = (int) (Math.random() * 100) + 1; // Randomizes the weight of the new edge.
        edgeCount++;
        makeSymmetric(); // Ensures that if the edge from A to B is x, then the edge from B to A is also x.
    }


    // Prints the path from the start vertex to the end vertex.
    public String getStringPath() {
        return stringPath;
    }

    // Gets the set of weights.
    public int[][] getWeights() {
        return weights;
    }

    // Gets the minimum distance between two vertices, accounting for edge weights.
    public int minDistance(int[] distance, boolean[] visited) {
        int min = Integer.MAX_VALUE; // Initialize min to max int.
        int minIndex = -1; // Initialize minIndex to -1.
        for (int i = 0; i < vertexCount; i++) { // Loop through all vertices.
            if (visited[i] == false && distance[i] <= min) { // If the vertex is not visited and the distance is less than min.
                min = distance[i]; // Set min to the distance.
                minIndex = i; // Set minIndex to the index.
            }
        }
        return minIndex;
    }

    // An implementation of Dijkstra's algorithm using our Graph class, accounting for edge weights. At the end, print the shortest path from start to destination.
    public int[] dijkstra(Graph graph, int start, int destination) {
        int[] distance = new int[vertexCount]; // Stores the distance from start to each vertex.
        boolean[] visited = new boolean[vertexCount]; // Stores whether or not a vertex has been visited.
        for (int i = 0; i < vertexCount; i++) { // Initialize the distance array.
            distance[i] = Integer.MAX_VALUE; // Set all distances to infinity.
            visited[i] = false; // Set all vertices to unvisited.
        }
        distance[start] = 0; // Set the distance from start to start to 0.
        for (int i = 0; i < vertexCount - 1; i++) { // Iterate through all vertices.
            int currentVertex = minDistance(distance, visited); // Get the vertex with the smallest distance.
            visited[currentVertex] = true; // Mark the vertex as visited.
            for (int j = 0; j < vertexCount; j++) { // Iterate through all vertices.
                if (!visited[j] && adjMatrix[currentVertex][j] != 0 && distance[currentVertex] != Integer.MAX_VALUE && distance[currentVertex] + weights[currentVertex][j] < distance[j]) { // If the vertex is unvisited, there is an edge between the two vertices, and the distance from start to the current vertex is not infinity, and the distance from start to the current vertex plus the edge weight is less than the distance from start to the vertex, and the edge weight is not 0 (meaning the edge is meant to an "exit", then update the distance from start to the vertex.
                    distance[j] = distance[currentVertex] + weights[currentVertex][j]; // Update the distance from start to the vertex.
                }
            }
        }
        System.out.println("Shortest path from " + vertices[start].getName() + " to " + vertices[destination].getName() + " is " + distance[destination]); // Print the shortest path from start to destination.


        // The following code is used to print the path from start to destination. It creates an ArrayList and adds the vertices in the shortest path to it. Then, it iterates through the ArrayList and prints the vertices in the path.
        ArrayList<Integer> path = new ArrayList<Integer>(); // Our ArrayList that will store the vertices in the shortest path.
        int currentVertex = destination; // Set the current vertex to the destination. We are working backwards from the destination to the start.
        path.add(currentVertex); // Add the destination to the ArrayList.
        while (currentVertex != start) { // While the current vertex is not the start vertex.
            int smallest = Integer.MAX_VALUE; // Initialize smallest to max int.
            int smallestIndex = -1; // Initialize smallestIndex to -1.
            for (int i = 0; i < vertexCount; i++) { // Loop through all vertices.
                if (adjMatrix[currentVertex][i] != 0 && distance[i] == distance[currentVertex] - weights[i][currentVertex] && distance[i] <= smallest) { // If there is an edge between the current vertex and the vertex, and the distance from start to the vertex is equal to the distance from start to the current vertex minus the edge weight, and the distance from start to the vertex is less than smallest, then set smallest to the distance from start to the vertex and smallestIndex to the index.
                    smallest = distance[i];
                    smallestIndex = i;
                }
            }
            currentVertex = smallestIndex; // Set the current vertex to the vertex with the smallest distance from start.
            path.add(currentVertex); // Add the current vertex to the ArrayList. This will always be the closest vertex to the start, and therefore the next vertex in the shortest path.
        }


        String pathString = "";
        System.out.println("Path: ");
        System.out.print("Begin");
        pathString = pathString + "Begin";
        for (int i = path.size() - 1; i >= 0; i--) {
            System.out.print("->");
            pathString = pathString + "->";
            System.out.print(vertices[path.get(i)].getName());
            pathString = pathString + vertices[path.get(i)].getName();
        }

        this.stringPath = pathString; // Please excuse the ugly code.

        this.stringPath = this.stringPath.replace("->O->S", "->O").replace("->S->O", "->S");

        // Returns an int[] of path.
        int[] pathArray = new int[path.size()];
        for (int i = 0; i < path.size(); i++) {
            pathArray[i] = path.get(i);
        }
        return pathArray;
    }

    // If the edge that goes from vertex i to vertex j has a weight of w, then the edge that goes from vertex j to vertex i should have a weight of w. If that is not the case, make it so. If either has a weight of 0, it should be overwritten with the other.
    public void makeSymmetric() {
        for (int i = 0; i < vertexCount; i++) { // For each vertex
            for (int j = 0; j < vertexCount; j++) { // For each vertex
                if (weights[i][j] == 0 && weights[j][i] != 0) { // If the edge from i to j has a weight of 0 and the edge from j to i has a weight of something other than 0
                    weights[i][j] = weights[j][i]; // Set the edge from i to j to have the same weight as the edge from j to i
                } else if (weights[i][j] != 0 && weights[j][i] == 0) { // If the edge from i to j has a weight of something other than 0 and the edge from j to i has a weight of 0
                    weights[j][i] = weights[i][j]; // Set the edge from j to i to have the same weight as the edge from i to j
                }else if (weights[i][j] != weights[j][i]) { // If the edge from i to j has a weight has a weight not equal to that of the edge from j to i
                    weights[j][i] = weights[i][j]; // Set the edge from j to i to have the same weight as the edge from i to j
                }
            }
        }
    }

    // Gets the adjacency matrix of our graph.
    public int[][] getAdjMatrix() {
        return adjMatrix;
    }

    // Prints the adjacency matrix of the graph.
    public void printAdjMatrix() {
        for (int i = 0; i < vertexCount; i++) {
            for (int j = 0; j < vertexCount; j++) {
                System.out.print(adjMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Prints the adjacency matrix of the graph with the names of the vertices at the top of the row and the left of the column.
    public void printAdjMatrixWithNames() {
        System.out.print("\n  ");
        for (int i = 0; i < vertexCount; i++) {
            System.out.print(vertices[i].getName() + " ");
        }
        System.out.println();
        for (int i = 0; i < vertexCount; i++) {
            System.out.print(vertices[i].getName() + " ");
            for (int j = 0; j < vertexCount; j++) {
                System.out.print(adjMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // This method effectively removes a connection between two vertices.
    public void restrict(int from, int to) {
        this.adjMatrix[from][to] = 0;
        this.adjMatrix[to][from] = 0;
    }

}

// Implements graphPanel class.
class GraphPanel extends JPanel {

    Graph graph = null;

    // Creates a graphPanel object.
    public GraphPanel(Graph g) {
        this.graph = g;
        setBackground(Color.WHITE);
    }

    // Draws the graph such that vertices are black elipses and edges are black lines connecting them. Edge weights are displayed on the edges if they are not 0.
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int[][] adjMatrix = graph.getAdjMatrix();
        for (int i = 0; i < graph.getVertexCount(); i++) {
            for (int j = 0; j < graph.getVertexCount(); j++) {
                if (adjMatrix[i][j] == 1) {
                    g.setColor(Color.BLACK);
                    if (graph.getWeights()[i][j] != 0) {
                        g.drawLine(graph.getVertices()[i].getX(), graph.getVertices()[i].getY(), graph.getVertices()[j].getX(), graph.getVertices()[j].getY());
                        g.setColor(Color.RED);
                        g.drawString(Integer.toString(graph.getWeights()[i][j]), (graph.getVertices()[i].getX() + graph.getVertices()[j].getX()) / 2, (graph.getVertices()[i].getY() + graph.getVertices()[j].getY()) / 2);
                    }
                }
            }
        }
        for (int i = 0; i < graph.getVertexCount(); i++) {
            g.setColor(Color.BLACK);
            g.fillOval(graph.getVertices()[i].getX() - 5, graph.getVertices()[i].getY() - 5, 10, 10);
            // Draws the names of the vertices to the left of the vertices, in green.
            g.setColor(Color.BLUE);
            g.drawString(graph.getVertices()[i].getName(), graph.getVertices()[i].getX() - 14, graph.getVertices()[i].getY() + 5);
        }
    }
}

public class Main {


    // Pseudocode of Dijkstra's algorithm.
    // 1. Initialize the distance array. This step is to initialize the distance array to infinity, since we don't know the distance from start to any other vertex.
    // 2. Initialize the visited array. This step is to initialize the visited array to false, since we haven't visited any vertices yet.
    // 3. Set the distance from start to start to 0. This step is to set the distance from start to start to 0, since we know that the distance from start to start is 0.
    // 4. Iterate through all vertices.
    // 5. Get the vertex with the smallest distance by iterating through all vertices, checking if the vertex is unvisited and the distance is less than min. If so, set min to the distance and minIndex to the index. The minIndex after this process is the vertex with the smallest distance. This step is to get the vertex with the smallest distance, since we want to find the shortest path from start to destination.
    // 6. Mark the vertex as visited. This step is to mark the vertex as visited, since we have visited the vertex with the smallest distance.
    // 7. For each vertex, we iterate through all vertices.
    // 8. If the vertex is unvisited, there is an edge between the two vertices, and the distance from start to the current vertex is not infinity, and the distance from start to the current vertex plus the edge weight is less than the distance from start to the vertex, update the distance from start to the vertex. This step is to fix the distance from start to the vertex if the distance from start to the current vertex plus the edge weight is less than the distance from start to the vertex.


    // Draws a visually representation of our Graph class.
    public static void drawGraph(Graph graph) {
        int[][] adjMatrix = graph.getAdjMatrix(); // Gets the adjacency matrix of the graph.
        for (int i = 0; i < graph.getVertexCount(); i++) { // For each vertex in the graph.
            for (int j = 0; j < graph.getVertexCount(); j++) { // For each vertex in the graph.
                if (adjMatrix[i][j] == 1) { // If the edge exists.
                    //System.out.println("Draw edge from " + graph.getVertices()[i].getName() + " to " + graph.getVertices()[j].getName()); // Prints the edge.
                }
            }
        }
    }

    // Using Swing, open a new window and in it, paint a visual representation of our Graph class.
    public static JFrame visualizeGraph(Graph graph, String path) {
        JFrame frame = new JFrame(); // Creates a new JFrame.

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Sets the default close operation to exit the program.
        frame.setSize(500, 550); // Sets the size of the JFrame.
        frame.setVisible(true); // Makes the JFrame visible.
        frame.add(new JLabel("Dijkstra's Algorithm"), BorderLayout.NORTH); // Adds a JLabel to the JFrame for our text.
        frame.add(new JLabel("Collins, Bales - 3310 Project | " + path), BorderLayout.SOUTH); // Adds a JLabel to the JFrame for our text.
        frame.add(new GraphPanel(graph)); // Adds a GraphPanel to the JFrame, our custom class that draws the graph.

        // Add a black border around the JFrame.
        frame.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        return frame;
    }


    public static void main(String[] args) {
        int extraX = 45;
        int extraY = 0;
        int xDiv = 3;
        int yDiv = 2;

        // Create a graph of 5 vertices and 5 edges using our Graph class.
        Graph graph = new Graph(20, 27); // 5 vertices, 7 edges.
        graph.addVertex(new Vertex("A", 100/xDiv + extraX, 150/yDiv + extraY)); // 0
        graph.addVertex(new Vertex("B", 335/xDiv + extraX, 150/yDiv + extraY)); // 1
        graph.addVertex(new Vertex("C", 565/xDiv + extraX, 150/yDiv + extraY)); // 2
        graph.addVertex(new Vertex("D", 785/xDiv + extraX, 150/yDiv + extraY)); // 3
        graph.addVertex(new Vertex("E", 1015/xDiv + extraX, 150/yDiv + extraY)); // 4

        graph.addVertex(new Vertex("F", 100/xDiv + extraX, 250/yDiv + extraY)); // 5
        graph.addVertex(new Vertex("G", 180/xDiv + extraX, 250/yDiv + extraY)); // 6
        graph.addVertex(new Vertex("H", 335/xDiv + extraX, 250/yDiv + extraY)); // 7
        graph.addVertex(new Vertex("I", 785/xDiv + extraX, 250/yDiv + extraY)); // 8
        graph.addVertex(new Vertex("J", 1015/xDiv + extraX, 250/yDiv + extraY)); // 9


        graph.addVertex(new Vertex("T", 180/xDiv + extraX, 365/yDiv + extraY)); // 10
        graph.addVertex(new Vertex("K", 335/xDiv + extraX, 365/yDiv + extraY)); // 11
        graph.addVertex(new Vertex("L", 565/xDiv + extraX, 365/yDiv + extraY)); // 12


        graph.addVertex(new Vertex("M", 785/xDiv + extraX, 510/yDiv + extraY)); // 13
        graph.addVertex(new Vertex("N", 1015/xDiv + extraX, 510/yDiv + extraY)); // 14

        graph.addVertex(new Vertex("O", 100/xDiv + extraX, 595/yDiv + extraY)); // 15
        graph.addVertex(new Vertex("P", 335/xDiv + extraX, 595/yDiv + extraY)); // 16
        graph.addVertex(new Vertex("Q", 565/xDiv + extraX, 595/yDiv + extraY)); // 17
        graph.addVertex(new Vertex("R", 785/xDiv + extraX, 595/yDiv + extraY)); // 18
        graph.addVertex(new Vertex("S", 1015/xDiv + extraX, 595/yDiv + extraY)); // 19


        graph.addEdge(0, 1, 235); // A -> B
        graph.addEdge(1, 2, 230); // B -> C
        graph.addEdge(2, 3, 220); // C -> D
        graph.addEdge(3, 4, 230); // D -> E
        graph.addEdge(5, 6, 80); // F -> G
        graph.addEdge(6, 7, 155); // G -> H
        graph.addEdge(8, 9, 230); // I -> J
        graph.addEdge(10, 11, 155); // T -> K
        graph.addEdge(11, 12, 230); // K -> L
        graph.addEdge(13, 14, 230); // M -> N
        graph.addEdge(15, 16, 235); // O -> P
        graph.addEdge(17, 18, 220); // Q -> R
        graph.addEdge(18, 19, 230); // R -> S

        graph.addEdge(0, 5, 100); // A -> F
        graph.addEdge(5, 15, 345); // F -> O
        graph.addEdge(6, 10, 115); // G -> T
        graph.addEdge(1, 7, 100); // B -> H
        graph.addEdge(11, 16, 225); // K -> P
        graph.addEdge(2, 12, 215); // C -> L
        graph.addEdge(12, 17, 255); // L -> Q
        graph.addEdge(3, 8, 100); // D -> I
        graph.addEdge(8, 13, 260); // I -> M
        graph.addEdge(13, 18, 110); // M -> R
        graph.addEdge(4, 9, 100); // E -> J
        graph.addEdge(9, 14, 260); // J -> N
        graph.addEdge(14, 19, 110); // N -> S

        // Edge from S to O.
        graph.addEdge(19, 15, 0); // S and O are our "exits," so in order for the algorithm to work, we need to add an edge from S to O with a weight of 0 so as to allow either S or O to be the "exit" vertex.

        // Restrictions. These correspond to obstructions in the graph.
        //graph.restrict(5, 15); // F and O
        // L and K
        //graph.restrict(12, 11);
        // D and E
        //graph.restrict(3, 4);
        // D and I
        //graph.restrict(3, 8);



        // The destination can be either vertex 19 or vertex 15, corresponding to S or O, our "exits."
        // It effectively means that the destination vertex is the closest "exit" vertex, since if we arrive to either S or O, we also arrive at the other, since they have a distance of 0 between each other.
        // Therefore, we should NEVER have a destination vertex value of anything but 19 or 15 if we are wanting the exit.
        // List of vertex indices and their respective names:
        // 0 = A, 1 = B, 2 = C, 3 = D, 4 = E, 5 = F, 6 = G, 7 = H, 8 = I, 9 = J, 10 = T, 11 = K, 12 = L, 13 = M, 14 = N, 15 = O, 16 = P, 17 = Q, 18 = R, 19 = S
        graph.dijkstra(graph, 3, 19);

        // Print the adjacency matrix.
        //graph.printAdjMatrixWithNames();

        // Print the adjacency list.
        drawGraph(graph);



        // Calculate the shortest path and store the results in a string.
        String ourPath = "Shortest Path: " + graph.getStringPath();

        // Visualize the graph.
        visualizeGraph(graph, ourPath);



    }
}
