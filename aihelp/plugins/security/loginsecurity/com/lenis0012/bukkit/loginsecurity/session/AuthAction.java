package com.lenis0012.bukkit.loginsecurity.session;

import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.libs.paper.PaperLib;
import com.lenis0012.bukkit.loginsecurity.libs.pluginutils.modules.ModularPlugin;
import com.lenis0012.bukkit.loginsecurity.session.action.ActionResponse;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerInventory;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerLocation;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerProfile;
import com.lenis0012.bukkit.loginsecurity.util.InventorySerializer;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class AuthAction {
   private final AuthActionType type;
   private final AuthService service;
   private final Object serviceProvider;

   public <T> AuthAction(AuthActionType type, AuthService<T> service, T serviceProvider) {
      this.type = type;
      this.service = service;
      this.serviceProvider = serviceProvider;
   }

   public AuthActionType getType() {
      return this.type;
   }

   public AuthService getService() {
      return this.service;
   }

   protected Object getServiceProvider() {
      return this.serviceProvider;
   }

   public abstract AuthMode run(PlayerSession var1, ActionResponse var2);

   protected void rehabPlayer(PlayerSession session) {
      Player player = session.getPlayer();
      PlayerProfile profile = session.getProfile();
      Bukkit.getScheduler().runTask(LoginSecurity.getInstance(), () -> {
         player.removePotionEffect(PotionEffectType.BLINDNESS);
      });
      if (profile.getInventoryId() != null) {
         try {
            PlayerInventory serializedInventory = LoginSecurity.getDatastore().getInventoryRepository().findByIdBlocking(profile.getInventoryId());
            if (serializedInventory != null) {
               Bukkit.getScheduler().runTask(LoginSecurity.getInstance(), () -> {
                  InventorySerializer.deserializeInventory(serializedInventory, player.getInventory());
                  profile.setInventoryId((Integer)null);
                  session.saveProfileAsync();
               });
            } else {
               LoginSecurity.getInstance().getLogger().log(Level.WARNING, "Couldn't find player's inventory");
               profile.setInventoryId((Integer)null);
               session.saveProfileAsync();
            }
         } catch (SQLException var6) {
            LoginSecurity.getInstance().getLogger().log(Level.SEVERE, "Failed to load player inventory", var6);
         }
      }

      if (profile.getLoginLocationId() != null) {
         try {
            PlayerLocation serializedLocation = LoginSecurity.getDatastore().getLocationRepository().findByIdBlocking(profile.getLoginLocationId());
            Bukkit.getScheduler().runTask(LoginSecurity.getInstance(), () -> {
               PaperLib.teleportAsync(player, serializedLocation.asLocation());
               profile.setLoginLocationId((Integer)null);
               session.saveProfileAsync();
               LoginSecurity.getDatastore().getLocationRepository().delete(serializedLocation);
            });
         } catch (SQLException var5) {
            LoginSecurity.getInstance().getLogger().log(Level.SEVERE, "Failed to load player login location", var5);
         }
      }

      if (LoginSecurity.getConfiguration().isHideInventory()) {
         BukkitScheduler var10000 = Bukkit.getScheduler();
         ModularPlugin var10001 = LoginSecurity.getInstance();
         Objects.requireNonNull(player);
         var10000.runTask(var10001, player::updateInventory);
      }

   }
}
