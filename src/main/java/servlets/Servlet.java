package servlets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.MalformedURLException;
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
        StringBuilder errors = new StringBuilder();

        String[] urls = req.getParameterValues("URL");
        System.out.println("URLS " + Arrays.toString(urls));
        //Checking: Are fields send URLs
        if (urls != null){
            //Checking: What count of URLs
            if (urls.length == 3) {

                //
                Document[] httpDocs = new Document[urls.length];
                for (int i = 0; i < httpDocs.length; i++) {
                    if (urls[i].isEmpty())
                        errors.append(i + " URL is empty. ");
                    else {
                        //Get file or write Exception "URL broken"
                        try {
                            httpDocs[i] = Jsoup.connect(urls[i]).get();
                        } catch (IOException ex) {
                            errors.append("URL Broken. " + ex);
                        }
                    }
                }

                //Parsing Files on pairs Tag-Value
                LinkedList<Entry> pairsTagValue = new LinkedList<>();
                for (Document document : httpDocs) {
                    System.out.println("Document name is " + document.baseUri());
                    for (Element el : document.children()) {
                        processTree(el, pairsTagValue);
                    }
                }

                DB.writeIntoDB(pairsTagValue);

                DB.getEntrys().forEach(System.out::println);

                //Send the pairs Forward
                req.setAttribute("pairs", DB.getEntrys());
            } else {
                req.setAttribute("errors", "Error" + "Must be three URLs");
            }
        }

        if (errors.length() > 0 ) req.setAttribute("errors", errors.toString());
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

}
