package com.coveros.training;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.StringTokenizer;

public class DatabaseUtils {
    public static String DATABASE_NAME = "database.txt";

    public static boolean searchDatabaseForKey(String username) {
        try {
            File database = new File(DATABASE_NAME);
            if (!database.exists() || database.isDirectory()) {
                return false;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(database))) {
                String line;
                while ((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line);
                    while (st.hasMoreTokens()) {
                        if (st.nextToken().equals(username)) {
                            return true;
                        }
                    }
                }
                // if we get to this point, we never found that username
                return false;
            }
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * save text to a file.  If the file exists, append.  If
     * the file doesn't exist, create a new file.
     */
    public static void saveTextToFile(String text) {
        try {
            final Path path = Paths.get(DATABASE_NAME);
            StandardOpenOption openOption = Files.exists(path) ?
                    StandardOpenOption.APPEND :
                    StandardOpenOption.CREATE;
            Files.write(path,
                    Arrays.asList(text),
                    StandardCharsets.UTF_8,
                    openOption);
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * clears the database.  Mostly used by the test system.
     */
    public static void destroyDatabase() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(DATABASE_NAME);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pw.close();
    }
}
