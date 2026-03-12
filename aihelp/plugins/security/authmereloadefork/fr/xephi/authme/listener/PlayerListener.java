package fr.xephi.authme.listener;

import fr.xephi.authme.data.QuickCommandsProtectionManager;
import fr.xephi.authme.data.auth.PlayerAuth;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.MessageKey;
import fr.xephi.authme.message.Messages;
import fr.xephi.authme.permission.PermissionsManager;
import fr.xephi.authme.permission.PlayerStatePermission;
import fr.xephi.authme.process.Management;
import fr.xephi.authme.service.AntiBotService;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.service.JoinMessageService;
import fr.xephi.authme.service.TeleportationService;
import fr.xephi.authme.service.ValidationService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.SpawnLoader;
import fr.xephi.authme.settings.properties.HooksSettings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.settings.properties.RegistrationSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.TeleportUtils;
import fr.xephi.authme.util.message.I18NUtils;
import fr.xephi.authme.util.message.MiniMessageUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.geysermc.floodgate.api.FloodgateApi;

public class PlayerListener implements Listener {
   @Inject
   private Settings settings;
   @Inject
   private Messages messages;
   @Inject
   private DataSource dataSource;
   @Inject
   private AntiBotService antiBotService;
   @Inject
   private Management management;
   @Inject
   private BukkitService bukkitService;
   @Inject
   private SpawnLoader spawnLoader;
   @Inject
   private OnJoinVerifier onJoinVerifier;
   @Inject
   private ListenerService listenerService;
   @Inject
   private TeleportationService teleportationService;
   @Inject
   private ValidationService validationService;
   @Inject
   private JoinMessageService joinMessageService;
   @Inject
   private PermissionsManager permissionsManager;
   @Inject
   private QuickCommandsProtectionManager quickCommandsProtectionManager;
   public static List<Inventory> PENDING_INVENTORIES = new ArrayList();

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onAsyncPlayerPreLoginEventLowest(AsyncPlayerPreLoginEvent event) {
      if (event.getLoginResult() == Result.ALLOWED) {
         String name = event.getName();
         if (event.getAddress() == null) {
            event.disallow(Result.KICK_OTHER, this.messages.retrieveSingle(name, MessageKey.KICK_UNRESOLVED_HOSTNAME));
         } else if (!this.validationService.isUnrestricted(name)) {
            if (!(Boolean)this.settings.getProperty(HooksSettings.HOOK_FLOODGATE_PLAYER) || !(Boolean)this.settings.getProperty(HooksSettings.IGNORE_BEDROCK_NAME_CHECK) || Bukkit.getServer().getPluginManager().getPlugin("floodgate") == null || !FloodgateApi.getInstance().isFloodgateId(event.getUniqueId())) {
               try {
                  this.onJoinVerifier.checkIsValidName(name);
               } catch (FailedVerificationException var4) {
                  event.setKickMessage(this.messages.retrieveSingle(name, var4.getReason(), var4.getArgs()));
                  event.setLoginResult(Result.KICK_OTHER);
               }

            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onAsyncPlayerPreLoginEventHighest(AsyncPlayerPreLoginEvent event) {
      if (event.getLoginResult() == Result.ALLOWED) {
         String name = event.getName();
         if (!this.validationService.isUnrestricted(name)) {
            try {
               PlayerAuth auth = this.dataSource.getAuth(name);
               boolean isAuthAvailable = auth != null;
               this.onJoinVerifier.checkKickNonRegistered(isAuthAvailable);
               this.onJoinVerifier.checkAntibot(name, isAuthAvailable);
               this.onJoinVerifier.checkNameCasing(name, auth);
               String ip = event.getAddress().getHostAddress();
               this.onJoinVerifier.checkPlayerCountry(name, ip, isAuthAvailable);
            } catch (FailedVerificationException var6) {
               event.setKickMessage(this.messages.retrieveSingle(name, var6.getReason(), var6.getArgs()));
               event.setLoginResult(Result.KICK_OTHER);
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.LOW
   )
   public void onPlayerLogin(PlayerLoginEvent event) {
      Player player = event.getPlayer();
      String name = player.getName();

      try {
         this.onJoinVerifier.checkSingleSession(name);
      } catch (FailedVerificationException var5) {
         event.setKickMessage(this.messages.retrieveSingle(name, var5.getReason(), var5.getArgs()));
         event.setResult(org.bukkit.event.player.PlayerLoginEvent.Result.KICK_OTHER);
         return;
      }

      if (!this.validationService.isUnrestricted(name)) {
         this.onJoinVerifier.refusePlayerForFullServer(event);
      }
   }

   @EventHandler(
      priority = EventPriority.NORMAL
   )
   public void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      if (!PlayerListener19Spigot.isPlayerSpawnLocationEventCalled()) {
         this.teleportationService.teleportOnJoin(player);
      }

      this.quickCommandsProtectionManager.processJoin(player);
      this.management.performJoin(player);
      this.teleportationService.teleportNewPlayerToFirstSpawn(player);
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onJoinMessage(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      if ((Boolean)this.settings.getProperty(RegistrationSettings.REMOVE_JOIN_MESSAGE)) {
         event.setJoinMessage((String)null);
      } else {
         String customJoinMessage = (String)this.settings.getProperty(RegistrationSettings.CUSTOM_JOIN_MESSAGE);
         if (!customJoinMessage.isEmpty()) {
            customJoinMessage = ChatColor.translateAlternateColorCodes('&', MiniMessageUtils.parseMiniMessageToLegacy(customJoinMessage));
            event.setJoinMessage(customJoinMessage.replace("{PLAYERNAME}", player.getName()).replace("{DISPLAYNAME}", player.getDisplayName()).replace("{DISPLAYNAMENOCOLOR}", ChatColor.stripColor(player.getDisplayName())));
         }

         if ((Boolean)this.settings.getProperty(RegistrationSettings.DELAY_JOIN_MESSAGE)) {
            String name = player.getName().toLowerCase(Locale.ROOT);
            String joinMsg = event.getJoinMessage();
            if (joinMsg != null) {
               event.setJoinMessage((String)null);
               this.joinMessageService.putMessage(name, joinMsg);
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      if ((Boolean)this.settings.getProperty(RegistrationSettings.REMOVE_LEAVE_MESSAGE)) {
         event.setQuitMessage((String)null);
      } else if ((Boolean)this.settings.getProperty(RegistrationSettings.REMOVE_UNLOGGED_LEAVE_MESSAGE) && this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setQuitMessage((String)null);
      }

      if ((Boolean)this.settings.getProperty(PluginSettings.I18N_MESSAGES)) {
         I18NUtils.removeLocale(player.getUniqueId());
      }

      if (!this.antiBotService.wasPlayerKicked(player.getName())) {
         this.management.performQuit(player);
      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.HIGHEST
   )
   public void onPlayerKick(PlayerKickEvent event) {
      if ((Boolean)this.settings.getProperty(RestrictionSettings.FORCE_SINGLE_SESSION) && event.getReason().contains("You logged in from another location")) {
         event.setCancelled(true);
      } else {
         Player player = event.getPlayer();
         if (!this.antiBotService.wasPlayerKicked(player.getName())) {
            this.management.performQuit(player);
         }

      }
   }

   private void removeUnauthorizedRecipients(AsyncPlayerChatEvent event) {
      if ((Boolean)this.settings.getProperty(RestrictionSettings.HIDE_CHAT)) {
         Set var10000 = event.getRecipients();
         ListenerService var10001 = this.listenerService;
         Objects.requireNonNull(var10001);
         var10000.removeIf(var10001::shouldCancelEvent);
         if (event.getRecipients().isEmpty()) {
            event.setCancelled(true);
         }
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerChat(AsyncPlayerChatEvent event) {
      if (!(Boolean)this.settings.getProperty(RestrictionSettings.ALLOW_CHAT)) {
         Player player = event.getPlayer();
         boolean mayPlayerSendChat = !this.listenerService.shouldCancelEvent(player) || this.permissionsManager.hasPermission(player, PlayerStatePermission.ALLOW_CHAT_BEFORE_LOGIN);
         if (mayPlayerSendChat) {
            this.removeUnauthorizedRecipients(event);
         } else {
            event.setCancelled(true);
            this.messages.send(player, MessageKey.DENIED_CHAT);
         }

      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
      String cmd = event.getMessage().split(" ")[0].toLowerCase(Locale.ROOT);
      if (!(Boolean)this.settings.getProperty(HooksSettings.USE_ESSENTIALS_MOTD) || !"/motd".equals(cmd)) {
         if (!((Set)this.settings.getProperty(RestrictionSettings.ALLOW_COMMANDS)).contains(cmd)) {
            Player player = event.getPlayer();
            if (!this.quickCommandsProtectionManager.isAllowed(player.getName())) {
               event.setCancelled(true);
               this.bukkitService.runTaskIfFolia((Entity)player, () -> {
                  player.kickPlayer(this.messages.retrieveSingle((CommandSender)player, MessageKey.QUICK_COMMAND_PROTECTION_KICK));
               });
            } else {
               if (this.listenerService.shouldCancelEvent(player)) {
                  event.setCancelled(true);
                  this.messages.send(player, MessageKey.DENIED_COMMAND);
               }

            }
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.HIGHEST
   )
   public void onPlayerMove(PlayerMoveEvent event) {
      if (!(Boolean)this.settings.getProperty(RestrictionSettings.ALLOW_UNAUTHED_MOVEMENT) || (Integer)this.settings.getProperty(RestrictionSettings.ALLOWED_MOVEMENT_RADIUS) > 0) {
         Location from = event.getFrom();
         Location to = event.getTo();
         if (to != null) {
            if (from.getX() != to.getX() || from.getZ() != to.getZ() || !(from.getY() - to.getY() >= 0.0D)) {
               Player player = event.getPlayer();
               if (this.listenerService.shouldCancelEvent(player)) {
                  if (!(Boolean)this.settings.getProperty(RestrictionSettings.ALLOW_UNAUTHED_MOVEMENT)) {
                     event.setTo(event.getFrom());
                  } else if (!(Boolean)this.settings.getProperty(RestrictionSettings.NO_TELEPORT)) {
                     Location spawn = this.spawnLoader.getSpawnLocation(player);
                     if (spawn != null && spawn.getWorld() != null) {
                        if (!player.getWorld().equals(spawn.getWorld())) {
                           TeleportUtils.teleport(player, spawn);
                        } else if (spawn.distance(player.getLocation()) > (double)(Integer)this.settings.getProperty(RestrictionSettings.ALLOWED_MOVEMENT_RADIUS)) {
                           TeleportUtils.teleport(player, spawn);
                        }
                     }

                  }
               }
            }
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.HIGHEST
   )
   public void onPlayerRespawn(PlayerRespawnEvent event) {
      if (!(Boolean)this.settings.getProperty(RestrictionSettings.NO_TELEPORT)) {
         if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
            Location spawn = this.spawnLoader.getSpawnLocation(event.getPlayer());
            if (spawn != null && spawn.getWorld() != null) {
               event.setRespawnLocation(spawn);
            }

         }
      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerInteract(PlayerInteractEvent event) {
      if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
      if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
      if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerHitPlayerEvent(EntityDamageByEntityEvent event) {
      if (this.listenerService.shouldCancelEvent((EntityEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerShear(PlayerShearEntityEvent event) {
      if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerFish(PlayerFishEvent event) {
      if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerBedEnter(PlayerBedEnterEvent event) {
      if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerEditBook(PlayerEditBookEvent event) {
      if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onSignChange(SignChangeEvent event) {
      Player player = event.getPlayer();
      if (this.listenerService.shouldCancelEvent(player)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerDropItem(PlayerDropItemEvent event) {
      if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerHeldItem(PlayerItemHeldEvent event) {
      if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setCancelled(true);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerConsumeItem(PlayerItemConsumeEvent event) {
      if (this.listenerService.shouldCancelEvent((PlayerEvent)event)) {
         event.setCancelled(true);
      }

   }

   private boolean isInventoryOpenedByApi(Inventory inventory) {
      if (inventory == null) {
         return false;
      } else if (PENDING_INVENTORIES.contains(inventory)) {
         PENDING_INVENTORIES.remove(inventory);
         return true;
      } else {
         return false;
      }
   }

   private boolean isInventoryWhitelisted(InventoryView inventory) {
      if (inventory == null) {
         return false;
      } else {
         Set<String> whitelist = (Set)this.settings.getProperty(RestrictionSettings.UNRESTRICTED_INVENTORIES);
         if (whitelist.isEmpty()) {
            return false;
         } else {
            String invName = ChatColor.stripColor(inventory.getTitle()).toLowerCase(Locale.ROOT);
            if ((Boolean)this.settings.getProperty(RestrictionSettings.STRICT_UNRESTRICTED_INVENTORIES_CHECK)) {
               return whitelist.contains(invName);
            } else {
               Iterator var4 = whitelist.iterator();

               String wl;
               do {
                  if (!var4.hasNext()) {
                     return false;
                  }

                  wl = (String)var4.next();
               } while(!invName.contains(wl));

               return true;
            }
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerInventoryOpen(InventoryOpenEvent event) {
      HumanEntity player = event.getPlayer();
      if (this.listenerService.shouldCancelEvent((Entity)player) && !this.isInventoryWhitelisted(event.getView()) && !this.isInventoryOpenedByApi(event.getInventory())) {
         event.setCancelled(true);
         BukkitService var10000 = this.bukkitService;
         Objects.requireNonNull(player);
         var10000.scheduleSyncDelayedTask(player::closeInventory, 1L);
      }

   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.LOWEST
   )
   public void onPlayerInventoryClick(InventoryClickEvent event) {
      if (this.listenerService.shouldCancelEvent((Entity)event.getWhoClicked()) && !this.isInventoryWhitelisted(event.getView())) {
         event.setCancelled(true);
      }

   }
}
