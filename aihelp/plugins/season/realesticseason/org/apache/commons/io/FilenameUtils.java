package org.apache.commons.io;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilenameUtils {
   private static final String[] EMPTY_STRING_ARRAY = new String[0];
   private static final String EMPTY_STRING = "";
   private static final int NOT_FOUND = -1;
   public static final char EXTENSION_SEPARATOR = '.';
   public static final String EXTENSION_SEPARATOR_STR = Character.toString('.');
   private static final char UNIX_SEPARATOR = '/';
   private static final char WINDOWS_SEPARATOR = '\\';
   private static final char SYSTEM_SEPARATOR;
   private static final char OTHER_SEPARATOR;
   private static final Pattern IPV4_PATTERN;
   private static final int IPV4_MAX_OCTET_VALUE = 255;
   private static final int IPV6_MAX_HEX_GROUPS = 8;
   private static final int IPV6_MAX_HEX_DIGITS_PER_GROUP = 4;
   private static final int MAX_UNSIGNED_SHORT = 65535;
   private static final int BASE_16 = 16;
   private static final Pattern REG_NAME_PART_PATTERN;

   static boolean isSystemWindows() {
      return SYSTEM_SEPARATOR == '\\';
   }

   private static boolean isSeparator(char var0) {
      return var0 == '/' || var0 == '\\';
   }

   public static String normalize(String var0) {
      return doNormalize(var0, SYSTEM_SEPARATOR, true);
   }

   public static String normalize(String var0, boolean var1) {
      int var2 = var1 ? 47 : 92;
      return doNormalize(var0, (char)var2, true);
   }

   public static String normalizeNoEndSeparator(String var0) {
      return doNormalize(var0, SYSTEM_SEPARATOR, false);
   }

   public static String normalizeNoEndSeparator(String var0, boolean var1) {
      int var2 = var1 ? 47 : 92;
      return doNormalize(var0, (char)var2, false);
   }

   private static String doNormalize(String var0, char var1, boolean var2) {
      if (var0 == null) {
         return null;
      } else {
         requireNonNullChars(var0);
         int var3 = var0.length();
         if (var3 == 0) {
            return var0;
         } else {
            int var4 = getPrefixLength(var0);
            if (var4 < 0) {
               return null;
            } else {
               char[] var5 = new char[var3 + 2];
               var0.getChars(0, var0.length(), var5, 0);
               char var6 = var1 == SYSTEM_SEPARATOR ? OTHER_SEPARATOR : SYSTEM_SEPARATOR;

               for(int var7 = 0; var7 < var5.length; ++var7) {
                  if (var5[var7] == var6) {
                     var5[var7] = var1;
                  }
               }

               boolean var10 = true;
               if (var5[var3 - 1] != var1) {
                  var5[var3++] = var1;
                  var10 = false;
               }

               int var8;
               for(var8 = var4 != 0 ? var4 : 1; var8 < var3; ++var8) {
                  if (var5[var8] == var1 && var5[var8 - 1] == var1) {
                     System.arraycopy(var5, var8, var5, var8 - 1, var3 - var8);
                     --var3;
                     --var8;
                  }
               }

               for(var8 = var4 + 1; var8 < var3; ++var8) {
                  if (var5[var8] == var1 && var5[var8 - 1] == '.' && (var8 == var4 + 1 || var5[var8 - 2] == var1)) {
                     if (var8 == var3 - 1) {
                        var10 = true;
                     }

                     System.arraycopy(var5, var8 + 1, var5, var8 - 1, var3 - var8);
                     var3 -= 2;
                     --var8;
                  }
               }

               label114:
               for(var8 = var4 + 2; var8 < var3; ++var8) {
                  if (var5[var8] == var1 && var5[var8 - 1] == '.' && var5[var8 - 2] == '.' && (var8 == var4 + 2 || var5[var8 - 3] == var1)) {
                     if (var8 == var4 + 2) {
                        return null;
                     }

                     if (var8 == var3 - 1) {
                        var10 = true;
                     }

                     for(int var9 = var8 - 4; var9 >= var4; --var9) {
                        if (var5[var9] == var1) {
                           System.arraycopy(var5, var8 + 1, var5, var9 + 1, var3 - var8);
                           var3 -= var8 - var9;
                           var8 = var9 + 1;
                           continue label114;
                        }
                     }

                     System.arraycopy(var5, var8 + 1, var5, var4, var3 - var8);
                     var3 -= var8 + 1 - var4;
                     var8 = var4 + 1;
                  }
               }

               if (var3 <= 0) {
                  return "";
               } else if (var3 <= var4) {
                  return new String(var5, 0, var3);
               } else if (var10 && var2) {
                  return new String(var5, 0, var3);
               } else {
                  return new String(var5, 0, var3 - 1);
               }
            }
         }
      }
   }

   public static String concat(String var0, String var1) {
      int var2 = getPrefixLength(var1);
      if (var2 < 0) {
         return null;
      } else if (var2 > 0) {
         return normalize(var1);
      } else if (var0 == null) {
         return null;
      } else {
         int var3 = var0.length();
         if (var3 == 0) {
            return normalize(var1);
         } else {
            char var4 = var0.charAt(var3 - 1);
            return isSeparator(var4) ? normalize(var0 + var1) : normalize(var0 + '/' + var1);
         }
      }
   }

   public static boolean directoryContains(String var0, String var1) {
      Objects.requireNonNull(var0, "canonicalParent");
      if (var1 == null) {
         return false;
      } else {
         return IOCase.SYSTEM.checkEquals(var0, var1) ? false : IOCase.SYSTEM.checkStartsWith(var1, var0);
      }
   }

   public static String separatorsToUnix(String var0) {
      return var0 != null && var0.indexOf(92) != -1 ? var0.replace('\\', '/') : var0;
   }

   public static String separatorsToWindows(String var0) {
      return var0 != null && var0.indexOf(47) != -1 ? var0.replace('/', '\\') : var0;
   }

   public static String separatorsToSystem(String var0) {
      if (var0 == null) {
         return null;
      } else {
         return isSystemWindows() ? separatorsToWindows(var0) : separatorsToUnix(var0);
      }
   }

   public static int getPrefixLength(String var0) {
      if (var0 == null) {
         return -1;
      } else {
         int var1 = var0.length();
         if (var1 == 0) {
            return 0;
         } else {
            char var2 = var0.charAt(0);
            if (var2 == ':') {
               return -1;
            } else if (var1 == 1) {
               if (var2 == '~') {
                  return 2;
               } else {
                  return isSeparator(var2) ? 1 : 0;
               }
            } else {
               int var4;
               if (var2 == '~') {
                  int var8 = var0.indexOf(47, 1);
                  var4 = var0.indexOf(92, 1);
                  if (var8 == -1 && var4 == -1) {
                     return var1 + 1;
                  } else {
                     var8 = var8 == -1 ? var4 : var8;
                     var4 = var4 == -1 ? var8 : var4;
                     return Math.min(var8, var4) + 1;
                  }
               } else {
                  char var3 = var0.charAt(1);
                  if (var3 == ':') {
                     var2 = Character.toUpperCase(var2);
                     if (var2 >= 'A' && var2 <= 'Z') {
                        if (var1 == 2 && !FileSystem.getCurrent().supportsDriveLetter()) {
                           return 0;
                        } else {
                           return var1 != 2 && isSeparator(var0.charAt(2)) ? 3 : 2;
                        }
                     } else {
                        return var2 == '/' ? 1 : -1;
                     }
                  } else if (isSeparator(var2) && isSeparator(var3)) {
                     var4 = var0.indexOf(47, 2);
                     int var5 = var0.indexOf(92, 2);
                     if ((var4 != -1 || var5 != -1) && var4 != 2 && var5 != 2) {
                        var4 = var4 == -1 ? var5 : var4;
                        var5 = var5 == -1 ? var4 : var5;
                        int var6 = Math.min(var4, var5) + 1;
                        String var7 = var0.substring(2, var6 - 1);
                        return isValidHostName(var7) ? var6 : -1;
                     } else {
                        return -1;
                     }
                  } else {
                     return isSeparator(var2) ? 1 : 0;
                  }
               }
            }
         }
      }
   }

   public static int indexOfLastSeparator(String var0) {
      if (var0 == null) {
         return -1;
      } else {
         int var1 = var0.lastIndexOf(47);
         int var2 = var0.lastIndexOf(92);
         return Math.max(var1, var2);
      }
   }

   public static int indexOfExtension(String var0) {
      if (var0 == null) {
         return -1;
      } else {
         int var1;
         if (isSystemWindows()) {
            var1 = var0.indexOf(58, getAdsCriticalOffset(var0));
            if (var1 != -1) {
               throw new IllegalArgumentException("NTFS ADS separator (':') in file name is forbidden.");
            }
         }

         var1 = var0.lastIndexOf(46);
         int var2 = indexOfLastSeparator(var0);
         return var2 > var1 ? -1 : var1;
      }
   }

   public static String getPrefix(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = getPrefixLength(var0);
         if (var1 < 0) {
            return null;
         } else if (var1 > var0.length()) {
            requireNonNullChars(var0 + '/');
            return var0 + '/';
         } else {
            String var2 = var0.substring(0, var1);
            requireNonNullChars(var2);
            return var2;
         }
      }
   }

   public static String getPath(String var0) {
      return doGetPath(var0, 1);
   }

   public static String getPathNoEndSeparator(String var0) {
      return doGetPath(var0, 0);
   }

   private static String doGetPath(String var0, int var1) {
      if (var0 == null) {
         return null;
      } else {
         int var2 = getPrefixLength(var0);
         if (var2 < 0) {
            return null;
         } else {
            int var3 = indexOfLastSeparator(var0);
            int var4 = var3 + var1;
            if (var2 < var0.length() && var3 >= 0 && var2 < var4) {
               String var5 = var0.substring(var2, var4);
               requireNonNullChars(var5);
               return var5;
            } else {
               return "";
            }
         }
      }
   }

   public static String getFullPath(String var0) {
      return doGetFullPath(var0, true);
   }

   public static String getFullPathNoEndSeparator(String var0) {
      return doGetFullPath(var0, false);
   }

   private static String doGetFullPath(String var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else {
         int var2 = getPrefixLength(var0);
         if (var2 < 0) {
            return null;
         } else if (var2 >= var0.length()) {
            return var1 ? getPrefix(var0) : var0;
         } else {
            int var3 = indexOfLastSeparator(var0);
            if (var3 < 0) {
               return var0.substring(0, var2);
            } else {
               int var4 = var3 + (var1 ? 1 : 0);
               if (var4 == 0) {
                  ++var4;
               }

               return var0.substring(0, var4);
            }
         }
      }
   }

   public static String getName(String var0) {
      if (var0 == null) {
         return null;
      } else {
         requireNonNullChars(var0);
         int var1 = indexOfLastSeparator(var0);
         return var0.substring(var1 + 1);
      }
   }

   private static void requireNonNullChars(String var0) {
      if (var0.indexOf(0) >= 0) {
         throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it");
      }
   }

   public static String getBaseName(String var0) {
      return removeExtension(getName(var0));
   }

   public static String getExtension(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = indexOfExtension(var0);
         return var1 == -1 ? "" : var0.substring(var1 + 1);
      }
   }

   private static int getAdsCriticalOffset(String var0) {
      int var1 = var0.lastIndexOf(SYSTEM_SEPARATOR);
      int var2 = var0.lastIndexOf(OTHER_SEPARATOR);
      if (var1 == -1) {
         return var2 == -1 ? 0 : var2 + 1;
      } else {
         return var2 == -1 ? var1 + 1 : Math.max(var1, var2) + 1;
      }
   }

   public static String removeExtension(String var0) {
      if (var0 == null) {
         return null;
      } else {
         requireNonNullChars(var0);
         int var1 = indexOfExtension(var0);
         return var1 == -1 ? var0 : var0.substring(0, var1);
      }
   }

   public static boolean equals(String var0, String var1) {
      return equals(var0, var1, false, IOCase.SENSITIVE);
   }

   public static boolean equalsOnSystem(String var0, String var1) {
      return equals(var0, var1, false, IOCase.SYSTEM);
   }

   public static boolean equalsNormalized(String var0, String var1) {
      return equals(var0, var1, true, IOCase.SENSITIVE);
   }

   public static boolean equalsNormalizedOnSystem(String var0, String var1) {
      return equals(var0, var1, true, IOCase.SYSTEM);
   }

   public static boolean equals(String var0, String var1, boolean var2, IOCase var3) {
      if (var0 != null && var1 != null) {
         if (var2) {
            var0 = normalize(var0);
            if (var0 == null) {
               return false;
            }

            var1 = normalize(var1);
            if (var1 == null) {
               return false;
            }
         }

         if (var3 == null) {
            var3 = IOCase.SENSITIVE;
         }

         return var3.checkEquals(var0, var1);
      } else {
         return var0 == null && var1 == null;
      }
   }

   public static boolean isExtension(String var0, String var1) {
      if (var0 == null) {
         return false;
      } else {
         requireNonNullChars(var0);
         if (var1 != null && !var1.isEmpty()) {
            String var2 = getExtension(var0);
            return var2.equals(var1);
         } else {
            return indexOfExtension(var0) == -1;
         }
      }
   }

   public static boolean isExtension(String var0, String... var1) {
      if (var0 == null) {
         return false;
      } else {
         requireNonNullChars(var0);
         if (var1 != null && var1.length != 0) {
            String var2 = getExtension(var0);
            String[] var3 = var1;
            int var4 = var1.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               String var6 = var3[var5];
               if (var2.equals(var6)) {
                  return true;
               }
            }

            return false;
         } else {
            return indexOfExtension(var0) == -1;
         }
      }
   }

   public static boolean isExtension(String var0, Collection<String> var1) {
      if (var0 == null) {
         return false;
      } else {
         requireNonNullChars(var0);
         if (var1 != null && !var1.isEmpty()) {
            String var2 = getExtension(var0);
            Iterator var3 = var1.iterator();

            String var4;
            do {
               if (!var3.hasNext()) {
                  return false;
               }

               var4 = (String)var3.next();
            } while(!var2.equals(var4));

            return true;
         } else {
            return indexOfExtension(var0) == -1;
         }
      }
   }

   public static boolean wildcardMatch(String var0, String var1) {
      return wildcardMatch(var0, var1, IOCase.SENSITIVE);
   }

   public static boolean wildcardMatchOnSystem(String var0, String var1) {
      return wildcardMatch(var0, var1, IOCase.SYSTEM);
   }

   public static boolean wildcardMatch(String var0, String var1, IOCase var2) {
      if (var0 == null && var1 == null) {
         return true;
      } else if (var0 != null && var1 != null) {
         if (var2 == null) {
            var2 = IOCase.SENSITIVE;
         }

         String[] var3 = splitOnTokens(var1);
         boolean var4 = false;
         int var5 = 0;
         int var6 = 0;
         ArrayDeque var7 = new ArrayDeque(var3.length);

         do {
            if (!var7.isEmpty()) {
               int[] var8 = (int[])var7.pop();
               var6 = var8[0];
               var5 = var8[1];
               var4 = true;
            }

            for(; var6 < var3.length; ++var6) {
               if (var3[var6].equals("?")) {
                  ++var5;
                  if (var5 > var0.length()) {
                     break;
                  }

                  var4 = false;
               } else if (var3[var6].equals("*")) {
                  var4 = true;
                  if (var6 == var3.length - 1) {
                     var5 = var0.length();
                  }
               } else {
                  if (var4) {
                     var5 = var2.checkIndexOf(var0, var5, var3[var6]);
                     if (var5 == -1) {
                        break;
                     }

                     int var9 = var2.checkIndexOf(var0, var5 + 1, var3[var6]);
                     if (var9 >= 0) {
                        var7.push(new int[]{var6, var9});
                     }
                  } else if (!var2.checkRegionMatches(var0, var5, var3[var6])) {
                     break;
                  }

                  var5 += var3[var6].length();
                  var4 = false;
               }
            }

            if (var6 == var3.length && var5 == var0.length()) {
               return true;
            }
         } while(!var7.isEmpty());

         return false;
      } else {
         return false;
      }
   }

   static String[] splitOnTokens(String var0) {
      if (var0.indexOf(63) == -1 && var0.indexOf(42) == -1) {
         return new String[]{var0};
      } else {
         char[] var1 = var0.toCharArray();
         ArrayList var2 = new ArrayList();
         StringBuilder var3 = new StringBuilder();
         char var4 = 0;
         char[] var5 = var1;
         int var6 = var1.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            char var8 = var5[var7];
            if (var8 != '?' && var8 != '*') {
               var3.append(var8);
            } else {
               if (var3.length() != 0) {
                  var2.add(var3.toString());
                  var3.setLength(0);
               }

               if (var8 == '?') {
                  var2.add("?");
               } else if (var4 != '*') {
                  var2.add("*");
               }
            }

            var4 = var8;
         }

         if (var3.length() != 0) {
            var2.add(var3.toString());
         }

         return (String[])var2.toArray(EMPTY_STRING_ARRAY);
      }
   }

   private static boolean isValidHostName(String var0) {
      return isIPv6Address(var0) || isRFC3986HostName(var0);
   }

   private static boolean isIPv4Address(String var0) {
      Matcher var1 = IPV4_PATTERN.matcher(var0);
      if (var1.matches() && var1.groupCount() == 4) {
         for(int var2 = 1; var2 <= 4; ++var2) {
            String var3 = var1.group(var2);
            int var4 = Integer.parseInt(var3);
            if (var4 > 255) {
               return false;
            }

            if (var3.length() > 1 && var3.startsWith("0")) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean isIPv6Address(String var0) {
      boolean var1 = var0.contains("::");
      if (var1 && var0.indexOf("::") != var0.lastIndexOf("::")) {
         return false;
      } else if ((!var0.startsWith(":") || var0.startsWith("::")) && (!var0.endsWith(":") || var0.endsWith("::"))) {
         String[] var2 = var0.split(":");
         if (var1) {
            ArrayList var3 = new ArrayList(Arrays.asList(var2));
            if (var0.endsWith("::")) {
               var3.add("");
            } else if (var0.startsWith("::") && !var3.isEmpty()) {
               var3.remove(0);
            }

            var2 = (String[])var3.toArray(EMPTY_STRING_ARRAY);
         }

         if (var2.length > 8) {
            return false;
         } else {
            int var10 = 0;
            int var4 = 0;

            for(int var5 = 0; var5 < var2.length; ++var5) {
               String var6 = var2[var5];
               if (var6.isEmpty()) {
                  ++var4;
                  if (var4 > 1) {
                     return false;
                  }
               } else {
                  var4 = 0;
                  if (var5 == var2.length - 1 && var6.contains(".")) {
                     if (!isIPv4Address(var6)) {
                        return false;
                     }

                     var10 += 2;
                     continue;
                  }

                  if (var6.length() > 4) {
                     return false;
                  }

                  int var7;
                  try {
                     var7 = Integer.parseInt(var6, 16);
                  } catch (NumberFormatException var9) {
                     return false;
                  }

                  if (var7 < 0 || var7 > 65535) {
                     return false;
                  }
               }

               ++var10;
            }

            return var10 <= 8 && (var10 >= 8 || var1);
         }
      } else {
         return false;
      }
   }

   private static boolean isRFC3986HostName(String var0) {
      String[] var1 = var0.split("\\.", -1);

      for(int var2 = 0; var2 < var1.length; ++var2) {
         if (var1[var2].isEmpty()) {
            return var2 == var1.length - 1;
         }

         if (!REG_NAME_PART_PATTERN.matcher(var1[var2]).matches()) {
            return false;
         }
      }

      return true;
   }

   static {
      SYSTEM_SEPARATOR = File.separatorChar;
      if (isSystemWindows()) {
         OTHER_SEPARATOR = '/';
      } else {
         OTHER_SEPARATOR = '\\';
      }

      IPV4_PATTERN = Pattern.compile("^(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})$");
      REG_NAME_PART_PATTERN = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9-]*$");
   }
}
