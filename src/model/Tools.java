package model;

import java.io.*;
import java.text.DecimalFormat;

public class Tools {
    Tools() {

    }

    private static String fileName;

    public static void creatTxtFile(String path, String name) throws IOException {
        if (path.endsWith(File.separator)) {
            fileName = path + name;
        } else {
            fileName = path + File.separator + name;
        }
        if (!fileName.endsWith(".txt")) {
            fileName = fileName + ".txt";
        }
        File filename = new File(fileName);
        if (!filename.exists()) {
            filename.createNewFile();
        }
    }

    public static double euclideanDistance2D(Node n1, Node n2) {
        if (n1 == null || n2 == null) {
            return -1;
        }
        int xd = n1.x - n2.x;
        int yd = n1.y - n2.y;
        return Math.sqrt(xd * xd + yd * yd);
    }

    public static String DecimalFormat1(double v) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(v);
    }

    public static void appendTxtLine(String newStr) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true)))) {
            out.write(newStr + "\r\n");
        }
    }
}
