package fr.xephi.authme.libs.org.postgresql.core;

import java.text.NumberFormat;
import java.text.ParsePosition;
import org.checkerframework.checker.nullness.qual.Nullable;

public enum ServerVersion implements Version {
   INVALID("0.0.0"),
   v8_2("8.2.0"),
   v8_3("8.3.0"),
   v8_4("8.4.0"),
   v9_0("9.0.0"),
   v9_1("9.1.0"),
   v9_2("9.2.0"),
   v9_3("9.3.0"),
   v9_4("9.4.0"),
   v9_5("9.5.0"),
   v9_6("9.6.0"),
   v10("10"),
   v11("11"),
   v12("12"),
   v13("13"),
   v14("14"),
   v15("15"),
   v16("16");

   private final int version;

   private ServerVersion(String version) {
      this.version = parseServerVersionStr(version);
   }

   public int getVersionNum() {
      return this.version;
   }

   public static Version from(@Nullable String version) {
      final int versionNum = parseServerVersionStr(version);
      return new Version() {
         public int getVersionNum() {
            return versionNum;
         }

         public boolean equals(@Nullable Object obj) {
            if (obj instanceof Version) {
               return this.getVersionNum() == ((Version)obj).getVersionNum();
            } else {
               return false;
            }
         }

         public int hashCode() {
            return this.getVersionNum();
         }

         public String toString() {
            return Integer.toString(versionNum);
         }
      };
   }

   static int parseServerVersionStr(@Nullable String serverVersion) throws NumberFormatException {
      if (serverVersion == null) {
         return 0;
      } else {
         NumberFormat numformat = NumberFormat.getIntegerInstance();
         numformat.setGroupingUsed(false);
         ParsePosition parsepos = new ParsePosition(0);
         int[] parts = new int[3];

         int versionParts;
         for(versionParts = 0; versionParts < 3; ++versionParts) {
            Number part = (Number)numformat.parseObject(serverVersion, parsepos);
            if (part == null) {
               break;
            }

            parts[versionParts] = part.intValue();
            if (parsepos.getIndex() == serverVersion.length() || serverVersion.charAt(parsepos.getIndex()) != '.') {
               break;
            }

            parsepos.setIndex(parsepos.getIndex() + 1);
         }

         ++versionParts;
         if (parts[0] >= 10000) {
            if (parsepos.getIndex() == serverVersion.length() && versionParts == 1) {
               return parts[0];
            } else {
               throw new NumberFormatException("First major-version part equal to or greater than 10000 in invalid version string: " + serverVersion);
            }
         } else if (versionParts >= 3) {
            if (parts[1] > 99) {
               throw new NumberFormatException("Unsupported second part of major version > 99 in invalid version string: " + serverVersion);
            } else if (parts[2] > 99) {
               throw new NumberFormatException("Unsupported second part of minor version > 99 in invalid version string: " + serverVersion);
            } else {
               return (parts[0] * 100 + parts[1]) * 100 + parts[2];
            }
         } else if (versionParts == 2) {
            if (parts[0] >= 10) {
               return parts[0] * 100 * 100 + parts[1];
            } else if (parts[1] > 99) {
               throw new NumberFormatException("Unsupported second part of major version > 99 in invalid version string: " + serverVersion);
            } else {
               return (parts[0] * 100 + parts[1]) * 100;
            }
         } else {
            return versionParts == 1 && parts[0] >= 10 ? parts[0] * 100 * 100 : 0;
         }
      }
   }

   // $FF: synthetic method
   private static ServerVersion[] $values() {
      return new ServerVersion[]{INVALID, v8_2, v8_3, v8_4, v9_0, v9_1, v9_2, v9_3, v9_4, v9_5, v9_6, v10, v11, v12, v13, v14, v15, v16};
   }
}
