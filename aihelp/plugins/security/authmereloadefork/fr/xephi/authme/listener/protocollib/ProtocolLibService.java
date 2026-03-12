package fr.xephi.authme.listener.protocollib;

import fr.xephi.authme.AuthMe;
import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.data.auth.PlayerCache;
import fr.xephi.authme.datasource.DataSource;
import fr.xephi.authme.initialization.SettingsDependent;
import fr.xephi.authme.libs.ch.jalu.injector.annotations.NoFieldScan;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.output.ConsoleLoggerFactory;
import fr.xephi.authme.service.BukkitService;
import fr.xephi.authme.settings.Settings;
import fr.xephi.authme.settings.properties.PluginSettings;
import fr.xephi.authme.settings.properties.RestrictionSettings;
import fr.xephi.authme.util.Utils;
import java.util.Iterator;
import org.bukkit.entity.Player;

@NoFieldScan
public class ProtocolLibService implements SettingsDependent {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(ProtocolLibService.class);
   private InventoryPacketAdapter inventoryPacketAdapter;
   private TabCompletePacketAdapter tabCompletePacketAdapter;
   private I18NGetLocalePacketAdapter i18nGetLocalePacketAdapter;
   private boolean protectInvBeforeLogin;
   private boolean denyTabCompleteBeforeLogin;
   private boolean i18nMessagesSending;
   private boolean isEnabled;
   private final AuthMe plugin;
   private final BukkitService bukkitService;
   private final PlayerCache playerCache;
   private final DataSource dataSource;

   @Inject
   ProtocolLibService(AuthMe plugin, Settings settings, BukkitService bukkitService, PlayerCache playerCache, DataSource dataSource) {
      this.plugin = plugin;
      this.bukkitService = bukkitService;
      this.playerCache = playerCache;
      this.dataSource = dataSource;
      this.reload(settings);
   }

   public void setup() {
      if (!this.plugin.getServer().getPluginManager().isPluginEnabled("ProtocolLib")) {
         if (this.protectInvBeforeLogin) {
            this.logger.warning("WARNING! The protectInventory feature requires ProtocolLib! Disabling it...");
         }

         if (this.denyTabCompleteBeforeLogin) {
            this.logger.warning("WARNING! The denyTabComplete feature requires ProtocolLib! Disabling it...");
         }

         if (this.i18nMessagesSending) {
            this.logger.warning("WARNING! The i18n Messages feature requires ProtocolLib on lower version (< 1.15.2)! Disabling it...");
         }

         this.isEnabled = false;
      } else {
         if (this.protectInvBeforeLogin) {
            if (this.inventoryPacketAdapter == null) {
               this.inventoryPacketAdapter = new InventoryPacketAdapter(this.plugin, this.playerCache, this.dataSource);
               this.inventoryPacketAdapter.register(this.bukkitService);
            }
         } else if (this.inventoryPacketAdapter != null) {
            this.inventoryPacketAdapter.unregister();
            this.inventoryPacketAdapter = null;
         }

         if (this.denyTabCompleteBeforeLogin) {
            if (this.tabCompletePacketAdapter == null) {
               this.tabCompletePacketAdapter = new TabCompletePacketAdapter(this.plugin, this.playerCache);
               this.tabCompletePacketAdapter.register();
            }
         } else if (this.tabCompletePacketAdapter != null) {
            this.tabCompletePacketAdapter.unregister();
            this.tabCompletePacketAdapter = null;
         }

         if (this.i18nMessagesSending) {
            if (this.i18nGetLocalePacketAdapter == null) {
               this.i18nGetLocalePacketAdapter = new I18NGetLocalePacketAdapter(this.plugin);
               this.i18nGetLocalePacketAdapter.register();
            }
         } else if (this.i18nGetLocalePacketAdapter != null) {
            this.i18nGetLocalePacketAdapter.unregister();
            this.i18nGetLocalePacketAdapter = null;
         }

         this.isEnabled = true;
      }
   }

   public void disable() {
      this.isEnabled = false;
      if (this.inventoryPacketAdapter != null) {
         this.inventoryPacketAdapter.unregister();
         this.inventoryPacketAdapter = null;
      }

      if (this.tabCompletePacketAdapter != null) {
         this.tabCompletePacketAdapter.unregister();
         this.tabCompletePacketAdapter = null;
      }

      if (this.i18nGetLocalePacketAdapter != null) {
         this.i18nGetLocalePacketAdapter.unregister();
         this.i18nGetLocalePacketAdapter = null;
      }

   }

   public void sendBlankInventoryPacket(Player player) {
      if (this.isEnabled && this.inventoryPacketAdapter != null) {
         this.inventoryPacketAdapter.sendBlankInventoryPacket(player);
      }

   }

   public void reload(Settings settings) {
      boolean oldProtectInventory = this.protectInvBeforeLogin;
      this.protectInvBeforeLogin = (Boolean)settings.getProperty(RestrictionSettings.PROTECT_INVENTORY_BEFORE_LOGIN);
      this.denyTabCompleteBeforeLogin = (Boolean)settings.getProperty(RestrictionSettings.DENY_TABCOMPLETE_BEFORE_LOGIN);
      this.i18nMessagesSending = (Boolean)settings.getProperty(PluginSettings.I18N_MESSAGES) && Utils.MAJOR_VERSION <= 15;
      if (oldProtectInventory && !this.protectInvBeforeLogin && this.inventoryPacketAdapter != null) {
         this.inventoryPacketAdapter.unregister();
         Iterator var3 = this.bukkitService.getOnlinePlayers().iterator();

         while(var3.hasNext()) {
            Player onlinePlayer = (Player)var3.next();
            if (!this.playerCache.isAuthenticated(onlinePlayer.getName())) {
               onlinePlayer.updateInventory();
            }
         }
      }

      this.setup();
   }
}
