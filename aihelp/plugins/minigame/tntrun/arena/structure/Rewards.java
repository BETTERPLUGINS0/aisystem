package tntrun.arena.structure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.IntStream;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tntrun.TNTRun;
import tntrun.messages.Messages;
import tntrun.utils.Utils;

public class Rewards {
   private Economy econ = TNTRun.getInstance().getVaultHandler().getEconomy();
   private Map<Integer, Integer> moneyreward = new HashMap();
   private Map<Integer, Integer> xpreward = new HashMap();
   private Map<Integer, List<String>> commandrewards = new HashMap();
   private Map<Integer, List<ItemStack>> materialrewards = new HashMap();
   private Map<Integer, Integer> minplayersrequired = new HashMap();
   private int startingPlayers;
   private int maxplaces;
   private int index;
   private String path;

   public List<ItemStack> getMaterialReward(int place) {
      return (List)this.materialrewards.get(place);
   }

   public int getMoneyReward(int place) {
      return (Integer)this.moneyreward.getOrDefault(place, 0);
   }

   public List<String> getCommandRewards(int place) {
      return (List)this.commandrewards.getOrDefault(place, Collections.emptyList());
   }

   public int getXPReward(int place) {
      return (Integer)this.xpreward.getOrDefault(place, 0);
   }

   public int getMinPlayersRequired(int place) {
      return (Integer)this.minplayersrequired.getOrDefault(place, 0);
   }

   private boolean isActiveReward(int place) {
      if (Utils.debug()) {
         Bukkit.getLogger().info("[TNTRun_reloaded] place = " + place + ", maxplaces = " + this.maxplaces + ", starters = " + this.startingPlayers + ", min = " + this.getMinPlayersRequired(place));
      }

      return place <= this.maxplaces && this.startingPlayers >= this.getMinPlayersRequired(place);
   }

   public void setMaterialReward(String item, String amount, String label, boolean isFirstItem, int place) {
      if (isFirstItem) {
         this.materialrewards.remove(place);
      }

      if (Utils.debug()) {
         Bukkit.getLogger().info("[TNTRun_reloaded] reward(" + place + ") = " + this.materialrewards.toString());
      }

      ItemStack reward = new ItemStack(Material.getMaterial(item), Integer.valueOf(amount));
      if (!label.isEmpty()) {
         this.setMaterialDisplayName(reward, label);
      }

      ((List)this.materialrewards.computeIfAbsent(place, (k) -> {
         return new ArrayList();
      })).add(reward);
      this.maxplaces = Math.max(this.maxplaces, place);
      if (Utils.debug()) {
         Bukkit.getLogger().info("[TNTRun_reloaded] reward(" + place + ") = " + this.materialrewards.toString());
      }

   }

   private void setMaterialDisplayName(ItemStack is, String label) {
      ItemMeta im = is.getItemMeta();
      im.setDisplayName(label);
      is.setItemMeta(im);
   }

   public void setMoneyReward(int money, int place) {
      this.moneyreward.put(place, money);
      this.maxplaces = Math.max(this.maxplaces, place);
   }

   public void setCommandReward(String cmdreward, boolean isFirstCmd, int place) {
      if (isFirstCmd) {
         this.commandrewards.remove(place);
      }

      if (Utils.debug()) {
         Bukkit.getLogger().info("[TNTRun_reloaded] reward(" + place + ") = " + this.commandrewards.toString());
      }

      ((List)this.commandrewards.computeIfAbsent(place, (k) -> {
         return new ArrayList();
      })).add(cmdreward);
      this.maxplaces = Math.max(this.maxplaces, place);
      if (Utils.debug()) {
         Bukkit.getLogger().info("[TNTRun_reloaded] reward(" + place + ") = " + this.commandrewards.toString());
      }

   }

   public void setCommandRewards(List<String> cmdreward, int place) {
      this.commandrewards.put(place, cmdreward);
   }

   public void setXPReward(int xprwd, int place) {
      this.xpreward.put(place, xprwd);
      this.maxplaces = Math.max(this.maxplaces, place);
   }

   public void deleteMaterialReward(int place) {
      this.materialrewards.remove(place);
   }

   public void deleteCommandReward(int place) {
      this.commandrewards.remove(place);
   }

   public void setMinPlayersRequired(int min, int place) {
      this.minplayersrequired.put(place, min);
   }

   public void rewardPlayer(Player player, int place) {
      if (this.isActiveReward(place)) {
         StringJoiner rewardmessage = new StringJoiner(", ");
         ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
         if (Utils.debug()) {
            Bukkit.getLogger().info("[TNTRun_reloaded] Checking rewards for " + player.getName());
         }

         if (this.getMaterialReward(place) != null) {
            this.getMaterialReward(place).forEach((reward) -> {
               if (player.getInventory().firstEmpty() != -1) {
                  player.getInventory().addItem(new ItemStack[]{reward});
                  player.updateInventory();
               } else {
                  player.getWorld().dropItemNaturally(player.getLocation(), reward);
               }

               int var10001 = reward.getAmount();
               rewardmessage.add(var10001 + " x " + reward.getType().toString());
            });
         }

         int moneyreward = this.getMoneyReward(place);
         if (moneyreward != 0) {
            OfflinePlayer offplayer = player.getPlayer();
            this.rewardMoney(offplayer, moneyreward);
            rewardmessage.add(Utils.getFormattedCurrency(String.valueOf(moneyreward)));
         }

         int xpreward = this.getXPReward(place);
         if (xpreward > 0) {
            player.giveExp(xpreward);
            rewardmessage.add(xpreward + " XP");
         }

         Iterator var7 = this.getCommandRewards(place).iterator();

         String var10001;
         while(var7.hasNext()) {
            String commandreward = (String)var7.next();
            if (commandreward != null && commandreward.length() != 0) {
               Bukkit.getServer().dispatchCommand(console, commandreward.replace("%PLAYER%", player.getName()));
               var10001 = String.valueOf(ChatColor.GOLD);
               console.sendMessage("[TNTRun_reloaded] Command " + var10001 + commandreward + String.valueOf(ChatColor.WHITE) + " has been executed for " + String.valueOf(ChatColor.AQUA) + player.getName());
            }
         }

         if (!rewardmessage.toString().isEmpty()) {
            var10001 = String.valueOf(ChatColor.AQUA);
            console.sendMessage("[TNTRun_reloaded] " + var10001 + player.getName() + String.valueOf(ChatColor.WHITE) + " has been rewarded " + String.valueOf(ChatColor.GOLD) + rewardmessage.toString());
            Messages.sendMessage(player, Messages.playerrewardmessage.replace("{REWARD}", rewardmessage.toString()));
         }

      }
   }

   private void rewardMoney(OfflinePlayer offplayer, int money) {
      if (this.econ != null) {
         this.econ.depositPlayer(offplayer, (double)money);
      }

   }

   public void setStartingPlayers(int starters) {
      this.startingPlayers = starters;
   }

   public void saveToConfig(FileConfiguration config) {
      this.path = "reward";
      IntStream.range(1, this.maxplaces + 1).forEach((index) -> {
         config.set(this.path + ".minPlayers", this.getMinPlayersRequired(index));
         config.set(this.path + ".money", this.getMoneyReward(index));
         config.set(this.path + ".xp", this.getXPReward(index));
         config.set(this.path + ".command", this.getCommandRewards(index));
         if (this.getMaterialReward(index) != null) {
            this.getMaterialReward(index).forEach((is) -> {
               config.set(this.path + ".material." + is.getType().toString() + ".amount", is.getAmount());
            });
         }

         this.path = "places." + (index + 1);
      });
   }

   public void loadFromConfig(FileConfiguration config) {
      if (config.isConfigurationSection("reward")) {
         this.index = 1;
         this.getPlacesFromFile(config).stream().forEach((path) -> {
            this.setMinPlayersRequired(config.getInt(path + ".minPlayers"), this.index);
            this.setMoneyReward(config.getInt(path + ".money"), this.index);
            this.setXPReward(config.getInt(path + ".xp"), this.index);
            this.setCommandRewards(config.getStringList(path + ".command"), this.index);
            if (config.getConfigurationSection(path + ".material") != null) {
               Set<String> materials = config.getConfigurationSection(path + ".material").getKeys(false);
               Iterator var4 = materials.iterator();

               while(var4.hasNext()) {
                  String material = (String)var4.next();
                  if (this.isValidReward(material, config.getInt(path + ".material." + material + ".amount"))) {
                     ItemStack is = new ItemStack(Material.getMaterial(material), config.getInt(path + ".material." + material + ".amount"));
                     ((List)this.materialrewards.computeIfAbsent(this.index, (k) -> {
                        return new ArrayList();
                     })).add(is);
                  }
               }
            }

            ++this.index;
         });
         this.maxplaces = this.index - 1;
      }
   }

   private List<String> getPlacesFromFile(FileConfiguration config) {
      List<String> paths = new ArrayList(List.of("reward"));
      if (!config.isConfigurationSection("places")) {
         return paths;
      } else {
         Iterator var3 = config.getConfigurationSection("places").getKeys(false).iterator();

         while(var3.hasNext()) {
            String key = (String)var3.next();
            if (key.equalsIgnoreCase("second")) {
               paths.add("places.2");
            } else if (key.equalsIgnoreCase("third")) {
               paths.add("places.3");
            } else {
               paths.add("places." + key);
            }
         }

         if (Utils.debug()) {
            Bukkit.getLogger().info("[TNTRun_reloaded] reward paths = " + paths.toString());
         }

         return paths;
      }
   }

   private boolean isValidReward(String materialreward, int materialamount) {
      return Material.getMaterial(materialreward) != null && materialamount > 0;
   }

   public void listRewards(Player player, String arenaName) {
      Messages.sendMessage(player, Messages.rewardshead.replace("{ARENA}", arenaName), false);
      IntStream.range(1, this.maxplaces + 1).forEach((i) -> {
         StringBuilder sb = new StringBuilder(200);
         String var10001;
         if (this.getXPReward(i) != 0) {
            var10001 = Messages.playerrewardxp;
            sb.append("\n   " + var10001 + this.getXPReward(i));
         }

         if (this.getMoneyReward(i) != 0) {
            var10001 = Messages.playerrewardmoney;
            sb.append("\n   " + var10001 + this.getMoneyReward(i));
         }

         if (this.getCommandRewards(i) != null) {
            this.getCommandRewards(i).forEach((reward) -> {
               sb.append("\n   " + Messages.playerrewardcommand + reward);
            });
         }

         if (this.getMaterialReward(i) != null) {
            sb.append("\n   " + Messages.playerrewardmaterial);
            this.getMaterialReward(i).forEach((reward) -> {
               String var10001 = String.valueOf(reward.getAmount());
               sb.append(var10001 + " x " + reward.getType().toString() + ", ");
            });
            sb.setLength(sb.length() - 2);
         }

         if (sb.length() != 0) {
            sb.insert(0, Messages.rewardlistposition.replace("{POS}", String.valueOf(i)));
            Messages.sendMessage(player, sb.toString(), false);
         }

      });
   }
}
