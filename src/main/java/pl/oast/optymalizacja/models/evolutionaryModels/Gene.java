package pl.oast.optymalizacja.models.evolutionaryModels;

import pl.oast.optymalizacja.Utility;
import pl.oast.optymalizacja.models.Demand;

import java.util.ArrayList;
import java.util.Random;

/******************************************************************************
 * Class implements gene object for evolutionary algorithm.
 ******************************************************************************/
public class Gene {
    //Random generator with seed value
    Random generator;
    //The demand, which is represented by gene.
    Demand demand;
    //Mutation probability value.
    double mutationProbability;
    //List of links capacity and path flow allocations.
    ArrayList<Integer> capacities;
    ArrayList<Integer> allocations;

    //Constructor creates gene object.
    public Gene(Random generator, Demand demand, double mutationProbability, ArrayList<Integer> capacities) {
        this.generator = generator;
        this.demand = demand;
        this.mutationProbability = mutationProbability;
        this.capacities = capacities;
        this.allocations = new ArrayList<>();
    }

    //Constructor creates gene clone object.
    public Gene(Random generator, Demand demand, double mutationProbability, ArrayList<Integer> capacities, ArrayList<Integer> allocations) {
        this.generator = generator;
        this.demand = demand;
        this.mutationProbability = mutationProbability;
        this.capacities = capacities;
        this.allocations = allocations;
    }

    //Method to generate path flow allocations, for demand volume.
    public Gene generateAllocations() {
        int pathNo;
        for (int i = 0; i < demand.getPathsNumber(); i++)
            allocations.add(0);
        for (int i = 0; i < demand.getDemandVolume(); i++) {
            pathNo = generator.nextInt(demand.getPathsNumber());
            allocations.set(pathNo, allocations.get(pathNo) + 1);
            for (Integer linkId : demand.getPathList().get(pathNo).getLinkList())
                capacities.set(linkId - 1, capacities.get(linkId - 1) - 1);
        }
        return this;
    }

    //Method to mutate gene.
    public void mutate(ArrayList<Integer> capacityList) {
        capacities = capacityList;
        if (allocations.size() <= 1)
            return;
        if (generator.nextDouble() >= mutationProbability)
            return;
        //Choose random non 0 allocation to reduce
        int allocationIdReduce = generator.nextInt(allocations.size());
        while (allocations.get(allocationIdReduce) == 0)
            allocationIdReduce = generator.nextInt(allocations.size());
        //Choose random non full allocation to increase
        int allocationIdIncrease = allocationIdReduce;
        while (allocationIdIncrease == allocationIdReduce)
            allocationIdIncrease = generator.nextInt(allocations.size());
        //Change allocation values
        allocations.set(allocationIdIncrease, allocations.get(allocationIdIncrease) + 1);
        allocations.set(allocationIdReduce, allocations.get(allocationIdReduce) - 1);
        //Change capacities values
        for (Integer linkId : demand.getPathList().get(allocationIdIncrease).getLinkList())
            capacities.set(linkId - 1, capacities.get(linkId - 1) - 1);
        for (Integer linkId : demand.getPathList().get(allocationIdReduce).getLinkList())
            capacities.set(linkId - 1, capacities.get(linkId - 1) + 1);
        //Increase mutation counter for STOP criteria.
        Utility.increaseMutationCounter();
    }

    //Method returns list of links usage.
    public ArrayList<Integer> getLinksLoads(int linkListSize) {
        ArrayList<Integer> linkLoads = new ArrayList<>();
        for (int i = 0; i < linkListSize; i++)
            linkLoads.add(0);
        for (int i = 0; i < allocations.size(); i++)
            for (Integer link : demand.getPathList().get(i).getLinkList()) {
                linkLoads.set(link - 1, linkLoads.get(link - 1) + allocations.get(i));
            }
        return linkLoads;
    }

    //Method returns list of links capacity.
    public ArrayList<Integer> getCapacities() {
        return (ArrayList<Integer>) capacities.clone();
    }

    //Method returns gene clone object.
    public Gene clone() {
        return new Gene(generator, demand, mutationProbability, getCapacities(), (ArrayList<Integer>) allocations.clone());
    }
}
