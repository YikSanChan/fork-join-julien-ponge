package webcrawler;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LinkFinder implements Runnable {

    private final String url;
    private final LinkHandler linkHandler;

    private static final long t0 = System.nanoTime();

    public LinkFinder(String url, LinkHandler handler) {
        this.url = url;
        this.linkHandler = handler;
    }

    public void run() {
        getSimpleLinks(url);
    }

    private void getSimpleLinks(String url) {
        // if not already visited
        if (!linkHandler.visited(url)) {
            try {
                URL uriLink = new URL(url);
                Parser parser = new Parser(uriLink.openConnection());
                NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
                List<String> urls = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    LinkTag extracted = (LinkTag) list.elementAt(i);
                    String link = extracted.getLink();
                    if (!link.isEmpty() && !linkHandler.visited(link)) {
                        urls.add(link);
                    }

                }
                // we visited this url
                linkHandler.addVisited(url);
//                System.out.println("Visited " + url);

                if (linkHandler.size() == 100) {
                    System.out.println("Time to visit 100 distinct links = " + (System.nanoTime() - t0));
                }

                for (String l : urls) {
                    linkHandler.queueLink(l);
                }

            } catch (Exception e) {
                // ignore all errors for now
            }
        }
    }
}