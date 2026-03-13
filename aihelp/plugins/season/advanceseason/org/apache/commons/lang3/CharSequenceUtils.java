/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package org.apache.commons.lang3;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class CharSequenceUtils {
    private static final int NOT_FOUND = -1;
    static final int TO_STRING_LIMIT = 16;

    private static boolean checkLaterThan1(CharSequence charSequence, CharSequence charSequence2, int n, int n2) {
        int n3 = 1;
        for (int i = n - 1; n3 <= i; ++n3, --i) {
            if (charSequence.charAt(n2 + n3) == charSequence2.charAt(n3) && charSequence.charAt(n2 + i) == charSequence2.charAt(i)) continue;
            return false;
        }
        return true;
    }

    static int indexOf(CharSequence charSequence, CharSequence charSequence2, int n) {
        if (charSequence instanceof String) {
            return ((String)charSequence).indexOf(charSequence2.toString(), n);
        }
        if (charSequence instanceof StringBuilder) {
            return ((StringBuilder)charSequence).indexOf(charSequence2.toString(), n);
        }
        if (charSequence instanceof StringBuffer) {
            return ((StringBuffer)charSequence).indexOf(charSequence2.toString(), n);
        }
        return charSequence.toString().indexOf(charSequence2.toString(), n);
    }

    static int indexOf(CharSequence charSequence, int n, int n2) {
        if (charSequence instanceof String) {
            return ((String)charSequence).indexOf(n, n2);
        }
        int n3 = charSequence.length();
        if (n2 < 0) {
            n2 = 0;
        }
        if (n < 65536) {
            for (int i = n2; i < n3; ++i) {
                if (charSequence.charAt(i) != n) continue;
                return i;
            }
            return -1;
        }
        if (n <= 0x10FFFF) {
            char[] cArray = Character.toChars(n);
            for (int i = n2; i < n3 - 1; ++i) {
                char c2 = charSequence.charAt(i);
                char c3 = charSequence.charAt(i + 1);
                if (c2 != cArray[0] || c3 != cArray[1]) continue;
                return i;
            }
        }
        return -1;
    }

    /*
     * Unable to fully structure code
     */
    static int lastIndexOf(CharSequence var0, CharSequence var1_1, int var2_2) {
        if (var1_1 == null || var0 == null) {
            return -1;
        }
        if (var1_1 instanceof String) {
            if (var0 instanceof String) {
                return ((String)var0).lastIndexOf((String)var1_1, var2_2);
            }
            if (var0 instanceof StringBuilder) {
                return ((StringBuilder)var0).lastIndexOf((String)var1_1, var2_2);
            }
            if (var0 instanceof StringBuffer) {
                return ((StringBuffer)var0).lastIndexOf((String)var1_1, var2_2);
            }
        }
        var3_3 = var0.length();
        var4_4 = var1_1.length();
        if (var2_2 > var3_3) {
            var2_2 = var3_3;
        }
        if (var2_2 < 0 || var4_4 > var3_3) {
            return -1;
        }
        if (var4_4 == 0) {
            return var2_2;
        }
        if (var4_4 <= 16) {
            if (var0 instanceof String) {
                return ((String)var0).lastIndexOf(var1_1.toString(), var2_2);
            }
            if (var0 instanceof StringBuilder) {
                return ((StringBuilder)var0).lastIndexOf(var1_1.toString(), var2_2);
            }
            if (var0 instanceof StringBuffer) {
                return ((StringBuffer)var0).lastIndexOf(var1_1.toString(), var2_2);
            }
        }
        if (var2_2 + var4_4 > var3_3) {
            var2_2 = var3_3 - var4_4;
        }
        var5_5 = var1_1.charAt(0);
        var6_6 = var2_2;
        do lbl-1000:
        // 3 sources

        {
            block14: {
                if (var0.charAt(var6_6) == var5_5) break block14;
                if (--var6_6 >= 0) ** GOTO lbl-1000
                return -1;
            }
            if (!CharSequenceUtils.checkLaterThan1(var0, var1_1, var4_4, var6_6)) continue;
            return var6_6;
        } while (--var6_6 >= 0);
        return -1;
    }

    static int lastIndexOf(CharSequence charSequence, int n, int n2) {
        if (charSequence instanceof String) {
            return ((String)charSequence).lastIndexOf(n, n2);
        }
        int n3 = charSequence.length();
        if (n2 < 0) {
            return -1;
        }
        if (n2 >= n3) {
            n2 = n3 - 1;
        }
        if (n < 65536) {
            for (int i = n2; i >= 0; --i) {
                if (charSequence.charAt(i) != n) continue;
                return i;
            }
            return -1;
        }
        if (n <= 0x10FFFF) {
            char[] cArray = Character.toChars(n);
            if (n2 == n3 - 1) {
                return -1;
            }
            for (int i = n2; i >= 0; --i) {
                char c2 = charSequence.charAt(i);
                char c3 = charSequence.charAt(i + 1);
                if (cArray[0] != c2 || cArray[1] != c3) continue;
                return i;
            }
        }
        return -1;
    }

    static boolean regionMatches(CharSequence charSequence, boolean bl, int n, CharSequence charSequence2, int n2, int n3) {
        if (charSequence instanceof String && charSequence2 instanceof String) {
            return ((String)charSequence).regionMatches(bl, n, (String)charSequence2, n2, n3);
        }
        int n4 = n;
        int n5 = n2;
        int n6 = n3;
        int n7 = charSequence.length() - n;
        int n8 = charSequence2.length() - n2;
        if (n < 0 || n2 < 0 || n3 < 0) {
            return false;
        }
        if (n7 < n3 || n8 < n3) {
            return false;
        }
        while (n6-- > 0) {
            char c2;
            char c3;
            char c4;
            if ((c4 = charSequence.charAt(n4++)) == (c3 = charSequence2.charAt(n5++))) continue;
            if (!bl) {
                return false;
            }
            char c5 = Character.toUpperCase(c4);
            if (c5 == (c2 = Character.toUpperCase(c3)) || Character.toLowerCase(c5) == Character.toLowerCase(c2)) continue;
            return false;
        }
        return true;
    }

    public static CharSequence subSequence(CharSequence charSequence, int n) {
        return charSequence == null ? null : charSequence.subSequence(n, charSequence.length());
    }

    public static char[] toCharArray(CharSequence charSequence) {
        int n = StringUtils.length(charSequence);
        if (n == 0) {
            return ArrayUtils.EMPTY_CHAR_ARRAY;
        }
        if (charSequence instanceof String) {
            return ((String)charSequence).toCharArray();
        }
        char[] cArray = new char[n];
        for (int i = 0; i < n; ++i) {
            cArray[i] = charSequence.charAt(i);
        }
        return cArray;
    }

    @Deprecated
    public CharSequenceUtils() {
    }
}

