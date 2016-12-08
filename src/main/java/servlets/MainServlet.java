package servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by tayvs on 04.12.2016.
 */
@WebServlet("/servlet")
public class MainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //Check: does "action" exist
        if (req.getParameter("action") == null) {
            req.setAttribute("errors", "Action not found");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        }


        LinkedList<String> errors = new LinkedList<>();

        //Check which function are calling
        if (req.getParameter("action").equals("PROCESS")){
            //Prepare input statement
            Parser parser = new Parser();
            parser.setURLs(req.getParameterValues("URL"));

            //Send the pairs Forward
            req.setAttribute("pairs", parser.getEntrys());
            errors.add(parser.getEx().getMessage());
        } else {
            errors.add("Error: Unexpected action");
        }


        if (!errors.isEmpty()) req.setAttribute("errors", errors.toString());
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

}
