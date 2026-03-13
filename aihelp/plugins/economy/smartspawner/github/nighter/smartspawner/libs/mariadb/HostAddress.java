package github.nighter.smartspawner.libs.mariadb;

import github.nighter.smartspawner.libs.mariadb.export.HaMode;
import github.nighter.smartspawner.libs.mariadb.export.SslMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HostAddress {
   public final String host;
   public final int port;
   public final String pipe;
   public final SslMode sslMode;
   public final String localSocket;
   public Boolean primary;
   private Long threadsConnected;
   private Long threadConnectedTimeout;

   private HostAddress(String host, int port, Boolean primary, String pipe, String localSocket, SslMode sslMode) {
      this.host = host;
      this.port = port;
      this.primary = primary;
      this.pipe = pipe;
      this.localSocket = localSocket;
      this.sslMode = sslMode;
   }

   public static HostAddress from(String host, int port) {
      return new HostAddress(host, port, (Boolean)null, (String)null, (String)null, (SslMode)null);
   }

   public static HostAddress pipe(String pipe) {
      return new HostAddress((String)null, 3306, (Boolean)null, pipe, (String)null, (SslMode)null);
   }

   public static HostAddress localSocket(String localSocket) {
      return new HostAddress((String)null, 3306, (Boolean)null, (String)null, localSocket, (SslMode)null);
   }

   public static HostAddress from(String host, int port, boolean primary) {
      return new HostAddress(host, port, primary, (String)null, (String)null, (SslMode)null);
   }

   public static HostAddress from(String host, int port, String sslMode) {
      return new HostAddress(host, port, (Boolean)null, (String)null, (String)null, sslMode == null ? null : SslMode.from(sslMode));
   }

   public static HostAddress from(String host, int port, boolean primary, String sslMode) {
      return new HostAddress(host, port, primary, (String)null, (String)null, sslMode == null ? null : SslMode.from(sslMode));
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
      return new HostAddress(host, port, primary, (String)null, (String)null, (SslMode)null);
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
      String sslMode = null;
      String pipe = null;
      String localsocket = null;
      Boolean primary = null;
      String[] array = str.replace(" ", "").split("(?=\\()|(?<=\\))");

      for(int i = 1; i < array.length; ++i) {
         String[] token = array[i].replace("(", "").replace(")", "").trim().split("=");
         if (token.length != 2) {
            throw new IllegalArgumentException("Invalid connection URL, expected key=value pairs, found " + array[i]);
         }

         String key = token[0].toLowerCase();
         String value = token[1].toLowerCase();
         byte var15 = -1;
         switch(key.hashCode()) {
         case -1966767458:
            if (key.equals("localsocket")) {
               var15 = 1;
            }
            break;
         case -1915809361:
            if (key.equals("sslmode")) {
               var15 = 4;
            }
            break;
         case 3208616:
            if (key.equals("host")) {
               var15 = 0;
            }
            break;
         case 3441070:
            if (key.equals("pipe")) {
               var15 = 2;
            }
            break;
         case 3446913:
            if (key.equals("port")) {
               var15 = 3;
            }
            break;
         case 3575610:
            if (key.equals("type")) {
               var15 = 5;
            }
         }

         switch(var15) {
         case 0:
            host = value.replace("[", "").replace("]", "");
            break;
         case 1:
            localsocket = token[1];
            break;
         case 2:
            pipe = token[1];
            break;
         case 3:
            port = getPort(value);
            break;
         case 4:
            sslMode = token[1];
            break;
         case 5:
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
         primary = haMode != HaMode.REPLICATION || first;
      }

      return new HostAddress(host, port, primary, pipe, localsocket, sslMode == null ? null : SslMode.from(sslMode));
   }

   public String toString() {
      if (this.pipe != null) {
         return String.format("address=(pipe=%s)", this.pipe);
      } else if (this.localSocket != null) {
         return String.format("address=(localSocket=%s)", this.localSocket);
      } else if (this.sslMode == null && this.primary != Boolean.FALSE) {
         return this.port != 3306 ? this.host + ":" + this.port : this.host;
      } else {
         return String.format("address=(host=%s)%s%s%s", this.host, this.port != 3306 ? "(port=" + this.port + ")" : "", this.sslMode != null ? "(sslMode=" + this.sslMode.getValue() + ")" : "", this.primary != null ? "(type=" + (this.primary ? "primary)" : "replica)") : "");
      }
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

   public HostAddress withPipe(String pipe) {
      return new HostAddress(this.host, this.port, this.primary, pipe, this.localSocket, this.sslMode);
   }

   public HostAddress withLocalSocket(String localSocket) {
      return new HostAddress(this.host, this.port, this.primary, this.pipe, localSocket, this.sslMode);
   }

   public HostAddress withPort(int port) {
      return new HostAddress(this.host, port, this.primary, this.pipe, this.localSocket, this.sslMode);
   }

   public Long getThreadConnectedTimeout() {
      return this.threadConnectedTimeout;
   }
}
