package Writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class WriteTask {

    private final String FILE_PATH;

    WriteTask(String filePath) {
        FILE_PATH = filePath;
    }

    void write(String str) {
        try (FileWriter writer = new FileWriter(new File(FILE_PATH), true)) {
            writer.write(str);
            writer.flush();
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
