import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/***
 * Represents an arrangement of frogs on a graph
 */
public class FrogArrangement {
    static Map<int[], Map<int[], int[]>> hopOptions = Collections.unmodifiableMap(new HashMap<int[], Map<int[], int[]>>() {{
        put(new int[]{0, 0}, new HashMap<int[], int[]>() { //Hop options for position (0, 0)
            {
                put(new int[]{0, 2}, new int[]{0, 4}); // {0,2} is the position we are skipping, {0,4} is the position we land at
                put(new int[]{2, 0}, new int[]{4, 0});
                put(new int[]{1, 1}, new int[]{2, 2});
            }});
        put(new int[]{2, 0}, new HashMap<int[], int[]>() {{//Hop options for position (2, 0)
            put(new int[]{3,1}, new int[]{4,2});
            put(new int[]{1,1}, new int[]{0,2});
            put(new int[]{2,2}, new int[]{2,4});
        }});
        put(new int[]{4, 0}, new HashMap<int[], int[]>() {{ //Hop options for position (4, 0)
            put(new int[]{2,0}, new int[]{0,0});
            put(new int[]{3,1}, new int[]{2,2});
            put(new int[]{4,2}, new int[]{4,4});
        }});
        put(new int[]{1, 1}, new HashMap<int[], int[]>() {{ //Hop options for position (1, 1)
            put(new int[]{2,2}, new int[]{3,3});
        }});
        put(new int[]{3, 1}, new HashMap<int[], int[]>() {{//Hop options for position (3,1)
            put(new int[]{2,2}, new int[]{1,3});
        }});
        put(new int[]{0, 2}, new HashMap<int[], int[]>() {{//Hop options for position (0, 2)
            put(new int[]{1,3}, new int[]{2,4});
            put(new int[]{2,2}, new int[]{4,2});
            put(new int[]{1,1}, new int[]{2,0});
        }});
        put(new int[]{2, 2}, new HashMap<int[], int[]>() {{//Hop options for position (2, 2)
            put(new int[]{1,3}, new int[]{1,4});
            put(new int[]{1,1}, new int[]{0,0});
            put(new int[]{3,3}, new int[]{4,4});
            put(new int[]{3,1}, new int[]{4,0});
        }});
        put(new int[]{4, 2}, new HashMap<int[], int[]>() {{//Hop options for position (4, 2)
            put(new int[]{3,1}, new int[]{2,0});
            put(new int[]{2,2}, new int[]{0,2});
            put(new int[]{3,3}, new int[]{2,4});
        }});
        put(new int[]{1, 3}, new HashMap<int[], int[]>() {{//Hop options for position (1, 3)
            put(new int[]{2,2}, new int[]{3,1});
        }});
        put(new int[]{3,3}, new HashMap<int[], int[]>() {{//Hop options for position (3, 3)
            put(new int[]{2,2}, new int[]{1,1});
        }});
        put(new int[]{0,4}, new HashMap<int[], int[]>() {{//Hop options for position (0, 4)
            put(new int[]{0,2}, new int[]{0,0});
            put(new int[]{2,4}, new int[]{4,4});
            put(new int[]{1,3}, new int[]{2,2});
        }});
        put(new int[]{2,4}, new HashMap<int[], int[]>() {{ //Hop options for position (2, 4)
            put(new int[]{1,3}, new int[]{0,2});
            put(new int[]{2,2}, new int[]{2,0});
            put(new int[]{3,3}, new int[]{4,2});
        }});
        put(new int[]{4, 4}, new HashMap<int[], int[]>() {{ //Hop options for position (4, 4)
            put(new int[]{2,4}, new int[]{0,4});
            put(new int[]{3,3}, new int[]{2,2});
            put(new int[]{4,2}, new int[]{4,0});
        }});
    }});
    int[][] frogs;

    /***
     * Creates a FrogArrangement with the given arrangement of frogs
     * I used this link for help deep copying an array https://www.geeksforgeeks.org/deep-copy-of-2d-array-in-java/
     * @param inputFrogs The arrangement of frogs
     */
    FrogArrangement(int[][] inputFrogs){
        int rows = inputFrogs.length;
        int columns = inputFrogs[0].length;
        frogs = new int[rows][columns];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                this.frogs[i][j] = inputFrogs[i][j];
            }
        }
    }
    /***
     * Checks if a frog can hop from one position to another.
     * @param from The starting position of the frog.
     * @param over The position the frog jumps over.
     * @param to The landing position of the frog.
     * @return True if the frog can hop to the specified position, else false.
     */
    public boolean canHop(int[] from, int[] over, int[] to){
        if(this.frogs[from[0]][from[1]] == 1 && this.frogs[over[0]][over[1]] == 1 && this.frogs[to[0]][to[1]] == 0){ //Check if a frog can hop from one position to another.
            return true;
        }
        else{
            return false;
        }
    }

    /***
     * Moves a frog from one position to another.
     * @param from The starting position of the frog.
     * @param over The position the frog jumps over.
     * @param to The landing position of the frog.
     */
    public void hop(int[] from, int[] over, int[] to){
        this.frogs[from[0]][from[1]] = 0;
        this.frogs[over[0]][over[1]] = 0;
        this.frogs[to[0]][to[1]] = 1;
    }

    /***
     * Checks if the current frog arrangement is a winning state.
     * @return True if the arrangement is a winning state, else false.
     */
    public boolean isWinningState(){
        int count = 0;
        for(int i = 0; i < frogs.length; i++){ // Check if the current frog arrangement is a winning state.
            for(int j = 0; j < frogs[i].length; j++){
                if(frogs[i][j] == 1){
                    count++;
                }
            }
        }
        if(count == 1){
            return true;
        }
        else {
            return false;
        }
    }

    /***
     * Prints out the current arrangement of frogs.
     */
    public void printFrogs(){
            for(int j = 0; j < frogs.length; j++){  //Print out the current arrangement of frogs.
                System.out.println(Arrays.toString(frogs[j]));
            }
            System.out.println();
    }

    /***
     * Calculates the hash code for the frog arrangement.
     * @param f The frog arrangement.
     * @return The hash code that was calculated.
     */
    private int calculateHash(int[][] f){
        int hash = 0;
        for (int i=0; i<5; i++){
            for (int j=0; j<5; j++){
                hash = (int) (hash + f[i][j]*Math.pow(2, (i*5+j)));
            }
        }
        return hash;
    }

    /***
     * Check if the current FrogArrangement is equal to another object.
     * @param o The object that will be compared to FrogArrangement.
     * @return True if the objects are equal, else false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == null)
            return false;
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        FrogArrangement that = (FrogArrangement) o;
        int thatHash = calculateHash(that.frogs);
        int thisHash = calculateHash(this.frogs);
        return (thisHash == thatHash);
    }

    /***
     * Calculates the hash code for the FrogArrangement by calling the calculateHash method.
     * @return The calculated has code.
     */
    @Override
    public int hashCode() {
        return calculateHash(this.frogs);
    }
}
