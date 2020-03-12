package Writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class WriteTask {

    private final String FILE_NAME = "PrimeChecker.java";

    void write(String str) {
        try (FileWriter writer = new FileWriter(new File(FILE_NAME), true)) {
            writer.write(str);
            writer.flush();
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
