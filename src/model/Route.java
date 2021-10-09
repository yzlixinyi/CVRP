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
}
