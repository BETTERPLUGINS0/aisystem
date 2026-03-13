/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3.math;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class NumberUtils {
    public static final Long LONG_ZERO = 0L;
    public static final Long LONG_ONE = 1L;
    public static final Long LONG_MINUS_ONE = -1L;
    public static final Integer INTEGER_ZERO = 0;
    public static final Integer INTEGER_ONE = 1;
    public static final Integer INTEGER_TWO = 2;
    public static final Integer INTEGER_MINUS_ONE = -1;
    public static final Short SHORT_ZERO = 0;
    public static final Short SHORT_ONE = 1;
    public static final Short SHORT_MINUS_ONE = -1;
    public static final Byte BYTE_ZERO = 0;
    public static final Byte BYTE_ONE = 1;
    public static final Byte BYTE_MINUS_ONE = -1;
    public static final Double DOUBLE_ZERO = 0.0;
    public static final Double DOUBLE_ONE = 1.0;
    public static final Double DOUBLE_MINUS_ONE = -1.0;
    public static final Float FLOAT_ZERO = Float.valueOf(0.0f);
    public static final Float FLOAT_ONE = Float.valueOf(1.0f);
    public static final Float FLOAT_MINUS_ONE = Float.valueOf(-1.0f);
    public static final Long LONG_INT_MAX_VALUE = Integer.MAX_VALUE;
    public static final Long LONG_INT_MIN_VALUE = Integer.MIN_VALUE;

    public static int compare(byte by, byte by2) {
        return by - by2;
    }

    public static int compare(int n, int n2) {
        if (n == n2) {
            return 0;
        }
        return n < n2 ? -1 : 1;
    }

    public static int compare(long l, long l2) {
        if (l == l2) {
            return 0;
        }
        return l < l2 ? -1 : 1;
    }

    public static int compare(short s, short s2) {
        if (s == s2) {
            return 0;
        }
        return s < s2 ? -1 : 1;
    }

    public static BigDecimal createBigDecimal(String string) {
        if (string == null) {
            return null;
        }
        if (StringUtils.isBlank(string)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        return new BigDecimal(string);
    }

    public static BigInteger createBigInteger(String string) {
        if (string == null) {
            return null;
        }
        if (string.isEmpty()) {
            throw new NumberFormatException("An empty string is not a valid number");
        }
        int n = 0;
        int n2 = 10;
        boolean bl = false;
        char c2 = string.charAt(0);
        if (c2 == '-') {
            bl = true;
            n = 1;
        } else if (c2 == '+') {
            n = 1;
        }
        if (string.startsWith("0x", n) || string.startsWith("0X", n)) {
            n2 = 16;
            n += 2;
        } else if (string.startsWith("#", n)) {
            n2 = 16;
            ++n;
        } else if (string.startsWith("0", n) && string.length() > n + 1) {
            n2 = 8;
            ++n;
        }
        BigInteger bigInteger = new BigInteger(string.substring(n), n2);
        return bl ? bigInteger.negate() : bigInteger;
    }

    public static Double createDouble(String string) {
        if (string == null) {
            return null;
        }
        return Double.valueOf(string);
    }

    public static Float createFloat(String string) {
        if (string == null) {
            return null;
        }
        return Float.valueOf(string);
    }

    public static Integer createInteger(String string) {
        if (string == null) {
            return null;
        }
        return Integer.decode(string);
    }

    public static Long createLong(String string) {
        if (string == null) {
            return null;
        }
        return Long.decode(string);
    }

    public static Number createNumber(String string) {
        String string2;
        String string3;
        boolean bl;
        String string4;
        if (string == null) {
            return null;
        }
        if (StringUtils.isBlank(string)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        String[] stringArray = new String[]{"0x", "0X", "#"};
        int n = string.length();
        int n2 = string.charAt(0) == '+' || string.charAt(0) == '-' ? 1 : 0;
        int n3 = 0;
        String[] stringArray2 = stringArray;
        int n4 = stringArray2.length;
        for (int i = 0; i < n4; ++i) {
            string4 = stringArray2[i];
            if (!string.startsWith(string4, n2)) continue;
            n3 += string4.length() + n2;
            break;
        }
        if (n3 > 0) {
            char c2 = '\u0000';
            for (n4 = n3; n4 < n && (c2 = string.charAt(n4)) == '0'; ++n4) {
                ++n3;
            }
            n4 = n - n3;
            if (n4 > 16 || n4 == 16 && c2 > '7') {
                return NumberUtils.createBigInteger(string);
            }
            if (n4 > 8 || n4 == 8 && c2 > '7') {
                return NumberUtils.createLong(string);
            }
            return NumberUtils.createInteger(string);
        }
        char c3 = string.charAt(n - 1);
        int n5 = string.indexOf(46);
        int n6 = string.indexOf(101) + string.indexOf(69) + 1;
        boolean bl2 = bl = !Character.isDigit(c3) && c3 != '.';
        if (n5 > -1) {
            if (n6 > -1) {
                if (n6 <= n5 || n6 > n) {
                    throw new NumberFormatException(string + " is not a valid number.");
                }
                string3 = string.substring(n5 + 1, n6);
            } else {
                string3 = string.substring(n5 + 1, bl ? n - 1 : n);
            }
            string2 = NumberUtils.getMantissa(string, n5);
        } else {
            if (n6 > -1) {
                if (n6 > n) {
                    throw new NumberFormatException(string + " is not a valid number.");
                }
                string2 = NumberUtils.getMantissa(string, n6);
            } else {
                string2 = NumberUtils.getMantissa(string, bl ? n - 1 : n);
            }
            string3 = null;
        }
        if (bl) {
            string4 = n6 > -1 && n6 < n - 1 ? string.substring(n6 + 1, n - 1) : null;
            String string5 = string.substring(0, n - 1);
            switch (c3) {
                case 'L': 
                case 'l': {
                    if (string3 == null && string4 == null && (!string5.isEmpty() && string5.charAt(0) == '-' && NumberUtils.isDigits(string5.substring(1)) || NumberUtils.isDigits(string5))) {
                        try {
                            return NumberUtils.createLong(string5);
                        } catch (NumberFormatException numberFormatException) {
                            return NumberUtils.createBigInteger(string5);
                        }
                    }
                    throw new NumberFormatException(string + " is not a valid number.");
                }
                case 'F': 
                case 'f': {
                    Number number;
                    try {
                        number = NumberUtils.createFloat(string);
                        if (!((Float)number).isInfinite() && (((Float)number).floatValue() != 0.0f || NumberUtils.isZero(string2, string3))) {
                            return number;
                        }
                    } catch (NumberFormatException numberFormatException) {
                        // empty catch block
                    }
                }
                case 'D': 
                case 'd': {
                    Number number;
                    try {
                        number = NumberUtils.createDouble(string);
                        if (!((Double)number).isInfinite() && ((Double)number != 0.0 || NumberUtils.isZero(string2, string3))) {
                            return number;
                        }
                    } catch (NumberFormatException numberFormatException) {
                        // empty catch block
                    }
                    try {
                        return NumberUtils.createBigDecimal(string5);
                    } catch (NumberFormatException numberFormatException) {
                        // empty catch block
                    }
                }
            }
            throw new NumberFormatException(string + " is not a valid number.");
        }
        string4 = n6 > -1 && n6 < n - 1 ? string.substring(n6 + 1) : null;
        if (string3 == null && string4 == null) {
            try {
                return NumberUtils.createInteger(string);
            } catch (NumberFormatException numberFormatException) {
                try {
                    return NumberUtils.createLong(string);
                } catch (NumberFormatException numberFormatException2) {
                    return NumberUtils.createBigInteger(string);
                }
            }
        }
        try {
            Float f = NumberUtils.createFloat(string);
            Double d = NumberUtils.createDouble(string);
            if (!f.isInfinite() && (f.floatValue() != 0.0f || NumberUtils.isZero(string2, string3)) && f.toString().equals(d.toString())) {
                return f;
            }
            if (!d.isInfinite() && (d != 0.0 || NumberUtils.isZero(string2, string3))) {
                BigDecimal bigDecimal = NumberUtils.createBigDecimal(string);
                if (bigDecimal.compareTo(BigDecimal.valueOf(d)) == 0) {
                    return d;
                }
                return bigDecimal;
            }
        } catch (NumberFormatException numberFormatException) {
            // empty catch block
        }
        return NumberUtils.createBigDecimal(string);
    }

    private static String getMantissa(String string, int n) {
        char c2 = string.charAt(0);
        boolean bl = c2 == '-' || c2 == '+';
        int n2 = string.length();
        if (n2 <= (bl ? 1 : 0) || n2 < n) {
            throw new NumberFormatException(string + " is not a valid number.");
        }
        return bl ? string.substring(1, n) : string.substring(0, n);
    }

    private static boolean isAllZeros(String string) {
        if (string == null) {
            return true;
        }
        for (int i = string.length() - 1; i >= 0; --i) {
            if (string.charAt(i) == '0') continue;
            return false;
        }
        return true;
    }

    public static boolean isCreatable(String string) {
        int n;
        int n2;
        if (StringUtils.isEmpty(string)) {
            return false;
        }
        char[] cArray = string.toCharArray();
        int n3 = cArray.length;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        int n4 = n2 = cArray[0] == '-' || cArray[0] == '+' ? 1 : 0;
        if (n3 > n2 + 1 && cArray[n2] == '0' && !StringUtils.contains((CharSequence)string, 46)) {
            if (cArray[n2 + 1] == 'x' || cArray[n2 + 1] == 'X') {
                int n5 = n2 + 2;
                if (n5 == n3) {
                    return false;
                }
                while (n5 < cArray.length) {
                    if (!(cArray[n5] >= '0' && cArray[n5] <= '9' || cArray[n5] >= 'a' && cArray[n5] <= 'f' || cArray[n5] >= 'A' && cArray[n5] <= 'F')) {
                        return false;
                    }
                    ++n5;
                }
                return true;
            }
            if (Character.isDigit(cArray[n2 + 1])) {
                for (int i = n2 + 1; i < cArray.length; ++i) {
                    if (cArray[i] >= '0' && cArray[i] <= '7') continue;
                    return false;
                }
                return true;
            }
        }
        --n3;
        for (n = n2; n < n3 || n < n3 + 1 && bl3 && !bl4; ++n) {
            if (cArray[n] >= '0' && cArray[n] <= '9') {
                bl4 = true;
                bl3 = false;
                continue;
            }
            if (cArray[n] == '.') {
                if (bl2 || bl) {
                    return false;
                }
                bl2 = true;
                continue;
            }
            if (cArray[n] == 'e' || cArray[n] == 'E') {
                if (bl) {
                    return false;
                }
                if (!bl4) {
                    return false;
                }
                bl = true;
                bl3 = true;
                continue;
            }
            if (cArray[n] == '+' || cArray[n] == '-') {
                if (!bl3) {
                    return false;
                }
                bl3 = false;
                bl4 = false;
                continue;
            }
            return false;
        }
        if (n < cArray.length) {
            if (cArray[n] >= '0' && cArray[n] <= '9') {
                return true;
            }
            if (cArray[n] == 'e' || cArray[n] == 'E') {
                return false;
            }
            if (cArray[n] == '.') {
                if (bl2 || bl) {
                    return false;
                }
                return bl4;
            }
            if (!(bl3 || cArray[n] != 'd' && cArray[n] != 'D' && cArray[n] != 'f' && cArray[n] != 'F')) {
                return bl4;
            }
            if (cArray[n] == 'l' || cArray[n] == 'L') {
                return bl4 && !bl && !bl2;
            }
            return false;
        }
        return !bl3 && bl4;
    }

    public static boolean isDigits(String string) {
        return StringUtils.isNumeric(string);
    }

    @Deprecated
    public static boolean isNumber(String string) {
        return NumberUtils.isCreatable(string);
    }

    public static boolean isParsable(String string) {
        if (StringUtils.isEmpty(string)) {
            return false;
        }
        if (string.charAt(string.length() - 1) == '.') {
            return false;
        }
        if (string.charAt(0) == '-') {
            if (string.length() == 1) {
                return false;
            }
            return NumberUtils.withDecimalsParsing(string, 1);
        }
        return NumberUtils.withDecimalsParsing(string, 0);
    }

    private static boolean isZero(String string, String string2) {
        return NumberUtils.isAllZeros(string) && NumberUtils.isAllZeros(string2);
    }

    public static byte max(byte ... byArray) {
        NumberUtils.validateArray(byArray);
        byte by = byArray[0];
        for (int i = 1; i < byArray.length; ++i) {
            if (byArray[i] <= by) continue;
            by = byArray[i];
        }
        return by;
    }

    public static byte max(byte by, byte by2, byte by3) {
        if (by2 > by) {
            by = by2;
        }
        if (by3 > by) {
            by = by3;
        }
        return by;
    }

    public static double max(double ... dArray) {
        NumberUtils.validateArray(dArray);
        double d = dArray[0];
        for (int i = 1; i < dArray.length; ++i) {
            if (Double.isNaN(dArray[i])) {
                return Double.NaN;
            }
            if (!(dArray[i] > d)) continue;
            d = dArray[i];
        }
        return d;
    }

    public static double max(double d, double d2, double d3) {
        return Math.max(Math.max(d, d2), d3);
    }

    public static float max(float ... fArray) {
        NumberUtils.validateArray(fArray);
        float f = fArray[0];
        for (int i = 1; i < fArray.length; ++i) {
            if (Float.isNaN(fArray[i])) {
                return Float.NaN;
            }
            if (!(fArray[i] > f)) continue;
            f = fArray[i];
        }
        return f;
    }

    public static float max(float f, float f2, float f3) {
        return Math.max(Math.max(f, f2), f3);
    }

    public static int max(int ... nArray) {
        NumberUtils.validateArray(nArray);
        int n = nArray[0];
        for (int i = 1; i < nArray.length; ++i) {
            if (nArray[i] <= n) continue;
            n = nArray[i];
        }
        return n;
    }

    public static int max(int n, int n2, int n3) {
        if (n2 > n) {
            n = n2;
        }
        if (n3 > n) {
            n = n3;
        }
        return n;
    }

    public static long max(long ... lArray) {
        NumberUtils.validateArray(lArray);
        long l = lArray[0];
        for (int i = 1; i < lArray.length; ++i) {
            if (lArray[i] <= l) continue;
            l = lArray[i];
        }
        return l;
    }

    public static long max(long l, long l2, long l3) {
        if (l2 > l) {
            l = l2;
        }
        if (l3 > l) {
            l = l3;
        }
        return l;
    }

    public static short max(short ... sArray) {
        NumberUtils.validateArray(sArray);
        short s = sArray[0];
        for (int i = 1; i < sArray.length; ++i) {
            if (sArray[i] <= s) continue;
            s = sArray[i];
        }
        return s;
    }

    public static short max(short s, short s2, short s3) {
        if (s2 > s) {
            s = s2;
        }
        if (s3 > s) {
            s = s3;
        }
        return s;
    }

    public static byte min(byte ... byArray) {
        NumberUtils.validateArray(byArray);
        byte by = byArray[0];
        for (int i = 1; i < byArray.length; ++i) {
            if (byArray[i] >= by) continue;
            by = byArray[i];
        }
        return by;
    }

    public static byte min(byte by, byte by2, byte by3) {
        if (by2 < by) {
            by = by2;
        }
        if (by3 < by) {
            by = by3;
        }
        return by;
    }

    public static double min(double ... dArray) {
        NumberUtils.validateArray(dArray);
        double d = dArray[0];
        for (int i = 1; i < dArray.length; ++i) {
            if (Double.isNaN(dArray[i])) {
                return Double.NaN;
            }
            if (!(dArray[i] < d)) continue;
            d = dArray[i];
        }
        return d;
    }

    public static double min(double d, double d2, double d3) {
        return Math.min(Math.min(d, d2), d3);
    }

    public static float min(float ... fArray) {
        NumberUtils.validateArray(fArray);
        float f = fArray[0];
        for (int i = 1; i < fArray.length; ++i) {
            if (Float.isNaN(fArray[i])) {
                return Float.NaN;
            }
            if (!(fArray[i] < f)) continue;
            f = fArray[i];
        }
        return f;
    }

    public static float min(float f, float f2, float f3) {
        return Math.min(Math.min(f, f2), f3);
    }

    public static int min(int ... nArray) {
        NumberUtils.validateArray(nArray);
        int n = nArray[0];
        for (int i = 1; i < nArray.length; ++i) {
            if (nArray[i] >= n) continue;
            n = nArray[i];
        }
        return n;
    }

    public static int min(int n, int n2, int n3) {
        if (n2 < n) {
            n = n2;
        }
        if (n3 < n) {
            n = n3;
        }
        return n;
    }

    public static long min(long ... lArray) {
        NumberUtils.validateArray(lArray);
        long l = lArray[0];
        for (int i = 1; i < lArray.length; ++i) {
            if (lArray[i] >= l) continue;
            l = lArray[i];
        }
        return l;
    }

    public static long min(long l, long l2, long l3) {
        if (l2 < l) {
            l = l2;
        }
        if (l3 < l) {
            l = l3;
        }
        return l;
    }

    public static short min(short ... sArray) {
        NumberUtils.validateArray(sArray);
        short s = sArray[0];
        for (int i = 1; i < sArray.length; ++i) {
            if (sArray[i] >= s) continue;
            s = sArray[i];
        }
        return s;
    }

    public static short min(short s, short s2, short s3) {
        if (s2 < s) {
            s = s2;
        }
        if (s3 < s) {
            s = s3;
        }
        return s;
    }

    public static byte toByte(String string) {
        return NumberUtils.toByte(string, (byte)0);
    }

    public static byte toByte(String string, byte by) {
        try {
            return Byte.parseByte(string);
        } catch (RuntimeException runtimeException) {
            return by;
        }
    }

    public static double toDouble(BigDecimal bigDecimal) {
        return NumberUtils.toDouble(bigDecimal, 0.0);
    }

    public static double toDouble(BigDecimal bigDecimal, double d) {
        return bigDecimal == null ? d : bigDecimal.doubleValue();
    }

    public static double toDouble(String string) {
        return NumberUtils.toDouble(string, 0.0);
    }

    public static double toDouble(String string, double d) {
        try {
            return Double.parseDouble(string);
        } catch (RuntimeException runtimeException) {
            return d;
        }
    }

    public static float toFloat(String string) {
        return NumberUtils.toFloat(string, 0.0f);
    }

    public static float toFloat(String string, float f) {
        try {
            return Float.parseFloat(string);
        } catch (RuntimeException runtimeException) {
            return f;
        }
    }

    public static int toInt(String string) {
        return NumberUtils.toInt(string, 0);
    }

    public static int toInt(String string, int n) {
        try {
            return Integer.parseInt(string);
        } catch (RuntimeException runtimeException) {
            return n;
        }
    }

    public static long toLong(String string) {
        return NumberUtils.toLong(string, 0L);
    }

    public static long toLong(String string, long l) {
        try {
            return Long.parseLong(string);
        } catch (RuntimeException runtimeException) {
            return l;
        }
    }

    public static BigDecimal toScaledBigDecimal(BigDecimal bigDecimal) {
        return NumberUtils.toScaledBigDecimal(bigDecimal, (int)INTEGER_TWO, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal toScaledBigDecimal(BigDecimal bigDecimal, int n, RoundingMode roundingMode) {
        if (bigDecimal == null) {
            return BigDecimal.ZERO;
        }
        return bigDecimal.setScale(n, roundingMode == null ? RoundingMode.HALF_EVEN : roundingMode);
    }

    public static BigDecimal toScaledBigDecimal(Double d) {
        return NumberUtils.toScaledBigDecimal(d, (int)INTEGER_TWO, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal toScaledBigDecimal(Double d, int n, RoundingMode roundingMode) {
        if (d == null) {
            return BigDecimal.ZERO;
        }
        return NumberUtils.toScaledBigDecimal(BigDecimal.valueOf(d), n, roundingMode);
    }

    public static BigDecimal toScaledBigDecimal(Float f) {
        return NumberUtils.toScaledBigDecimal(f, (int)INTEGER_TWO, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal toScaledBigDecimal(Float f, int n, RoundingMode roundingMode) {
        if (f == null) {
            return BigDecimal.ZERO;
        }
        return NumberUtils.toScaledBigDecimal(BigDecimal.valueOf(f.floatValue()), n, roundingMode);
    }

    public static BigDecimal toScaledBigDecimal(String string) {
        return NumberUtils.toScaledBigDecimal(string, (int)INTEGER_TWO, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal toScaledBigDecimal(String string, int n, RoundingMode roundingMode) {
        if (string == null) {
            return BigDecimal.ZERO;
        }
        return NumberUtils.toScaledBigDecimal(NumberUtils.createBigDecimal(string), n, roundingMode);
    }

    public static short toShort(String string) {
        return NumberUtils.toShort(string, (short)0);
    }

    public static short toShort(String string, short s) {
        try {
            return Short.parseShort(string);
        } catch (RuntimeException runtimeException) {
            return s;
        }
    }

    private static void validateArray(Object object) {
        Objects.requireNonNull(object, "array");
        Validate.isTrue(Array.getLength(object) != 0, "Array cannot be empty.", new Object[0]);
    }

    private static boolean withDecimalsParsing(String string, int n) {
        int n2 = 0;
        for (int i = n; i < string.length(); ++i) {
            boolean bl;
            char c2 = string.charAt(i);
            boolean bl2 = bl = c2 == '.';
            if (bl) {
                ++n2;
            }
            if (n2 > 1) {
                return false;
            }
            if (bl || Character.isDigit(c2)) continue;
            return false;
        }
        return true;
    }

    @Deprecated
    public NumberUtils() {
    }
}

