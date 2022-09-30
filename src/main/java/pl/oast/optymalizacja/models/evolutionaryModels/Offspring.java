package pl.oast.optymalizacja.models.evolutionaryModels;

import pl.oast.optymalizacja.models.Link;

import java.util.ArrayList;
import java.util.Random;

/******************************************************************************
 * Class implements offspring object, which inherit from Chromosome class.
 ******************************************************************************/
public class Offspring extends Chromosome {

    //Parents objects, which gives genes for this subclass object.
    Chromosome parent1;
    Chromosome parent2;

    //Constructor creates Offspring object and randomize genes from parents.
    public Offspring(Chromosome parent1, Chromosome parent2, Random generator, ArrayList<Link> links, double mutationProbability) {
        super(links);
        this.generator = generator;
        this.mutationProbability = mutationProbability;
        this.parent1 = parent1;
        this.parent2 = parent2;

        if (parent1.genes.size() != parent2.genes.size())
            try {
                throw new Exception("Parents have different number of genes.");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        genes.clear();
        //Randomize inherit of genes from parents.
        for (int i = 0; i < parent1.genes.size(); i++) {
            if (generator.nextBoolean())
                genes.add(parent1.genes.get(i).clone());
            else
                genes.add(parent2.genes.get(i).clone());
        }
        capacities = genes.get(genes.size() - 1).getCapacities();
        for (Gene gene : genes) {
            gene.mutate(capacities);
            capacities = gene.getCapacities();
        }
    }
}
