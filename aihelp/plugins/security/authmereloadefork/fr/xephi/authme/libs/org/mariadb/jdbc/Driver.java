package fr.xephi.authme.libs.org.mariadb.jdbc;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Client;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.MultiPrimaryClient;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.MultiPrimaryReplicaClient;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.ReplayClient;
import fr.xephi.authme.libs.org.mariadb.jdbc.client.impl.StandardClient;
import fr.xephi.authme.libs.org.mariadb.jdbc.pool.Pools;
import fr.xephi.authme.libs.org.mariadb.jdbc.util.VersionFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public final class Driver implements java.sql.Driver {
   public static Connection connect(Configuration configuration) throws SQLException {
      ReentrantLock lock = new ReentrantLock();
      Object client;
      switch(configuration.haMode()) {
      case LOADBALANCE:
      case SEQUENTIAL:
         client = new MultiPrimaryClient(configuration, lock);
         break;
      case REPLICATION:
         client = new MultiPrimaryReplicaClient(configuration, lock);
         break;
      default:
         Driver.ClientInstance<Configuration, HostAddress, ReentrantLock, Boolean, Client> clientInstance = configuration.transactionReplay() ? ReplayClient::new : StandardClient::new;
         if (!configuration.addresses().isEmpty()) {
            SQLException lastException = null;
            Iterator var5 = configuration.addresses().iterator();

            while(var5.hasNext()) {
               HostAddress host = (HostAddress)var5.next();

               try {
                  Client client = (Client)clientInstance.apply(configuration, host, lock, false);
                  return new Connection(configuration, lock, client);
               } catch (SQLException var8) {
                  lastException = var8;
               }
            }

            throw lastException;
         }

         client = (Client)clientInstance.apply(configuration, (Object)null, lock, false);
      }

      return new Connection(configuration, lock, (Client)client);
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
                  Field fieldConf = Configuration.class.getDeclaredField(field.getName());
                  fieldConf.setAccessible(true);
                  Object obj = fieldConf.get(conf);
                  String value = obj == null ? null : obj.toString();
                  DriverPropertyInfo propertyInfo = new DriverPropertyInfo(field.getName(), value);
                  propertyInfo.description = value == null ? "" : (String)propDesc.get(field.getName());
                  propertyInfo.required = false;
                  props.add(propertyInfo);
               } catch (NoSuchFieldException | IllegalAccessException var14) {
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

   static {
      try {
         DriverManager.registerDriver(new Driver());
      } catch (SQLException var1) {
      }

   }

   @FunctionalInterface
   private interface ClientInstance<T, U, V, W, R> {
      R apply(T var1, U var2, V var3, W var4) throws SQLException;
   }
}
