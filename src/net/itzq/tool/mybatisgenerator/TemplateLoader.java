package net.itzq.tool.mybatisgenerator;

import net.itzq.tool.mybatisgenerator.util.StrUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
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
                return "IO错误：读取模版失败，文件不存在";
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
            return "";
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
