package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.io.IOException;
import java.sql.SQLException;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Utils {
   public static String toHexString(byte[] data) {
      StringBuilder sb = new StringBuilder(data.length * 2);
      byte[] var2 = data;
      int var3 = data.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte element = var2[var4];
         sb.append(Integer.toHexString(element >> 4 & 15));
         sb.append(Integer.toHexString(element & 15));
      }

      return sb.toString();
   }

   public static StringBuilder escapeLiteral(@Nullable StringBuilder sbuf, String value, boolean standardConformingStrings) throws SQLException {
      if (sbuf == null) {
         sbuf = new StringBuilder((value.length() + 10) / 10 * 11);
      }

      doAppendEscapedLiteral(sbuf, value, standardConformingStrings);
      return sbuf;
   }

   private static void doAppendEscapedLiteral(Appendable sbuf, String value, boolean standardConformingStrings) throws SQLException {
      try {
         int i;
         char ch;
         if (standardConformingStrings) {
            for(i = 0; i < value.length(); ++i) {
               ch = value.charAt(i);
               if (ch == 0) {
                  throw new PSQLException(GT.tr("Zero bytes may not occur in string parameters."), PSQLState.INVALID_PARAMETER_VALUE);
               }

               if (ch == '\'') {
                  sbuf.append('\'');
               }

               sbuf.append(ch);
            }
         } else {
            for(i = 0; i < value.length(); ++i) {
               ch = value.charAt(i);
               if (ch == 0) {
                  throw new PSQLException(GT.tr("Zero bytes may not occur in string parameters."), PSQLState.INVALID_PARAMETER_VALUE);
               }

               if (ch == '\\' || ch == '\'') {
                  sbuf.append(ch);
               }

               sbuf.append(ch);
            }
         }

      } catch (IOException var5) {
         throw new PSQLException(GT.tr("No IOException expected from StringBuffer or StringBuilder"), PSQLState.UNEXPECTED_ERROR, var5);
      }
   }

   public static StringBuilder escapeIdentifier(@Nullable StringBuilder sbuf, String value) throws SQLException {
      if (sbuf == null) {
         sbuf = new StringBuilder(2 + (value.length() + 10) / 10 * 11);
      }

      doAppendEscapedIdentifier(sbuf, value);
      return sbuf;
   }

   private static void doAppendEscapedIdentifier(Appendable sbuf, String value) throws SQLException {
      try {
         sbuf.append('"');

         for(int i = 0; i < value.length(); ++i) {
            char ch = value.charAt(i);
            if (ch == 0) {
               throw new PSQLException(GT.tr("Zero bytes may not occur in identifiers."), PSQLState.INVALID_PARAMETER_VALUE);
            }

            if (ch == '"') {
               sbuf.append(ch);
            }

            sbuf.append(ch);
         }

         sbuf.append('"');
      } catch (IOException var4) {
         throw new PSQLException(GT.tr("No IOException expected from StringBuffer or StringBuilder"), PSQLState.UNEXPECTED_ERROR, var4);
      }
   }

   /** @deprecated */
   @Deprecated
   public static int parseServerVersionStr(@Nullable String serverVersion) throws NumberFormatException {
      return ServerVersion.parseServerVersionStr(serverVersion);
   }
}
