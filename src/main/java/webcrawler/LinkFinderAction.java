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
    private final LinkHandler cr;

    private static final long t0 = System.nanoTime();

    public LinkFinderAction(String url, LinkHandler cr) {
        this.url = url;
        this.cr = cr;
    }

    @Override
    public void compute() {
        if (!cr.visited(url)) {
            try {
                List<RecursiveAction> actions = new ArrayList<>();
                URL uriLink = new URL(url);
                Parser parser = new Parser(uriLink.openConnection());
                NodeList list = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));

                for (int i = 0; i < list.size(); i++) {
                    LinkTag extracted = (LinkTag) list.elementAt(i);
                    String link = extracted.extractLink();

                    if (!link.isEmpty() && !cr.visited(link)) {
                        actions.add(new LinkFinderAction(link, cr));
                    }
                }
                cr.addVisited(url);

                if (cr.size() == 100) {
                    System.out.println("Time for visit 100 distinct links= " + (System.nanoTime() - t0));
                }

                //invoke recursively
                invokeAll(actions);
            } catch (Exception e) {
                //ignore 404, unknown protocol or other server errors
            }
        }
    }
}