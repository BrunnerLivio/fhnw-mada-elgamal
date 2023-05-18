package src.ch.fhnw.mada;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileHelper {
  public static void writeFile(String fileName, String content) {
    File keyFile = new File(fileName);

    try {
      keyFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      Files.write(keyFile.toPath(), content.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String readFile(String filePath) {
    File file = new File(filePath);
    String content = null;
    try {
      content = new String(Files.readAllBytes(file.toPath()));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return content;
  }
}
