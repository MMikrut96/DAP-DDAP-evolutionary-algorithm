package pl.oast.optymalizacja.models.evolutionaryModels;

import pl.oast.optymalizacja.models.Demand;
import pl.oast.optymalizacja.models.Link;

import java.util.ArrayList;
import java.util.Random;

/******************************************************************************
 * Class implements chromosome object for evolutionary algorithm.
 ******************************************************************************/
public class Chromosome {
    //Random generator with seed value.
    Random generator;
    //Lists for genes and links objects.
    ArrayList<Gene> genes;
    ArrayList<Link> links;
    //Mutation probability value.
    double mutationProbability;
    //Parameter for checking validation of chromosome.
    boolean isValid;
    //Lists contains value of links capacity and links maximum capacity.
    ArrayList<Integer> capacities;
    ArrayList<Integer> totalCapacities;

    //Constructor creates chromosome, and generates genes for every demand in demand list.
    public Chromosome(Random generator, ArrayList<Link> links, double mutationProbability, ArrayList<Demand> demands) {
        this.generator = generator;
        this.genes = new ArrayList<>();
        this.links = links;
        this.mutationProbability = mutationProbability;
        this.isValid = false;
        this.capacities = new ArrayList<>();
        this.totalCapacities = new ArrayList<>();
        for (int i = 0; i < links.size(); i++) {
            capacities.add(0);
            totalCapacities.add(0);
        }
        for (Link link : links) {
            capacities.set(link.getId() - 1, link.getMaxCapacity());
            totalCapacities.set(link.getId() - 1, link.getMaxCapacity());
        }
        for (Demand demand : demands) {
            Gene gene = new Gene(generator, demand, mutationProbability, capacities);
            gene = gene.generateAllocations();
            capacities = gene.getCapacities();
            genes.add(gene);
        }
    }

    //Constructor for Offspring super method, without generating genes.
    public Chromosome(ArrayList<Link> links) {
        this.links = links;
        this.genes = new ArrayList<>();
        this.capacities = new ArrayList<>();
        this.totalCapacities = new ArrayList<>();
        for (int i = 0; i < links.size(); i++)
            totalCapacities.add(0);
        for (Link link : links)
            totalCapacities.set(link.getId() - 1, link.getMaxCapacity());
    }

    //Method returns cost for Chromosome.
    public double getCost(boolean ddap) {
        ArrayList<Integer> costList = getSolutionLinkLoad();
        if (ddap) {
            double cost = 0;
            for (int i = 0; i < links.size(); i++) {
                cost += Math.ceil(((double) costList.get(i) / (double) links.get(i).getLinkModule()) * links.get(i).getModuleCost());
            }
            return cost;
        } else
            return getDAPCost(costList);
    }

    //Method to calculate DAP Cost for Chromosome.
    private double getDAPCost(ArrayList<Integer> linkUsage) {
        int max = linkUsage.get(0) - links.get(0).getMaxCapacity();
        int tmp = 0;
        for (int i = 1; i < linkUsage.size(); i++) {
            tmp = linkUsage.get(i) - links.get(i).getMaxCapacity();
            if (max < tmp)
                max = tmp;
        }
        return max;
    }

    //Method returns list of link load.
    public ArrayList<Integer> getSolutionLinkLoad() {
        ArrayList<Integer> linkLoad = new ArrayList<>();
        ArrayList<Integer> genLoads;
        for (int i = 0; i < links.size(); i++)
            linkLoad.add(0);
        for (Gene gene : genes) {
            genLoads = gene.getLinksLoads(links.size());
            for (int i = 0; i < links.size(); i++)
                linkLoad.set(i, linkLoad.get(i) + genLoads.get(i));
        }
        return linkLoad;
    }
}
