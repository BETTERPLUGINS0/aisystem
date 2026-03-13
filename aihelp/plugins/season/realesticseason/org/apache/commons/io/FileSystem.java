package org.apache.commons.io;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public enum FileSystem {
   GENERIC(false, false, Integer.MAX_VALUE, Integer.MAX_VALUE, new char[]{'\u0000'}, new String[0], false),
   LINUX(true, true, 255, 4096, new char[]{'\u0000', '/'}, new String[0], false),
   MAC_OSX(true, true, 255, 1024, new char[]{'\u0000', '/', ':'}, new String[0], false),
   WINDOWS(false, true, 255, 32000, new char[]{'\u0000', '\u0001', '\u0002', '\u0003', '\u0004', '\u0005', '\u0006', '\u0007', '\b', '\t', '\n', '\u000b', '\f', '\r', '\u000e', '\u000f', '\u0010', '\u0011', '\u0012', '\u0013', '\u0014', '\u0015', '\u0016', '\u0017', '\u0018', '\u0019', '\u001a', '\u001b', '\u001c', '\u001d', '\u001e', '\u001f', '"', '*', '/', ':', '<', '>', '?', '\\', '|'}, new String[]{"AUX", "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9", "CON", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9", "NUL", "PRN"}, true);

   private static final boolean IS_OS_LINUX = getOsMatchesName("Linux");
   private static final boolean IS_OS_MAC = getOsMatchesName("Mac");
   private static final String OS_NAME_WINDOWS_PREFIX = "Windows";
   private static final boolean IS_OS_WINDOWS = getOsMatchesName("Windows");
   private final boolean casePreserving;
   private final boolean caseSensitive;
   private final char[] illegalFileNameChars;
   private final int maxFileNameLength;
   private final int maxPathLength;
   private final String[] reservedFileNames;
   private final boolean supportsDriveLetter;

   public static FileSystem getCurrent() {
      if (IS_OS_LINUX) {
         return LINUX;
      } else if (IS_OS_MAC) {
         return MAC_OSX;
      } else {
         return IS_OS_WINDOWS ? WINDOWS : GENERIC;
      }
   }

   private static boolean getOsMatchesName(String var0) {
      return isOsNameMatch(getSystemProperty("os.name"), var0);
   }

   private static String getSystemProperty(String var0) {
      try {
         return System.getProperty(var0);
      } catch (SecurityException var2) {
         System.err.println("Caught a SecurityException reading the system property '" + var0 + "'; the SystemUtils property value will default to null.");
         return null;
      }
   }

   private static boolean isOsNameMatch(String var0, String var1) {
      return var0 == null ? false : var0.toUpperCase(Locale.ROOT).startsWith(var1.toUpperCase(Locale.ROOT));
   }

   private FileSystem(final boolean param3, final boolean param4, final int param5, final int param6, final char[] param7, final String[] param8, final boolean param9) {
      this.maxFileNameLength = var5;
      this.maxPathLength = var6;
      this.illegalFileNameChars = (char[])Objects.requireNonNull(var7, "illegalFileNameChars");
      this.reservedFileNames = (String[])Objects.requireNonNull(var8, "reservedFileNames");
      this.caseSensitive = var3;
      this.casePreserving = var4;
      this.supportsDriveLetter = var9;
   }

   public char[] getIllegalFileNameChars() {
      return (char[])this.illegalFileNameChars.clone();
   }

   public int getMaxFileNameLength() {
      return this.maxFileNameLength;
   }

   public int getMaxPathLength() {
      return this.maxPathLength;
   }

   public String[] getReservedFileNames() {
      return (String[])this.reservedFileNames.clone();
   }

   public boolean isCasePreserving() {
      return this.casePreserving;
   }

   public boolean isCaseSensitive() {
      return this.caseSensitive;
   }

   private boolean isIllegalFileNameChar(char var1) {
      return Arrays.binarySearch(this.illegalFileNameChars, var1) >= 0;
   }

   public boolean isLegalFileName(CharSequence var1) {
      if (var1 != null && var1.length() != 0 && var1.length() <= this.maxFileNameLength) {
         if (this.isReservedFileName(var1)) {
            return false;
         } else {
            for(int var2 = 0; var2 < var1.length(); ++var2) {
               if (this.isIllegalFileNameChar(var1.charAt(var2))) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public boolean isReservedFileName(CharSequence var1) {
      return Arrays.binarySearch(this.reservedFileNames, var1) >= 0;
   }

   public boolean supportsDriveLetter() {
      return this.supportsDriveLetter;
   }

   public String toLegalFileName(String var1, char var2) {
      if (this.isIllegalFileNameChar(var2)) {
         throw new IllegalArgumentException(String.format("The replacement character '%s' cannot be one of the %s illegal characters: %s", var2 == 0 ? "\\0" : var2, this.name(), Arrays.toString(this.illegalFileNameChars)));
      } else {
         String var3 = var1.length() > this.maxFileNameLength ? var1.substring(0, this.maxFileNameLength) : var1;
         boolean var4 = false;
         char[] var5 = var3.toCharArray();

         for(int var6 = 0; var6 < var5.length; ++var6) {
            if (this.isIllegalFileNameChar(var5[var6])) {
               var5[var6] = var2;
               var4 = true;
            }
         }

         return var4 ? String.valueOf(var5) : var3;
      }
   }

   // $FF: synthetic method
   private static FileSystem[] $values() {
      return new FileSystem[]{GENERIC, LINUX, MAC_OSX, WINDOWS};
   }
}
