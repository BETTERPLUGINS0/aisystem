package tntrun.datahandler;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import tntrun.TNTRun;

public class PlayerDataStore {
   private Map<String, ItemStack[]> plinv = new HashMap();
   private Map<String, ItemStack[]> plarmor = new HashMap();
   private Map<String, Collection<PotionEffect>> pleffects = new HashMap();
   private Map<String, Location> plloc = new HashMap();
   private Map<String, Integer> plhunger = new HashMap();
   private Map<String, GameMode> plgamemode = new HashMap();
   private Map<String, Integer> pllevel = new HashMap();
   private Map<String, Double> plhealth = new HashMap();
   private Map<String, Boolean> plflight = new HashMap();
   private File file;
   private final TNTRun plugin;

   public PlayerDataStore(TNTRun plugin) {
      this.plugin = plugin;
      this.file = new File(plugin.getDataFolder(), "players.yml");
      if (!this.file.exists()) {
         try {
            this.file.createNewFile();
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

   }

   public void storePlayerInventory(Player player) {
      PlayerInventory pinv = player.getInventory();
      this.plinv.put(player.getName(), pinv.getContents());
      pinv.clear();
   }

   public void storePlayerFlight(Player player) {
      this.plflight.put(player.getName(), player.getAllowFlight());
   }

   public void storePlayerArmor(Player player) {
      PlayerInventory pinv = player.getInventory();
      this.plarmor.put(player.getName(), pinv.getArmorContents());
      pinv.setArmorContents((ItemStack[])null);
   }

   public void storePlayerPotionEffects(Player player) {
      Collection<PotionEffect> peff = player.getActivePotionEffects();
      this.pleffects.put(player.getName(), peff);
      Iterator var3 = peff.iterator();

      while(var3.hasNext()) {
         PotionEffect peffect = (PotionEffect)var3.next();
         player.removePotionEffect(peffect.getType());
      }

   }

   public void storePlayerLocation(Player player) {
      this.plloc.put(player.getName(), player.getLocation());
   }

   public void storePlayerHunger(Player player) {
      this.plhunger.put(player.getName(), player.getFoodLevel());
      if (this.plugin.getConfig().getBoolean("onjoin.fillhunger")) {
         player.setFoodLevel(20);
      }

   }

   public void storePlayerGameMode(Player player) {
      this.plgamemode.put(player.getName(), player.getGameMode());
      player.setGameMode(this.getGameMode());
   }

   public void storePlayerLevel(Player player) {
      this.pllevel.put(player.getName(), player.getLevel());
      player.setLevel(0);
   }

   public void storePlayerHealth(Player player) {
      this.plhealth.put(player.getName(), player.getHealth());
      if (this.plugin.getConfig().getBoolean("onjoin.fillhealth")) {
         player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getValue());
      }

   }

   public void restorePlayerInventory(Player player) {
      player.getInventory().setContents((ItemStack[])this.plinv.remove(player.getName()));
   }

   public void restorePlayerArmor(Player player) {
      player.getInventory().setArmorContents((ItemStack[])this.plarmor.remove(player.getName()));
   }

   public void restorePlayerPotionEffects(Player player) {
      player.addPotionEffects((Collection)this.pleffects.remove(player.getName()));
   }

   public void restorePlayerLocation(Player player) {
      player.teleport((Location)this.plloc.remove(player.getName()));
   }

   public void restorePlayerFlight(Player player) {
      player.setAllowFlight((Boolean)this.plflight.get(player.getName()));
   }

   public void clearPlayerLocation(Player player) {
      this.plloc.remove(player.getName());
   }

   public void restorePlayerHunger(Player player) {
      player.setFoodLevel((Integer)this.plhunger.remove(player.getName()));
   }

   public void restorePlayerGameMode(Player player) {
      player.setGameMode((GameMode)this.plgamemode.remove(player.getName()));
   }

   public void restorePlayerLevel(Player player) {
      player.setLevel((Integer)this.pllevel.remove(player.getName()));
   }

   public void restorePlayerHealth(Player player) {
      player.setHealth(Math.min(player.getAttribute(Attribute.MAX_HEALTH).getValue(), (Double)this.plhealth.remove(player.getName())));
   }

   public void saveDoubleJumpsToFile(OfflinePlayer player, int amount) {
      String uuid = this.plugin.useUuid() ? player.getUniqueId().toString() : player.getName();
      this.saveConfigFile(uuid, ".doublejumps", amount);
   }

   public void saveDoubleJumpsToFile(String name, int amount) {
      this.saveConfigFile(name, ".doublejumps", amount);
   }

   public void setWinStreak(OfflinePlayer player, int amount) {
      String uuid = this.plugin.useUuid() ? player.getUniqueId().toString() : player.getName();
      if (this.plugin.isFile()) {
         this.saveConfigFile(uuid, ".winstreak", amount);
      } else {
         this.plugin.getStats().addStreakToDB(player, amount);
      }
   }

   private void saveConfigFile(String uuid, String path, int amount) {
      FileConfiguration config = YamlConfiguration.loadConfiguration(this.file);
      if (amount == 0) {
         config.set(uuid + path, (Object)null);
         ConfigurationSection section = config.getConfigurationSection(uuid);
         if (section != null && section.getKeys(false).isEmpty()) {
            config.set(uuid, (Object)null);
         }
      } else {
         config.set(uuid + path, amount);
      }

      try {
         config.save(this.file);
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }

   public int getDoubleJumpsFromFile(OfflinePlayer player) {
      String uuid = this.plugin.useUuid() ? player.getUniqueId().toString() : player.getName();
      return this.getDoubleJumpsFromFile(uuid);
   }

   public int getDoubleJumpsFromFile(String name) {
      FileConfiguration config = YamlConfiguration.loadConfiguration(this.file);
      return config.getInt(name + ".doublejumps", 0);
   }

   public int getWinStreak(OfflinePlayer player) {
      String uuid = this.plugin.useUuid() ? player.getUniqueId().toString() : player.getName();
      return this.plugin.isFile() ? this.getWinStreakFromFile(uuid) : this.getWinStreakFromDB(uuid);
   }

   private int getWinStreakFromFile(String name) {
      FileConfiguration config = YamlConfiguration.loadConfiguration(this.file);
      return config.getInt(name + ".winstreak", 0);
   }

   private int getWinStreakFromDB(String name) {
      return this.plugin.getStats().getStreak(name);
   }

   public boolean hasStoredDoubleJumps(Player player) {
      return this.getDoubleJumpsFromFile((OfflinePlayer)player) > 0;
   }

   private GameMode getGameMode() {
      String gamemode = this.plugin.getConfig().getString("gamemode", "SURVIVAL");
      if (!gamemode.equalsIgnoreCase("ADVENTURE")) {
         gamemode = "SURVIVAL";
      }

      return GameMode.valueOf(gamemode.toUpperCase());
   }
}
