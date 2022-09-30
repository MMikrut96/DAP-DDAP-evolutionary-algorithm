package pl.oast.optymalizacja;

import pl.oast.optymalizacja.algorithms.BruteforceAlgorithm;
import pl.oast.optymalizacja.algorithms.EvolutionaryAlgorithm;

import java.util.Scanner;

/******************************************************************************
 * Main class, gets parameters from user, and runs algorithms.
 ******************************************************************************/
public class Main {

    //Paths to data files. For jar file i had to remove path and saves only file name.
    private static final String file1 = "src\\main\\java\\pl\\oast\\optymalizacja\\inputFiles\\net4.txt";
    private static final String file2 = "src\\main\\java\\pl\\oast\\optymalizacja\\inputFiles\\net12_1.txt";
    private static final String file3 = "src\\main\\java\\pl\\oast\\optymalizacja\\inputFiles\\net12_2.txt";

    //Field for user option value.
    private static int option;
    //Parameters definition
    private static boolean ddap;
    private static String chosenFile;
    private static int seed;
    private static int populationSize;
    private static double mutationProbability;

    //Parameters for STOP criteria. Default values.
    private static long simulationTime = 1000000000;
    private static int mutationNumber = 1000000000;
    private static int generationNumber = 0;
    private static int genNumWithoutBetterCost = -1;

    public static void main(String[] args) {
        FileParser fileParser = new FileParser();
        Scanner input = new Scanner(System.in);

        //Getting parameters and file with network topology.
        chosenFile = getFile(chosenFile, input);
        getParameters(input);

        //Getting parameters for stop criterion.
        getSTOPCriterion(input);

        //Parse data from file.
        fileParser.getData(chosenFile);

        //Create object of brute force algorithm and evolutionary algorithm with data and parameters.
        BruteforceAlgorithm bfa = new BruteforceAlgorithm(fileParser.demandList, fileParser.linkList);
        EvolutionaryAlgorithm evolutionaryAlgorithm = new EvolutionaryAlgorithm(fileParser.demandList, fileParser.linkList, seed, populationSize, mutationProbability);

        //Run evolutionary algorithm.
        evolutionaryAlgorithm.runAlgorithm(ddap, generationNumber, simulationTime, mutationNumber, genNumWithoutBetterCost);

        //Run brute force algorithm if user want to.
        runBruteForce(input, bfa, ddap);
    }

    //Method to get STOP criterion from user.
    private static void getSTOPCriterion(Scanner input) {
        int option;
        System.out.println("Choose STOP criterion\n" +
                "1. Number of generations.\n" +
                "2. Number of mutations.\n" +
                "3. Simulation time.\n" +
                "4. Number of generations without better solution.");
        option = input.nextInt();
        while (option != 1 && option != 2 && option != 3 && option != 4) {
            System.out.println("Wrong number. Input again.");
            option = input.nextInt();
        }
        switch (option) {
            case 1:
                System.out.println("Input number of generation.(More than 0)");
                generationNumber = input.nextInt();
                while (generationNumber <= 0) {
                    System.out.println("Number of generation must be more than 0. Input again.");
                    generationNumber = input.nextInt();
                }
                break;
            case 2:
                System.out.println("Input number of mutation.(More than 0)");
                mutationNumber = input.nextInt();
                while (mutationNumber <= 0) {
                    System.out.println("Number of mutation must be more than 0. Input again.");
                    mutationNumber = input.nextInt();
                }
                break;
            case 3:
                System.out.println("Input simulation time in milliseconds.(More than 0, 1000ms = 1s)");
                simulationTime = input.nextInt();
                while (simulationTime <= 0) {
                    System.out.println("Simulation time must be more than 0. Input again.");
                    simulationTime = input.nextInt();
                }
                break;
            case 4:
                System.out.println("Input number of generation without better solution.(More than 0)");
                genNumWithoutBetterCost = input.nextInt();
                while (genNumWithoutBetterCost <= 0) {
                    System.out.println("Number must be more than 0. Input again.");
                    genNumWithoutBetterCost = input.nextInt();
                }
        }
    }

    //Method to get parameters from user.
    private static void getParameters(Scanner input) {
        System.out.println("Choose problem to solve.\n 1. DAP\n 2. DDAP");
        option = input.nextInt();
        while (option != 1 && option != 2) {
            System.out.println("Wrong option number. Input again.");
            option = input.nextInt();
        }
        if (option == 1)
            ddap = false;
        else
            ddap = true;
        System.out.println("Input integer seed.");
        seed = input.nextInt();
        System.out.println("Input population size. (Must be even)");
        populationSize = input.nextInt();
        while (populationSize % 2 != 0) {
            System.out.println("Population size must be even. Input again.");
            populationSize = input.nextInt();
        }
        System.out.println("Input mutation probability value. (ex. 0,05)");
        mutationProbability = input.nextDouble();
    }

    //Method to runs brute force algorithm after asking user to do it.
    private static void runBruteForce(Scanner input, BruteforceAlgorithm bfa, boolean ddap) {
        int option;
        System.out.println("Do you want to run brute force algorithm?\n 1. Yes\n 2. No");
        option = input.nextInt();
        while (option != 1 && option != 2) {
            System.out.println("Wrong option number. Input again.");
            option = input.nextInt();
        }
        if (option == 1)
            bfa.runAlgorithm(ddap);
    }

    //Method to choose file with network topology.
    private static String getFile(String chosenFile, Scanner input) {
        int option;
        System.out.println("Input file number:\n" +
                "1. net4.txt\n" +
                "2. net12_1.txt\n" +
                "3. net12_2.txt");
        option = input.nextInt();
        switch (option) {
            case 1:
                chosenFile = file1;
                break;
            case 2:
                chosenFile = file2;
                break;
            case 3:
                chosenFile = file3;
                break;
            default:
                System.out.println("Wrong file number. Close app.");
                System.exit(0);
                break;
        }
        return chosenFile;
    }
}
