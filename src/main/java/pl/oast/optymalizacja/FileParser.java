package pl.oast.optymalizacja;

import pl.oast.optymalizacja.models.Demand;
import pl.oast.optymalizacja.models.DemandPath;
import pl.oast.optymalizacja.models.Link;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/******************************************************************************
 * Class contains method to parse data from file to demand and link objects.
 ******************************************************************************/
public class FileParser {

    //List of parsed demands and links.
    ArrayList<Demand> demandList;
    ArrayList<Link> linkList;
    //List of raw data read from file.
    List<String> data;
    //Demand field.
    Demand d = null;

    //Read data from file.
    public void getData(String filePath) {
        ArrayList<String> data = new ArrayList<>();
        try {
            Reader reader = new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8);
            BufferedReader fin = new BufferedReader(reader);
            String s;
            while ((s = fin.readLine()) != null) {
                data.add(s.trim());
            }
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.data = data;
        parseData();
    }

    //Parse data to demand and link models.
    public void parseData() {
        int linkCnt = 0;
        int linkIdCnt = 1;
        int demandCnt = -1;
        boolean first = true;
        boolean paths = false;
        linkList = new ArrayList<>();
        demandList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            //Gets number of links in file.
            if (i == 0) {
                linkCnt = Integer.parseInt(data.get(i));
                continue;
            }
            //End of link data in file.
            if (data.get(i).equals("-1")) {
                continue;
            }
            //Creating new link with parameters in values table.
            if (linkCnt != 0) {
                String[] values = data.get(i).split(" ");
                linkList.add(new Link(values, linkIdCnt));
                linkIdCnt++;
                linkCnt--;
                continue;
            }
            //Save number of demand in file.
            if (linkCnt == 0 && demandCnt == -1 && data.get(i).length() != 0) {
                demandCnt = Integer.parseInt(data.get(i)) + 1;
                continue;
            }
            //Creating new demand with parameters in values table, "first" flag informs about data containing demand parameters. If false it means data contains demand path.
            if (demandCnt != -1 && demandCnt != 0) {
                if (data.get(i).equals("")) {
                    demandCnt--;
                    if (d != null)
                        demandList.add(d);
                    d = null;
                    first = true;
                    continue;
                }
                String[] values = data.get(i).split(" ");
                if (d == null) {
                    d = new Demand(demandCnt);
                }
                if (first) {
                    d.setStartNode(Integer.parseInt(values[0]));
                    d.setEndNode(Integer.parseInt(values[1]));
                    d.setDemandVolume(Integer.parseInt(values[2]));
                    first = false;
                    paths = true;
                    continue;
                }
                if (values.length == 1 && paths) {
                    d.setPathsNumber(Integer.parseInt(values[0]));
                    paths = false;
                } else {
                    DemandPath dp = new DemandPath(Integer.parseInt(values[0]));
                    for (int k = 1; k < values.length; k++)
                        dp.addToList(Integer.parseInt(values[k]));
                    d.addToPathList(dp);
                }

            }
        }
        //Add new demand to list.
        if (d != null)
            demandList.add(d);
    }
}


