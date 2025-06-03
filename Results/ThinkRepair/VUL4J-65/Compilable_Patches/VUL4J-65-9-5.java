import java.io.File;

public class FileUtil {
    
    static public File allocateFile(File dir, String name) {
        // Check if the name contains a query string
        int q = name.indexOf('?');
        if (q > 0) {
            name = name.substring(0, q); // Exclude query string from the file name
        }

        String baseName = "";
        String extension = "";

        // Split the name into baseName and extension (if exists)
        int lastDotIndex = name.lastIndexOf('.');
        if (lastDotIndex > 0) {
            baseName = name.substring(0, lastDotIndex);
            extension = name.substring(lastDotIndex);
        } else {
            baseName = name;
        }

        File file = new File(dir, name);
        int index = 1;

        // Handle file name conflicts by incrementing index
        while (file.exists()) {
            file = new File(dir, baseName + "-" + index + extension);
            index++;
        }

        // Create parent directories if they do not exist
        file.getParentFile().mkdirs();

        return file;
    }
}