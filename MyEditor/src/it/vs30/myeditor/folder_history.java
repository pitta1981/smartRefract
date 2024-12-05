package it.vs30.myeditor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class folder_history {

    private static final String HISTORY_FILE = System.getProperty("user.home")
            + "/smartRefract-data/folder_history.txt";

    public static void saveLastOpenedFolder(String folderPath) {
        try (FileWriter writer = new FileWriter(HISTORY_FILE)) {
            writer.write(folderPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveLastOpenedFolder(File folder) {
        saveLastOpenedFolder(folder.getAbsolutePath());
    }

    public static String getLastOpenedFolder() {
        try {
            if (Files.exists(Paths.get(HISTORY_FILE))) {
                return new String(Files.readAllBytes(Paths.get(HISTORY_FILE)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return System.getProperty("user.home");
    }
}
