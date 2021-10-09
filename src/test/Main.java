package test;

import methods.SolomonInsertion;
import model.Algorithm;
import model.Problem;
import model.Solution;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Implementation of tour constructive heuristics for solving CVRP");

        List<String> testFiles = DataIO.getFolderFiles("./data/A-VRP/");
        System.out.println(testFiles.size());
        for (String file : testFiles) {
            System.out.println(file);
            Problem problem = DataIO.readProblem(file);
            System.out.println(problem.nNode);
//            System.out.println(problem.nodeMap.get(problem.nNode).demand);
            Algorithm algo = new SolomonInsertion();
            Solution solution = algo.solve(problem);
        }
    }

}
