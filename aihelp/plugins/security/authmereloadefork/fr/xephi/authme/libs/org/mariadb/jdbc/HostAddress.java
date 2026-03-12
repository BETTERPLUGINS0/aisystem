package fr.xephi.authme.libs.org.mariadb.jdbc;

import fr.xephi.authme.libs.org.mariadb.jdbc.export.HaMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HostAddress {
   public final String host;
   public int port;
   public Boolean primary;
   private Long threadsConnected;
   private Long threadConnectedTimeout;

   private HostAddress(String host, int port, Boolean primary) {
      this.host = host;
      this.port = port;
      this.primary = primary;
   }

   public static HostAddress from(String host, int port) {
      return new HostAddress(host, port, (Boolean)null);
   }

   public static HostAddress from(String host, int port, boolean primary) {
      return new HostAddress(host, port, primary);
   }

   public static List<HostAddress> parse(String spec, HaMode haMode) throws SQLException {
      if ("".equals(spec)) {
         return new ArrayList(0);
      } else {
         String[] tokens = spec.trim().split(",");
         int size = tokens.length;
         List<HostAddress> arr = new ArrayList(size);

         for(int i = 0; i < tokens.length; ++i) {
            String token = tokens[i];
            if (token.startsWith("address=")) {
               arr.add(parseParameterHostAddress(token, haMode, i == 0));
            } else {
               arr.add(parseSimpleHostAddress(token, haMode, i == 0));
            }
         }

         return arr;
      }
   }

   private static HostAddress parseSimpleHostAddress(String str, HaMode haMode, boolean first) throws SQLException {
      int port = 3306;
      String host;
      if (str.charAt(0) == '[') {
         int ind = str.indexOf(93);
         host = str.substring(1, ind);
         if (ind != str.length() - 1 && str.charAt(ind + 1) == ':') {
            port = getPort(str.substring(ind + 2));
         }
      } else if (str.contains(":")) {
         String[] hostPort = str.split(":");
         host = hostPort[0];
         port = getPort(hostPort[1]);
      } else {
         host = str;
      }

      boolean primary = haMode != HaMode.REPLICATION || first;
      return new HostAddress(host, port, primary);
   }

   private static int getPort(String portString) throws SQLException {
      try {
         return Integer.parseInt(portString);
      } catch (NumberFormatException var2) {
         throw new SQLException("Incorrect port value : " + portString);
      }
   }

   private static HostAddress parseParameterHostAddress(String str, HaMode haMode, boolean first) throws SQLException {
      String host = null;
      int port = 3306;
      Boolean primary = null;
      String[] array = str.replace(" ", "").split("(?=\\()|(?<=\\))");

      for(int i = 1; i < array.length; ++i) {
         String[] token = array[i].replace("(", "").replace(")", "").trim().split("=");
         if (token.length != 2) {
            throw new IllegalArgumentException("Invalid connection URL, expected key=value pairs, found " + array[i]);
         }

         String key = token[0].toLowerCase();
         String value = token[1].toLowerCase();
         byte var12 = -1;
         switch(key.hashCode()) {
         case 3208616:
            if (key.equals("host")) {
               var12 = 0;
            }
            break;
         case 3446913:
            if (key.equals("port")) {
               var12 = 1;
            }
            break;
         case 3575610:
            if (key.equals("type")) {
               var12 = 2;
            }
         }

         switch(var12) {
         case 0:
            host = value.replace("[", "").replace("]", "");
            break;
         case 1:
            port = getPort(value);
            break;
         case 2:
            if (!"master".equalsIgnoreCase(value) && !"primary".equalsIgnoreCase(value)) {
               if (!"slave".equalsIgnoreCase(value) && !"replica".equalsIgnoreCase(value)) {
                  throw new SQLException(String.format("Wrong type value %s (possible value primary/replica)", array[i]));
               }

               primary = false;
            } else {
               primary = true;
            }
         }
      }

      if (primary == null) {
         if (haMode == HaMode.REPLICATION) {
            primary = first;
         } else {
            primary = true;
         }
      }

      return new HostAddress(host, port, primary);
   }

   public String toString() {
      return String.format("address=(host=%s)(port=%s)%s", this.host, this.port, this.primary != null ? "(type=" + (this.primary ? "primary)" : "replica)") : "");
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         HostAddress that = (HostAddress)o;
         return this.port == that.port && Objects.equals(this.host, that.host) && Objects.equals(this.primary, that.primary);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.host, this.port, this.primary});
   }

   public Long getThreadsConnected() {
      return this.threadsConnected;
   }

   public void setThreadsConnected(long threadsConnected) {
      this.threadsConnected = threadsConnected;
      this.threadConnectedTimeout = System.currentTimeMillis() + 180000L;
   }

   public void forceThreadsConnected(long threadsConnected, long threadConnectedTimeout) {
      this.threadsConnected = threadsConnected;
      this.threadConnectedTimeout = threadConnectedTimeout;
   }

   public Long getThreadConnectedTimeout() {
      return this.threadConnectedTimeout;
   }
}
