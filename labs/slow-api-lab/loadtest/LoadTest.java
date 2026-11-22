import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 설치 없이 쓰는 간단한 부하 생성기.
 *
 * 사용법: java loadtest/LoadTest.java [URL] [동시 사용자 수] [측정 시간(초)]
 * 예)     java loadtest/LoadTest.java http://localhost:8080/api/teams 20 10
 *
 * 처음 3초는 워밍업이라 결과에서 제외한다.
 */
public class LoadTest {

    private static final int WARMUP_SECONDS = 3;

    public static void main(String[] args) throws Exception {
        String url = args.length > 0 ? args[0] : "http://localhost:8080/api/teams";
        int concurrency = args.length > 1 ? Integer.parseInt(args[1]) : 10;
        int seconds = args.length > 2 ? Integer.parseInt(args[2]) : 10;

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        long start = System.nanoTime();
        long measureFrom = start + WARMUP_SECONDS * 1_000_000_000L;
        long end = measureFrom + seconds * 1_000_000_000L;

        List<Long> latenciesNanos = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger errors = new AtomicInteger();
        CountDownLatch done = new CountDownLatch(concurrency);
        ExecutorService pool = Executors.newFixedThreadPool(concurrency);

        System.out.printf("Load start: %s (concurrency=%d, warmup=%ds, measure=%ds)%n",
                url, concurrency, WARMUP_SECONDS, seconds);

        for (int i = 0; i < concurrency; i++) {
            pool.submit(() -> {
                try {
                    while (System.nanoTime() < end) {
                        long t0 = System.nanoTime();
                        boolean ok;
                        try {
                            HttpResponse<Void> response =
                                    client.send(request, HttpResponse.BodyHandlers.discarding());
                            ok = response.statusCode() == 200;
                        } catch (Exception e) {
                            ok = false;
                        }
                        long t1 = System.nanoTime();
                        if (t0 >= measureFrom) {
                            if (ok) {
                                latenciesNanos.add(t1 - t0);
                            } else {
                                errors.incrementAndGet();
                            }
                        }
                    }
                } finally {
                    done.countDown();
                }
            });
        }

        done.await();
        pool.shutdown();

        List<Long> sorted = new ArrayList<>(latenciesNanos);
        Collections.sort(sorted);
        int n = sorted.size();
        if (n == 0) {
            System.out.println("No successful requests. Is the server running and the URL correct? errors=" + errors.get());
            return;
        }

        System.out.println("---------------- RESULT ----------------");
        System.out.printf("successful requests : %d (failed %d)%n", n, errors.get());
        System.out.printf("throughput (TPS)    : %.1f req/s%n", n / (double) seconds);
        System.out.printf("latency p50         : %.1f ms%n", ms(sorted.get((int) (n * 0.50))));
        System.out.printf("latency p95         : %.1f ms%n", ms(sorted.get(Math.min(n - 1, (int) (n * 0.95)))));
        System.out.printf("latency p99         : %.1f ms%n", ms(sorted.get(Math.min(n - 1, (int) (n * 0.99)))));
        System.out.printf("latency max         : %.1f ms%n", ms(sorted.get(n - 1)));
    }

    private static double ms(long nanos) {
        return nanos / 1_000_000.0;
    }
}
