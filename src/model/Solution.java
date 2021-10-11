package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    /**
     * number of vehicles needed
     */
    public int nVehicle = 0;
    /**
     * sum of route lengths
     */
    public double totalLength = 0;
    /**
     * routes
     */
    public List<Route> routeList = new ArrayList<>();
    /**
     * Computational time
     */
    public int time;

    public double gap;

    public void print() {
        System.out.println("Use " + nVehicle + " vehicles. Total length " + (int) totalLength);
        for (Route route : routeList) {
            System.out.printf("%d\tload %d,", route.vID, route.load);
            StringBuilder sb = new StringBuilder();
            for (Node node : route.customers) {
                sb.append("\t").append(node.id);
            }
            System.out.println(sb);
        }
    }

    public void write() throws IOException {
        Tools.appendTxtLine("Use %d vehicles. Total length %d.".formatted(nVehicle, (int) totalLength));
        for (Route route : routeList) {
            StringBuilder sb = new StringBuilder();
            sb.append(route.vID).append("\tload ").append(route.load).append(",");
            for (Node node : route.customers) {
                sb.append("\t").append(node.id);
            }
            Tools.appendTxtLine(sb.toString());
        }
    }
}
