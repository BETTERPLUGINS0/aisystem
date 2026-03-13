package tntrun.kits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import tntrun.TNTRun;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class Kits {
   private HashMap<String, Kits.Kit> kits = new HashMap();
   private File kitsconfig = new File(TNTRun.getInstance().getDataFolder(), "kits.yml");
   private FileConfiguration config;

   public Kits() {
      this.config = YamlConfiguration.loadConfiguration(this.kitsconfig);
   }

   public boolean kitExists(String name) {
      return this.kits.containsKey(name);
   }

   public HashSet<String> getKits() {
      return new HashSet(this.kits.keySet());
   }

   public void registerKit(String name, Player player) {
      if (this.kitExists(name)) {
         Messages.sendMessage(player, Messages.kitexists.replace("{KIT}", name));
      } else {
         Kits.Kit kit = new Kits.Kit(player.getInventory().getContents(), player.getActivePotionEffects());
         this.registerKit(name, kit);
         Messages.sendMessage(player, Messages.kitadd.replace("{KIT}", name));
      }
   }

   private void registerKit(String name, Kits.Kit kit) {
      this.kits.put(name, kit);
   }

   public void unregisterKit(String name, Player player) {
      if (!this.kitExists(name)) {
         Messages.sendMessage(player, Messages.kitnotexists.replace("{KIT}", name));
      } else {
         this.kits.remove(name);
         this.config.set("kits." + name, (Object)null);
         this.saveKits();
         Messages.sendMessage(player, Messages.kitdel.replace("{KIT}", name));
      }
   }

   public void giveKit(String name, Player player) {
      try {
         ((Kits.Kit)this.kits.get(name)).giveKit(player);
         Messages.sendMessage(player, Messages.playerkit.replace("{KIT}", name));
         if (Utils.debug()) {
            Logger var10000 = TNTRun.getInstance().getLogger();
            String var10001 = player.getName();
            var10000.info(var10001 + " has received kit " + name);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void loadFromConfig() {
      if (!this.kitsconfig.exists()) {
         try {
            this.kitsconfig.createNewFile();
         } catch (IOException var5) {
            var5.printStackTrace();
         }
      }

      ConfigurationSection cs = this.config.getConfigurationSection("kits");
      if (cs != null) {
         Iterator var2 = cs.getKeys(false).iterator();

         while(var2.hasNext()) {
            String name = (String)var2.next();
            Kits.Kit kit = new Kits.Kit();
            kit.loadFromConfig(this.config, "kits." + name);
            this.kits.put(name, kit);
         }
      }

   }

   public void saveToConfig() {
      Iterator var1 = this.kits.keySet().iterator();

      while(var1.hasNext()) {
         String name = (String)var1.next();
         ((Kits.Kit)this.kits.get(name)).saveToConfig(this.config, "kits." + name);
      }

      this.saveKits();
   }

   private void saveKits() {
      try {
         this.config.save(this.kitsconfig);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public void listKit(String name, Player player) {
      if (!this.kitExists(name)) {
         Messages.sendMessage(player, Messages.kitnotexists.replace("{KIT}", name));
      } else {
         Messages.sendMessage(player, "&7============" + Messages.trprefix + "============", false);
         Messages.sendMessage(player, "&7Kit Details: &a" + name, false);
         ItemStack[] var3 = ((Kits.Kit)this.kits.get(name)).items;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ItemStack is = var3[var5];
            if (is != null && is.getType() != Material.AIR) {
               StringBuilder message = new StringBuilder(200);
               message.append("&6" + is.getType().toString());
               if (is.getAmount() > 1) {
                  message.append("&7 x &c" + is.getAmount());
               }

               Messages.sendMessage(player, message.toString(), false);
            }
         }

         Iterator var8 = ((Kits.Kit)this.kits.get(name)).effects.iterator();

         while(var8.hasNext()) {
            PotionEffect pe = (PotionEffect)var8.next();
            if (pe != null) {
               Messages.sendMessage(player, "&6Potion Effect&7 : &c" + pe.getType().getKey().getKey(), false);
            }
         }

      }
   }

   public static class Kit {
      private ItemStack[] items;
      private Collection<PotionEffect> effects;

      protected Kit() {
      }

      public Kit(ItemStack[] items, Collection<PotionEffect> effects) {
         this.items = items;
         this.effects = effects;
      }

      public void giveKit(Player player) {
         player.getInventory().setContents(this.items);
         if (!this.effects.contains((Object)null)) {
            player.addPotionEffects(this.effects);
         }

      }

      public void loadFromConfig(FileConfiguration config, String path) {
         this.items = (ItemStack[])config.getList(path + ".items").toArray(new ItemStack[1]);
         this.effects = Arrays.asList((PotionEffect[])config.getList(path + ".effects").toArray(new PotionEffect[1]));
      }

      public void saveToConfig(FileConfiguration config, String path) {
         config.set(path + ".items", Arrays.asList(this.items));
         config.set(path + ".effects", new ArrayList(this.effects));
      }
   }
}
