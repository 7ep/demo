package com.coveros.training;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.StringTokenizer;

import java.nio.charset.StandardCharsets;

@WebServlet(name = "RegisterServlet", urlPatterns = {"register"}, loadOnStartup = 1)
public class RegisterServlet extends HttpServlet {

    private static String DATABASE_NAME = "database.txt";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().print("Hello, World!");
    }

    private String putUsernameInRequest(HttpServletRequest request) {
        String username = request.getParameter("username");
        if (username == null) username = "EMPTY_USERNAME";
        request.setAttribute("username", username);
        return username;
    }

    private String putPasswordInRequest(HttpServletRequest request) {
        String password = request.getParameter("password");
        if (password == null) password = "EMPTY_PASSWORD";
        request.setAttribute("password", password);
        return password;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = putUsernameInRequest(request);
        String password = putPasswordInRequest(request);

        if (!isUserInDatabase(username)) {
            saveToDatabase(username, password);
            request.getRequestDispatcher("registration.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("already_registered.jsp").forward(request, response);
        }
    }

    protected void saveToDatabase(String username, String password) {
        saveTextToFile(username + " " + password);
    }

    protected boolean isUserInDatabase(String username) {
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

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * save text to a file.  If the file exists, append.  If
     * the file doesn't exist, create a new file.
     */
    private void saveTextToFile(String text) {
        try {
            final Path path = Paths.get(DATABASE_NAME);
            StandardOpenOption openOption = Files.exists(path) ?
                    StandardOpenOption.APPEND :
                    StandardOpenOption.CREATE;
            Files.write(path,
                    Arrays.asList(text),
                    StandardCharsets.UTF_8,
                    openOption );
        } catch (final IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

}

