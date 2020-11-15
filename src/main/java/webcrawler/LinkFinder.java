package webcrawler;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.net.URL;

public class LinkFinder implements Runnable {

    private final String url;
    private final LinkHandler linkHandler;

    private static final long t0 = System.currentTimeMillis();

    public LinkFinder(String url, LinkHandler handler) {
        this.url = url;
        this.linkHandler = handler;
    }

    public void run() {
        getSimpleLinks(url);
    }

    private void getSimpleLinks(String url) {
        if (!linkHandler.visited(url)) {
            try {
                URL uriLink = new URL(url);
                Parser parser = new Parser(uriLink.openConnection());
                NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));

                for (int i = 0; i < list.size(); i++) {
                    LinkTag extracted = (LinkTag) list.elementAt(i);
                    String link = extracted.getLink();
                    if (!link.isEmpty() && !linkHandler.visited(link)) {
                        linkHandler.queueLink(link);
                    }
                }

                linkHandler.addVisited(url);

                if (linkHandler.size() % 100 == 0) {
                    System.out.printf("Visiting %d links takes %d ms%n", linkHandler.size(), System.currentTimeMillis() - t0);
                }

            } catch (Exception ignored) {
            }
        }
    }
}