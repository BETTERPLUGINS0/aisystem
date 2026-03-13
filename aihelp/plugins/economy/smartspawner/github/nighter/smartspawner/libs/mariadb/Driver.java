package github.nighter.smartspawner.libs.mariadb;

import github.nighter.smartspawner.libs.mariadb.client.Client;
import github.nighter.smartspawner.libs.mariadb.client.impl.MultiPrimaryClient;
import github.nighter.smartspawner.libs.mariadb.client.impl.MultiPrimaryReplicaClient;
import github.nighter.smartspawner.libs.mariadb.client.impl.ReplayClient;
import github.nighter.smartspawner.libs.mariadb.client.impl.StandardClient;
import github.nighter.smartspawner.libs.mariadb.client.util.ClosableLock;
import github.nighter.smartspawner.libs.mariadb.export.HaMode;
import github.nighter.smartspawner.libs.mariadb.pool.Pools;
import github.nighter.smartspawner.libs.mariadb.util.VersionFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Driver implements java.sql.Driver {
   private static final Pattern identifierPattern = Pattern.compile("[0-9a-zA-Z$_\\u0080-\\uFFFF]*", 64);
   private static final Pattern escapePattern = Pattern.compile("[\u0000'\"\b\n\r\t\u001a\\\\]");
   private static final Map<String, String> mapper = new HashMap();

   public static Connection connect(Configuration configuration) throws SQLException {
      ClosableLock lock = new ClosableLock();
      if (configuration.haMode() != HaMode.NONE) {
         Client client = configuration.havePrimaryHostOnly() ? new MultiPrimaryClient(configuration, lock) : new MultiPrimaryReplicaClient(configuration, lock);
         return new Connection(configuration, lock, (Client)client);
      } else {
         Driver.ClientInstance<Configuration, HostAddress, ClosableLock, Boolean, Client> clientInstance = configuration.transactionReplay() ? ReplayClient::new : StandardClient::new;
         if (configuration.addresses().isEmpty()) {
            throw new SQLException("host, pipe or local socket must be set to connect socket");
         } else {
            SQLException lastException = null;
            Iterator var4 = configuration.addresses().iterator();

            while(var4.hasNext()) {
               HostAddress host = (HostAddress)var4.next();

               try {
                  Client client = (Client)clientInstance.apply(configuration, host, lock, false);
                  return new Connection(configuration, lock, client);
               } catch (SQLException var7) {
                  lastException = var7;
               }
            }

            throw lastException;
         }
      }
   }

   public Connection connect(String url, Properties props) throws SQLException {
      Configuration configuration = Configuration.parse(url, props);
      if (configuration != null) {
         return configuration.pool() ? Pools.retrievePool(configuration).getPoolConnection().getConnection() : connect(configuration);
      } else {
         return null;
      }
   }

   public boolean acceptsURL(String url) {
      return Configuration.acceptsUrl(url);
   }

   public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
      Configuration conf = Configuration.parse(url, info);
      if (conf == null) {
         return new DriverPropertyInfo[0];
      } else {
         Properties propDesc = new Properties();

         try {
            InputStream inputStream = Driver.class.getClassLoader().getResourceAsStream("driver.properties");

            try {
               propDesc.load(inputStream);
            } catch (Throwable var16) {
               if (inputStream != null) {
                  try {
                     inputStream.close();
                  } catch (Throwable var15) {
                     var16.addSuppressed(var15);
                  }
               }

               throw var16;
            }

            if (inputStream != null) {
               inputStream.close();
            }
         } catch (IOException var17) {
         }

         List<DriverPropertyInfo> props = new ArrayList();
         Field[] var6 = Configuration.Builder.class.getDeclaredFields();
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            Field field = var6[var8];
            if (!field.getName().startsWith("_")) {
               try {
                  Method getterMethod = Configuration.class.getDeclaredMethod(field.getName());
                  Object obj = getterMethod.invoke(conf);
                  String value = obj == null ? null : obj.toString();
                  DriverPropertyInfo propertyInfo = new DriverPropertyInfo(field.getName(), value);
                  propertyInfo.description = value == null ? "" : (String)propDesc.get(field.getName());
                  propertyInfo.required = false;
                  props.add(propertyInfo);
               } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException var14) {
               }
            }
         }

         return (DriverPropertyInfo[])props.toArray(new DriverPropertyInfo[0]);
      }
   }

   public int getMajorVersion() {
      return VersionFactory.getInstance().getMajorVersion();
   }

   public int getMinorVersion() {
      return VersionFactory.getInstance().getMinorVersion();
   }

   public boolean jdbcCompliant() {
      return true;
   }

   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
      throw new SQLFeatureNotSupportedException("Use logging parameters for enabling logging.");
   }

   public static String enquoteIdentifier(String identifier, boolean alwaysQuote) throws SQLException {
      int len = identifier.length();
      if (isSimpleIdentifier(identifier)) {
         if (len >= 1 && len <= 64) {
            if (alwaysQuote) {
               return "`" + identifier + "`";
            } else {
               for(int i = 0; i < identifier.length(); ++i) {
                  if (!Character.isDigit(identifier.charAt(i))) {
                     return identifier;
                  }
               }

               return "`" + identifier + "`";
            }
         } else {
            throw new SQLException("Invalid identifier length");
         }
      } else if (identifier.contains("\u0000")) {
         throw new SQLException("Invalid name - containing u0000 character", "42000");
      } else {
         if (identifier.matches("^`.+`$")) {
            identifier = identifier.substring(1, identifier.length() - 1);
         }

         if (len >= 1 && len <= 64) {
            return "`" + identifier.replace("`", "``") + "`";
         } else {
            throw new SQLException("Invalid identifier length");
         }
      }
   }

   public static String enquoteLiteral(String val) {
      Matcher matcher = escapePattern.matcher(val);
      StringBuffer escapedVal = new StringBuffer("'");

      while(matcher.find()) {
         matcher.appendReplacement(escapedVal, (String)mapper.get(matcher.group()));
      }

      matcher.appendTail(escapedVal);
      escapedVal.append("'");
      return escapedVal.toString();
   }

   public static boolean isSimpleIdentifier(String identifier) {
      return identifier != null && !identifier.isEmpty() && identifierPattern.matcher(identifier).matches();
   }

   static {
      try {
         DriverManager.registerDriver(new Driver());
      } catch (SQLException var1) {
      }

      mapper.put("\u0000", "\\0");
      mapper.put("'", "\\\\'");
      mapper.put("\"", "\\\\\"");
      mapper.put("\b", "\\\\b");
      mapper.put("\n", "\\\\n");
      mapper.put("\r", "\\\\r");
      mapper.put("\t", "\\\\t");
      mapper.put("\u001a", "\\\\Z");
      mapper.put("\\", "\\\\");
   }

   @FunctionalInterface
   private interface ClientInstance<T, U, V, W, R> {
      R apply(T var1, U var2, V var3, W var4) throws SQLException;
   }
}
