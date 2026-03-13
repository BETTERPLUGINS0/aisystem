package github.nighter.smartspawner.hooks.rpg;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.skill.Skills;
import dev.aurelium.auraskills.api.user.SkillsUser;
import github.nighter.smartspawner.SmartSpawner;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class AuraSkillsIntegration {
   private final SmartSpawner plugin;
   private final Map<EntityType, AuraSkillsIntegration.SkillConfig> entitySkillMap;
   private FileConfiguration config;
   private File configFile;
   private boolean enabled = false;
   private AuraSkillsApi auraSkillsApi;

   public AuraSkillsIntegration(SmartSpawner plugin) {
      this.plugin = plugin;
      this.entitySkillMap = new HashMap();
      if (this.initializeAuraSkills()) {
         this.loadConfig();
         this.loadEntityMappings();
      }

   }

   private boolean initializeAuraSkills() {
      try {
         if (this.plugin.getServer().getPluginManager().getPlugin("AuraSkills") == null) {
            this.plugin.debug("AuraSkills plugin not found");
            return false;
         } else {
            this.auraSkillsApi = AuraSkillsApi.get();
            if (this.auraSkillsApi == null) {
               this.plugin.getLogger().warning("Failed to get AuraSkills API instance");
               return false;
            } else {
               this.plugin.getLogger().info("AuraSkills integration initialized successfully!");
               return true;
            }
         }
      } catch (Exception | NoClassDefFoundError var2) {
         this.plugin.debug("AuraSkills not available: " + var2.getMessage());
         return false;
      }
   }

   private void loadConfig() {
      this.configFile = new File(this.plugin.getDataFolder(), "auraskills.yml");
      if (!this.configFile.exists()) {
         this.plugin.saveResource("auraskills.yml", false);
      }

      this.config = YamlConfiguration.loadConfiguration(this.configFile);
      this.enabled = this.config.getBoolean("enabled", true);
      this.plugin.debug("AuraSkills config loaded - Enabled: " + this.enabled);
   }

   private void loadEntityMappings() {
      if (this.enabled) {
         this.entitySkillMap.clear();
         ConfigurationSection entitySection = this.config.getConfigurationSection("entity_skills");
         if (entitySection == null) {
            this.plugin.getLogger().warning("No entity_skills section found in auraskills.yml");
         } else {
            Iterator var2 = entitySection.getKeys(false).iterator();

            while(var2.hasNext()) {
               String entityName = (String)var2.next();

               try {
                  EntityType entityType = EntityType.valueOf(entityName.toUpperCase());
                  ConfigurationSection entityConfig = entitySection.getConfigurationSection(entityName);
                  if (entityConfig != null) {
                     String skillName = entityConfig.getString("skill", "FIGHTING");
                     double ratio = entityConfig.getDouble("ratio", 0.5D);
                     AuraSkillsIntegration.SkillConfig skillConfig = new AuraSkillsIntegration.SkillConfig(skillName, ratio);
                     this.entitySkillMap.put(entityType, skillConfig);
                     this.plugin.debug("Mapped " + String.valueOf(entityType) + " to skill " + skillName + " with ratio " + ratio);
                  }
               } catch (IllegalArgumentException var10) {
                  this.plugin.getLogger().warning("Invalid entity type in auraskills.yml: " + entityName);
               }
            }

            this.plugin.getLogger().info("Loaded " + this.entitySkillMap.size() + " AuraSkills entity mappings");
         }
      }
   }

   public boolean isEnabled() {
      return this.enabled && this.auraSkillsApi != null;
   }

   public void giveSkillXp(Player player, EntityType entityType, int spawnerExp) {
      if (this.isEnabled()) {
         AuraSkillsIntegration.SkillConfig skillConfig = (AuraSkillsIntegration.SkillConfig)this.entitySkillMap.get(entityType);
         if (skillConfig == null) {
            this.plugin.debug("No skill mapping found for entity: " + String.valueOf(entityType));
         } else {
            try {
               SkillsUser user = this.auraSkillsApi.getUser(player.getUniqueId());
               if (user == null) {
                  this.plugin.debug("Could not get SkillsUser for player: " + player.getName());
                  return;
               }

               double skillXp = (double)spawnerExp * skillConfig.ratio();
               String var8 = skillConfig.skillName().toUpperCase();
               byte var9 = -1;
               switch(var8.hashCode()) {
               case -2032068064:
                  if (var8.equals("DEFENSE")) {
                     var9 = 7;
                  }
                  break;
               case -2020709296:
                  if (var8.equals("MINING")) {
                     var9 = 2;
                  }
                  break;
               case -2001589015:
                  if (var8.equals("ENCHANTING")) {
                     var9 = 11;
                  }
                  break;
               case -1307109217:
                  if (var8.equals("SORCERY")) {
                     var9 = 12;
                  }
                  break;
               case -360261684:
                  if (var8.equals("FARMING")) {
                     var9 = 1;
                  }
                  break;
               case -339379163:
                  if (var8.equals("AGILITY")) {
                     var9 = 9;
                  }
                  break;
               case -201897759:
                  if (var8.equals("ALCHEMY")) {
                     var9 = 10;
                  }
                  break;
               case -130453910:
                  if (var8.equals("FISHING")) {
                     var9 = 4;
                  }
                  break;
               case -92330542:
                  if (var8.equals("FIGHTING")) {
                     var9 = 0;
                  }
                  break;
               case -30122698:
                  if (var8.equals("ARCHERY")) {
                     var9 = 6;
                  }
                  break;
               case 40367684:
                  if (var8.equals("FORGING")) {
                     var9 = 14;
                  }
                  break;
               case 70350800:
                  if (var8.equals("EXCAVATION")) {
                     var9 = 5;
                  }
                  break;
               case 1071874535:
                  if (var8.equals("ENDURANCE")) {
                     var9 = 8;
                  }
                  break;
               case 1245792979:
                  if (var8.equals("FORAGING")) {
                     var9 = 3;
                  }
                  break;
               case 1513532634:
                  if (var8.equals("HEALING")) {
                     var9 = 13;
                  }
               }

               switch(var9) {
               case 0:
                  user.addSkillXp(Skills.FIGHTING, skillXp);
                  break;
               case 1:
                  user.addSkillXp(Skills.FARMING, skillXp);
                  break;
               case 2:
                  user.addSkillXp(Skills.MINING, skillXp);
                  break;
               case 3:
                  user.addSkillXp(Skills.FORAGING, skillXp);
                  break;
               case 4:
                  user.addSkillXp(Skills.FISHING, skillXp);
                  break;
               case 5:
                  user.addSkillXp(Skills.EXCAVATION, skillXp);
                  break;
               case 6:
                  user.addSkillXp(Skills.ARCHERY, skillXp);
                  break;
               case 7:
                  user.addSkillXp(Skills.DEFENSE, skillXp);
                  break;
               case 8:
                  user.addSkillXp(Skills.ENDURANCE, skillXp);
                  break;
               case 9:
                  user.addSkillXp(Skills.AGILITY, skillXp);
                  break;
               case 10:
                  user.addSkillXp(Skills.ALCHEMY, skillXp);
                  break;
               case 11:
                  user.addSkillXp(Skills.ENCHANTING, skillXp);
                  break;
               case 12:
                  user.addSkillXp(Skills.SORCERY, skillXp);
                  break;
               case 13:
                  user.addSkillXp(Skills.HEALING, skillXp);
                  break;
               case 14:
                  user.addSkillXp(Skills.FORGING, skillXp);
                  break;
               default:
                  this.plugin.getLogger().warning("Unknown skill type: " + skillConfig.skillName());
                  return;
               }

               if (this.config.getBoolean("debug", false)) {
                  this.plugin.getLogger().info("Gave " + skillXp + " " + skillConfig.skillName() + " XP to " + player.getName() + " from " + String.valueOf(entityType) + " spawner");
               }
            } catch (Exception var10) {
               this.plugin.getLogger().log(Level.WARNING, "Error giving AuraSkills XP to player " + player.getName(), var10);
            }

         }
      }
   }

   public void reloadConfig() {
      this.loadConfig();
      this.loadEntityMappings();
   }

   public void saveConfig() {
      try {
         this.config.save(this.configFile);
      } catch (IOException var2) {
         this.plugin.getLogger().log(Level.SEVERE, "Could not save auraskills.yml", var2);
      }

   }

   private static record SkillConfig(String skillName, double ratio) {
      private SkillConfig(String skillName, double ratio) {
         this.skillName = skillName;
         this.ratio = ratio;
      }

      public String skillName() {
         return this.skillName;
      }

      public double ratio() {
         return this.ratio;
      }
   }
}
