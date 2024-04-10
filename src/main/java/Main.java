import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    static File progressDirectory = new File("C://Games/savegames");

    public static void main(String[] args) {
        String firstFileName = getFileName(1);
        String secondFileName = getFileName(2);
        String thirdFileName = getFileName(3);
        List<String> progressFiles = Arrays.asList(firstFileName, secondFileName, thirdFileName);

        GameProgress firstPlayerProgress = new GameProgress(90, 2, 3, 3);
        GameProgress secondPlayerProgress = new GameProgress(86, 3, 4, 5);
        GameProgress thirdPlayerProgress = new GameProgress(73, 4, 6, 7);

        saveProgress(firstPlayerProgress, firstFileName);
        saveProgress(secondPlayerProgress, secondFileName);
        saveProgress(thirdPlayerProgress, thirdFileName);

        filesToZipArchive(progressFiles);

        deleteFile(firstFileName);
        deleteFile(secondFileName);
        deleteFile(thirdFileName);
    }

    public static String getFileName(int number) {
        return progressDirectory.getPath() + "/save" + number + ".dat";
    }

    public static void saveProgress(GameProgress progress, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(progress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void filesToZipArchive(List<String> progressFiles) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(progressDirectory.getPath() + "/zip_output.zip"))) {
            for (String file : progressFiles) {
                File fileToZip = new File(file);
                FileInputStream fis = new FileInputStream(fileToZip.getPath());
                ZipEntry entry = new ZipEntry(fileToZip.getName());
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                zout.write(buffer);
                zout.closeEntry();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteFile(String fileName) {
        try {
            Files.delete(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}