package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by tayvs on 08.12.2016.
 */
@WebServlet("/api/servlet")
public class APIServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");

        Parser parser = new Parser();
        String[] urls = req.getParameterValues("URL");

        System.out.println(Arrays.toString(urls));

        parser.setURLs(urls);

        resp.getWriter().write(parser.getEntrysJSON());
    }

}
