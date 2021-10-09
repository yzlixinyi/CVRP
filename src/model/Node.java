package model;

public class Node {
    public int id;
    public int x;
    public int y;
    public int demand = 0;

    public Node(int nID, int corX, int corY) {
        id = nID;
        x = corX;
        y = corY;
    }
}
