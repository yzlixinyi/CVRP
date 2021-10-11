package model;

import java.util.ArrayList;
import java.util.List;

public class Route {
    public int vID = 0;
    public int load = 0;
    public double length = 0;
    public List<Node> customers = new ArrayList<>();

    public Route(int id) {
        vID = id;
    }

    /**
     * An initial allocation of one vehicle to each customer.
     */
    public Route(Node node) {
        load = node.demand;
        customers.add(node);
    }

    public double calRouteLength(Problem p) {
        Node last = p.depot;
        double l = 0;
        for (Node node : customers) {
            l += p.distMat[last.id][node.id];
            last = node;
        }
        l += p.distMat[last.id][p.depot.id];
        length = l;
        return length;
    }
}
