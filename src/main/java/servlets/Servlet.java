package servlets;

import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by tayvs on 04.12.2016.
 */
@WebServlet("/servlet")
public class Servlet extends HttpServlet {

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
            String[] urls = urlPrepare(req.getParameterValues("URL"));
            System.out.println("URLS " + Arrays.toString(urls));

            if (urls.length > 0) {
                //getting documents by urls (multiThreading)
                Document[] httpDocs = (new Downloader()).download(urls);

                //Parsing Files on pairs Tag-Value and put into DB
                for (Document document : httpDocs) {
                    LinkedList<Entry> pairsTagValue = new LinkedList<>();

                    for (Element el : document.children()) {
                        System.out.println("doc is " + document.baseUri());
                        System.out.println("el is " + el);
                        processTree(el, pairsTagValue);
                    }

                    //Put URL and his tag-value
                    DB.writeCollection(document.baseUri(), pairsTagValue);
                }

                //Send the pairs Forward
                req.setAttribute("pairs", DB.getEntrys());
            } else {
                errors.add("Error: Invalid input data. Empty URL");
            }
        } else {
            errors.add("Error: Unexpected action");
        }


        if (!errors.isEmpty()) req.setAttribute("errors", errors.toString());
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    private void processTree(Element e, LinkedList<Entry> list) {
        String name = e.tagName();
        String value = e.attributes().html();

        list.add(new Entry(name, value));

        for (Element el : e.children()) {
            processTree(el, list);
        }
    }


    /**
     * If inputURL is empty, then return zero-length-array
     * Add protocol, if url haven't it, and remove empty lines.
     * @param inputURL array with URLs
     * @return array with processed URLs
     */
    private String[] urlPrepare(String[] inputURL) {
        if (inputURL == null) return new String[0];

        LinkedList<String> outputURL = new LinkedList<>();
        for (String s : inputURL) {
            if (s.isEmpty()) continue;
            if (s.startsWith("http://") || s.startsWith("https://")) {
                outputURL.add(s);
            } else {
                outputURL.add("http://" + s);
            }
        }

        return outputURL.toArray(new String[outputURL.size()]);
    }

}
