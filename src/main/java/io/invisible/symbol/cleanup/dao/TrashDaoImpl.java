package io.invisible.symbol.cleanup.dao;

import io.invisible.symbol.cleanup.Configuration;
import io.invisible.symbol.cleanup.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ValeriiL
 */
public class TrashDaoImpl {

    private static final Logger LOGGER = Logger.getLogger(TrashDaoImpl.class.getName());

    private static TrashDaoImpl instance;

    private final File root;
    private final File trashRoot;

    private TrashDaoImpl() {
        root = new File(Configuration.getRootPath());
        trashRoot = new File(root, ".trash");
    }

    public long getTrashSize() {
        if (trashRoot.exists()) {
            try {
                return FileUtil.getFolderSize(trashRoot);
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
                return 0;
            }
        } else {
            return 0;
        }
    }

    public void moveToTrash(File file) {
        File trashFile = getTrashFile(file);

        trashFile.getParentFile().mkdirs();

        file.renameTo(trashFile);
    }

    public File getTrashFile(File file) {
        String rootPath = root.getAbsolutePath();
        String filePath = file.getAbsolutePath();
        String targetPath = filePath.replace(rootPath, "");
        return new File(trashRoot, targetPath);
    }

    public static TrashDaoImpl getInstance() {
        if (instance == null) {
            instance = new TrashDaoImpl();
        }
        return instance;
    }
}
