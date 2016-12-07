package servlets;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by tayvs on 06.12.2016.
 */
public class Downloader {

    private int countURLS;
    private int countDoneURLS;
    private Document[] documents;
    private IOException ex = new IOException();

    public Document[] download(String[] urls) {
        countDoneURLS = 0;
        countURLS = urls.length;
        documents = new Document[countURLS];

        for (String url : urls) {
            (new DownloadThread(url, this)).start();
        }

        while (countDoneURLS != countURLS) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return documents;
    }

    public IOException getEx() {
        return ex;
    }

    private void setThreadDone(DownloadThread thread) {
        if (thread.isError) ex.addSuppressed(thread.ex);
        else if (!thread.isDownload)
            ex.addSuppressed(new Exception("Unknown Error. Call setThreadDone method without error or download file. "
                    + "File name is " + thread.url));

        documents[countDoneURLS++] = thread.html;
    }


    private class DownloadThread extends Thread {

        //flags
        private boolean isDownload = false;
        private boolean isError = false;

        //Work classes
        private final String url;
        private Document html;
        private Exception ex;

        //FeedBack reference
        private final Downloader feedBack;

        public DownloadThread(String url, Downloader feedBack) {
            this.url = url;
            this.feedBack = feedBack;
        }

        @Override
        public void run() {
            try {
                html = Jsoup.connect(url).get();
                isDownload = true;
            } catch (IOException e) {
                ex = e;
                isError = true;
            }

            feedBack.setThreadDone(this);
        }

        @Override
        public String toString() {
            return "URL " + url + "  doc " + html.baseUri() + "  ex " + ex;
        }
    }
}
