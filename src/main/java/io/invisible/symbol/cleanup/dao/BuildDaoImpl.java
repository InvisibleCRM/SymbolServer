package io.invisible.symbol.cleanup.dao;

import io.invisible.symbol.cleanup.Configuration;
import io.invisible.symbol.cleanup.model.BuildInfo;
import io.invisible.symbol.cleanup.model.BuildSymbol;
import io.invisible.symbol.cleanup.util.CommonUtil;
import io.invisible.symbol.cleanup.util.FileUtil;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ValeriiL
 */
public class BuildDaoImpl {

    private static final Logger LOGGER = Logger.getLogger(BuildDaoImpl.class.getName());

    private static BuildDaoImpl instance;

    private final File root = new File(Configuration.getRootPath());

    public List<String> getProjects() {
        List<String> result = new ArrayList<>();

        for (String fileName : root.list()) {
            File childDirectory = new File(root, fileName);
            if (!childDirectory.isDirectory()) {
                continue;
            }

            File adminRoot = new File(childDirectory, "000Admin");
            if (adminRoot.exists()) {
                result.add(fileName);
            }
        }

        return result;
    }

    public List<BuildInfo> getBuilds(String projectName) {
        File projectRoot = new File(root, projectName);
        File adminRoot = new File(projectRoot, "000Admin");
        File projectDescriptor = new File(adminRoot, "server.txt");
        List<BuildInfo> result = new ArrayList<>();

        try (BufferedReader projectDescriptorReader = new BufferedReader(new FileReader(projectDescriptor))) {
            while (projectDescriptorReader.ready()) {
                String line = projectDescriptorReader.readLine();

                String[] parts = line.split(",");

                BuildInfo buildInfo = new BuildInfo();
                buildInfo.setId(parts[0]);
                buildInfo.setDate(parts[3]);
                buildInfo.setTime(parts[4]);
                buildInfo.setName(unquote(parts[5]));
                buildInfo.setVersion(unquote(parts[6]));
                buildInfo.setComment(unquote(parts[7]));
                buildInfo.setSize(getBuildSize(projectRoot, parts[0]));

                result.add(buildInfo);
            }
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public long getBuildSize(File projectRoot, String buildId) {
        File adminRoot = new File(projectRoot, "000Admin");
        File buildDescriptor = new File(adminRoot, buildId);
        long result = 0;

        for (BuildSymbol buildSymbol : getBuildSymbols(buildDescriptor)) {
            File buildSymbolRoot = new File(projectRoot, buildSymbol.getId());

            if (buildSymbolRoot.exists()) {
                try {
                    result += FileUtil.getFolderSize(buildSymbolRoot);
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, null, ex);
                }
            }
        }

        return result;
    }

    public void deleteBuilds(String projectName, List<String> buildIds) {
        File projectRoot = new File(root, projectName);
        File adminRoot = new File(projectRoot, "000Admin");
        File projectDescriptor = new File(adminRoot, "server.txt");
        File resultProjectDescriptor = new File(adminRoot, String.format("server.txt.tmp%d", System.currentTimeMillis()));

        try (BufferedReader projectDescriptorReader = new BufferedReader(new FileReader(projectDescriptor));
                BufferedWriter resultProjectDescriptorWriter = new BufferedWriter(new FileWriter(resultProjectDescriptor))) {
            while (projectDescriptorReader.ready()) {
                String line = projectDescriptorReader.readLine();

                String buildId = line.split(",")[0];
                if (!buildIds.contains(buildId)) {
                    resultProjectDescriptorWriter.write(line);
                    resultProjectDescriptorWriter.newLine();
                }
            }

        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        projectDescriptor.delete();
        resultProjectDescriptor.renameTo(projectDescriptor);

        for (String buildId : buildIds) {
            File buildDescriptor = new File(adminRoot, buildId);

            for (BuildSymbol buildSymbol : getBuildSymbols(buildDescriptor)) {
                File buildSymbolRoot = new File(projectRoot, buildSymbol.getId());

                if (buildSymbolRoot.exists()) {
                    LOGGER.log(Level.INFO, "Deleting {0}", buildSymbolRoot.getAbsolutePath());
                    try {
                        FileUtil.deleteFolder(buildSymbolRoot);
                    } catch (IOException ex) {
                        LOGGER.log(Level.SEVERE, null, ex);
                    }
                }
            }

            buildDescriptor.delete();
        }
    }

    public List<BuildSymbol> getBuildSymbols(File buildDescriptor) {
        List<BuildSymbol> result = new ArrayList<>();

        try (BufferedReader buildDescriptorReader = new BufferedReader(new FileReader(buildDescriptor))) {
            int lineNumber = 0;

            while (buildDescriptorReader.ready()) {
                String line = buildDescriptorReader.readLine();
                lineNumber++;

                String[] parts = line.split(",");
                if (parts.length != 2) {
                    LOGGER.log(Level.WARNING, "Unable to read build symbols from file {0} at line {1}", CommonUtil.asArray(buildDescriptor.getAbsolutePath(), lineNumber));
                    continue;
                }

                BuildSymbol buildSymbol = new BuildSymbol();
                buildSymbol.setId(unquote(parts[0]));
                buildSymbol.setPath(unquote(parts[1]));

                result.add(buildSymbol);
            }
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private static String unquote(String value) {
        return value.substring(1, value.length() - 1);
    }

    public static BuildDaoImpl getInstance() {
        if (instance == null) {
            instance = new BuildDaoImpl();
        }
        return instance;
    }
}
