package github.nighter.smartspawner.hooks;

import com.plotsquared.core.PlotAPI;
import fr.xyness.SCS.SimpleClaimSystem;
import fr.xyness.SCS.API.SimpleClaimSystemAPI_Provider;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.hooks.bedrock.FloodgateHook;
import github.nighter.smartspawner.hooks.drops.MythicMobsHook;
import github.nighter.smartspawner.hooks.protections.api.IridiumSkyblock;
import github.nighter.smartspawner.hooks.protections.api.Lands;
import github.nighter.smartspawner.hooks.protections.api.PlotSquared;
import github.nighter.smartspawner.hooks.protections.api.SuperiorSkyblock2;
import github.nighter.smartspawner.hooks.rpg.AuraSkillsIntegration;
import java.util.logging.Level;
import lombok.Generated;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class IntegrationManager {
   private final SmartSpawner plugin;
   private boolean hasTowny = false;
   private boolean hasLands = false;
   private boolean hasWorldGuard = false;
   private boolean hasGriefPrevention = false;
   private boolean hasSuperiorSkyblock2 = false;
   private boolean hasBentoBox = false;
   private boolean hasSimpleClaimSystem = false;
   private boolean hasRedProtect = false;
   private boolean hasMinePlots = false;
   private boolean hasMythicMobs = false;
   private boolean hasIridiumSkyblock = false;
   private boolean hasPlotSquared = false;
   private boolean hasResidence = false;
   private boolean hasAuraSkills = false;
   private boolean hasFloodgate = false;
   public AuraSkillsIntegration auraSkillsIntegration;
   public FloodgateHook floodgateHook;

   public IntegrationManager(SmartSpawner plugin) {
      this.plugin = plugin;
   }

   public void initializeIntegrations() {
      this.checkProtectionPlugins();
      this.checkIntegrationPlugins();
   }

   private void checkProtectionPlugins() {
      this.hasWorldGuard = this.checkPlugin("WorldGuard", () -> {
         Plugin worldGuardPlugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
         return worldGuardPlugin != null && worldGuardPlugin.isEnabled();
      }, true);
      this.hasGriefPrevention = this.checkPlugin("GriefPrevention", () -> {
         Plugin griefPlugin = Bukkit.getPluginManager().getPlugin("GriefPrevention");
         return griefPlugin instanceof GriefPrevention;
      }, true);
      this.hasLands = this.checkPlugin("Lands", () -> {
         Plugin landsPlugin = Bukkit.getPluginManager().getPlugin("Lands");
         if (landsPlugin != null) {
            new Lands(this.plugin);
            return true;
         } else {
            return false;
         }
      }, true);
      this.hasTowny = this.checkPlugin("Towny", () -> {
         Plugin townyPlugin = Bukkit.getPluginManager().getPlugin("Towny");
         return townyPlugin != null && townyPlugin.isEnabled();
      }, true);
      this.hasSuperiorSkyblock2 = this.checkPlugin("SuperiorSkyblock2", () -> {
         Plugin superiorSkyblock2 = Bukkit.getPluginManager().getPlugin("SuperiorSkyblock2");
         if (superiorSkyblock2 != null) {
            SuperiorSkyblock2 ssb2 = new SuperiorSkyblock2();
            Bukkit.getPluginManager().registerEvents(ssb2, this.plugin);
            return true;
         } else {
            return false;
         }
      }, true);
      this.hasBentoBox = this.checkPlugin("BentoBox", () -> {
         Plugin bentoPlugin = Bukkit.getPluginManager().getPlugin("BentoBox");
         return bentoPlugin != null && bentoPlugin.isEnabled();
      }, true);
      this.hasSimpleClaimSystem = this.checkPlugin("SimpleClaimSystem", () -> {
         Plugin simpleClaimPlugin = Bukkit.getPluginManager().getPlugin("SimpleClaimSystem");
         if (simpleClaimPlugin != null && simpleClaimPlugin.isEnabled()) {
            SimpleClaimSystemAPI_Provider.initialize((SimpleClaimSystem)simpleClaimPlugin);
            return SimpleClaimSystemAPI_Provider.getAPI() != null;
         } else {
            return false;
         }
      }, true);
      this.hasRedProtect = this.checkPlugin("RedProtect", () -> {
         Plugin pRP = Bukkit.getPluginManager().getPlugin("RedProtect");
         return pRP != null && pRP.isEnabled();
      }, true);
      this.hasMinePlots = this.checkPlugin("minePlots", () -> {
         Plugin mP = Bukkit.getPluginManager().getPlugin("minePlots");
         return mP != null && mP.isEnabled();
      }, true);
      this.hasMythicMobs = this.checkPlugin("MythicMobs", () -> {
         Plugin mm = Bukkit.getPluginManager().getPlugin("MythicMobs");
         if (mm != null && mm.isEnabled()) {
            Bukkit.getPluginManager().registerEvents(new MythicMobsHook(), SmartSpawner.getInstance());
            return true;
         } else {
            return false;
         }
      }, true);
      this.hasIridiumSkyblock = this.checkPlugin("IridiumSkyblock", () -> {
         Plugin is = Bukkit.getPluginManager().getPlugin("IridiumSkyblock");
         if (is != null && is.isEnabled()) {
            IridiumSkyblock.init();
            return true;
         } else {
            return false;
         }
      }, true);
      this.hasPlotSquared = this.checkPlugin("PlotSquared", () -> {
         Plugin is = Bukkit.getPluginManager().getPlugin("PlotSquared");
         if (is != null && is.isEnabled()) {
            PlotAPI api = new PlotAPI();
            if (api == null) {
               return false;
            } else {
               PlotSquared ps = new PlotSquared();
               api.registerListener(ps);
               Bukkit.getPluginManager().registerEvents(ps, SmartSpawner.getInstance());
               return true;
            }
         } else {
            return false;
         }
      }, true);
      this.hasResidence = this.checkPlugin("Residence", () -> {
         Plugin residence = Bukkit.getPluginManager().getPlugin("Residence");
         return residence != null && residence.isEnabled();
      }, true);
   }

   private void checkIntegrationPlugins() {
      this.hasAuraSkills = this.checkPlugin("AuraSkills", () -> {
         Plugin auraSkillsPlugin = Bukkit.getPluginManager().getPlugin("AuraSkills");
         if (auraSkillsPlugin != null && auraSkillsPlugin.isEnabled()) {
            this.auraSkillsIntegration = new AuraSkillsIntegration(this.plugin);
            return true;
         } else {
            this.auraSkillsIntegration = null;
            return false;
         }
      }, true);
      this.hasFloodgate = this.checkPlugin("Floodgate", () -> {
         Plugin floodgatePlugin = Bukkit.getPluginManager().getPlugin("floodgate");
         if (floodgatePlugin != null && floodgatePlugin.isEnabled()) {
            this.floodgateHook = new FloodgateHook(this.plugin);
            return this.floodgateHook.isEnabled();
         } else {
            this.floodgateHook = null;
            return false;
         }
      }, true);
   }

   private boolean checkPlugin(String pluginName, IntegrationManager.PluginCheck checker, boolean logSuccess) {
      try {
         if (checker.check()) {
            if (logSuccess) {
               this.plugin.getLogger().info(pluginName + " integration enabled successfully!");
            }

            return true;
         }
      } catch (Exception var5) {
         this.plugin.getLogger().log(Level.WARNING, "Failed to initialize " + pluginName + " integration", var5);
      }

      return false;
   }

   public void reload() {
      if (this.auraSkillsIntegration != null) {
         this.auraSkillsIntegration.reloadConfig();
      }

   }

   @Generated
   public SmartSpawner getPlugin() {
      return this.plugin;
   }

   @Generated
   public boolean isHasTowny() {
      return this.hasTowny;
   }

   @Generated
   public boolean isHasLands() {
      return this.hasLands;
   }

   @Generated
   public boolean isHasWorldGuard() {
      return this.hasWorldGuard;
   }

   @Generated
   public boolean isHasGriefPrevention() {
      return this.hasGriefPrevention;
   }

   @Generated
   public boolean isHasSuperiorSkyblock2() {
      return this.hasSuperiorSkyblock2;
   }

   @Generated
   public boolean isHasBentoBox() {
      return this.hasBentoBox;
   }

   @Generated
   public boolean isHasSimpleClaimSystem() {
      return this.hasSimpleClaimSystem;
   }

   @Generated
   public boolean isHasRedProtect() {
      return this.hasRedProtect;
   }

   @Generated
   public boolean isHasMinePlots() {
      return this.hasMinePlots;
   }

   @Generated
   public boolean isHasMythicMobs() {
      return this.hasMythicMobs;
   }

   @Generated
   public boolean isHasIridiumSkyblock() {
      return this.hasIridiumSkyblock;
   }

   @Generated
   public boolean isHasPlotSquared() {
      return this.hasPlotSquared;
   }

   @Generated
   public boolean isHasResidence() {
      return this.hasResidence;
   }

   @Generated
   public boolean isHasAuraSkills() {
      return this.hasAuraSkills;
   }

   @Generated
   public boolean isHasFloodgate() {
      return this.hasFloodgate;
   }

   @Generated
   public AuraSkillsIntegration getAuraSkillsIntegration() {
      return this.auraSkillsIntegration;
   }

   @Generated
   public FloodgateHook getFloodgateHook() {
      return this.floodgateHook;
   }

   @FunctionalInterface
   private interface PluginCheck {
      boolean check();
   }
}
