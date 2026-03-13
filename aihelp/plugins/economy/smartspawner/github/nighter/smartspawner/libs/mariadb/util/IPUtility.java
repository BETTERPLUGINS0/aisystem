package github.nighter.smartspawner.libs.mariadb.util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtility {
   public static boolean isInetAddress(String ipString) {
      if (ipString != null && !ipString.isEmpty()) {
         int percent;
         for(percent = 0; percent < ipString.length(); ++percent) {
            char c = ipString.charAt(percent);
            boolean ok = c >= '0' && c <= '9' || c >= 'a' && c <= 'f' || c >= 'A' && c <= 'F' || c == '.' || c == ':' || c == '%';
            if (!ok) {
               return false;
            }
         }

         percent = ipString.indexOf(37);
         String literal = percent == -1 ? ipString : ipString.substring(0, percent);
         if (literal.isEmpty()) {
            return false;
         } else if (literal.indexOf(58) != -1) {
            try {
               return InetAddress.getByName(literal) instanceof Inet6Address;
            } catch (UnknownHostException var11) {
               return false;
            }
         } else if (percent != -1) {
            return false;
         } else {
            String[] parts = literal.split("\\.", -1);
            if (parts.length != 4) {
               return false;
            } else {
               String[] var4 = parts;
               int var5 = parts.length;

               for(int var6 = 0; var6 < var5; ++var6) {
                  String part = var4[var6];
                  if (part.isEmpty() || part.length() > 3) {
                     return false;
                  }

                  if (part.length() > 1 && part.charAt(0) == '0') {
                     return false;
                  }

                  int value = 0;

                  for(int i = 0; i < part.length(); ++i) {
                     char c = part.charAt(i);
                     if (c < '0' || c > '9') {
                        return false;
                     }

                     value = value * 10 + (c - 48);
                  }

                  if (value > 255) {
                     return false;
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }
}
