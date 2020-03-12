package Writer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BufferedWriter {

    private final int MAX_SIZE = 100;
    private int currentSize;
    private ExecutorService executorService;
    private LinkedList<Future<?>> tasks;
    private StringBuilder buffer;

    public BufferedWriter() {
        currentSize = 0;
        executorService = Executors.newSingleThreadExecutor();
        tasks = new LinkedList<>();
        buffer = new StringBuilder();
    }

    public void write(String str) {
        currentSize++;
        buffer.append(str).append("\n");
        if (currentSize >= MAX_SIZE) {
            String toWrite = buffer.toString();
            tasks.add(executorService.submit(() -> new WriteTask().write(toWrite)));
            cleanTasks();
            currentSize = 0;
            buffer.setLength(0);
        }
    }

    public void writeFinal(String str) {
        cancelTasks();
        Future<?> write = executorService.submit(() -> new WriteTask().write(str));
        try {
            write.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        cancelTasks();
        executorService.shutdown();
    }

    private void cancelTasks() {
        for (Iterator<Future<?>> it = tasks.descendingIterator(); it.hasNext(); ) {
            it.next().cancel(false);
        }
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
