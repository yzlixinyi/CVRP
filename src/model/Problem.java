package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem {
    /**
     * NAME of the benchmark instance
     */
    public String name = "";
    /**
     * DEMENSION of the instance (number of nodes, including the depot)
     */
    public int nNode = 0;
    /**
     * CAPACITY of each vehicle
     */
    public int capacity = 0;
    /**
     * DEPOT_SECTION
     */
    public Node depot;
    /**
     * Information of nodes (NODE_COORD_SECTION and DEMAND_SECTION)
     */
    public Map<Integer, Node> nodeMap = new HashMap<>();
    /**
     * COMMENT: Min number of trucks; Sum of route lengths
     */
    public Solution optimal = new Solution();
    
    public double[][] distMat;

    public boolean initDistMatrix() {
        // check if depot is included
        if (nodeMap.get(1).demand != 0) {
            return false;
        }
        depot = nodeMap.get(1);
        distMat = new double[nNode + 1][nNode + 1];
        for (int i = 1; i <= nNode; i++) {
            for (int j = i + 1; j <= nNode; j++) {
                distMat[i][j] = Tools.euclideanDistance2D(nodeMap.get(i), nodeMap.get(j));
                distMat[j][i] = distMat[i][j];
            }
        }
        return true;
    }

    /**
     * Validate feasibility and correctness of solution
     * TODO log errors
     * @return true - OK; false - ERROR occurs
     */
    public boolean validate(Solution solution) {
        int count = 0; // count visited customers
        int nVeh = 0;  // count used vehicles
        double sumD = 0;  // calculate total route length
        List<Node> unvisited = new ArrayList<>(nodeMap.values());
        unvisited.remove(depot);
        for (Route route : solution.routeList) {
            nVeh++;
            int ld = 0;
            double dist = 0;
            Node last = depot;
            for (Node c : route.customers) {
                ld += c.demand;
                dist += distMat[last.id][c.id];
                last = c;
                if (!unvisited.remove(c)) {
                    return false;
                }
            }
            dist += distMat[last.id][depot.id];
            if (ld != route.load || dist != route.length) {
                return false;
            }
            sumD += dist;
            count += route.customers.size();
        }
        return unvisited.isEmpty() &&
                count == (nodeMap.size() - 1 ) &&
                nVeh == solution.nVehicle &&
                sumD == solution.totalLength;
    }
}
