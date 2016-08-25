package org.mcmonkey.denizen2core.utilities;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Useful utility methods.
 */
public class CoreUtilities {

    public final static String encoding = "UTF-8";

    public final static int buff10k = 1024 * 10;

    public final static Random random = new Random();

    public static String doubleToString(double input) {
        String temp = String.valueOf(input);
        if (temp.endsWith(".0")) {
            return temp.substring(0, temp.length() - 2);
        }
        return temp;
    }

    public static String exceptionString(Throwable except) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        while (except != null) {
            sb.append((first ? "" : "Caused by: ") + except.toString() + "\n");
            for (StackTraceElement ste : except.getStackTrace()) {
                sb.append(ste.toString() + "\n");
            }
            if (except.getCause() == except) {
                return sb.toString();
            }
            except = except.getCause();
            first = false;
        }
        return sb.toString();
    }

    /**
     * The necessity of this method makes me hate Java a fair bit.
     */
    public static String streamToString(InputStream is) {
        try {
            InputStreamReader isr = new InputStreamReader(is, encoding);
            final char[] buffer = new char[buff10k];
            final StringBuilder out = new StringBuilder();
            try (Reader in = new InputStreamReader(is, encoding)) {
                while (true) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0) {
                        break;
                    }
                    out.append(buffer, 0, rsz);
                }
            }
            return out.toString();
        }
        catch (Exception ex) {
            return null;
        }
    }

    public static List<String> split(String str, char c) {
        List<String> strings = new ArrayList<String>();
        int start = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                strings.add(str.substring(start, i));
                start = i + 1;
            }
        }
        strings.add(str.substring(start, str.length()));
        return strings;
    }

    public static String concat(List<String> str, String split) {
        StringBuilder sb = new StringBuilder();
        if (str.size() > 0) {
            sb.append(str.get(0));
        }
        for (int i = 1; i < str.size(); i++) {
            sb.append(split).append(str.get(i));
        }
        return sb.toString();
    }

    public static List<String> split(String str, char c, int max) {
        List<String> strings = new ArrayList<String>();
        int start = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c) {
                strings.add(str.substring(start, i));
                start = i + 1;
                if (strings.size() + 1 == max) {
                    break;
                }
            }
        }
        strings.add(str.substring(start, str.length()));
        return strings;
    }

    public static String toLowerCase(String input) {
        char[] data = input.toCharArray();
        for (int i = 0; i < data.length; i++) {
            if (data[i] >= 'A' && data[i] <= 'Z') {
                data[i] -= 'A' - 'a';
            }
        }
        return new String(data);
    }

    public static String toUpperCase(String input) {
        char[] data = input.toCharArray();
        for (int i = 0; i < data.length; i++) {
            if (data[i] >= 'a' && data[i] <= 'z') {
                data[i] -= 'a' - 'A';
            }
        }
        return new String(data);
    }

    public static String getXthArg(int argc, String args) {
        char[] data = args.toCharArray();
        StringBuilder nArg = new StringBuilder();
        int arg = 0;
        int x = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == ' ') {
                arg++;
                if (arg > argc) {
                    return nArg.toString();
                }
            }
            else if (arg == argc) {
                nArg.append(data[i]);
            }
        }
        return nArg.toString();
    }

    public static boolean xthArgEquals(int argc, String args, String input) {
        char[] data = args.toCharArray();
        char[] data2 = input.toCharArray();
        int arg = 0;
        int x = 0;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == ' ') {
                arg++;
            }
            else if (arg == argc) {
                if (x == data2.length) {
                    return false;
                }
                if (data2[x++] != data[i]) {
                    return false;
                }
            }
        }
        return x == data2.length;
    }
}
