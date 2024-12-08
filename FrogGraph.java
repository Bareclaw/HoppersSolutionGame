import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/***
 * Represents a graph of frog arrangements and provides methods to manipulate and analyze the graph.
 */
public class FrogGraph {
    public static void main(String[] args) {
        int[][] testFrogs = new int[][]{{0, 0, 1, 0, 0},
                {0, 1, 0, 1, 0},
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1}};

        int[][] testFrogs_a = new int[][]{
                {0, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0}};
        int[][] testFrogs_b = new int[][]{{1, 0, 1, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0}};
        FrogArrangement myFrogs = new FrogArrangement(testFrogs);
        FrogArrangement myFrogs_a = new FrogArrangement(testFrogs_a);
        FrogArrangement myFrogs_b = new FrogArrangement(testFrogs_b);
        FrogGraph myGraph4 = new FrogGraph(myFrogs);
        myGraph4.printSolutions();
        System.out.println("This should say true: " + myGraph4.areAdjacent(myFrogs_a, myFrogs_b));

        ArrayDeque<Integer> solsArray = loadFrogs("Frog_inputs.csv");
        System.out.println("The following line should be 2 6 1 1 5 0 0 0 1:");
        while(!solsArray.isEmpty()){
            int curInt = solsArray.remove();
            System.out.print(curInt + " ");
        }
    }
    FrogArrangement startingArrangement; // This will be the starting arrangement from which you create your graph
    /***
     * You must use the predecessorMap below.
     * If Arrangement2 was "discovered" by an edge from Arrangement1,
     * then Arrangement 1 is the predecessor of Arrangement 2.
     * Using this predecessorMap will help you re-create the steps
     * to a winning arrangement.
     */
    HashMap<FrogArrangement, FrogArrangement> predecessorMap;
    /***
     * frogNeighbors corresponds to the adjacency list representation for this graph
     * frogNeighbors.get(someFrogs) should return a list (a queue) of frog arrangements
     * that someFrogs are adjacent to.  Remember that this is an undirected graph, so if
     * frogNeighbors.get(someFrogs) contains otherFrogs, then frogNeighbors.get(otherFrogs)
     * should contain someFrogs
     */
    HashMap<FrogArrangement, Queue<FrogArrangement>> frogNeighbors;
    /***
     * You don't *need* to have distFromStarting, but it doesn't hurt, and may be helpful
     * in debugging.  It should give the distance that a frog arrangement is from the startingArrangement.
     * Here, distance is the minimum number of edges traversed from the startingArrangement
     * to the frog arrangement.
     */
    HashMap<FrogArrangement, Integer> distFromStarting;
    /***
     * winningArrangements should contain all of the winning arrangements (with exactly 1 frog)
     * that can be reached by the startingArrangement
     */
    Queue<FrogArrangement> winningArrangements = new ArrayDeque<>();

    /***
     * Creates a frog graph based on the given starting arrangement
     * @param frog The starting frog arrangement
     */
    FrogGraph(FrogArrangement frog) {
        startingArrangement = frog;
        predecessorMap = new HashMap<>();
        frogNeighbors = new HashMap<>();
        distFromStarting = new HashMap<>();
        createGraph(frog);
    }

    /***
     * Creates the graph of the starting arrangements beginning from the frog arrangement
     * @param frog The starting frog arrangement.
     */
    protected void createGraph(FrogArrangement frog) {//the beginning of this code was done in class with Professor Ellen and I got some help from Carmen and Pratha
        FrogArrangement curFrogs = startingArrangement;
        Queue<FrogArrangement> curQueue = new ArrayDeque<>(); //Initialize a queue for neighbors of the current arrangement
        frogNeighbors.put(curFrogs, curQueue); //Put the current arrangement and its queue in the adjacency list
        Queue<FrogArrangement> bfsQueue = new ArrayDeque<>(); //Initialize a queue for breadth-first search
        bfsQueue.add(curFrogs); //Add the current arrangement to the BFS queue
        if(curFrogs.isWinningState()){
            winningArrangements.add(curFrogs);
            return;
        }
        // Populate the graph using breadth-first search
        while(!bfsQueue.isEmpty()){ //do the following in a while loop (while bfsQueue is not empty)
            curFrogs = bfsQueue.remove();
            for (int[] from : FrogArrangement.hopOptions.keySet()) {
                for (int[] over : FrogArrangement.hopOptions.get(from).keySet()) {
                    int[] to = FrogArrangement.hopOptions.get(from).get(over);
                    if(curFrogs.canHop(from, over, to)){
                        FrogArrangement newFrogs = new FrogArrangement(curFrogs.frogs); //deep copy of currentFrogs
                        newFrogs.hop(from, over, to);
                        if(!predecessorMap.containsKey(newFrogs)){ //Check if the new arrangement is already visited
                            bfsQueue.add(newFrogs);
                            predecessorMap.put(newFrogs, curFrogs); //map currentFrogs to its predecessor = curFrogs.
                            Queue<FrogArrangement> newQueue = new ArrayDeque<>();
                            frogNeighbors.put(newFrogs, newQueue);
                            if(newFrogs.isWinningState()){ //Check if the new arrangement is a winning state
                                winningArrangements.add(newFrogs);
                            }
                        }
                        frogNeighbors.get(newFrogs).add(curFrogs);
                        frogNeighbors.get(curFrogs).add(newFrogs);
                    }
                }
            }
        }
    }

    /***
     * Prints the graph of the frog arrangement and the neighbors.
     */
    public void printgraph(){
        // Call createGraph to ensure the graph is constructed
        createGraph(startingArrangement);// Iterate over each frog arrangement in the graph
        for (Map.Entry<FrogArrangement, Queue<FrogArrangement>> entry : frogNeighbors.entrySet()) {
            FrogArrangement frog = entry.getKey();
            Queue<FrogArrangement> neighbors = entry.getValue();
            System.out.println("Arrangement: " + frog); //Print the current frog arrangement
            System.out.println("Neighbors:");  //Print its neighbors
            for (FrogArrangement neighbor : neighbors) {
                System.out.println("- " + neighbor);
            }
            System.out.println();
        }
    }

    /***
     *Prints the winning arrangement and the steps that it took to get to the winning state.
     */
    public void printSolutions(){ //used the help of chatgpt to help understand how a stack would be useful here
        if (winningArrangements.isEmpty()) {
            System.out.println("Winning arrangement has not been found");
            return;
        }
        // Iterate past each winning arrangement
        Iterator<FrogArrangement> iterate = winningArrangements.iterator();
        while(iterate.hasNext()) {
            FrogArrangement winningArrangement = iterate.next();
            System.out.println("Winning Arrangement:"); // Print the winning arrangement
            winningArrangement.printFrogs();
            System.out.println();
            Stack<FrogArrangement> stackSteps = new Stack<>();
            FrogArrangement currentArrangement = winningArrangement;
            while (predecessorMap.containsKey(currentArrangement)) { //loop through and print out each step of the frog arrangement
                FrogArrangement predecessor = predecessorMap.get(currentArrangement);
                stackSteps.push(predecessor);
                currentArrangement = predecessor; // Move to the predecessor
            }
            while(!stackSteps.isEmpty()){
                stackSteps.pop().printFrogs();
                System.out.println();
            }
            winningArrangement.printFrogs(); //print the winning arrangement
            System.out.println();
        }
    }

    /***
     *Checks if the two frog arrangements can be reached by each other with only one hop (basically check if they are adjacent to each other).
     * @param frogs1 First frog arrangement
     * @param frogs2 Second frog arrangement
     * @return True if frogs1 and frogs2 are adjacent, else false
     */
    public boolean areAdjacent(FrogArrangement frogs1, FrogArrangement frogs2){//just test if frog 1 and frog 2 are in the adjacency list
        Queue<FrogArrangement> neighbors = frogNeighbors.get(frogs2);
        while(neighbors != null){ // Iterate over neighbors
            FrogArrangement neighbor = neighbors.poll();
            if(neighbor.equals(frogs1)){
                return true;
            }
            else{
                return false;
            }
        }
        return false;
    }

    /***
     * Loads the frog arrangements from a file and returns the number of winnung arrangements for each
     * @param frogFilename The file name that stores the frog arrangements.
     * @return The deque containing the number of winning arrangements for each arrangement that is input.
     */
    public static ArrayDeque<Integer> loadFrogs(String frogFilename){
        ArrayDeque<Integer> numOfSolutionsArray = new ArrayDeque<>();
        try {
            FileInputStream inStream = new FileInputStream(frogFilename);
            Scanner scanner = new Scanner(inStream);
            while(scanner.hasNextLine()) {
                int count = 0;
                int[][] curFrogs = new int[5][];
                while (count < 5 && scanner.hasNextLine()) {
                    String curLine = scanner.nextLine();
                    String[] clean = curLine.trim().split(",");
                    if (!(clean.length == 5)) {
                        System.out.println("Something is wrong with the input file");
                        return numOfSolutionsArray;
                    } else {
                        curFrogs[count] = new int[]{intValue(clean[0]), intValue(clean[1]), intValue(clean[2]), intValue(clean[3]), intValue(clean[4])};
                    }
                    count += 1;
                }
                if (count !=5){
                    System.out.println("Somehow we didn't get 5 lines");
                    return numOfSolutionsArray;
                }
                else{
                    FrogArrangement curArrangement = new FrogArrangement(curFrogs);
                    FrogGraph curGraph = new FrogGraph(curArrangement);
                    numOfSolutionsArray.add(curGraph.winningArrangements.size());
                }
            }
            return numOfSolutionsArray;
        } catch (IOException var9) {
            var9.printStackTrace();
            return numOfSolutionsArray;
        }
    }

    /***
     * Converts the string 0 or 1 to an integer
     * @param s The string that is converted
     * @return 0 of the string is "0", else return 1
     */
    private static int intValue(String s){
        if (s.equals("0")){
            return 0;
        }
        else{
            return 1;
        }
    }
}
