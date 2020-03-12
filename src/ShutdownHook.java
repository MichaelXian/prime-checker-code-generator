import Writer.BufferedWriter;

public class ShutdownHook extends Thread {

    private final String END_CODE =
            "        }\n" +
            "        return false;\n" +
            "    }\n" +
            "}";
    private BufferedWriter writer;
    private Counter counter;

    ShutdownHook(BufferedWriter writer, Counter counter) {
        this.writer = writer;
        this.counter = counter;
    }

    public void run() {
        System.out.println("Shutting down");
        writer.writeFinal(END_CODE);
        writer.close();
        System.out.println("Current counter: " + counter.get());
    }
}
