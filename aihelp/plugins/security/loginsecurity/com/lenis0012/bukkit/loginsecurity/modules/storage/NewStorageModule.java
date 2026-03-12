package com.lenis0012.bukkit.loginsecurity.modules.storage;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.database.LoginSecurityDatabase;
import com.lenis0012.bukkit.loginsecurity.database.datasource.SingleConnectionDataSource;
import com.lenis0012.bukkit.loginsecurity.database.datasource.sqlite.SQLiteConnectionPoolDataSource;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.CommentConfiguration;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules.Module;
import com.lenis0012.bukkit.loginsecurity.util.ReflectionBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.sql.ConnectionPoolDataSource;
import org.bukkit.Bukkit;

public class NewStorageModule extends Module<LoginSecurity> {
   private SingleConnectionDataSource dataSource;
   private LoginSecurityDatabase database;
   private String platform;

   public NewStorageModule(LoginSecurity plugin) {
      super(plugin);
   }

   public void enable() {
      File configFile = new File(((LoginSecurity)this.plugin).getDataFolder(), "database.yml");
      if (!configFile.exists()) {
         this.copyFile(((LoginSecurity)this.plugin).getResource("database.yml"), configFile);
      }

      CommentConfiguration config = new CommentConfiguration(configFile);
      config.reload();
      ConnectionPoolDataSource dataSourceConfig;
      if (config.getBoolean("mysql.enabled")) {
         this.platform = "mysql";
         dataSourceConfig = this.createMysqlDataSource(config);
      } else {
         this.platform = "sqlite";
         dataSourceConfig = this.createSqliteDataSource();
      }

      this.dataSource = new SingleConnectionDataSource(this.plugin, dataSourceConfig);
      this.database = new LoginSecurityDatabase((LoginSecurity)this.plugin, this.dataSource);

      try {
         this.dataSource.createConnection();
         (new MigrationRunner((LoginSecurity)this.plugin, this.dataSource, this.platform)).run();
      } catch (SQLException var5) {
         ((LoginSecurity)this.plugin).getLogger().log(Level.SEVERE, "Failed to initiate database", var5);
      }

   }

   public void disable() {
      try {
         this.dataSource.shutdown();
      } catch (SQLException var2) {
         Bukkit.getLogger().log(Level.WARNING, "Failed to shut down database", var2);
      }

   }

   ConnectionPoolDataSource createMysqlDataSource(CommentConfiguration config) {
      if (ReflectionBuilder.classExists("com.mysql.cj.jdbc.MysqlConnectionPoolDataSource")) {
         return this.createMysqlDataSourceCJ(config);
      } else if (ReflectionBuilder.classExists("com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource")) {
         return this.createMysqlDataSourceOld(config);
      } else {
         throw new IllegalStateException("Failed to create MySQL data source (no compatible driver found)");
      }
   }

   private ConnectionPoolDataSource createMysqlDataSourceCJ(CommentConfiguration config) {
      return (ConnectionPoolDataSource)(new ReflectionBuilder("com.mysql.cj.jdbc.MysqlConnectionPoolDataSource")).call("setUrl", "jdbc:mysql://" + config.getString("mysql.host") + "/" + config.getString("mysql.database")).call("setUser", config.getString("mysql.username")).call("setPassword", config.getString("mysql.password")).build(ConnectionPoolDataSource.class);
   }

   private ConnectionPoolDataSource createMysqlDataSourceOld(CommentConfiguration config) {
      return (ConnectionPoolDataSource)(new ReflectionBuilder("com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource")).call("setUrl", "jdbc:mysql://" + config.getString("mysql.host") + "/" + config.getString("mysql.database")).call("setUser", config.getString("mysql.username")).call("setPassword", config.getString("mysql.password")).build(ConnectionPoolDataSource.class);
   }

   ConnectionPoolDataSource createSqliteDataSource() {
      File backupFile = new File(((LoginSecurity)this.plugin).getDataFolder(), "LoginSecurity.db.3.0.backup");
      if (!backupFile.exists()) {
         try {
            this.copyFile(new FileInputStream(new File(((LoginSecurity)this.plugin).getDataFolder(), "LoginSecurity.db")), backupFile);
         } catch (FileNotFoundException var4) {
         }
      }

      SQLiteConnectionPoolDataSource sqliteConfig = new SQLiteConnectionPoolDataSource();
      String path = (new File(((LoginSecurity)this.plugin).getDataFolder(), "LoginSecurity.db")).getPath();
      sqliteConfig.setUrl("jdbc:sqlite:" + path);
      return sqliteConfig;
   }

   public LoginSecurityDatabase getDatabase() {
      return this.database;
   }

   private void copyFile(InputStream from, File to) {
      try {
         FileOutputStream output = new FileOutputStream(to);
         byte[] buffer = new byte[1024];

         int length;
         while((length = from.read(buffer)) != -1) {
            output.write(buffer, 0, length);
         }

         output.close();
         from.close();
      } catch (IOException var6) {
         ((LoginSecurity)this.plugin).getLogger().log(Level.WARNING, "Failed to copy resource", var6);
      }

   }

   public String getPlatform() {
      return this.platform;
   }
}
