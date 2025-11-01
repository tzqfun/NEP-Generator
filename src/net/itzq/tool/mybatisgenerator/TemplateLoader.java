package net.itzq.tool.mybatisgenerator;

import net.itzq.tool.mybatisgenerator.util.CrossPlatformAppDataUtil;
import net.itzq.tool.mybatisgenerator.util.StrUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 *
 * @discription
 *
 * @created 2022/11/22 21:15
 */
public class TemplateLoader {

    private static final String TEMPLATE_FILE = "template/";

    public static String convertTemplate(String fileName, Map<String, String> params) {
        String content = readTemplateFile(fileName);
        if (StrUtil.isBlank(content)) {
            return null;
        }

        for (String key : params.keySet()) {
            String rpk = "\\$\\{" + key + "}";
            String val = params.get(key);
            if (val == null) {
                val = "";
            }
            content = content.replaceAll(rpk, val);
        }

        return content;
    }

    /**
     * 从模版文件加载
     */
    public static String readTemplateFile(String fileName) {
        StringBuilder cfg = new StringBuilder();
        String filePath = TEMPLATE_FILE + fileName;
        File file = new File(filePath);
        InputStream in = null;
        if (file.exists()) {
            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            in = TemplateLoader.class.getClassLoader().getResourceAsStream(filePath);
        }

        BufferedReader buffer = null;
        try {
            if (in == null) {
                return null;
            }
            buffer = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String tempString = null;
            while ((tempString = buffer.readLine()) != null) {
                cfg.append(tempString).append("\r\n");
            }
            buffer.close();
        } catch (IOException e) {
            System.out.println("读取文件错误");
            e.printStackTrace();
            return null;
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException e1) {
                }
            }
        }
        return cfg.toString();
    }

    public static String readLocalTemplateFile(String dirName, String fileName) throws IOException {
        String dataPath = CrossPlatformAppDataUtil.getOrCreateAppDataPathDefault(dirName);
        File dir = new File(dataPath);
        if (dir.exists() && dir.isDirectory()) {
            String filePath = dataPath + "/" + fileName;

            File targetFile = new File(filePath);
            if (targetFile.exists()) {
                String data = readLocalFile(filePath);
                return data;
            } else {
                String templateFile = readTemplateFile(dirName + "/" + fileName);
                if (StringUtils.isNotBlank(templateFile)) {
                    Path path = Paths.get(filePath);
                    Files.write(path, templateFile.getBytes(StandardCharsets.UTF_8));
                }

                if (targetFile.exists()) {
                    String data = readLocalFile(filePath);
                    return data;
                }
            }

        }
        return null;
    }

    public static String readLocalFile(String filePath) {
        try {

            byte[] bytes = Files.readAllBytes(Paths.get(filePath));

            String content = new String(bytes, StandardCharsets.UTF_8);
            return content;
        } catch (IOException e) {
            System.err.println("读取文件失败: " + e.getMessage());
            return null;
        }
    }

    public static boolean testReadTemplateFile(String fileName) {
        String filePath = TEMPLATE_FILE + fileName;
        File file = new File(filePath);
        InputStream in = null;
        if (file.exists()) {
            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            in = TemplateLoader.class.getClassLoader().getResourceAsStream(filePath);
        }
        if (in == null) {
            return false;
        }
        try {
            in.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
