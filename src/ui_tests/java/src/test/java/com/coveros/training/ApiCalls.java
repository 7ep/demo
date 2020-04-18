package com.coveros.training;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

import java.io.IOException;

public class ApiCalls {

    /**
     * Use the API to register a user
     * @param username some username
     * @param password a really good password (otherwise it will probably respond with a complaint
     * @return returns the body of the result, if you wish to use it.
     */
    public static String registerUser(String username, String password) {
        try {
            final Content content = Request.Post("http://localhost:8080/demo/register")
                    .bodyForm(Form.form().add("username", username).add("password", password).build())
                    .execute().returnContent();
            return content.asString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static String registerBook(String title) {
        try {
            final Content content = Request.Post("http://localhost:8080/demo/registerbook")
                    .bodyForm(Form.form().add("book", title).build())
                    .execute().returnContent();
            return content.asString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String registerBorrowers(String name) {
        try {
            final Content content = Request.Post("http://localhost:8080/demo/registerborrower")
                    .bodyForm(Form.form().add("borrower", name).build())
                    .execute().returnContent();
            return content.asString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
