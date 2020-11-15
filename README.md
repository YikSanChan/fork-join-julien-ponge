# Fork / Join Study

Study Fork / Join with a few examples.

## Word Count

The code shows the performance gain via running word count using fork join pool.

```
Single thread,Fork/Join
1337,242
714,255
693,467
692,238
693,245
700,244
689,241
691,242
685,243
691,248
```

To run `wordcount.WordCounter`, set program arguments as `wordcount me 10`.

Reference: https://www.oracle.com/technical-resources/articles/java/fork-join.html

## Web Crawler

The code shows how to do web crawler using fork join or fixed thread pool pattern.
It also shows the performance gain of using fork join in the specific case.

```
-----------------
Fixed thread pool
-----------------
Visiting 100 links takes 2755 ms
Visiting 200 links takes 4833 ms
Visiting 300 links takes 6761 ms
Visiting 400 links takes 11252 ms
Visiting 500 links takes 12970 ms
Visiting 600 links takes 16050 ms
Visiting 700 links takes 17360 ms
Visiting 800 links takes 19572 ms
Visiting 800 links takes 19599 ms
Visiting 900 links takes 45158 ms
Visiting 1000 links takes 63665 ms

-----------------
Fork join pool
-----------------
Visiting 100 links takes 1340 ms
Visiting 200 links takes 2431 ms
Visiting 300 links takes 3357 ms
Visiting 400 links takes 4239 ms
Visiting 400 links takes 4239 ms
Visiting 500 links takes 5111 ms
Visiting 600 links takes 5967 ms
Visiting 700 links takes 7049 ms
Visiting 800 links takes 8044 ms
Visiting 800 links takes 8047 ms
Visiting 800 links takes 8049 ms
Visiting 900 links takes 9035 ms
Visiting 1000 links takes 10092 ms
Visiting 1000 links takes 10099 ms
```

Note that it shows duplicated logs such as

```
Visiting 1000 links takes 10092 ms
Visiting 1000 links takes 10099 ms
```

This is because there can be multiple threads hitting the size=1000 during a short period
when no new link is added to the set.

Reference: https://www.infoworld.com/article/2078440/java-tip-when-to-use-forkjoinpool-vs-executorservice.htm

