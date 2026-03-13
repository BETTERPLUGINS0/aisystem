package github.nighter.smartspawner.hooks.bedrock;

import github.nighter.smartspawner.SmartSpawner;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.geysermc.floodgate.api.FloodgateApi;

public class FloodgateHook {
   private final SmartSpawner plugin;
   private FloodgateApi floodgateApi;
   private boolean enabled = false;

   public FloodgateHook(SmartSpawner plugin) {
      this.plugin = plugin;
      this.initialize();
   }

   private void initialize() {
      try {
         if (this.plugin.getServer().getPluginManager().getPlugin("floodgate") == null) {
            this.plugin.debug("Floodgate plugin not found");
            return;
         }

         this.floodgateApi = FloodgateApi.getInstance();
         if (this.floodgateApi == null) {
            this.plugin.getLogger().warning("Failed to get FloodgateApi instance");
            return;
         }

         this.enabled = true;
         this.plugin.getLogger().info("Floodgate integration initialized successfully!");
      } catch (NullPointerException | NoClassDefFoundError var2) {
         this.plugin.debug("Floodgate API not available: " + var2.getMessage());
         this.enabled = false;
      } catch (Exception var3) {
         this.plugin.getLogger().warning("Error initializing Floodgate integration: " + var3.getMessage());
         this.enabled = false;
      }

   }

   public boolean isEnabled() {
      return this.enabled && this.floodgateApi != null;
   }

   public boolean isBedrockPlayer(Player player) {
      if (this.isEnabled() && player != null) {
         try {
            return this.floodgateApi.isFloodgatePlayer(player.getUniqueId());
         } catch (Exception var3) {
            this.plugin.debug("Error checking if player is Bedrock: " + var3.getMessage());
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean isBedrockPlayer(UUID uuid) {
      if (this.isEnabled() && uuid != null) {
         try {
            return this.floodgateApi.isFloodgatePlayer(uuid);
         } catch (Exception var3) {
            this.plugin.debug("Error checking if UUID is Bedrock: " + var3.getMessage());
            return false;
         }
      } else {
         return false;
      }
   }

   public FloodgateApi getApi() {
      return this.floodgateApi;
   }
}
