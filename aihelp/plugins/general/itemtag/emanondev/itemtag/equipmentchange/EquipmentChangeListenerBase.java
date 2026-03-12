package emanondev.itemtag.equipmentchange;

import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.ItemTagUtility;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public abstract class EquipmentChangeListenerBase implements Listener {
   protected final HashMap<Player, EnumMap<EquipmentSlot, ItemStack>> equips = new HashMap();
   protected final HashSet<Player> clickDrop = new HashSet();
   private int maxCheckedPlayerPerTick = 5;
   private EquipmentChangeListenerBase.TimerCheckTask timerTask = null;

   public void reload() {
      if (this.timerTask != null) {
         this.timerTask.cancel();
      }

      long timerCheckFrequencyTicks = (long)Math.max(5, ItemTag.get().getConfig().getInteger("equipment_change.frequency_ticks", 10));
      this.maxCheckedPlayerPerTick = Math.max(1, ItemTag.get().getConfig().getInteger("equipment_change.max_checked_players_per_tick", 5));
      Iterator var3 = this.equips.keySet().iterator();

      Player p;
      while(var3.hasNext()) {
         p = (Player)var3.next();
         this.untrackPlayer(p);
      }

      this.equips.clear();
      var3 = Bukkit.getOnlinePlayers().iterator();

      while(var3.hasNext()) {
         p = (Player)var3.next();
         this.trackPlayer(p);
      }

      this.timerTask = new EquipmentChangeListenerBase.TimerCheckTask();
      this.timerTask.runTaskTimer(ItemTag.get(), timerCheckFrequencyTicks, timerCheckFrequencyTicks);
   }

   @EventHandler(
      priority = EventPriority.HIGHEST,
      ignoreCancelled = true
   )
   private void event(PlayerJoinEvent event) {
      this.trackPlayer(event.getPlayer());
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   private void event(PlayerQuitEvent event) {
      this.untrackPlayer(event.getPlayer());
   }

   @EventHandler
   private void event(PlayerDeathEvent event) {
      if (!event.getEntity().hasMetadata("NPC") && !event.getEntity().hasMetadata("BOT")) {
         Iterator var2 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

         while(var2.hasNext()) {
            EquipmentSlot type = (EquipmentSlot)var2.next();
            ItemStack item = this.getEquip(event.getEntity(), type);
            if (!ItemUtils.isAirOrNull(item)) {
               this.onEquipChange(event.getEntity(), EquipmentChangeEvent.EquipMethod.DEATH, type, item, (ItemStack)null);
            }
         }

      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.MONITOR
   )
   private void event(PlayerTeleportEvent event) {
      if (!event.getPlayer().hasMetadata("NPC") && !event.getPlayer().hasMetadata("BOT")) {
         if (this.equips.containsKey(event.getPlayer())) {
            (new EquipmentChangeListenerBase.SlotCheck(event.getPlayer(), EquipmentChangeEvent.EquipMethod.PLUGIN_WORLD_CHANGE, ItemTagUtility.getPlayerEquipmentSlots())).runTaskLater(ItemTag.get(), 1L);
         }
      }
   }

   @EventHandler
   private void event(PlayerRespawnEvent event) {
      if (!event.getPlayer().hasMetadata("NPC") && !event.getPlayer().hasMetadata("BOT")) {
         Iterator var2 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

         while(var2.hasNext()) {
            EquipmentSlot type = (EquipmentSlot)var2.next();
            ItemStack item = this.getEquip(event.getPlayer(), type);
            if (!ItemUtils.isAirOrNull(item)) {
               this.onEquipChange(event.getPlayer(), EquipmentChangeEvent.EquipMethod.RESPAWN, type, (ItemStack)null, item);
            }
         }

      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   private void event(InventoryDragEvent event) {
      if (event.getWhoClicked() instanceof Player) {
         if (!event.getWhoClicked().hasMetadata("NPC") && !event.getWhoClicked().hasMetadata("BOT")) {
            Player p = (Player)event.getWhoClicked();
            Iterator var3 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

            while(var3.hasNext()) {
               EquipmentSlot type = (EquipmentSlot)var3.next();
               int pos = this.getSlotPosition(type, p, event.getView());
               if (pos != -1 && event.getNewItems().containsKey(pos)) {
                  ItemStack itemOld = event.getView().getItem(pos);
                  ItemStack itemNew = (ItemStack)event.getNewItems().get(pos);
                  if (!this.isSimilarIgnoreDamage(itemOld, itemNew)) {
                     this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.INVENTORY_DRAG, type, itemOld, itemNew);
                  }
               }
            }

         }
      }
   }

   @EventHandler
   private void event(PlayerItemBreakEvent e) {
      if (!e.getPlayer().hasMetadata("NPC") && !e.getPlayer().hasMetadata("BOT")) {
         ArrayList<EquipmentSlot> slots = new ArrayList();
         Iterator var3 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

         while(var3.hasNext()) {
            EquipmentSlot slot = (EquipmentSlot)var3.next();
            if (e.getBrokenItem().equals(this.getEquip(e.getPlayer(), slot))) {
               slots.add(slot);
            }
         }

         if (slots.isEmpty()) {
            throw new IllegalStateException();
         } else if (slots.size() == 1) {
            this.onEquipChange(e.getPlayer(), EquipmentChangeEvent.EquipMethod.BROKE, (EquipmentSlot)slots.get(0), e.getBrokenItem(), (ItemStack)null);
         } else {
            (new EquipmentChangeListenerBase.SlotCheck(e.getPlayer(), EquipmentChangeEvent.EquipMethod.BROKE, slots)).runTaskLater(ItemTag.get(), 1L);
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.MONITOR
   )
   private void event(PlayerArmorStandManipulateEvent event) {
      if (!event.getPlayer().hasMetadata("NPC") && !event.getPlayer().hasMetadata("BOT")) {
         if (!this.isSimilarIgnoreDamage(event.getArmorStandItem(), event.getPlayerItem())) {
            this.onEquipChange(event.getPlayer(), EquipmentChangeEvent.EquipMethod.ARMOR_STAND_MANIPULATE, EquipmentSlot.HAND, event.getPlayerItem(), event.getArmorStandItem());
         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void event(PlayerDropItemEvent event) {
      if (!event.getPlayer().hasMetadata("NPC") && !event.getPlayer().hasMetadata("BOT")) {
         if (!this.clickDrop.remove(event.getPlayer())) {
            if (!event.isCancelled()) {
               if (ItemUtils.isAirOrNull(this.getEquip(event.getPlayer(), EquipmentSlot.HAND))) {
                  this.onEquipChange(event.getPlayer(), EquipmentChangeEvent.EquipMethod.DROP, EquipmentSlot.HAND, event.getItemDrop().getItemStack(), (ItemStack)null);
               }

            }
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.MONITOR
   )
   private void event(PlayerInteractEntityEvent event) {
      if (!event.getPlayer().hasMetadata("NPC") && !event.getPlayer().hasMetadata("BOT")) {
         EquipmentSlot slot;
         if (VersionUtils.isVersionAfter(1, 9)) {
            slot = event.getHand();
         } else {
            slot = EquipmentSlot.HAND;
         }

         ItemStack handItem = this.getEquip(event.getPlayer(), slot);
         if (!ItemUtils.isAirOrNull(handItem) && handItem.getAmount() <= 1 && (event.getRightClicked().getType() != EntityType.ARMOR_STAND || handItem.getType() == Material.NAME_TAG)) {
            if (handItem.getType() == Material.NAME_TAG) {
               if (event.getRightClicked() instanceof LivingEntity && !(event.getRightClicked() instanceof Player)) {
                  if (handItem.hasItemMeta() && handItem.getItemMeta().hasDisplayName()) {
                     this.onEquipChange(event.getPlayer(), EquipmentChangeEvent.EquipMethod.NAMETAG_APPLY, slot, handItem, (ItemStack)null);
                  }
               }
            } else if (event.getRightClicked() instanceof Sheep) {
               Sheep sheep = (Sheep)event.getRightClicked();
               if (!sheep.isSheared()) {
                  if (handItem.getType().name().endsWith("_DYE")) {
                     if (!handItem.getType().name().equals(sheep.getColor().name() + "_DYE")) {
                        this.onEquipChange(event.getPlayer(), EquipmentChangeEvent.EquipMethod.SHEEP_COLOR, slot, handItem, (ItemStack)null);
                     }
                  }
               }
            } else {
               (new EquipmentChangeListenerBase.SlotCheck(event.getPlayer(), EquipmentChangeEvent.EquipMethod.USE_ON_ENTITY, new EquipmentSlot[]{slot})).runTaskLater(ItemTag.get(), 1L);
            }
         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   private void event(PlayerInteractEvent e) {
      if (!ItemUtils.isAirOrNull(e.getItem())) {
         if (e.useItemInHand() != Result.DENY) {
            if (!e.getPlayer().hasMetadata("NPC") && !e.getPlayer().hasMetadata("BOT")) {
               EquipmentSlot slot;
               if (VersionUtils.isVersionAfter(1, 9)) {
                  slot = e.getHand();
               } else {
                  slot = EquipmentSlot.HAND;
               }

               EquipmentSlot type = this.guessRightClickSlotType(e.getItem());
               switch(e.getAction()) {
               case RIGHT_CLICK_AIR:
                  if (type != null && ItemUtils.isAirOrNull(this.getEquip(e.getPlayer(), type))) {
                     this.onEquipChange(e.getPlayer(), EquipmentChangeEvent.EquipMethod.RIGHT_CLICK, type, (ItemStack)null, e.getItem());
                     if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
                        this.onEquipChange(e.getPlayer(), EquipmentChangeEvent.EquipMethod.RIGHT_CLICK, slot, e.getItem(), (ItemStack)null);
                     }
                  } else if (e.getItem().getAmount() == 1) {
                     (new EquipmentChangeListenerBase.SlotCheck(e.getPlayer(), EquipmentChangeEvent.EquipMethod.USE, new EquipmentSlot[]{slot})).runTaskLater(ItemTag.get(), 1L);
                  }

                  return;
               case RIGHT_CLICK_BLOCK:
                  if (e.useItemInHand() == Result.DENY) {
                     return;
                  }

                  if (type != null && ItemUtils.isAirOrNull(this.getEquip(e.getPlayer(), type))) {
                     (new EquipmentChangeListenerBase.SlotCheck(e.getPlayer(), EquipmentChangeEvent.EquipMethod.RIGHT_CLICK, new EquipmentSlot[]{slot, type})).runTaskLater(ItemTag.get(), 1L);
                  } else if (e.getItem().getAmount() == 1) {
                     (new EquipmentChangeListenerBase.SlotCheck(e.getPlayer(), EquipmentChangeEvent.EquipMethod.USE, new EquipmentSlot[]{slot})).runTaskLater(ItemTag.get(), 1L);
                  }

                  return;
               default:
               }
            }
         }
      }
   }

   @EventHandler(
      ignoreCancelled = true,
      priority = EventPriority.MONITOR
   )
   private void event(PlayerItemConsumeEvent event) {
      if (!event.getPlayer().hasMetadata("NPC") && !event.getPlayer().hasMetadata("BOT")) {
         if (event.getItem().getAmount() == 1) {
            List<EquipmentSlot> slots = new ArrayList(1);
            if (event.getItem().equals(this.getEquip(event.getPlayer(), EquipmentSlot.HAND))) {
               slots.add(EquipmentSlot.HAND);
            }

            if (VersionUtils.isVersionAfter(1, 9) && event.getItem().equals(this.getEquip(event.getPlayer(), EquipmentSlot.OFF_HAND))) {
               slots.add(EquipmentSlot.OFF_HAND);
            }

            if (slots.size() == 1) {
               this.onEquipChange(event.getPlayer(), EquipmentChangeEvent.EquipMethod.CONSUME, (EquipmentSlot)slots.get(0), event.getItem(), event.getItem().getType() == Material.MILK_BUCKET ? new ItemStack(Material.BUCKET) : null);
            } else if (slots.size() > 1) {
               (new EquipmentChangeListenerBase.SlotCheck(event.getPlayer(), EquipmentChangeEvent.EquipMethod.CONSUME, slots)).runTaskLater(ItemTag.get(), 1L);
            } else if (VersionUtils.isVersionAfter(1, 9)) {
               (new EquipmentChangeListenerBase.SlotCheck(event.getPlayer(), EquipmentChangeEvent.EquipMethod.CONSUME, Arrays.asList(EquipmentSlot.HAND, EquipmentSlot.OFF_HAND))).runTaskLater(ItemTag.get(), 1L);
            }

         }
      }
   }

   @EventHandler
   private void event(PlayerItemHeldEvent event) {
      if (!event.getPlayer().hasMetadata("NPC") && !event.getPlayer().hasMetadata("BOT")) {
         ItemStack i1 = event.getPlayer().getInventory().getItem(event.getPreviousSlot());
         ItemStack i2 = event.getPlayer().getInventory().getItem(event.getNewSlot());
         if (i1 != i2) {
            if (i1 == null || !i1.isSimilar(i2)) {
               this.onEquipChange(event.getPlayer(), EquipmentChangeEvent.EquipMethod.HOTBAR_HAND_CHANGE, EquipmentSlot.HAND, i1, i2);
            }
         }
      }
   }

   public abstract boolean isSimilarIgnoreDamage(ItemStack var1, ItemStack var2);

   protected ItemStack getEquip(Player p, EquipmentSlot slot) {
      switch(slot) {
      case CHEST:
         return p.getEquipment().getChestplate();
      case FEET:
         return p.getEquipment().getBoots();
      case HAND:
         if (VersionUtils.isVersionAfter(1, 9)) {
            return p.getInventory().getItemInMainHand();
         }

         return p.getInventory().getItemInHand();
      case HEAD:
         return p.getEquipment().getHelmet();
      case LEGS:
         return p.getEquipment().getLeggings();
      default:
         return VersionUtils.isVersionAfter(1, 9) && slot == EquipmentSlot.OFF_HAND ? p.getInventory().getItemInOffHand() : null;
      }
   }

   public void onEquipChange(Player p, EquipmentChangeEvent.EquipMethod reason, @NotNull EquipmentSlot type, ItemStack oldItem, ItemStack newItem) {
      ((EnumMap)this.equips.get(p)).put(type, ItemUtils.isAirOrNull(newItem) ? null : new ItemStack(newItem));
      Bukkit.getPluginManager().callEvent(new EquipmentChangeEvent(p, reason, type, oldItem, newItem));
   }

   protected EquipmentSlot guessRightClickSlotType(ItemStack item) {
      if (ItemUtils.isAirOrNull(item)) {
         return null;
      } else {
         if (VersionUtils.isVersionAfter(1, 21, 5)) {
            ItemMeta meta = ItemUtils.getMeta(item);
            if (meta.hasEquippable()) {
               return meta.getEquippable().getSlot();
            }
         }

         String type = item.getType().name();
         if (!type.endsWith("_HELMET") && !type.endsWith("_SKULL") && !type.endsWith("_HEAD")) {
            if (!type.endsWith("_CHESTPLATE") && !type.equals("ELYTRA")) {
               if (type.endsWith("_LEGGINGS")) {
                  return EquipmentSlot.LEGS;
               } else {
                  return type.endsWith("_BOOTS") ? EquipmentSlot.FEET : null;
               }
            } else {
               return EquipmentSlot.CHEST;
            }
         } else {
            return EquipmentSlot.HEAD;
         }
      }
   }

   protected EquipmentSlot guessDispenserSlotType(ItemStack item) {
      EquipmentSlot slot = this.guessRightClickSlotType(item);
      if (VersionUtils.isVersionAfter(1, 21, 5)) {
         ItemMeta meta = ItemUtils.getMeta(item);
         if (meta.hasEquippable()) {
            return meta.getEquippable().getSlot();
         }
      }

      if (slot == null && item != null) {
         if (item.getType().name().endsWith("PUMPKIN")) {
            return EquipmentSlot.HEAD;
         }

         if (VersionUtils.isVersionAfter(1, 9) && item.getType() == Material.SHIELD) {
            return EquipmentSlot.OFF_HAND;
         }
      }

      return slot;
   }

   protected int getSlotPosition(EquipmentSlot slot, Player p, InventoryView view) {
      if (view.getTopInventory().getType() == InventoryType.CRAFTING) {
         switch(slot) {
         case CHEST:
            return 6;
         case FEET:
            return 8;
         case HAND:
            return p.getInventory().getHeldItemSlot() + 36;
         case HEAD:
            return 5;
         case LEGS:
            return 7;
         default:
            return VersionUtils.isVersionAfter(1, 9) && slot == EquipmentSlot.OFF_HAND ? 45 : -1;
         }
      } else {
         return slot == EquipmentSlot.HAND ? p.getInventory().getHeldItemSlot() + view.getTopInventory().getSize() + 27 : -1;
      }
   }

   protected EquipmentSlot getEquipmentSlotAtPosition(int pos, Player p, InventoryView view) {
      if (view.getTopInventory().getType() == InventoryType.CRAFTING) {
         switch(pos) {
         case 5:
            return EquipmentSlot.HEAD;
         case 6:
            return EquipmentSlot.CHEST;
         case 7:
            return EquipmentSlot.LEGS;
         case 8:
            return EquipmentSlot.FEET;
         case 45:
            if (VersionUtils.isVersionAfter(1, 9)) {
               return EquipmentSlot.OFF_HAND;
            }
         default:
            return p.getInventory().getHeldItemSlot() + 36 == pos ? EquipmentSlot.HAND : null;
         }
      } else {
         return pos == p.getInventory().getHeldItemSlot() + view.getTopInventory().getSize() + 27 ? EquipmentSlot.HAND : null;
      }
   }

   public boolean trackPlayer(Player p) {
      if (this.equips.containsKey(p)) {
         return false;
      } else {
         EnumMap<EquipmentSlot, ItemStack> map = new EnumMap(EquipmentSlot.class);
         this.equips.put(p, map);
         Iterator var3 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

         while(var3.hasNext()) {
            EquipmentSlot slot = (EquipmentSlot)var3.next();
            map.put(slot, (Object)null);
            this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.JOIN, slot, (ItemStack)null, this.getEquip(p, slot));
         }

         return true;
      }
   }

   public boolean untrackPlayer(Player p) {
      if (!this.equips.containsKey(p)) {
         return false;
      } else {
         EnumMap<EquipmentSlot, ItemStack> map = (EnumMap)this.equips.get(p);
         Iterator var3 = map.keySet().iterator();

         while(var3.hasNext()) {
            EquipmentSlot slot = (EquipmentSlot)var3.next();
            this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.QUIT, slot, (ItemStack)map.get(slot), (ItemStack)null);
         }

         this.equips.remove(p);
         return true;
      }
   }

   private class TimerCheckTask extends BukkitRunnable {
      private EquipmentChangeListenerBase.TimerCheckTask.PlayerCheck subTask;

      private TimerCheckTask() {
         this.subTask = null;
      }

      public void run() {
         if (this.subTask == null && !Bukkit.getOnlinePlayers().isEmpty()) {
            this.subTask = new EquipmentChangeListenerBase.TimerCheckTask.PlayerCheck();
            this.subTask.runTaskTimer(ItemTag.get(), 1L, 1L);
         }

      }

      public void cancel() {
         super.cancel();
         if (this.subTask != null) {
            this.subTask.cancel();
            this.subTask = null;
         }

      }

      // $FF: synthetic method
      TimerCheckTask(Object x1) {
         this();
      }

      private class PlayerCheck extends BukkitRunnable {
         private final List<Player> players;
         private int index;

         private PlayerCheck() {
            this.players = new ArrayList();
            this.index = 0;
            this.players.addAll(Bukkit.getOnlinePlayers());
         }

         public void run() {
            int counter = 0;

            while(true) {
               Player p;
               do {
                  do {
                     if (counter >= EquipmentChangeListenerBase.this.maxCheckedPlayerPerTick) {
                        return;
                     }

                     if (this.index >= this.players.size()) {
                        this.cancel();
                        if (TimerCheckTask.this.subTask != null) {
                           TimerCheckTask.this.subTask = null;
                        }

                        return;
                     }

                     p = (Player)this.players.get(this.index);
                     ++this.index;
                  } while(!p.isOnline());
               } while(p.hasMetadata("BOT"));

               EquipmentChangeListenerBase.this.trackPlayer(p);
               ++counter;
               Iterator var3 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

               while(var3.hasNext()) {
                  EquipmentSlot slot = (EquipmentSlot)var3.next();
                  ItemStack newItem = EquipmentChangeListenerBase.this.getEquip(p, slot);
                  ItemStack oldItem = (ItemStack)((EnumMap)EquipmentChangeListenerBase.this.equips.get(p)).get(slot);
                  if (!EquipmentChangeListenerBase.this.isSimilarIgnoreDamage(oldItem, newItem)) {
                     EquipmentChangeListenerBase.this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.UNKNOWN, slot, oldItem, newItem);
                  }
               }
            }
         }

         // $FF: synthetic method
         PlayerCheck(Object x1) {
            this();
         }
      }
   }

   protected class SlotCheck extends BukkitRunnable {
      private final EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
      private final Player p;
      private final EquipmentChangeEvent.EquipMethod method;

      public SlotCheck(Player param2, EquipmentChangeEvent.EquipMethod param3, EquipmentSlot... param4) {
         if (slots != null && slots.length != 0 && p != null && method != null) {
            this.p = p;
            this.method = method;
            if (EquipmentChangeListenerBase.this.trackPlayer(p)) {
               (new IllegalStateException()).printStackTrace();
            }

            EquipmentSlot[] var5 = slots;
            int var6 = slots.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               EquipmentSlot slot = var5[var7];
               if (!EquipmentChangeListenerBase.this.isSimilarIgnoreDamage((ItemStack)((EnumMap)EquipmentChangeListenerBase.this.equips.get(p)).get(slot), EquipmentChangeListenerBase.this.getEquip(p, slot))) {
                  EquipmentChangeListenerBase.this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.UNKNOWN, slot, (ItemStack)((EnumMap)EquipmentChangeListenerBase.this.equips.get(p)).get(slot), EquipmentChangeListenerBase.this.getEquip(p, slot));
               }

               this.slots.add(slot);
            }

         } else {
            throw new IllegalArgumentException();
         }
      }

      public SlotCheck(Player param2, EquipmentChangeEvent.EquipMethod param3, Collection<EquipmentSlot> param4) {
         if (slots != null && !slots.isEmpty() && p != null && method != null) {
            this.p = p;
            this.method = method;
            if (EquipmentChangeListenerBase.this.trackPlayer(p)) {
               (new IllegalStateException()).printStackTrace();
            }

            EquipmentSlot slot;
            for(Iterator var5 = slots.iterator(); var5.hasNext(); this.slots.add(slot)) {
               slot = (EquipmentSlot)var5.next();
               ItemStack equip = EquipmentChangeListenerBase.this.getEquip(p, slot);
               if (ItemUtils.isAirOrNull(equip)) {
                  equip = null;
               } else {
                  equip = new ItemStack(equip);
               }

               if (!EquipmentChangeListenerBase.this.isSimilarIgnoreDamage((ItemStack)((EnumMap)EquipmentChangeListenerBase.this.equips.get(p)).get(slot), equip)) {
                  EquipmentChangeListenerBase.this.onEquipChange(p, EquipmentChangeEvent.EquipMethod.UNKNOWN, slot, (ItemStack)((EnumMap)EquipmentChangeListenerBase.this.equips.get(p)).get(slot), equip);
               }
            }

         } else {
            throw new IllegalArgumentException();
         }
      }

      public void run() {
         if (this.p.isOnline()) {
            if (EquipmentChangeListenerBase.this.trackPlayer(this.p)) {
               (new IllegalStateException()).printStackTrace();
            }

            Iterator var1 = this.slots.iterator();

            while(var1.hasNext()) {
               EquipmentSlot slot = (EquipmentSlot)var1.next();
               ItemStack item = EquipmentChangeListenerBase.this.getEquip(this.p, slot);
               if (ItemUtils.isAirOrNull(item)) {
                  item = null;
               }

               if (!EquipmentChangeListenerBase.this.isSimilarIgnoreDamage(item, (ItemStack)((EnumMap)EquipmentChangeListenerBase.this.equips.get(this.p)).get(slot))) {
                  EquipmentChangeListenerBase.this.onEquipChange(this.p, this.method, slot, (ItemStack)((EnumMap)EquipmentChangeListenerBase.this.equips.get(this.p)).get(slot), item);
               }
            }

         }
      }
   }
}
