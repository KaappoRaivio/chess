package chess.misc;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class ReadWriter {
    public static String readFile (String path) {

        try {
            return StringUtils.join(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.toList()), '\n');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeFile(String path, String text) {

        File file = new File(path);

        try {
            if (file.createNewFile()) {
                System.out.println("Creating new file " + path);

            } else {
                System.out.println("File " + path + " already exists");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        try {
            FileOutputStream fileOut = new FileOutputStream(path);

            PrintWriter writer = new PrintWriter(fileOut);
            writer.write(text);
            writer.close();

            fileOut.close();

            System.out.println("Saved " + text.getClass() + " to: " + path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
