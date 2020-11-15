package webcrawler;

import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class LinkFinderAction extends RecursiveAction {

    private static final long serialVersionUID = 1L;
    private final String url;
    private final LinkHandler linkHandler;

    private static final long t0 = System.currentTimeMillis();

    public LinkFinderAction(String url, LinkHandler linkHandler) {
        this.url = url;
        this.linkHandler = linkHandler;
    }

    @Override
    public void compute() {
        if (!linkHandler.visited(url)) {
            try {
                List<RecursiveAction> actions = new ArrayList<>();
                URL uriLink = new URL(url);
                Parser parser = new Parser(uriLink.openConnection());
                NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));

                for (int i = 0; i < list.size(); i++) {
                    LinkTag extracted = (LinkTag) list.elementAt(i);
                    String link = extracted.extractLink();

                    if (!link.isEmpty() && !linkHandler.visited(link)) {
                        actions.add(new LinkFinderAction(link, linkHandler));
                    }
                }
                linkHandler.addVisited(url);

                if (linkHandler.size() % 100 == 0) {
                    System.out.printf("Visiting %d links takes %d ms%n", linkHandler.size(), System.currentTimeMillis() - t0);
                }

                invokeAll(actions);
            } catch (Exception ignored) {
            }
        }
    }
}