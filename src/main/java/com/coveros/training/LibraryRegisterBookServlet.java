package com.coveros.training;

import com.coveros.training.domainobjects.LibraryActionResults;
import com.coveros.training.persistence.LibraryUtils;
import com.coveros.training.persistence.RegistrationUtils;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "LibraryRegisterBookServlet", urlPatterns = {"/registerbook"}, loadOnStartup = 1)
public class LibraryRegisterBookServlet extends HttpServlet {

  private static final Logger logger = LoggerFactory.getLogger(RegistrationUtils.class);
  static LibraryUtils libraryUtils = new LibraryUtils();

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    final String book = request.getParameter("book");
    request.setAttribute("book", book);

    final LibraryActionResults libraryActionResults = libraryUtils.registerBook(book);

    request.setAttribute("result", libraryActionResults.toString());
    ServletUtils.forwardToResult(request, response, logger);
  }

}
