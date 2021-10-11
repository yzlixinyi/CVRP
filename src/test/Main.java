package test;

import methods.Algorithm;
import methods.ClarkeWrightSaving;
import methods.NearestNeighbor;
import model.Problem;
import model.Solution;
import model.Tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Implementation of tour constructive heuristics for solving CVRP");

        List<String> testFiles = DataIO.getFolderFiles("./data/A-VRP/");
        System.out.println("Solving " + testFiles.size() + " instances");

        List<Solution> solutionList = new ArrayList<>();

        switch (args[0]) {
            case "NN":
                Tools.creatTxtFile("./data/", "A-VRP_NN_solution.txt");
                Tools.appendTxtLine("----Nearest Neighbour----");
                break;
            case "CW":
                Tools.creatTxtFile("./data/", "A-VRP_CW_solution.txt");
                Tools.appendTxtLine("----Clarke-Wright Saving----");
                break;
            default:
                return;
        }

        for (String file : testFiles) {
            System.out.println(file);
            Problem problem = DataIO.readProblem(file);
            if (!problem.initDistMatrix()) {
                System.out.println("ERROR INPUT: " + problem.name);
                continue;
            }

            Algorithm algo;
            switch (args[0]) {
                case "NN":
                    algo = new NearestNeighbor();
                    break;
                case "CW":
                    algo = new ClarkeWrightSaving();
                    break;
                default:
                    return;
            }
            Solution solution = algo.solve(problem);
            Tools.appendTxtLine(problem.name);
            if (problem.validate(solution)) {
                problem.calGap(solution);
                Tools.appendTxtLine("Gap %s (Opt %d)".formatted(Tools.DecimalFormat3(solution.gap),
                        (int) problem.optimal.totalLength));
                solution.write();
                solutionList.add(solution);
            } else {
                Tools.appendTxtLine("ERROR");
            }
            Tools.appendTxtLine("------------");
            System.out.println();
        }

        // statistics
        writeSummary(solutionList);
    }

    private static void writeSummary(List<Solution> solutionList) throws IOException {
        double avgLength = 0;
        double avgNumVeh = 0;
        double avgTime = 0;
        double agvGap = 0;

        Tools.appendTxtLine("\n----Summary----");
        Tools.appendTxtLine("t(ms)\tgap\tnVeh\tsumL");
        for (Solution s : solutionList) {
            avgTime += s.time;
            agvGap += s.gap;
            avgNumVeh += s.nVehicle;
            avgLength += s.totalLength;
            Tools.appendTxtLine(String.format("%d\t%s\t%d\t%d", s.time, Tools.DecimalFormat3(s.gap),
                    s.nVehicle, (int) s.totalLength));
        }

        Tools.appendTxtLine("\n----Average----");
        avgTime /= solutionList.size();
        agvGap /= solutionList.size();
        avgNumVeh /= solutionList.size();
        avgLength /= solutionList.size();
        Tools.appendTxtLine(String.format("%s\t%s\t%s\t%s", Tools.DecimalFormat1(avgTime), Tools.DecimalFormat3(agvGap),
                Tools.DecimalFormat1(avgNumVeh), Tools.DecimalFormat1(avgLength)));
    }

}
