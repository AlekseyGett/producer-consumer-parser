import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class WordsReader implements Runnable {
    private final File source;
    private final BlockingQueue<String> buffer;
    private final BlockingQueue<Boolean> exitQueue;

    public WordsReader(File source, BlockingQueue<String> buffer, BlockingQueue<Boolean> exitQueue) {
        this.source = source;
        this.buffer = buffer;
        this.exitQueue = exitQueue;
    }

    @Override
    public void run() {
        Scanner scanner;

        try {
            String charset = "UTF-8";
            scanner = new Scanner(source, charset);
        } catch (FileNotFoundException e) {
            exit();
            return;
        }

        String pattern = "[^a-zA-Zа-яА-Я]+";
        scanner.useDelimiter(pattern);

        while (scanner.hasNext()) {
            String word = scanner.next().toLowerCase();

            try {
                buffer.put(word);
            } catch (InterruptedException e) {
                break;
            }
        }

        scanner.close();

        exit();

        Thread.currentThread().interrupt();
    }

    private void exit() {
        try {
            exitQueue.put(true);
        } catch (InterruptedException ignored) {

        }
    }
}
