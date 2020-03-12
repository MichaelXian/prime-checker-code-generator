import Writer.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private static final String FILE_PATH = "PrimeChecker.java";
    private static final String START_CODE = "public class PrimeChecker {\n" +
            "\n" +
            "    boolean isPrime(int i) {\n" +
            "        if (i <= 1) return false;\n" +
            "        switch (i) {";

    public static void main(String[] args) {
        Counter c = new Counter();
        BufferedWriter writer = new BufferedWriter();
        ShutdownHook shutdownHook = new ShutdownHook(writer, c);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        clearFile();
        writer.write(START_CODE);
        for (; c.get() < Integer.MAX_VALUE; c.increment()) {
            writer.write(
                    String.format("            case %d: return %b;", c.get(), isPrime(c.get()))
            );
        }
        shutdownHook.start();
    }

    private static void clearFile() {
        try (FileWriter writer = new FileWriter(new File(FILE_PATH))) {
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean isPrime(int n) {
        if(n < 2) return false;
        if(n == 2 || n == 3) return true;
        if(n%2 == 0 || n%3 == 0) return false;
        long sqrtN = (int)Math.sqrt(n)+1;
        for(long i = 6L; i <= sqrtN; i += 6) {
            if(n%(i-1) == 0 || n%(i+1) == 0) return false;
        }
        return true;
    }

}
