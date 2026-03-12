package me.gypopo.economyshopgui.events;

import java.util.Iterator;
import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.ItemBuilder;
import me.gypopo.economyshopgui.util.XMaterial;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class SpawnerBreakEvent implements Listener {
   private final boolean self;
   private final EconomyShopGUI plugin;
   private final List<ItemStack> allowedTools;
   private final boolean dropEXP;
   private final boolean interact;

   public SpawnerBreakEvent(EconomyShopGUI plugin) {
      this.plugin = plugin;
      this.allowedTools = plugin.createItem.getAllowedTools();
      this.dropEXP = ConfigManager.getConfig().getBoolean("player-placed-spawners-drop-exp");
      this.self = ConfigManager.getConfig().getBoolean("only-mine-plugin-spawners");
      this.interact = ConfigManager.getConfig().getBoolean("allow-spawner-type-change");
   }

   @EventHandler(
      priority = EventPriority.HIGHEST
   )
   public void onBlockBreak(BlockBreakEvent e) {
      if (!e.isCancelled()) {
         if (e.getBlock().getType() == XMaterial.SPAWNER.parseMaterial()) {
            if (this.plugin.getSpawnerManager().getProvider().getProviderName().equalsIgnoreCase("DEFAULT")) {
               Block b = e.getBlock();
               Player player = e.getPlayer();
               CreatureSpawner spawnertype = (CreatureSpawner)b.getState();
               if (!this.self || this.isSelf(spawnertype)) {
                  if (this.isAllowedTool(this.plugin.versionHandler.getItemInHand(player))) {
                     if (!this.dropEXP) {
                        e.setExpToDrop(0);
                     }

                     this.giveSpawner(player, spawnertype.getSpawnedType());
                  } else {
                     SendMessage.logDebugMessage("Player " + player.getName() + " tried to mine a spawner with a not allowed spawner-break-tool: " + this.plugin.versionHandler.getItemInHand(player).toString());
                  }

               }
            }
         }
      }
   }

   private boolean isAllowedTool(ItemStack itemInHand) {
      Iterator var2 = this.allowedTools.iterator();

      ItemStack allowedTool;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         allowedTool = (ItemStack)var2.next();
      } while(!itemInHand.getType().equals(allowedTool.getType()) || !this.hasEnchants(allowedTool, itemInHand));

      return true;
   }

   private boolean isSelf(CreatureSpawner spawner) {
      return this.plugin.version >= 114 && spawner.getPersistentDataContainer().has(new NamespacedKey(this.plugin, "SpawnerType"), PersistentDataType.STRING);
   }

   private boolean hasEnchants(ItemStack allowedTool, ItemStack itemInHand) {
      Iterator var3 = allowedTool.getEnchantments().keySet().iterator();

      Enchantment ench;
      do {
         if (!var3.hasNext()) {
            return true;
         }

         ench = (Enchantment)var3.next();
      } while(itemInHand.getEnchantmentLevel(ench) >= allowedTool.getEnchantmentLevel(ench));

      return false;
   }

   private void giveSpawner(Player player, EntityType spawnerentity) {
      if (spawnerentity != null) {
         ItemStack spawner = this.plugin.versionHandler.getSpawnerToGive(spawnerentity);
         if (!this.interact) {
            spawner = (new ItemBuilder(spawner)).hideInteractLore().build();
         }

         if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(new ItemStack[]{spawner});
            SendMessage.chatToPlayer(player, Lang.SPAWNER_BROKE_AND_APPLIED_TO_INVENTORY.get().replace("%spawnertype%", spawner.getItemMeta().getDisplayName()).replace("%spawner-type%", spawner.getItemMeta().getDisplayName()));
         } else {
            Location loc = player.getLocation();
            World world = player.getWorld();
            world.dropItem(loc, spawner);
         }

      }
   }
}
