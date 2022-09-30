package pl.oast.optymalizacja.models;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * Class contains demand data.
 ******************************************************************************/
public class Demand {
    //Demand parameters.
    int id;
    int startNode;
    int endNode;
    int demandVolume;
    int pathsNumber;
    //List of demand paths.
    List<DemandPath> pathList;
    //List of possible flows for demand, used in brute force algorithm.
    ArrayList<ArrayList<Integer>> possibleFlowsList;
    //List of path allocation, used in brute force algorithm.
    ArrayList<Integer> usedPaths;

    //Constructor of demand object.
    public Demand(int cnt) {
        id = cnt;
        pathList = new ArrayList<>();
        possibleFlowsList = new ArrayList<>();
        usedPaths = new ArrayList<>();
    }

    //Method to fill path list.
    public void addToPathList(DemandPath dp) {
        pathList.add(dp);
    }

    /*************************************
     *Getters and Setters for Demand fields.
     **************************************/
    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(int endNode) {
        this.endNode = endNode;
    }

    public void setDemandVolume(int demandVolume) {
        this.demandVolume = demandVolume;
    }

    public void setPathsNumber(int pathsNumber) {
        this.pathsNumber = pathsNumber;
    }

    public int getId() {
        return id;
    }

    public int getDemandVolume() {
        return demandVolume;
    }

    public int getPathsNumber() {
        return pathsNumber;
    }

    public List<DemandPath> getPathList() {
        return pathList;
    }

    public void addPossibleFlowsList(ArrayList<Integer> possiblePathList) {
        this.possibleFlowsList.add(possiblePathList);
    }

    public int getPossibleFlowsListLength() {
        return this.possibleFlowsList.size();
    }

    public void setUsedPaths(ArrayList<Integer> usedPaths) {
        this.usedPaths = usedPaths;
    }

    public ArrayList<ArrayList<Integer>> getPossibleFlowsList() {
        return possibleFlowsList;
    }

    public ArrayList<Integer> getUsedPaths() {
        return usedPaths;
    }
}
