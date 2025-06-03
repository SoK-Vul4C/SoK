import java.io.File;

public class FileUtil {
    
    static public File allocateFile(File dir, String name) {
        // Check if the name contains a query string
        int q = name.indexOf('?');
        if (q > 0) {
            name = name.substring(0, q); // Exclude query string from the file name
        }

        File file = new File(dir, name);
        
        // Check for existing files with the same name
        String baseName = "";
        String extension = "";
        int lastDotIndex = name.lastIndexOf(".");
        if (lastDotIndex > 0) {
            baseName = name.substring(0, lastDotIndex);
            extension = name.substring(lastDotIndex);
        } else {
            baseName = name;
        }

        int index = 1;
        while (file.exists()) {
            file = new File(dir, baseName + "-" + index + extension);
            index++;
        }
        
        // Create parent directories if they do not exist
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        return file;
    }
}