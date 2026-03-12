package com.lenis0012.bukkit.loginsecurity.modules.general;

import com.google.common.collect.Lists;
import com.lenis0012.bukkit.loginsecurity.LoginSecurity;
import com.lenis0012.bukkit.loginsecurity.LoginSecurityConfig;
import com.lenis0012.bukkit.loginsecurity.events.AuthModeChangedEvent;
import com.lenis0012.bukkit.loginsecurity.libs.paper.PaperLib;
import com.lenis0012.bukkit.loginsecurity.modules.language.LanguageKeys;
import com.lenis0012.bukkit.loginsecurity.session.AuthMode;
import com.lenis0012.bukkit.loginsecurity.session.AuthService;
import com.lenis0012.bukkit.loginsecurity.session.PlayerSession;
import com.lenis0012.bukkit.loginsecurity.session.action.BypassAction;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerLocation;
import com.lenis0012.bukkit.loginsecurity.storage.PlayerProfile;
import com.lenis0012.bukkit.loginsecurity.util.MetaData;
import com.lenis0012.bukkit.loginsecurity.util.UserIdMode;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerListener implements Listener {
   private final List<String> ALLOWED_COMMANDS = Lists.newArrayList(new String[]{"/login ", "/register "});
   private final GeneralModule general;

   public PlayerListener(GeneralModule general) {
      this.general = general;
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
      Iterator var2 = Bukkit.getOnlinePlayers().iterator();

      PlayerSession session;
      while(var2.hasNext()) {
         Player player = (Player)var2.next();
         if (player.getName().equalsIgnoreCase(event.getName())) {
            session = LoginSecurity.getSessionManager().getPlayerSession(player);
            if (session.isAuthorized()) {
               event.setLoginResult(Result.KICK_OTHER);
               event.setKickMessage("[LoginSecurity] " + LoginSecurity.translate(LanguageKeys.KICK_ALREADY_ONLINE));
               return;
            }
         }
      }

      String name = event.getName();
      LoginSecurityConfig config = LoginSecurity.getConfiguration();
      if (config.isFilterSpecialChars() && !name.replaceAll("[^a-zA-Z0-9_]", "").equals(name)) {
         event.setLoginResult(Result.KICK_OTHER);
         event.setKickMessage("[LoginSecurity] " + LoginSecurity.translate(LanguageKeys.KICK_USERNAME_CHARS));
      } else if (name.length() >= config.getUsernameMinLength() && name.length() <= config.getUsernameMaxLength()) {
         session = LoginSecurity.getSessionManager().preloadSession(event.getName(), event.getUniqueId());
         if (LoginSecurity.getConfiguration().isMatchUsernameExact() && session.getProfile().getUniqueIdMode() == UserIdMode.OFFLINE && session.getProfile().getLastName() != null && !event.getName().equals(session.getProfile().getLastName())) {
            event.setLoginResult(Result.KICK_OTHER);
            event.setKickMessage("[LoginSecurity] " + LoginSecurity.translate(LanguageKeys.KICK_USERNAME_REGISTERED).param("username", session.getProfile().getLastName()));
         }

      } else {
         event.setLoginResult(Result.KICK_OTHER);
         event.setKickMessage("[LoginSecurity] " + LoginSecurity.translate(LanguageKeys.KICK_USERNAME_LENGTH).param("min", config.getUsernameMinLength()).param("max", config.getPasswordMaxLength()));
      }
   }

   @EventHandler
   public void onPlayerLogin(PlayerLoginEvent event) {
      Player player = event.getPlayer();
      PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
      if (!session.isRegistered() && player.isPermissionSet("ls.bypass") && player.hasPermission("ls.bypass")) {
         session.performAction(new BypassAction(AuthService.PLAYER, player));
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onPlayerQuit(PlayerQuitEvent event) {
      LoginSecurity.getSessionManager().onPlayerLogout(event.getPlayer());
      MetaData.unset(event.getPlayer(), "ls_login_tries");
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      if (!this.isInvalidPlayer(player)) {
         PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
         PlayerProfile profile = session.getProfile();
         if (profile.getLastName() == null || !player.getName().equals(profile.getLastName())) {
            profile.setLastName(player.getName());
            if (session.isRegistered()) {
               session.saveProfileAsync();
            }
         }

         if (session.isAuthorized() && player.hasPermission("ls.update")) {
            this.general.checkUpdates(player);
         }

         if (!session.isAuthorized() && session.isRegistered()) {
            LoginSecurityConfig config = LoginSecurity.getConfiguration();
            if (config.isBlindness()) {
               player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1));
            }

         }
      }
   }

   @EventHandler
   public void maskPlayerLocation(PlayerSpawnLocationEvent event) {
      Player player = event.getPlayer();
      PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
      if (this.general.getLocationMode() == LocationMode.SPAWN) {
         if (session.isRegistered()) {
            if (session.getProfile().getLoginLocationId() == null) {
               PlayerLocation rememberedLocation = new PlayerLocation(event.getSpawnLocation());
               event.setSpawnLocation(((World)Bukkit.getWorlds().get(0)).getSpawnLocation());
               LoginSecurity.getDatastore().getLocationRepository().insertLoginLocation(session.getProfile(), rememberedLocation, (result) -> {
                  if (!result.isSuccess()) {
                     LoginSecurity.getInstance().getLogger().log(Level.SEVERE, "Failed to save player location", result.getError());
                     player.teleport(rememberedLocation.asLocation());
                  } else if (session.isAuthorized() && player.isOnline()) {
                     LoginSecurity.getInstance().getLogger().log(Level.WARNING, "Player was logged in prematurely while still saving location");
                     PaperLib.teleportAsync(player, rememberedLocation.asLocation());
                     session.getProfile().setLoginLocationId((Integer)null);
                     session.saveProfileAsync();
                     LoginSecurity.getDatastore().getLocationRepository().delete(rememberedLocation);
                  }

               });
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onAuthChange(AuthModeChangedEvent event) {
      PlayerSession session = event.getSession();
      if (event.getCurrentMode() == AuthMode.AUTHENTICATED) {
         Player player = session.getPlayer();
         if (player != null && session.isLoggedIn() && player.hasPermission("ls.update")) {
            this.general.checkUpdates(player);
         }
      }
   }

   @EventHandler
   public void onInventoryOpen(InventoryOpenEvent event) {
      if (!this.isInvalidPlayer(event.getPlayer())) {
         Player player = (Player)event.getPlayer();
         PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
         if (!session.isAuthorized()) {
            event.setCancelled(true);
         }
      }
   }

   @EventHandler
   public void onBlockBreak(BlockBreakEvent event) {
      Player player = event.getPlayer();
      if (!this.isInvalidPlayer(player)) {
         PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
         if (!session.isAuthorized()) {
            event.setCancelled(true);
         }
      }
   }

   @EventHandler
   public void onBlockPlace(BlockPlaceEvent event) {
      Player player = event.getPlayer();
      if (!this.isInvalidPlayer(player)) {
         PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
         if (!session.isAuthorized()) {
            event.setCancelled(true);
         }
      }
   }

   @EventHandler
   public void onPlayerDropItem(PlayerDropItemEvent event) {
      this.defaultEventAction(event);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
      Player player = event.getPlayer();
      if (!this.isInvalidPlayer(player)) {
         PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
         LoginSecurityConfig config = LoginSecurity.getConfiguration();
         if (config.isUseCommandShortcut()) {
            if (event.getMessage().toLowerCase().startsWith(config.getLoginCommandShortcut() + " ")) {
               event.setMessage("/login " + event.getMessage().substring(config.getLoginCommandShortcut().length() + 1));
            } else if (event.getMessage().toLowerCase().startsWith(config.getRegisterCommandShortcut() + " ")) {
               event.setMessage("/register " + event.getMessage().substring(config.getRegisterCommandShortcut().length() + 1));
            } else if (event.getMessage().equalsIgnoreCase(config.getLoginCommandShortcut())) {
               event.setMessage("/login");
            } else if (event.getMessage().equalsIgnoreCase(config.getRegisterCommandShortcut())) {
               event.setMessage("/register");
            }
         }

         if (!session.isAuthorized()) {
            String message = event.getMessage().toLowerCase();
            Iterator var6 = this.ALLOWED_COMMANDS.iterator();

            String cmd;
            do {
               if (!var6.hasNext()) {
                  if (message.startsWith("/f")) {
                     event.setMessage("/LOGIN_SECURITY_FACTION_REPLACEMENT_FIX");
                  }

                  event.setCancelled(true);
                  return;
               }

               cmd = (String)var6.next();
            } while(!message.startsWith(cmd));

         }
      }
   }

   @EventHandler
   public void onPlayerChat(AsyncPlayerChatEvent event) {
      this.defaultEventAction(event);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerMove(PlayerMoveEvent event) {
      Player player = event.getPlayer();
      if (!this.isInvalidPlayer(player)) {
         PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
         if (!session.isAuthorized()) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ()) {
               event.setTo(event.getFrom());
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerInteract(PlayerInteractEvent event) {
      this.defaultEventAction(event);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
      this.defaultEventAction(event);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onFoodLevelChange(FoodLevelChangeEvent event) {
      Player player = (Player)event.getEntity();
      if (!this.isInvalidPlayer(player)) {
         PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
         if (!session.isAuthorized()) {
            event.setCancelled(true);
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onEntityDamage(EntityDamageEvent event) {
      if (event.getEntityType() == EntityType.PLAYER) {
         Player player = (Player)event.getEntity();
         if (!this.isInvalidPlayer(player)) {
            PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
            if (!session.isAuthorized()) {
               event.setCancelled(true);
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onTarget(EntityTargetEvent event) {
      if (event.getTarget() instanceof Player) {
         Player player = (Player)event.getTarget();
         if (player.isOnline()) {
            if (!this.isInvalidPlayer(player)) {
               PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
               if (!session.isAuthorized()) {
                  event.setCancelled(true);
               }
            }
         }
      }
   }

   private void defaultEventAction(PlayerEvent event) {
      if (!(event instanceof Cancellable)) {
         throw new IllegalArgumentException("Event cannot be cancelled!");
      } else {
         Player player = event.getPlayer();
         if (!this.isInvalidPlayer(player)) {
            PlayerSession session = LoginSecurity.getSessionManager().getPlayerSession(player);
            if (!session.isAuthorized()) {
               ((Cancellable)event).setCancelled(true);
            }
         }
      }
   }

   private boolean isInvalidPlayer(HumanEntity human) {
      if (!(human instanceof Player)) {
         return true;
      } else {
         Player player = (Player)human;
         return player.hasMetadata("NPC") || !player.isOnline();
      }
   }
}
