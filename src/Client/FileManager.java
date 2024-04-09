import java.io.File;
import java.io.PrintWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileManager {
    private static FileManager instance;
    private String route;
    private PrintWriter printWriter;
    private File file;

    // Constructor privado para evitar la creación de instancias fuera de la clase
    private FileManager() {
        // Inicializa los atributos según sea necesario
        route = System.getProperty("user.dir")  +File.separator+ "data" + File.separator + "myData.txt";
        System.out.println(route);
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
}
