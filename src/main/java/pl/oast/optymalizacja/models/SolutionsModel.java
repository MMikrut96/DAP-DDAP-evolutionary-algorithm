package pl.oast.optymalizacja.models;

import java.util.ArrayList;

/******************************************************************************
 * Class contains solution with calculated cost for dap and ddap problem.
 ******************************************************************************/
public class SolutionsModel {
    ArrayList<Integer> flows;
    ArrayList<Integer> linkUsage;
    double ddapCost;
    int dapCost;

    //Constructor creates SolutionModel object.
    public SolutionsModel(ArrayList<Integer> flows, ArrayList<Integer> linkUsage, double cost, int dapCost) {
        this.flows = flows;
        this.linkUsage = linkUsage;
        this.ddapCost = cost;
        this.dapCost = dapCost;
    }

    /******************************************************************************
     * Getters for dap and ddap problem costs, and method to print full solution.
     ******************************************************************************/
    public double getDDAP() {
        return ddapCost;
    }

    public double getDAP() {
        return dapCost;
    }

    public String toString(boolean ddap) {
        StringBuilder buff = new StringBuilder();
        buff.append("Flows: " + flows.toString() + "\nLink Loads:");
        int count = 1;
        for (Integer i : linkUsage) {
            buff.append(" Link " + count + ": " + i);
            count++;
        }
        if (ddap)
            buff.append(" \nCost: " + ddapCost);
        else
            buff.append(" \nDAP Cost: " + dapCost);
        return buff.toString();
    }
}
