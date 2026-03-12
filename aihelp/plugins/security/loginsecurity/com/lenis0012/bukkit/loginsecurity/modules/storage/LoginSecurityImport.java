package com.lenis0012.bukkit.loginsecurity.modules.storage;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.database.InventoryRepository;
import com.lenis0012.bukkit.loginsecurity.database.LocationRepository;
import com.lenis0012.bukkit.loginsecurity.database.LoginSecurityDatabase;
import com.lenis0012.bukkit.loginsecurity.database.ProfileRepository;
import com.lenis0012.bukkit.loginsecurity.database.datasource.SingleConnectionDataSource;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.config.CommentConfiguration;
import java.io.File;
import java.sql.SQLException;
import java.util.Objects;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class LoginSecurityImport implements StorageImport {
   private final LoginSecurity loginSecurity;
   private final CommandSender sender;

   public LoginSecurityImport(LoginSecurity loginSecurity, CommandSender sender) {
      this.loginSecurity = loginSecurity;
      this.sender = sender;
   }

   public void run() {
      try {
         this.message("Initializing secondary import database.");
         NewStorageModule storageModule = (NewStorageModule)this.loginSecurity.getModule(NewStorageModule.class);
         SingleConnectionDataSource secondaryDataSource = this.createDataSource(storageModule);
         LoginSecurityDatabase datastore = LoginSecurity.getDatastore();
         ProfileRepository profiles = new ProfileRepository(this.loginSecurity, secondaryDataSource);
         LocationRepository locations = new LocationRepository(this.loginSecurity, secondaryDataSource);
         InventoryRepository inventories = new InventoryRepository(this.loginSecurity, secondaryDataSource);
         this.message("Importing inventories.");
         InventoryRepository var10000 = datastore.getInventoryRepository();
         Objects.requireNonNull(inventories);
         var10000.batchInsert(inventories::iterateAllBlocking);
         this.message("Importing locations.");
         LocationRepository var8 = datastore.getLocationRepository();
         Objects.requireNonNull(locations);
         var8.batchInsert(locations::iterateAllBlocking);
         this.message("Importing profiles.");
         ProfileRepository var9 = datastore.getProfileRepository();
         Objects.requireNonNull(profiles);
         var9.batchInsert(profiles::iterateAllBlocking);
         this.message("Done, imported all profiles.");
      } catch (SQLException var7) {
         this.message("Failed to import profiles: " + var7.getMessage());
      }

   }

   public boolean isPossible() {
      NewStorageModule storageModule = (NewStorageModule)this.loginSecurity.getModule(NewStorageModule.class);
      return storageModule.getPlatform().equalsIgnoreCase("mysql") ? (new File(this.loginSecurity.getDataFolder(), "LoginSecurity.db")).exists() : true;
   }

   private void message(String message) {
      Bukkit.getScheduler().runTask(this.loginSecurity, () -> {
         this.sender.sendMessage(message);
      });
   }

   private SingleConnectionDataSource createDataSource(NewStorageModule storageModule) {
      if (storageModule.getPlatform().equalsIgnoreCase("mysql")) {
         return new SingleConnectionDataSource(this.loginSecurity, storageModule.createSqliteDataSource());
      } else {
         CommentConfiguration configuration = new CommentConfiguration(new File(this.loginSecurity.getDataFolder(), "database.yml"));
         configuration.reload();
         return new SingleConnectionDataSource(this.loginSecurity, storageModule.createMysqlDataSource(configuration));
      }
   }
}
