package Writer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

public class BufferedWriter {

    private final int MAX_SIZE = 1000000;
    private final String FILE_PATH;
    private int currentSize;
    private ExecutorService executorService;
    private LinkedList<Future<?>> tasks;
    private StringBuilder buffer;
    private AtomicBoolean lock;

    public BufferedWriter(String filePath) {
        FILE_PATH = filePath;
        currentSize = 0;
        executorService = Executors.newSingleThreadExecutor();
        tasks = new LinkedList<>();
        buffer = new StringBuilder();
        lock = new AtomicBoolean();
        lock.set(false);
    }

    public void write(String str) {
        currentSize++;
        buffer.append(str).append("\n");
        if (currentSize >= MAX_SIZE) {
            flush();
        }
    }

    public void writeFinal(String str) {
        flush();
        cancelTasks();
        Future<?> write = executorService.submit(() -> new WriteTask(FILE_PATH).write(str));
        try {
            write.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void flush() {
        String toWrite = buffer.toString();
        while (lock.getAndSet(true)) {

        }
        tasks.add(executorService.submit(() -> new WriteTask(FILE_PATH).write(toWrite)));
        lock.set(false);
        cleanTasks();
        currentSize = 0;
        buffer.setLength(0);
    }

    public void close() {
        cancelTasks();
        executorService.shutdown();
    }

    private void cancelTasks() {
        while (lock.getAndSet(true)) {}
        for (Iterator<Future<?>> it = tasks.descendingIterator(); it.hasNext(); ) {
            it.next().cancel(false);
        }
        lock.set(false);
    }

    private void cleanTasks() {
        boolean isTaskDone = true;
        while (isTaskDone) {
            isTaskDone = tasks.peek() != null && tasks.peek().isDone();
            if (isTaskDone) {
                tasks.poll();
            }
        }
    }

}
