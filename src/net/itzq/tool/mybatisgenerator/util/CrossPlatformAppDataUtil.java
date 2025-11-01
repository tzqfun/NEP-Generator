package net.itzq.tool.mybatisgenerator.util;

import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *  CrossPlatformAppDataUtil
 *
 *  @author tangzq
 */
public class CrossPlatformAppDataUtil {

    public static String getOrCreateAppDataPathDefault( String dirName ) {
         return   CrossPlatformAppDataUtil.getOrCreateAppDataPath("nep_frame", "generator_plugin", dirName);

    }
    public static String getOrCreateAppDataPath(String groupName, String appName, String subDir) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String appDataPath;

            if (os.contains("win")) {
                // Windows系统
                appDataPath = getWindowsLocalLowPath();
            } else if (os.contains("mac")) {
                // macOS系统
                appDataPath = System.getProperty("user.home") + "/Library/Application Support";
            } else {
                // Linux/Unix系统
                appDataPath = System.getProperty("user.home") + "/.local/share";
            }

            String fullPath = appDataPath + "/" + groupName + "/" + appName;

            if (StringUtils.isNotBlank(subDir)) {
                fullPath = fullPath + "/" + subDir;
            }

            createDirectory(fullPath);

            return fullPath;

        } catch (Exception e) {
            throw new RuntimeException("无法创建应用数据目录: " + e.getMessage(), e);
        }
    }

    private static String getWindowsLocalLowPath() {
        // 优先使用环境变量
        String localAppData = System.getenv("LOCALAPPDATA");
        if (localAppData != null) {
            return localAppData.replace("Local", "LocalLow");
        }


        return System.getProperty("user.home") + "\\AppData\\LocalLow";
    }

    private static void createDirectory(String path) throws Exception {
        Path dirPath = Paths.get(path);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
    }
}
