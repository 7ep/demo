package com.coveros.training;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.regex.Matcher;

public class DatabaseUtils {
    public static String AUTH_DATABASE_NAME = "authentication.txt";
    public static String LIBRARY_BORROWER_DATABASE_NAME = "borrowers.txt";
    public static String LIBRARY_BOOKS_DATABASE_NAME = "books.txt";
    public static String LIBRARY_LENDING_DATABASE = "lending.txt";

    // the database we are currently pointing at.
    private final String databaseName;

    public static DatabaseUtils obtainDatabaseAccess(String databaseName) {
        return new DatabaseUtils(databaseName);
    }

    private DatabaseUtils(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * Scan through the lines of the file, return the first line
     * that has this key.
     */
    public String searchDatabaseForKey(String key) {
        try {
            File database = new File(databaseName);
            if (!database.exists() || database.isDirectory()) {
                return null;
            }
            try (BufferedReader br = new BufferedReader(new FileReader(database))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains(key)) return line;
                }
                // if we get to this point, we never found the key
                return null;
            }
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * save text to a file.  If the file exists, append.  If
     * the file doesn't exist, create a new file.
     */
    public void saveTextToFile(String text) {
        try {
            final Path path = Paths.get(databaseName);
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

    public boolean isUsernameAndPasswordInDatabase(String username, String password) {
        try {
            File database = new File(databaseName);
            try (BufferedReader br = new BufferedReader(new FileReader(database))) {
                String line;
                while ((line = br.readLine()) != null) {
                    StringTokenizer st = new StringTokenizer(line);
                    while (st.hasMoreTokens()) {
                        if (st.nextToken().equals(username) && st.nextToken().equals(password)) {
                            return true;
                        }
                    }
                }
                // if we get to this point, we never found that username
                return false;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * clears the database.  Mostly used by the test system.
     */
    public void clearDatabaseContents() {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(databaseName);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        pw.close();
    }
}
