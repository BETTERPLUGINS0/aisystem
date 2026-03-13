package github.nighter.smartspawner.spawner.gui.main;

import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.api.events.SpawnerExpClaimEvent;
import github.nighter.smartspawner.hooks.rpg.AuraSkillsIntegration;
import github.nighter.smartspawner.language.LanguageManager;
import github.nighter.smartspawner.language.MessageService;
import github.nighter.smartspawner.spawner.gui.layout.GuiButton;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayout;
import github.nighter.smartspawner.spawner.gui.layout.GuiLayoutConfig;
import github.nighter.smartspawner.spawner.gui.sell.SpawnerSellConfirmUI;
import github.nighter.smartspawner.spawner.gui.stacker.SpawnerStackerUI;
import github.nighter.smartspawner.spawner.gui.storage.SpawnerStorageUI;
import github.nighter.smartspawner.spawner.gui.synchronization.SpawnerGuiViewManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import github.nighter.smartspawner.spawner.sell.SpawnerSellManager;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class SpawnerMenuAction implements Listener {
   private static final Set<Material> SPAWNER_INFO_MATERIALS;
   private final SmartSpawner plugin;
   private final SpawnerMenuUI spawnerMenuUI;
   private final SpawnerStackerUI spawnerStackerUI;
   private final SpawnerStorageUI spawnerStorageUI;
   private final SpawnerGuiViewManager spawnerGuiViewManager;
   private final LanguageManager languageManager;
   private final MessageService messageService;
   private final SpawnerSellManager spawnerSellManager;
   private AuraSkillsIntegration auraSkills;
   private final Map<UUID, Long> lastInfoClickTime = new ConcurrentHashMap();

   public SpawnerMenuAction(SmartSpawner plugin) {
      this.plugin = plugin;
      this.spawnerMenuUI = plugin.getSpawnerMenuUI();
      this.spawnerStackerUI = plugin.getSpawnerStackerUI();
      this.spawnerStorageUI = plugin.getSpawnerStorageUI();
      this.spawnerGuiViewManager = plugin.getSpawnerGuiViewManager();
      this.languageManager = plugin.getLanguageManager();
      this.messageService = plugin.getMessageService();
      this.spawnerSellManager = plugin.getSpawnerSellManager();
      this.auraSkills = plugin.getIntegrationManager().getAuraSkillsIntegration();
   }

   public void reload() {
      this.auraSkills = this.plugin.getIntegrationManager().getAuraSkillsIntegration();
   }

   @EventHandler
   public void onMenuClick(InventoryClickEvent event) {
      HumanEntity var3 = event.getWhoClicked();
      if (var3 instanceof Player) {
         Player player = (Player)var3;
         InventoryHolder var4 = event.getInventory().getHolder(false);
         if (var4 instanceof SpawnerMenuHolder) {
            SpawnerMenuHolder holder = (SpawnerMenuHolder)var4;
            event.setCancelled(true);
            SpawnerData spawner = holder.getSpawnerData();
            if (event.getClickedInventory() != null && event.getClickedInventory().getHolder(false) instanceof SpawnerMenuHolder) {
               ItemStack clickedItem = event.getCurrentItem();
               if (clickedItem != null && clickedItem.getType() != Material.AIR) {
                  int slot = event.getRawSlot();
                  String clickType = this.getClickTypeString(event.getClick());
                  if (!this.handleLayoutAction(player, spawner, slot, clickType)) {
                     Material itemType = clickedItem.getType();
                     if (itemType == Material.CHEST) {
                        this.handleStorageClick(player, spawner);
                     } else if (SPAWNER_INFO_MATERIALS.contains(itemType)) {
                        this.handleSpawnerInfoClick(player, spawner, event.getClick());
                     } else if (itemType == Material.EXPERIENCE_BOTTLE) {
                        this.handleExpBottleClick(player, spawner, false);
                     }

                  }
               }
            }
         }
      }
   }

   private boolean handleLayoutAction(Player player, SpawnerData spawner, int slot, String clickType) {
      GuiLayoutConfig layoutConfig = this.plugin.getGuiLayoutConfig();
      GuiLayout layout = layoutConfig.getCurrentMainLayout();
      if (layout == null) {
         return false;
      } else {
         Optional<GuiButton> buttonOpt = layout.getButtonAtSlot(slot);
         if (buttonOpt.isEmpty()) {
            return false;
         } else {
            GuiButton button = (GuiButton)buttonOpt.get();
            String action = button.getActionWithFallback(clickType);
            if (action != null && !action.isEmpty()) {
               if (action.equals("none")) {
                  return true;
               } else {
                  byte var11 = -1;
                  switch(action.hashCode()) {
                  case -1410715584:
                     if (action.equals("open_stacker")) {
                        var11 = 1;
                     }
                     break;
                  case -1397348986:
                     if (action.equals("open_storage")) {
                        var11 = 0;
                     }
                     break;
                  case 672232392:
                     if (action.equals("sell_and_exp")) {
                        var11 = 2;
                     }
                     break;
                  case 1197899572:
                     if (action.equals("sell_all")) {
                        var11 = 3;
                     }
                     break;
                  case 1853584776:
                     if (action.equals("collect_exp")) {
                        var11 = 4;
                     }
                  }

                  switch(var11) {
                  case 0:
                     this.handleStorageClick(player, spawner);
                     return true;
                  case 1:
                     if (this.isClickTooFrequent(player)) {
                        return true;
                     } else {
                        if (!player.hasPermission("smartspawner.stack")) {
                           this.messageService.sendMessage(player, "no_permission");
                           return true;
                        }

                        this.spawnerStackerUI.openStackerGui(player, spawner);
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
                        return true;
                     }
                  case 2:
                     if (this.isClickTooFrequent(player)) {
                        return true;
                     } else {
                        if (this.plugin.hasSellIntegration() && player.hasPermission("smartspawner.sellall")) {
                           if (spawner.getVirtualInventory().getUsedSlots() == 0) {
                              this.handleExpBottleClick(player, spawner, true);
                              return true;
                           }

                           player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
                           this.plugin.getSpawnerSellConfirmUI().openSellConfirmGui(player, spawner, SpawnerSellConfirmUI.PreviousGui.MAIN_MENU, true);
                           return true;
                        }

                        this.messageService.sendMessage(player, "no_permission");
                        return true;
                     }
                  case 3:
                     if (this.isClickTooFrequent(player)) {
                        return true;
                     } else {
                        if (this.plugin.hasSellIntegration() && player.hasPermission("smartspawner.sellall")) {
                           this.handleSellAllItems(player, spawner);
                           return true;
                        }

                        this.messageService.sendMessage(player, "no_permission");
                        return true;
                     }
                  case 4:
                     this.handleExpBottleClick(player, spawner, false);
                     return true;
                  default:
                     return false;
                  }
               }
            } else {
               return true;
            }
         }
      }
   }

   private String getClickTypeString(ClickType clickType) {
      String var10000;
      switch(clickType) {
      case LEFT:
         var10000 = "left_click";
         break;
      case RIGHT:
         var10000 = "right_click";
         break;
      case SHIFT_LEFT:
         var10000 = "shift_left_click";
         break;
      case SHIFT_RIGHT:
         var10000 = "shift_right_click";
         break;
      default:
         var10000 = "left_click";
      }

      return var10000;
   }

   public void handleStorageClick(Player player, SpawnerData spawner) {
      Inventory pageInventory = this.spawnerStorageUI.createStorageInventory(spawner, 1, -1);
      player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
      player.openInventory(pageInventory);
   }

   private void handleSpawnerInfoClick(Player player, SpawnerData spawner, ClickType clickType) {
      if (!this.isClickTooFrequent(player)) {
         boolean hasShopIntegration = this.plugin.hasSellIntegration() && player.hasPermission("smartspawner.sellall");
         if (hasShopIntegration) {
            if (clickType == ClickType.LEFT) {
               this.handleExpBottleClick(player, spawner, true);
               this.handleSellAllItems(player, spawner);
            } else if (clickType == ClickType.RIGHT) {
               if (!player.hasPermission("smartspawner.stack")) {
                  this.messageService.sendMessage(player, "no_permission");
                  return;
               }

               this.spawnerStackerUI.openStackerGui(player, spawner);
               player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
            }
         } else {
            if (!player.hasPermission("smartspawner.stack")) {
               this.messageService.sendMessage(player, "no_permission");
               return;
            }

            this.spawnerStackerUI.openStackerGui(player, spawner);
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
         }

      }
   }

   private boolean isClickTooFrequent(Player player) {
      long now = System.currentTimeMillis();
      long last = (Long)this.lastInfoClickTime.getOrDefault(player.getUniqueId(), 0L);
      this.lastInfoClickTime.put(player.getUniqueId(), now);
      return now - last < 300L;
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      this.lastInfoClickTime.remove(event.getPlayer().getUniqueId());
   }

   private void handleSellAllItems(Player player, SpawnerData spawner) {
      if (this.plugin.hasSellIntegration()) {
         if (!player.hasPermission("smartspawner.sellall")) {
            this.messageService.sendMessage(player, "no_permission");
         } else if (spawner.getVirtualInventory().getUsedSlots() == 0) {
            this.messageService.sendMessage(player, "no_items");
         } else {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F);
            this.plugin.getSpawnerSellConfirmUI().openSellConfirmGui(player, spawner, SpawnerSellConfirmUI.PreviousGui.MAIN_MENU, false);
         }
      }
   }

   public void handleExpBottleClick(Player player, SpawnerData spawner, boolean isSell) {
      if (!this.isClickTooFrequent(player) || isSell) {
         int exp = spawner.getSpawnerExp();
         if (exp <= 0 && !isSell) {
            this.messageService.sendMessage(player, "no_exp");
         } else {
            int initialExp = exp;
            int expUsedForMending = 0;
            if (this.plugin.getConfig().getBoolean("spawner_properties.default.allow_exp_mending")) {
               expUsedForMending = this.applyMendingFromExp(player, exp);
               exp -= expUsedForMending;
            }

            if (this.auraSkills != null) {
               this.giveAuraSkillsXp(player, spawner, exp);
            }

            if (exp > 0) {
               if (SpawnerExpClaimEvent.getHandlerList().getRegisteredListeners().length != 0) {
                  SpawnerExpClaimEvent expClaimEvent = new SpawnerExpClaimEvent(player, spawner.getSpawnerLocation(), exp);
                  Bukkit.getPluginManager().callEvent(expClaimEvent);
                  if (expClaimEvent.isCancelled()) {
                     return;
                  }

                  if (exp != expClaimEvent.getExpAmount()) {
                     exp = expClaimEvent.getExpAmount();
                  }
               }

               player.giveExp(exp);
               player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            }

            spawner.setSpawnerExp(0);
            this.plugin.getSpawnerManager().markSpawnerModified(spawner.getSpawnerId());
            if (this.isBedrockPlayer(player)) {
               if (this.plugin.getSpawnerMenuFormUI() != null) {
                  this.plugin.getSpawnerMenuFormUI().openSpawnerForm(player, spawner);
               } else {
                  this.spawnerMenuUI.openSpawnerMenu(player, spawner, true);
               }
            } else {
               this.spawnerMenuUI.openSpawnerMenu(player, spawner, true);
            }

            this.spawnerGuiViewManager.updateSpawnerMenuViewers(spawner);
            if (spawner.getSpawnerExp() < spawner.getMaxStoredExp() && spawner.getIsAtCapacity()) {
               spawner.setIsAtCapacity(false);
            }

            this.sendExpCollectionMessage(player, initialExp, expUsedForMending);
         }
      }
   }

   private int applyMendingFromExp(Player player, int availableExp) {
      if (availableExp <= 0) {
         return 0;
      } else {
         int expUsed = 0;
         PlayerInventory inventory = player.getInventory();
         List<ItemStack> itemsToCheck = Arrays.asList(inventory.getItemInMainHand(), inventory.getItemInOffHand(), inventory.getHelmet(), inventory.getChestplate(), inventory.getLeggings(), inventory.getBoots());
         Iterator var6 = itemsToCheck.iterator();

         while(var6.hasNext()) {
            ItemStack item = (ItemStack)var6.next();
            if (availableExp <= 0) {
               break;
            }

            if (item != null && item.getType() != Material.AIR && item.getEnchantments().containsKey(Enchantment.MENDING)) {
               ItemMeta var9 = item.getItemMeta();
               if (var9 instanceof Damageable) {
                  Damageable damageable = (Damageable)var9;
                  if (damageable.getDamage() > 0) {
                     int damage = damageable.getDamage();
                     int durabilityToRepair = Math.min(damage, availableExp * 2);
                     int expNeeded = (durabilityToRepair + 1) / 2;
                     if (expNeeded > 0) {
                        int actualExpUsed = Math.min(expNeeded, availableExp);
                        int actualRepair = actualExpUsed * 2;
                        int newDamage = Math.max(0, damage - actualRepair);
                        Damageable meta = (Damageable)item.getItemMeta();
                        meta.setDamage(newDamage);
                        item.setItemMeta(meta);
                        availableExp -= actualExpUsed;
                        expUsed += actualExpUsed;
                        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 0.5F, 1.0F);
                        player.spawnParticle(Particle.HAPPY_VILLAGER, player.getLocation().add(0.0D, 1.0D, 0.0D), 5);
                     }
                  }
               }
            }
         }

         return expUsed;
      }
   }

   private void sendExpCollectionMessage(Player player, int totalExp, int mendingExp) {
      Map<String, String> placeholders = new HashMap();
      if (mendingExp > 0) {
         int remainingExp = totalExp - mendingExp;
         placeholders.put("exp_mending", this.languageManager.formatNumber((double)mendingExp));
         placeholders.put("exp", this.languageManager.formatNumber((double)remainingExp));
         this.messageService.sendMessage((Player)player, "exp_collected_with_mending", placeholders);
      } else if (totalExp > 0) {
         placeholders.put("exp", this.plugin.getLanguageManager().formatNumber((double)totalExp));
         this.messageService.sendMessage((Player)player, "exp_collected", placeholders);
      }

   }

   private void giveAuraSkillsXp(Player player, SpawnerData spawner, int totalExp) {
      try {
         if (this.auraSkills == null || !this.auraSkills.isEnabled()) {
            return;
         }

         EntityType entityType = spawner.getEntityType();
         if (entityType == null) {
            this.plugin.debug("Could not determine entity type for spawner at " + String.valueOf(spawner.getSpawnerLocation()));
            return;
         }

         this.auraSkills.giveSkillXp(player, entityType, totalExp);
      } catch (Exception var5) {
         this.plugin.getLogger().warning("Error giving AuraSkills XP: " + var5.getMessage());
         this.plugin.debug("AuraSkills integration error: " + var5.toString());
      }

   }

   private boolean isBedrockPlayer(Player player) {
      return this.plugin.getIntegrationManager() != null && this.plugin.getIntegrationManager().getFloodgateHook() != null ? this.plugin.getIntegrationManager().getFloodgateHook().isBedrockPlayer(player) : false;
   }

   static {
      SPAWNER_INFO_MATERIALS = Set.of(Material.PLAYER_HEAD, Material.SPAWNER, Material.ZOMBIE_HEAD, Material.SKELETON_SKULL, Material.WITHER_SKELETON_SKULL, Material.CREEPER_HEAD, Material.PIGLIN_HEAD, Material.DRAGON_HEAD);
   }
}
