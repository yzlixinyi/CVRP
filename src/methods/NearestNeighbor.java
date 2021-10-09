package methods;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Tour Construction Heuristic;
 * Nearest Neighbour (NN);
 * Flood, 1956;
 * Running time O(n^2)
 */
public class NearestNeighbor implements Algorithm {

    @Override
    public Solution solve(Problem problem) {
        Solution solution = new Solution();
        System.out.println("Solving CVRP with Nearest Neighbour Algorithm.");

        Random rdm = new Random(1009);
        // 1. Randomly Select a starting node

        // 2. Add to the last node the closest node until no more node is available

        // 3. Connect the last node with the first node
        List<Node> unvisited = new ArrayList<>(problem.nodeMap.values());
        unvisited.remove(problem.depot);
        int n = 0;
        while (!unvisited.isEmpty()) {
            n++;
            Node node = unvisited.remove(rdm.nextInt(unvisited.size()));
            Route route = new Route(n);
            double minDist = problem.distMat[node.id][problem.depot.id];
            while ((route.load + node.demand) < problem.capacity) {
                route.customers.add(node);
                route.length += minDist;
                route.load += node.demand;
                unvisited.remove(node);
                if (unvisited.isEmpty()) {
                    break;
                }
                Node next = unvisited.get(0);
                minDist = problem.distMat[node.id][next.id];
                for (int i = 1; i < unvisited.size(); i++) {
                    Node cur = unvisited.get(i);
                    if (problem.distMat[node.id][cur.id] < minDist) {
                        next = cur;
                        minDist = problem.distMat[node.id][cur.id];
                    }
                }
                node = next;
            }
            // go back to depot
            node = route.customers.get(route.customers.size() - 1);
            route.length += problem.distMat[node.id][problem.depot.id];
            solution.routeList.add(route);
            solution.totalLength += route.length;
        }
        assert(n == solution.routeList.size());
        solution.nVehicle = n;
        return solution;
    }
}
