package test;

import methods.NearestNeighbor;
import methods.Algorithm;
import model.Problem;
import model.Solution;
import model.Tools;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Implementation of tour constructive heuristics for solving CVRP");

        List<String> testFiles = DataIO.getFolderFiles("./data/A-VRP/");
        System.out.println("Solving " + testFiles.size() + " instances");

        Tools.creatTxtFile("./data/", "A-VRP_solution.txt");

        for (String file : testFiles) {
            System.out.println(file);
            Problem problem = DataIO.readProblem(file);
            if (!problem.initDistMatrix()) {
                System.out.println("ERROR INPUT: " + problem.name);
                continue;
            }
            System.out.println(problem.optimal.totalLength);
            Algorithm algo = new NearestNeighbor();
            Solution solution = algo.solve(problem);
            Tools.appendTxtLine(problem.name);
            if (problem.validate(solution)) {
                solution.write();
            } else {
                Tools.appendTxtLine("ERROR");
            }
            Tools.appendTxtLine("------------");
            System.out.println();
        }
    }

}
