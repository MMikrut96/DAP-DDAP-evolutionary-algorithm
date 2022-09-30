package pl.oast.optymalizacja.algorithms;

import pl.oast.optymalizacja.Utility;
import pl.oast.optymalizacja.models.Demand;
import pl.oast.optymalizacja.models.Link;
import pl.oast.optymalizacja.models.evolutionaryModels.Chromosome;
import pl.oast.optymalizacja.models.evolutionaryModels.Offspring;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/******************************************************************************
 * Class contains evolutionary algorithm implementation.
 ******************************************************************************/
public class EvolutionaryAlgorithm {

    //Lists of demands and links object in network.
    ArrayList<Demand> demands;
    ArrayList<Link> links;
    //Parameters to run evolutionary algorithm, and save STOP criterion.
    int seed;
    int populationSize;
    int generationsCounter;
    int noBetterCostCounter;
    double mutationProbability;
    //Random generator with given seed.
    Random generator;
    //List of chromosomes, created by algorithm.
    ArrayList<ArrayList<Chromosome>> chromosomes;
    //Start time.
    Long startTime;

    //Constructor creates evolutionary algorithm object with given parameters.
    public EvolutionaryAlgorithm(ArrayList<Demand> demands, ArrayList<Link> links, int seed, int populationSize, double mutationProbability) {
        this.demands = demands;
        this.links = links;
        this.seed = seed;
        this.populationSize = populationSize;
        this.mutationProbability = mutationProbability;
        chromosomes = new ArrayList<>();
        if (seed > 0)
            generator = new Random(seed);
        else
            generator = new Random();
    }

    //Method to run evolutionary algorithm.
    public void runAlgorithm(boolean ddap, int generationsNumber, long simulationTime, int mutationNumber, int genNumWithoutBetterSolution) {
        int solutionsCreated = 0;
        int solutionsChosen = 0;
        generationsCounter = 0;
        noBetterCostCounter = 0;
        System.out.println("Evolutionary algorithm started.");
        this.startTime = System.nanoTime();
        ArrayList<Chromosome> generation = new ArrayList<>();
        ArrayList<Chromosome> tmp = new ArrayList<>();

        //Creating first generation.
        for (int i = 0; i < populationSize; i++)
            generation.add(new Chromosome(generator, links, mutationProbability, demands));
        chromosomes.add((ArrayList<Chromosome>) generation.clone());
        generationsCounter++;
        Chromosome solution = findNewBestSolution(ddap,generation);
        Chromosome newBest;
        generation.clear();
        solutionsCreated += populationSize;
        solutionsChosen += populationSize;

        //Creating offspring generations.
        while (generationsCounter != generationsNumber &&
                simulationTime >= (TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)) &&
                mutationNumber >= Utility.getMutationCounter() &&
                noBetterCostCounter!=genNumWithoutBetterSolution) {
            for (int k = 0; k < populationSize; k++) {
                generateOffspring(tmp, generationsCounter, k);
                generation.add(getBestOffspring(tmp, ddap));
                solutionsCreated += 4;
                solutionsChosen += 1;
                tmp.clear();
            }
            chromosomes.add((ArrayList<Chromosome>) generation.clone());

            //Check for better solution
            newBest = findNewBestSolution(ddap,generation);
            if(solution.getCost(ddap)>newBest.getCost(ddap)) {
                solution = newBest;
                noBetterCostCounter = 0;
            } else
                noBetterCostCounter++;
            generation.clear();
            generationsCounter++;
        }
        if (ddap)
            System.out.println("DDAP Solving Statistics.");
        else
            System.out.println("DAP Solving Statistics.");
        System.out.println("Solving time: " + (TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS)) + "ms.");
        System.out.println("Population size: " + populationSize);
        System.out.println("Generation number: " + generationsCounter);
        System.out.println("Number of mutations: " + Utility.getMutationCounter());
        System.out.println("Solution created by algorithm: " + solutionsCreated);
        System.out.println("Solution chosen by algorithm: " + solutionsChosen);
        System.out.println("Best found solution cost: " + solution.getCost(ddap));
        System.out.println("Best found solution link loads: " + solution.getSolutionLinkLoad().toString());
    }

    //Method to generate offspring from given parent chromosome list.
    private void generateOffspring(ArrayList<Chromosome> tmp, int i, int k) {
        if (k == 0) {
            tmp.add(new Offspring(chromosomes.get(i - 1).get(chromosomes.get(i - 1).size() - 1), chromosomes.get(i - 1).get(k), generator, links, mutationProbability));
            tmp.add(new Offspring(chromosomes.get(i - 1).get(chromosomes.get(i - 1).size() - 1), chromosomes.get(i - 1).get(k), generator, links, mutationProbability));
            tmp.add(new Offspring(chromosomes.get(i - 1).get(chromosomes.get(i - 1).size() - 1), chromosomes.get(i - 1).get(k), generator, links, mutationProbability));
            tmp.add(new Offspring(chromosomes.get(i - 1).get(chromosomes.get(i - 1).size() - 1), chromosomes.get(i - 1).get(k), generator, links, mutationProbability));
        } else {
            tmp.add(new Offspring(chromosomes.get(i - 1).get(k - 1), chromosomes.get(i - 1).get(k), generator, links, mutationProbability));
            tmp.add(new Offspring(chromosomes.get(i - 1).get(k - 1), chromosomes.get(i - 1).get(k), generator, links, mutationProbability));
            tmp.add(new Offspring(chromosomes.get(i - 1).get(k - 1), chromosomes.get(i - 1).get(k), generator, links, mutationProbability));
            tmp.add(new Offspring(chromosomes.get(i - 1).get(k - 1), chromosomes.get(i - 1).get(k), generator, links, mutationProbability));
        }
    }

    //Method returns offspring with lowest cost.
    private Chromosome getBestOffspring(ArrayList<Chromosome> tmp, boolean ddap) {
        Chromosome best = tmp.get(0);
        for (int i = 1; i < 4; i++) {
            if (best.getCost(ddap) > tmp.get(i).getCost(ddap))
                best = tmp.get(i);
        }
        return best;
    }

    //Method returns best solution in list of chromosome.
    private Chromosome findNewBestSolution(boolean ddap, ArrayList<Chromosome> list){
        Chromosome best = list.get(0);
        for(Chromosome chromosome: list){
            if(chromosome.getCost(ddap)<best.getCost(ddap))
                best = chromosome;
        }
        return best;
    }
}
