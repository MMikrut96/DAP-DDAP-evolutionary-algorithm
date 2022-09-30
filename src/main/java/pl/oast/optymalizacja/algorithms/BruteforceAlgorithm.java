package pl.oast.optymalizacja.algorithms;

import org.paukov.combinatorics3.Generator;
import pl.oast.optymalizacja.Utility;
import pl.oast.optymalizacja.models.Demand;
import pl.oast.optymalizacja.models.Link;
import pl.oast.optymalizacja.models.SolutionsModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/******************************************************************************
 * Class contains brute force algorithm implementation.
 ******************************************************************************/
public class BruteforceAlgorithm {
    //List of demand objects.
    ArrayList<Demand> demands;
    //List of links objects.
    ArrayList<Link> links;
    //Field for saving start time.
    Long startTime;
    //List of all possible solutions, and list of solutions with calculated costs.
    ArrayList<ArrayList<Integer>> possibleSolutions;
    ArrayList<SolutionsModel> calculatedSolutions;
    //Counter field.
    int counter;

    //Constructor creating brute force algorithm object.
    public BruteforceAlgorithm(ArrayList<Demand> demands, ArrayList<Link> links) {
        this.demands = demands;
        this.links = links;
        this.possibleSolutions = new ArrayList<>();
        this.calculatedSolutions = new ArrayList<>();
    }

    //Method to run brute force algorithm.
    public void runAlgorithm(boolean ddap) {
        System.out.println("Bruteforce algorithm started.");
        this.startTime = System.nanoTime();
        for (Demand demand : demands) {
            generatePossibleFlows(demand);
        }
        generateSolutions();
        solve();
        System.out.println("Time to all solutions: " + (TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)) + "ms.");
        SolutionsModel theBestSolution = getBestSolution(ddap);
        System.out.println("Best solution found by algorithm:\n" + theBestSolution.toString(ddap));
        System.out.println("Time to best solution: " + (TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)) + "ms.");
    }

    //Method generates and fills possible flow list for demand.
    private void generatePossibleFlows(Demand demand) {
        //Print all possible flows for demand.
        counter = 0;
        System.out.println("Demand " + demand.getId() + ":");
        System.out.println("Demand volumes: " + Utility.getRange(demand.getDemandVolume()).toString());
        System.out.println("Paths number: " + demand.getPathList().size() +
                "\n------------------------------------------------------------");
        //Generate all possible allocation combinations and save in possible flow list of this demand.
        Generator.permutation(Utility.getRange(demand.getDemandVolume())).withRepetitions(demand.getPathsNumber()).stream().forEach(combination -> {
            int sum = combination.stream().mapToInt(Integer::intValue).sum();
            if (sum == demand.getDemandVolume()) {
                demand.addPossibleFlowsList((ArrayList<Integer>) combination);
                System.out.println(counter + " flow: " + combination.toString());
                counter++;
            }
        });
        System.out.println("------------------------------------------------------------");
    }

    //Method generates and fills possible solutions list.
    private void generateSolutions() {
        int maxFlowsNumber = 0;
        for (Demand demand : demands) {
            if (maxFlowsNumber < demand.getPossibleFlowsListLength())
                maxFlowsNumber = demand.getPossibleFlowsListLength();
        }
        //Generate all possible flow combinations and save in possible solution list.
        Generator.permutation(Utility.getRange(maxFlowsNumber + 1)).withRepetitions(demands.size()).stream().forEach(solution -> {
            boolean flag = true;
            for (int i = 0; i < demands.size(); i++) {
                if (solution.get(i) >= demands.get(i).getPossibleFlowsListLength())
                    flag = false;
            }
            if (flag) {
                possibleSolutions.add((ArrayList<Integer>) solution);
            }
        });
    }

    //Method prepares demand for every possible solutions and runs, calculations.
    private void solve() {
        for (ArrayList solution : possibleSolutions) {
            for (int i = 0; i < demands.size(); i++) {
                demands.get(i).setUsedPaths(demands.get(i).getPossibleFlowsList().get((Integer) solution.get(i)));
            }
            calcSolutionCost(solution);
        }
    }

    //Method calculating solution cost and creates SolutionModel object.
    private void calcSolutionCost(ArrayList<Integer> solution) {
        //Prepare list for count link usage.
        ArrayList<Integer> linkUsage = new ArrayList<>();
        for (int i = 0; i < links.size(); i++) {
            linkUsage.add(0);
        }
        //Count link usage.
        for (Demand demand : demands) {
            for (int i = 0; i < demand.getPathsNumber(); i++) {
                if (demand.getUsedPaths().get(i) != 0)
                    for (Integer link : demand.getPathList().get(i).getLinkList()) {
                        int sum = linkUsage.get(link - 1);
                        sum += demand.getUsedPaths().get(i);
                        linkUsage.set(link - 1, sum);
                    }
            }
        }
        //Calculating solution cost.
        double cost = 0;
        ArrayList<Integer> linkResult = new ArrayList<>();
        for (int i = 0; i < links.size(); i++) {
            linkResult.add(linkUsage.get(i) - links.get(i).getMaxCapacity()); // DAP Cost
            cost += Math.ceil((double) linkUsage.get(i) / (double) links.get(i).getLinkModule()) * links.get(i).getModuleCost(); // DDAP Cost
        }
        //Create and save calculated solution.
        calculatedSolutions.add(new SolutionsModel(solution, linkUsage, cost, getMax(linkResult)));
    }

    //Method returns maximum value of list.
    private int getMax(ArrayList<Integer> list) {
        int max = list.get(0);
        for (int i : list) {
            if (max < i)
                max = i;
        }
        return max;
    }

    //Method returns solution with minimum cost of ddap or dap problem.
    private SolutionsModel getBestSolution(boolean ddap) {
        SolutionsModel theBest = calculatedSolutions.get(0);
        for (SolutionsModel solutionsModel : calculatedSolutions) {
            if (ddap) {
                if (theBest.getDDAP() > solutionsModel.getDDAP())
                    theBest = solutionsModel;
            } else if (theBest.getDAP() > solutionsModel.getDAP())
                theBest = solutionsModel;
        }
        return theBest;
    }
}
