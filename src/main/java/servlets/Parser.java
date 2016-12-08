package servlets;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by tayvs on 07.12.2016.
 */
public class Parser {

    private DB db = new DB();
    private Exception ex = new IOException();

    public Parser() {}

    public boolean setURLs(String[] urls) {
        urls = urlPrepare(urls);

        if (urls.length <= 0) {
            ex.addSuppressed(new Exception("Error: Invalid input data. Empty URL"));
            return false;
        }

        //getting documents by urls (multiThreading)
        Downloader downloader = new Downloader();
        Document[] httpDocs = downloader.download(urls);
        ex.addSuppressed(downloader.getEx());

        //Parsing Files on pairs Tag-Value and put into DB
        for (Document document : httpDocs) {
            LinkedList<Entry> pairsTagValue = new LinkedList<>();

            for (Element el : document.children()) {
                System.out.println("doc is " + document.baseUri());
                System.out.println("el is " + el);
                processTree(el, pairsTagValue);
            }

            //Put URL and his tag-value
            db.writeCollection(document.baseUri(), pairsTagValue);
        }

        return true;
    }

    public HashMap getEntrys() {
        return db.getAlllEntrys();
    }

    public String getEntrysJSON(String[] collNames) {
        return db.getEntrysJSON(urlPrepare(collNames));
    }

    public Exception getEx() {
        return ex;
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

    private void processTree(Element e, LinkedList<Entry> list) {
        String name = e.tagName();
        String value = e.attributes().html();

        list.add(new Entry(name, value));

        for (Element el : e.children()) {
            processTree(el, list);
        }
    }

}
