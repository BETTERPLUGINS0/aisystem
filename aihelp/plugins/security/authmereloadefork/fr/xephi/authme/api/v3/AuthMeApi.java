package fr.xephi.authme.api.v3;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.listener.PlayerListener;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.process.register.executors.ApiPasswordRegisterParams;
import fr.xephi.authme.process.register.executors.RegistrationMethod;
import fr.xephi.authme.security.PasswordSecurity;
import fr.xephi.authme.security.crypts.HashedPassword;
import fr.xephi.authme.service.GeoIpService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.util.PlayerUtils;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class AuthMeApi {
   private static AuthMeApi singleton;
   private final AuthMe plugin;
   private final DataSource dataSource;
   private final PasswordSecurity passwordSecurity;
   private final Management management;
   private final ValidationService validationService;
   private final PlayerCache playerCache;
   private final GeoIpService geoIpService;

   @Inject
   AuthMeApi(AuthMe plugin, DataSource dataSource, PlayerCache playerCache, PasswordSecurity passwordSecurity, Management management, ValidationService validationService, GeoIpService geoIpService) {
      this.plugin = plugin;
      this.dataSource = dataSource;
      this.passwordSecurity = passwordSecurity;
      this.management = management;
      this.validationService = validationService;
      this.playerCache = playerCache;
      this.geoIpService = geoIpService;
      singleton = this;
   }

   public static AuthMeApi getInstance() {
      return singleton;
   }

   public AuthMe getPlugin() {
      return this.plugin;
   }

   public String getPluginVersion() {
      return AuthMe.getPluginVersion();
   }

   public boolean isAuthenticated(Player player) {
      return this.playerCache.isAuthenticated(player.getName());
   }

   public boolean isNpc(Player player) {
      return PlayerUtils.isNpc(player);
   }

   public boolean isUnrestricted(Player player) {
      return this.validationService.isUnrestricted(player.getName());
   }

   public Location getLastLocation(Player player) {
      PlayerAuth auth = this.playerCache.getAuth(player.getName());
      return auth != null ? new Location(Bukkit.getWorld(auth.getWorld()), auth.getQuitLocX(), auth.getQuitLocY(), auth.getQuitLocZ(), auth.getYaw(), auth.getPitch()) : null;
   }

   public Optional<AuthMePlayer> getPlayerInfo(String playerName) {
      PlayerAuth auth = this.playerCache.getAuth(playerName);
      if (auth == null) {
         auth = this.dataSource.getAuth(playerName);
      }

      return AuthMePlayerImpl.fromPlayerAuth(auth);
   }

   public String getLastIp(String playerName) {
      PlayerAuth auth = this.playerCache.getAuth(playerName);
      if (auth == null) {
         auth = this.dataSource.getAuth(playerName);
      }

      return auth != null ? auth.getLastIp() : null;
   }

   public List<String> getNamesByIp(String address) {
      return this.dataSource.getAllAuthsByIp(address);
   }

   /** @deprecated */
   @Deprecated
   public Date getLastLogin(String playerName) {
      Long lastLogin = this.getLastLoginMillis(playerName);
      return lastLogin == null ? null : new Date(lastLogin);
   }

   public Instant getLastLoginTime(String playerName) {
      Long lastLogin = this.getLastLoginMillis(playerName);
      return lastLogin == null ? null : Instant.ofEpochMilli(lastLogin);
   }

   private Long getLastLoginMillis(String playerName) {
      PlayerAuth auth = this.playerCache.getAuth(playerName);
      if (auth == null) {
         auth = this.dataSource.getAuth(playerName);
      }

      return auth != null ? auth.getLastLogin() : null;
   }

   public boolean isRegistered(String playerName) {
      String player = playerName.toLowerCase(Locale.ROOT);
      return this.dataSource.isAuthAvailable(player);
   }

   public boolean checkPassword(String playerName, String passwordToCheck) {
      return this.passwordSecurity.comparePassword(passwordToCheck, playerName);
   }

   public boolean registerPlayer(String playerName, String password) {
      String name = playerName.toLowerCase(Locale.ROOT);
      if (this.isRegistered(name)) {
         return false;
      } else {
         HashedPassword result = this.passwordSecurity.computeHash(password, name);
         PlayerAuth auth = PlayerAuth.builder().name(name).password(result).realName(playerName).registrationDate(System.currentTimeMillis()).build();
         return this.dataSource.saveAuth(auth);
      }
   }

   public InventoryView openInventory(Player player, Inventory inventory) {
      PlayerListener.PENDING_INVENTORIES.add(inventory);
      return player.openInventory(inventory);
   }

   public void forceLogin(Player player) {
      this.management.forceLogin(player);
   }

   public void forceLogin(Player player, boolean quiet) {
      this.management.forceLogin(player, quiet);
   }

   public void forceLogout(Player player) {
      this.management.performLogout(player);
   }

   public void forceRegister(Player player, String password, boolean autoLogin) {
      this.management.performRegister(RegistrationMethod.API_REGISTRATION, ApiPasswordRegisterParams.of(player, password, autoLogin));
   }

   public void forceRegister(Player player, String password) {
      this.forceRegister(player, password, true);
   }

   public void forceUnregister(Player player) {
      this.management.performUnregisterByAdmin((CommandSender)null, player.getName(), player);
   }

   public void forceUnregister(String name) {
      this.management.performUnregisterByAdmin((CommandSender)null, name, Bukkit.getPlayer(name));
   }

   public void changePassword(String name, String newPassword) {
      this.management.performPasswordChangeAsAdmin((CommandSender)null, name, newPassword);
   }

   public List<String> getRegisteredNames() {
      List<String> registeredNames = new ArrayList();
      this.dataSource.getAllAuths().forEach((auth) -> {
         registeredNames.add(auth.getNickname());
      });
      return registeredNames;
   }

   public List<String> getRegisteredRealNames() {
      List<String> registeredNames = new ArrayList();
      this.dataSource.getAllAuths().forEach((auth) -> {
         registeredNames.add(auth.getRealName());
      });
      return registeredNames;
   }

   public String getCountryCode(String ip) {
      return this.geoIpService.getCountryCode(ip);
   }

   public String getCountryName(String ip) {
      return this.geoIpService.getCountryName(ip);
   }
}
