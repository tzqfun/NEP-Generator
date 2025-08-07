package net.itzq.tool.mybatisgenerator;

import net.itzq.tool.mybatisgenerator.util.StrUtil;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @discription
 *
 * @created 2022/11/22 21:33
 */
public class DataConvert {
    static final Map<String, String> mysqlDataTypeMapping = new HashMap<>();

    public static final String NEWLINE = "\r\n";

    public static final String INDENT = "    ";
    public static final String INDENTx2 = "        ";
    public static final String INDENTx3 = "            ";
    public static final String INDENTx4 = "                ";

    static {
        Map<String, String> m = mysqlDataTypeMapping;
        m.put("tinyint", "Integer");//如果配置属性 tinyInt1isBit 设置为 true (默认)并且存储大小为1，则为  Boolean 否则 Integer
        m.put("smallint", "Integer");
        m.put("mediumint", "Integer");
        m.put("int", "Integer"); //  如果是 unsigned 则是  Long
        m.put("integer", "Integer"); //  如果是 unsigned 则是  Long
        m.put("bigint", "Long"); //如果是 unsigned 则是 java.math.BigInteger
        m.put("bit", "byte[]"); // >1  如果是  bit（1） Boolean
        m.put("double", "Double");
        m.put("float", "Float");
        m.put("decimal", "BigDecimal");    //    decimal	java.math.BigDecimal
        m.put("numeric", "Object");
        m.put("char", "String");  //     除非列的字符集是BINARY，否则返回byte[]
        m.put("varchar", "String");  //     除非列的字符集是BINARY，否则返回byte[]
        m.put("date", "Date");  //
        m.put("time", "Time");  //   时间 HH:MM:SS
        m.put("year", "Date");  //  如果 yearIsDateType 配置属性设置为 false，则返回的对象类型为 java.sql.Short
        m.put("timestamp", "Timestamp");
        m.put("datetime", "Date");  // 对应 Timestamp 但一般用Date
        m.put("tinyblob", "byte[]");
        m.put("blob", "byte[]");
        m.put("mediumblob", "byte[]");
        m.put("longblob", "byte[]");
        m.put("tinytext", "String");
        m.put("longtext", "String");
        m.put("text", "String");
        m.put("mediumtext", "String");
        m.put("enum", "String");
        m.put("set", "String");
        m.put("json", "String");
        m.put("binary", "byte[]");
        m.put("varbinary", "byte[]");

    }

    /**
     * 转驼峰
     */
    public static String toUpperCamel(String fieldName) {
        return toCamel(fieldName, true);
    }

    public static String toLowerCamel(String fieldName) {
        return toCamel(fieldName, false);
    }

    private static String toCamel(String fieldName, boolean upper) {
        String rtn = fieldName;
        if (StrUtil.isNotBlank(fieldName) && fieldName.contains("_")) {
            //            fieldName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, fieldName.toLowerCase());
            StringBuilder builder = new StringBuilder();

            String string = fieldName.toLowerCase();
            for (int i = 0; i < string.length(); i++) {
                char c = string.charAt(i);
                if (c != '_') {
                    builder.append(c);
                } else {
                    if (i + 1 < string.length()) {
                        builder.append(String.valueOf(string.charAt(i + 1)).toUpperCase());
                        i++;
                    }
                }
            }

            if (upper && builder.length() > 0) {
                builder.replace(0, 1, String.valueOf(builder.charAt(0)).toUpperCase());
            }

            rtn = builder.toString();
        }

        return rtn;
    }

    /**
     * 映射类型
     */
    public static String mysqlDataTypeMapping(String dbType, Set<String> importList) {
        if (importList == null) {
            importList = new LinkedHashSet<>();
        }

        if (mysqlDataTypeMapping.containsKey(dbType.toLowerCase())) {
            return mysqlDataTypeMapping.get(dbType.toLowerCase());
        }
        return "Object";
    }
}
