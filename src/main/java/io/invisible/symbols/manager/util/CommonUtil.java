package io.invisible.symbols.manager.util;

/**
 *
 * @author ValeriiL
 */
public class CommonUtil {

    private CommonUtil() {
        // hidden constructor
    }

    public static String quote(String value) {
        return "\"" + value + "\"";
    }

    public static String unquote(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        } else {
            return value;
        }
    }

    public static String increaseId(String id) {
        return String.format("%010d", Integer.parseInt(id) + 1);
    }

    public static Object[] asArray(Object... params) {
        return params;
    }
}
