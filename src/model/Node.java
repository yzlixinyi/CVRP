package model;

public class Node {
    public int id;
    public int x;
    public int y;
    public int demand = 0;

    public int routeID;

    public Node(int nID, int corX, int corY) {
        id = nID;
        x = corX;
        y = corY;
    }

    public boolean nextToDepot(Route route) {
        if (route == null || !route.customers.contains(this)) {
            return false;
        }
        return this.equals(route.customers.get(0)) || this.equals(route.customers.get(route.customers.size() - 1));
    }
}
