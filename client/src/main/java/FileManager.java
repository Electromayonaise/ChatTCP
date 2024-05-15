import java.io.File;
import java.io.PrintWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;

public class FileManager {
    private static FileManager instance;
    private String route;
    private PrintWriter printWriter;
    private File file;
    private static final String FILE_NAME = "mylog.txt";
    private static final String DIRECTORY_NAME = "/data";

    // Constructor privado para evitar la creación de instancias fuera de la clase
    private FileManager() {
        // Inicializa los atributos según sea necesario
        route = getFilePath();
        System.out.println("Guardando historial del chat en: " + route);
        try {
            file = new File(route);
            file.delete();
            file.createNewFile();
            printWriter = new PrintWriter(new FileWriter(file, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    public void writeToFile(String content) {
        printWriter.println(content);
        printWriter.flush();
    }

    public void closeFile() {
        printWriter.close();
    }

    public String getFilePath() {
        return Paths.get(System.getProperty("user.dir"), DIRECTORY_NAME, FILE_NAME).toString();
    }

}
