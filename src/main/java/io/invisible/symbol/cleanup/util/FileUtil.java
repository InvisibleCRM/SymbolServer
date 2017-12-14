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

    public static boolean deleteFolder(File folder) throws IOException {
        return deleteFolder(folder, new HashSet<String>());
    }

    private static boolean deleteFolder(File folder, Set<String> history) throws IOException {
        boolean result = true;

        for (File file : folder.listFiles()) {
            String canonicalPath = file.getCanonicalPath();

            if (!history.contains(canonicalPath)) {
                history.add(canonicalPath);
                if (file.isDirectory()) {
                    result &= deleteFolder(file, history);
                } else {
                    result &= file.delete();
                }
            }
        }
        folder.delete();

        return result;
    }
}
