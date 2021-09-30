import java.util.concurrent.BlockingQueue;

public class WordsCounter implements Runnable {
    private final WordsFilter filter;
    private final BlockingQueue<String> buffer;
    private final BlockingQueue<Integer> resultQueue;

    private int count = 0;

    public WordsCounter(WordsFilter filter, BlockingQueue<String> buffer, BlockingQueue<Integer> resultQueue) {
        this.filter = filter;
        this.buffer = buffer;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String word = buffer.take();

                if (filter.apply(word)) {
                    count++;
                }
            } catch (InterruptedException e) {
                break;
            }
        }

        try {
            resultQueue.put(count);
        } catch (InterruptedException ignored) {

        }

        Thread.currentThread().interrupt();
    }
}
