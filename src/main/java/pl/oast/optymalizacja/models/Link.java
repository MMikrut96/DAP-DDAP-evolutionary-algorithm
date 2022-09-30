package pl.oast.optymalizacja.models;

/******************************************************************************
 * Class contains link parameters.
 ******************************************************************************/
public class Link {
    //Link parameters.
    int id;
    int startNode;
    int endNode;
    int numberOfModules;
    int moduleCost;
    int linkModule;
    //Link maximum capacity value.
    int maxCapacity;

    //Constructor creates link object, and calculate maximum capacity value.
    public Link(String[] values, int id) {
        this.startNode = Integer.parseInt(values[0]);
        this.endNode = Integer.parseInt(values[1]);
        this.numberOfModules = Integer.parseInt(values[2]);
        this.moduleCost = Integer.parseInt(values[3]);
        this.linkModule = Integer.parseInt(values[4]);
        this.id = id;
        this.maxCapacity = this.linkModule * this.numberOfModules;
    }

    /**************************
     * Getters of link fields.
     **************************/
    public int getModuleCost() {
        return moduleCost;
    }

    public int getLinkModule() {
        return linkModule;
    }

    public int getId() {
        return id;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }
}
