package webcrawler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//reference: http://www.javaworld.com/article/2078440/enterprise-java/java-tip-when-to-use-forkjoinpool-vs-executorservice.html?page=2

public class FixedTheadPoolCrawler implements LinkHandler {

    private final Collection<String> visitedLinks = Collections.synchronizedSet(new HashSet<String>());

    private final String startingURL;
    private final ExecutorService executorService;

    public FixedTheadPoolCrawler(String startingURL, int maxThreads) {
        this.startingURL = startingURL;
        executorService = Executors.newFixedThreadPool(maxThreads);
    }

    @Override
    public void queueLink(String link) throws Exception {
        startNewThread(link);
    }

    @Override
    public int size() {
        return visitedLinks.size();
    }

    @Override
    public void addVisited(String s) {
        visitedLinks.add(s);
    }

    @Override
    public boolean visited(String s) {
        return visitedLinks.contains(s);
    }

    private void startNewThread(String link) throws Exception {
        executorService.execute(new LinkFinder(link, this));
    }

    private void startCrawling() throws Exception {
        startNewThread(startingURL);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        new FixedTheadPoolCrawler("https://web.mit.edu", 64).startCrawling();
    }
}