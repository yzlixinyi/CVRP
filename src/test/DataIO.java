package test;

import model.Problem;
import model.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataIO {

    private DataIO() {
    }

    public static List<String> getFolderFiles(String dirPath) {
        List<String> folders = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        folders.add(dirPath);

        while (!folders.isEmpty()) {
            String folder = folders.remove(0);
            File dir = new File(folder);
            File[] files = dir.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        folders.add(f.getPath() + File.separator);
                    } else {
                        fileNames.add(f.getPath());
                    }
                }
            }
        }

        return fileNames;
    }

    public static Problem readProblem(String path) throws IOException {
        Problem p = new Problem();
        try (BufferedReader br = new BufferedReader(new FileReader(path))){
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("NAME")) {
                    p.name = line.split(" : ")[1];
                } else if (line.startsWith("DIMENSION")) {
                    p.nNode = Integer.parseInt(line.split(" : ")[1]);
                } else if (line.startsWith("CAPACITY")) {
                    p.capacity = Integer.parseInt(line.split(" : ")[1]);
                } else if (line.startsWith("NODE_COORD")) {
                    for (int i = 0; i < p.nNode; i++) {
                        line = br.readLine().strip();
                        String[] seq = line.split("\\s");
                        Node node = new Node(Integer.parseInt(seq[0]), Integer.parseInt(seq[1]), Integer.parseInt(seq[2]));
                        p.nodeMap.put(node.id, node);
                    }
                } else if (line.startsWith("DEMAND")) {
                    for (int i = 0; i < p.nNode; i++) {
                        line = br.readLine().strip();
                        String[] seq = line.split("\\s");
                        int id = Integer.parseInt(seq[0]);
                        Node node = p.nodeMap.get(id);
                        node.demand = Integer.parseInt(seq[1]);
                    }
                }
            }
        }
        return p;
    }
}
