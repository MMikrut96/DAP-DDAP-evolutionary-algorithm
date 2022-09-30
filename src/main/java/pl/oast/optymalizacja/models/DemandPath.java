package pl.oast.optymalizacja.models;

import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * Class contains demand path parameters.
 ******************************************************************************/
public class DemandPath {
    //Identification number of demand path.
    int id;
    //List of link ids in demand path.
    List<Integer> linkList = new ArrayList<>();

    //Constructor creates demand path object.
    public DemandPath(int id) {
        this.id = id;
    }

    //Method returns list of links in demand path.
    public List<Integer> getLinkList() {
        return linkList;
    }

    //Method adds link id to list of links in demand path.
    public void addToList(int link) {
        linkList.add(link);
    }
}
