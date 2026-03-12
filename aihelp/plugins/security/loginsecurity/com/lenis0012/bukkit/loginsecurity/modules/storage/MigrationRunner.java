package com.lenis0012.bukkit.loginsecurity.modules.storage;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.sql.DataSource;
import org.bukkit.plugin.java.JavaPlugin;

public class MigrationRunner implements Runnable {
   private final LoginSecurity loginSecurity;
   private final DataSource dataSource;
   private final String platform;

   public MigrationRunner(LoginSecurity loginSecurity, DataSource dataSource, String platform) {
      this.loginSecurity = loginSecurity;
      this.dataSource = dataSource;
      this.platform = platform;
   }

   public void run() {
      try {
         Connection connection = this.dataSource.getConnection();

         try {
            boolean originAutoCommit = connection.getAutoCommit();

            try {
               connection.setAutoCommit(false);
               boolean installed = this.isInstalled(connection);
               Iterator var4 = this.readMigrations().iterator();

               while(var4.hasNext()) {
                  String migrationFileName = (String)var4.next();
                  String[] migrationData = migrationFileName.split(Pattern.quote("__"));
                  String version = migrationData[0];
                  String name = migrationData[1].replace("_", " ");
                  name = name.substring(0, name.length() - ".sql".length());
                  if (!installed || !this.isMigrationInstalled(connection, version)) {
                     this.loginSecurity.getLogger().log(Level.INFO, "Applying database upgrade " + version + ": " + name);
                     String content = this.getContent("sql/" + this.platform + "/" + migrationFileName);
                     Statement statement = connection.createStatement();

                     try {
                        String[] var11 = content.split(";");
                        int var12 = var11.length;

                        for(int var13 = 0; var13 < var12; ++var13) {
                           String query = var11[var13];
                           if (!query.trim().isEmpty()) {
                              statement.executeUpdate(query);
                           }
                        }

                        this.insertMigration(connection, version, name);
                        connection.commit();
                     } catch (Throwable var24) {
                        if (statement != null) {
                           try {
                              statement.close();
                           } catch (Throwable var23) {
                              var24.addSuppressed(var23);
                           }
                        }

                        throw var24;
                     }

                     if (statement != null) {
                        statement.close();
                     }
                  }
               }
            } finally {
               connection.setAutoCommit(originAutoCommit);
            }
         } catch (Throwable var26) {
            if (connection != null) {
               try {
                  connection.close();
               } catch (Throwable var22) {
                  var26.addSuppressed(var22);
               }
            }

            throw var26;
         }

         if (connection != null) {
            connection.close();
         }
      } catch (SQLException var27) {
         var27.printStackTrace();
      }

   }

   private void insertMigration(Connection connectionm, String version, String name) throws SQLException {
      PreparedStatement statement = connectionm.prepareStatement("INSERT INTO ls_upgrades (version, description, applied_at) VALUES (?,?,?);");

      try {
         statement.setString(1, version);
         statement.setString(2, name);
         statement.setTimestamp(3, Timestamp.from(Instant.now()));
         statement.executeUpdate();
      } catch (Throwable var8) {
         if (statement != null) {
            try {
               statement.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }
         }

         throw var8;
      }

      if (statement != null) {
         statement.close();
      }

   }

   private boolean isMigrationInstalled(Connection connection, String version) throws SQLException {
      PreparedStatement statement = connection.prepareStatement("SELECT version FROM ls_upgrades WHERE version=?;");

      boolean var5;
      try {
         statement.setString(1, version);
         ResultSet result = statement.executeQuery();

         try {
            var5 = result.next();
         } catch (Throwable var9) {
            if (result != null) {
               try {
                  result.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (result != null) {
            result.close();
         }
      } catch (Throwable var10) {
         if (statement != null) {
            try {
               statement.close();
            } catch (Throwable var7) {
               var10.addSuppressed(var7);
            }
         }

         throw var10;
      }

      if (statement != null) {
         statement.close();
      }

      return var5;
   }

   private boolean isInstalled(Connection connection) throws SQLException {
      DatabaseMetaData metaData = connection.getMetaData();
      ResultSet tables = metaData.getTables((String)null, (String)null, "ls_upgrades", new String[]{"TABLE"});

      boolean var4;
      try {
         var4 = tables.next();
      } catch (Throwable var7) {
         if (tables != null) {
            try {
               tables.close();
            } catch (Throwable var6) {
               var7.addSuppressed(var6);
            }
         }

         throw var7;
      }

      if (tables != null) {
         tables.close();
      }

      return var4;
   }

   private String getContent(String resource) {
      try {
         InputStream input = this.loginSecurity.getResource(resource);
         BufferedReader reader = new BufferedReader(new InputStreamReader(input));
         StringBuilder builder = new StringBuilder();

         String line;
         while((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
         }

         reader.close();
         return builder.toString();
      } catch (Exception var6) {
         throw new RuntimeException("Couldn't read resource content", var6);
      }
   }

   private List<String> readMigrations() {
      ArrayList migrations = new ArrayList();

      try {
         JarFile jarFile = new JarFile(this.getPluginFile());
         Enumeration entries = jarFile.entries();

         while(entries.hasMoreElements()) {
            JarEntry entry = (JarEntry)entries.nextElement();
            if (entry.getName().startsWith("sql/sqlite/") && entry.getName().contains("__")) {
               migrations.add(entry.getName().substring("sql/sqlite/".length()));
            }
         }
      } catch (IOException var5) {
         this.loginSecurity.getLogger().log(Level.SEVERE, "Failed to scan migration scripts!");
      }

      migrations.sort((o1, o2) -> {
         int i0 = Integer.valueOf(o1.split(Pattern.quote("__"))[0]);
         int i1 = Integer.valueOf(o2.split(Pattern.quote("__"))[0]);
         return Integer.compare(i0, i1);
      });
      return migrations;
   }

   private File getPluginFile() {
      try {
         Method method = JavaPlugin.class.getDeclaredMethod("getFile");
         method.setAccessible(true);
         return (File)method.invoke(this.loginSecurity);
      } catch (Exception var2) {
         throw new RuntimeException("Couldn't get context class loader", var2);
      }
   }
}
