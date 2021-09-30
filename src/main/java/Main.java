import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main {
    public static void main(String[] args) {
        BlockingQueue<String> buffer = new LinkedBlockingQueue<>();
        BlockingQueue<Boolean> exitQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> resultQueue = new LinkedBlockingQueue<>();

        File file = new File("text.txt");
        WordsReader reader = new WordsReader(file, buffer, exitQueue);
        Thread readerThread = new Thread(reader);
        readerThread.start();

        WordsFilter filter = word -> word.contains("страда");
        WordsCounter counter = new WordsCounter(filter, buffer, resultQueue);
        Thread counterThread = new Thread(counter);
        counterThread.start();

        try {
            exitQueue.take();
        } catch (InterruptedException ignored) {

        }

        readerThread.interrupt();
        counterThread.interrupt();

        try {
            int count = resultQueue.take();
            System.out.printf("The number of occurrences of the word \"страдание\": %d\n", count);
        } catch (InterruptedException ignored) {

        }
    }
}