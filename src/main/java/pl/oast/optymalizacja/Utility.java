package pl.oast.optymalizacja;

import java.util.ArrayList;

/******************************************************************************
 * Class contains utility methods for project.
 ******************************************************************************/
public class Utility {
    //Field for counting of mutation event in evolutionary algorithm.
    private static int mutationCounter = 0;

    //Method return list of range. ex.(for 4 returns [0,1,2,3,4])
    public static ArrayList<Integer> getRange(int number) {
        ArrayList<Integer> range = new ArrayList<>();
        for (int i = 0; i < number + 1; i++)
            range.add(i);
        return range;
    }

    //Method returns value of mutation counter field.
    public static int getMutationCounter() {
        return mutationCounter;
    }

    //Method increase mutation counter field value.
    public static void increaseMutationCounter() {
        Utility.mutationCounter++;
    }
}
