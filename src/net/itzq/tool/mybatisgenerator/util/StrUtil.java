package net.itzq.tool.mybatisgenerator.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @discription
 *
 * @created 2022/11/24 20:50
 */
public class StrUtil {

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static boolean isBlank(CharSequence str) {
        int length;
        if (str != null && (length = str.length()) != 0) {
            for (int i = 0; i < length; ++i) {
                if (!isBlankChar(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isBlankChar(char c) {
        return isBlankChar((int) c);
    }

    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == 65279 || c == 8234;
    }

    public static boolean equals(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, false);
    }

    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            return str2 == null;
        } else if (null == str2) {
            return false;
        } else {
            return ignoreCase ? str1.toString().equalsIgnoreCase(str2.toString()) : str1.equals(str2);
        }
    }

    public static boolean equalsAny(final CharSequence string, final CharSequence... searchStrings) {
        if (ArrayUtils.isNotEmpty(searchStrings)) {
            for (CharSequence next : searchStrings) {
                if (equals(string, next)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean endsWithAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) {
        if (!StringUtils.isEmpty(string) && !ArrayUtils.isEmpty(searchStrings)) {

            for (int i = 0; i < searchStrings.length; i++) {
                CharSequence searchString = searchStrings[i];
                if (StringUtils.endsWithIgnoreCase(string, searchString)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    public static boolean containsAnyIgnoreCase(CharSequence cs, CharSequence... searchCharSequences) {
        if (!StringUtils.isEmpty(cs) && !ArrayUtils.isEmpty(searchCharSequences)) {

            for (int i = 0; i < searchCharSequences.length; i++) {
                CharSequence searchCharSequence = searchCharSequences[i];
                if (StringUtils.containsIgnoreCase(cs, searchCharSequence)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }
}
