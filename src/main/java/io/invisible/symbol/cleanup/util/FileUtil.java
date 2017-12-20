package io.invisible.symbol.cleanup.util;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author ValeriiL
 */
public class FileUtil {

    private FileUtil() {
        // hidden constructor
    }

    public static long getFolderSize(File folder) throws IOException {
        return getFolderSize(folder, new HashSet<String>());
    }

    private static long getFolderSize(File folder, Set<String> history) throws IOException {
        long result = 0;

        for (File file : folder.listFiles()) {
            String canonicalPath = file.getCanonicalPath();

            if (!history.contains(canonicalPath)) {
                history.add(canonicalPath);
                if (file.isDirectory()) {
                    result += getFolderSize(file, history);
                } else {
                    result += file.length();
                }
            }
        }

        return result;
    }
}
