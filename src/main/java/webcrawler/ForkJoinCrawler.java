package webcrawler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;

// reference: http://www.javaworld.com/article/2078440/enterprise-java/java-tip-when-to-use-forkjoinpool-vs-executorservice.html?page=2

public class ForkJoinCrawler implements LinkHandler {

    private final Collection<String> visitedLinks = Collections.synchronizedSet(new HashSet<String>());

    private final String url;
    private final ForkJoinPool forkJoinPool;

    public ForkJoinCrawler(String startingURL, int maxThreads) {
        this.url = startingURL;
        forkJoinPool = new ForkJoinPool(maxThreads);
    }

    private void startCrawling() {
        forkJoinPool.invoke(new LinkFinderAction(this.url, this));
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

    @Override
    public void queueLink(String link) throws Exception {
        throw new UnsupportedOperationException("This is done in LinkFinderAction");
    }

    public static void main(String[] args) throws Exception {
        new ForkJoinCrawler("https://web.mit.edu", 64).startCrawling();
    }
}