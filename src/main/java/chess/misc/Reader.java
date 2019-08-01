package chess.misc;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Reader {
    public static String readFile (String path) {

        try {
            return StringUtils.join(Files.lines(Paths.get(path), StandardCharsets.UTF_8).collect(Collectors.toList()), '\n');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
