package methods;

import model.Node;
import model.Problem;
import model.Route;
import model.Solution;

import java.util.*;
import java.util.stream.Collectors;

public class ClarkeWrightSaving implements Algorithm{
    @Override
    public Solution solve(Problem problem) {
        long startTime = System.currentTimeMillis();

        Solution solution = new Solution();

        List<Node> nodes = new ArrayList<>(problem.nodeMap.values());
        nodes.remove(problem.depot);

        // 1.Start with an initial allocation of one vehicle to each customer
        Map<Integer, Route> tempRoutes = new HashMap<>();
        for (Node node : nodes) {
            node.routeID = node.id;
            Route route = new Route(node);
            route.vID = node.id;
            tempRoutes.put(route.vID, route);
        }

        // 2.Calculate saving s_ij = c_di + c_dj - c_ij
        List<Saving> savings = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = i + 1; j < nodes.size(); j++) {
                savings.add(new Saving(i, j, problem));
            }
        }

        /*
        3.At each step find the largest saving s_ij where
        i and j are not in the same tour
        neither i nor j is interior to an existing route
        vehicle capacity is not exceeded
         */
        // sort in descending order
        savings.sort((o1, o2) -> (int) o2.sij - (int) o1.sij);
        for (Saving s : savings) {
            Node nodeI = problem.nodeMap.get(s.iID);
            Node nodeJ = problem.nodeMap.get(s.jID);
            if (nodeI != null && nodeJ != null && nodeI.routeID != nodeJ.routeID) {
                // i and j are not in the same tour
                Route rI = tempRoutes.get(nodeI.routeID);
                Route rJ = tempRoutes.get(nodeJ.routeID);
                if (rI != null && rJ != null) {
                    int load = rI.load + rJ.load;
                    if (load <= problem.capacity /*&& nodeI.nextToDepot(rI) && nodeJ.nextToDepot(rJ)*/) {
                        // 4.Link i and j together to form a new tour
                        if (nodeI.equals(rI.customers.get(rI.customers.size() - 1))) {
                            if (nodeJ.equals(rJ.customers.get(0))) {
                                rJ.customers.forEach(node -> node.routeID = rI.vID);
                                rI.customers.addAll(rJ.customers);
                                rI.load = load;
                                tempRoutes.remove(rJ.vID);
                            } else if (nodeJ.equals(rJ.customers.get(rJ.customers.size() - 1))) {
                                Collections.reverse(rJ.customers);
                                rJ.customers.forEach(node -> node.routeID = rI.vID);
                                rI.customers.addAll(rJ.customers);
                                rI.load = load;
                                tempRoutes.remove(rJ.vID);
                            }
                        } else if (nodeI.equals(rI.customers.get(0))) {
                            if (nodeJ.equals(rJ.customers.get(rJ.customers.size() - 1))) {
                                rI.customers.forEach(node -> node.routeID = rJ.vID);
                                rJ.customers.addAll(rI.customers);
                                rJ.load = load;
                                tempRoutes.remove(rI.vID);
                            } else if (nodeJ.equals(rJ.customers.get(0))) {
                                Collections.reverse(rJ.customers);
                                rI.customers.forEach(node -> node.routeID = rJ.vID);
                                rJ.customers.addAll(rI.customers);
                                rJ.load = load;
                                tempRoutes.remove(rI.vID);
                            }
                        }
                    }
                }
            }
        }

        solution.routeList = new ArrayList<>(tempRoutes.values());
        solution.nVehicle = solution.routeList.size();
        int n = 1;
        for (Route r : solution.routeList) {
            r.vID = n;
            solution.totalLength += r.calRouteLength(problem);
            n++;
        }

        solution.time = (int) (System.currentTimeMillis() - startTime);

        return solution;
    }

    private static class Saving {
        int iID;
        int jID;
        /**
         * s_ij = c_di + c_dj - c_ij
         */
        double sij;

        /**
         * Calculate saving
         * @param i ID of node i
         * @param j ID of node j
         * @param p Problem
         */
        Saving(int i, int j, Problem p) {
            iID = i;
            jID = j;
            sij = p.distMat[p.depot.id][i] + p.distMat[p.depot.id][j] - p.distMat[i][j];
        }
    }
}
