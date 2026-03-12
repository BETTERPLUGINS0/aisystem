package emanondev.itemtag.command.itemtag;

import emanondev.itemedit.aliases.Aliases;
import emanondev.itemedit.utility.CompleteUtility;
import emanondev.itemedit.utility.ItemUtils;
import emanondev.itemedit.utility.VersionUtils;
import emanondev.itemtag.EffectsInfo;
import emanondev.itemtag.ItemTag;
import emanondev.itemtag.ItemTagUtility;
import emanondev.itemtag.command.ItemTagCommand;
import emanondev.itemtag.command.ListenerSubCmd;
import emanondev.itemtag.equipmentchange.EquipmentChangeEvent;
import emanondev.itemtag.gui.EffectsGui;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Effects extends ListenerSubCmd {
   public Effects(ItemTagCommand cmd) {
      super("effects", cmd, true, true);
      this.load();
      if (VersionUtils.isVersionAfter(1, 11)) {
         this.getPlugin().registerListener(new EffectsResurrectListener(this));
      }

   }

   public void reload() {
      super.reload();
      this.load();
   }

   private void load() {
   }

   public void onCommand(@NotNull CommandSender sender, @NotNull String alias, String[] args) {
      Player p = (Player)sender;
      if (args.length != 1) {
         String var5 = args[1].toLowerCase();
         byte var6 = -1;
         switch(var5.hashCode()) {
         case -1068795718:
            if (var5.equals("modify")) {
               var6 = 1;
            }
            break;
         case -934610812:
            if (var5.equals("remove")) {
               var6 = 3;
            }
            break;
         case 113762:
            if (var5.equals("set")) {
               var6 = 0;
            }
            break;
         case 109532725:
            if (var5.equals("slots")) {
               var6 = 2;
            }
         }

         switch(var6) {
         case 0:
            this.set(p, alias, args);
            return;
         case 1:
            this.modify(p, alias, args);
            return;
         case 2:
            this.slots(p, alias, args);
            return;
         case 3:
            this.remove(p, alias, args);
            return;
         default:
            this.onFail(p, alias);
         }
      } else {
         p.openInventory((new EffectsGui(p, this.getItemInHand(p))).getInventory());
      }
   }

   private void set(Player p, String alias, String[] args) {
      try {
         PotionEffectType type = (PotionEffectType)Aliases.POTION_EFFECT.convertAlias(args[2]);
         int amplifier = Integer.parseInt(args[3]) - 1;
         Boolean particles = args.length >= 5 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[4]) : Boolean.TRUE;
         Boolean ambient = args.length >= 6 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[5]) : Boolean.FALSE;
         Boolean icon = args.length >= 7 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[6]) : Boolean.TRUE;
         if (type == null || particles == null || ambient == null || icon == null) {
            throw new IllegalArgumentException();
         }

         EffectsInfo info = new EffectsInfo(this.getItemInHand(p));
         if (amplifier < 0) {
            info.removeEffect(type);
         } else {
            info.addEffect(EffectsInfo.craftPotionEffect(type, amplifier, ambient, particles, icon));
         }

         info.update();
         ItemUtils.setHandItem(p, info.getItem());
      } catch (Exception var10) {
         this.onFail(p, alias);
      }

   }

   private void modify(Player p, String alias, String[] args) {
      try {
         PotionEffectType type = (PotionEffectType)Aliases.POTION_EFFECT.convertAlias(args[2]);
         EffectsInfo info = new EffectsInfo(this.getItemInHand(p));
         PotionEffect effect = info.getEffect(type);
         int amplifier = Integer.parseInt(args[3]) - 1;
         Boolean particles = args.length >= 5 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[4]) : (effect == null ? Boolean.TRUE : effect.hasParticles());
         Boolean ambient = args.length >= 6 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[5]) : (effect == null ? Boolean.FALSE : effect.isAmbient());
         Boolean icon = args.length >= 7 ? (Boolean)Aliases.BOOLEAN.convertAlias(args[6]) : (effect == null ? Boolean.TRUE : effect.hasIcon());
         if (type == null || particles == null || ambient == null || icon == null) {
            throw new IllegalArgumentException();
         }

         amplifier += effect != null ? effect.getAmplifier() : 0;
         if (amplifier < 0) {
            info.removeEffect(type);
         } else {
            info.addEffect(EffectsInfo.craftPotionEffect(type, amplifier, ambient, particles, icon));
         }

         info.update();
         ItemUtils.setHandItem(p, info.getItem());
      } catch (Exception var11) {
         this.onFail(p, alias);
      }

   }

   private void remove(Player p, String alias, String[] args) {
      try {
         PotionEffectType type = (PotionEffectType)Aliases.POTION_EFFECT.convertAlias(args[2]);
         if (type == null) {
            throw new IllegalArgumentException();
         }

         EffectsInfo info = new EffectsInfo(this.getItemInHand(p));
         if (!info.hasEffect(type)) {
            return;
         }

         info.removeEffect(type);
         info.update();
         ItemUtils.setHandItem(p, info.getItem());
      } catch (Exception var6) {
         this.onFail(p, alias);
      }

   }

   private void slots(Player p, String alias, String[] args) {
      try {
         EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);

         for(int i = 2; i < args.length; ++i) {
            slots.add((EquipmentSlot)Aliases.EQUIPMENT_SLOTS.convertAlias(args[i]));
         }

         EffectsInfo info = new EffectsInfo(this.getItemInHand(p));
         Iterator var6 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

         while(var6.hasNext()) {
            EquipmentSlot slot = (EquipmentSlot)var6.next();
            if (slots.contains(slot) != info.isValidSlot(slot)) {
               info.toggleSlot(slot);
            }
         }

         info.update();
         ItemUtils.setHandItem(p, info.getItem());
      } catch (Exception var8) {
         this.onFail(p, alias);
      }

   }

   public List<String> onComplete(@NotNull CommandSender sender, String[] args) {
      String var3;
      byte var4;
      switch(args.length) {
      case 2:
         return CompleteUtility.complete(args[1], new String[]{"set", "modify", "slots", "remove"});
      case 3:
         var3 = args[1].toLowerCase();
         var4 = -1;
         switch(var3.hashCode()) {
         case -1068795718:
            if (var3.equals("modify")) {
               var4 = 1;
            }
            break;
         case -934610812:
            if (var3.equals("remove")) {
               var4 = 2;
            }
            break;
         case 113762:
            if (var3.equals("set")) {
               var4 = 0;
            }
            break;
         case 109532725:
            if (var3.equals("slots")) {
               var4 = 3;
            }
         }

         switch(var4) {
         case 0:
         case 1:
         case 2:
            return CompleteUtility.complete(args[2], Aliases.POTION_EFFECT);
         case 3:
            return CompleteUtility.complete(args[2], Aliases.EQUIPMENT_SLOTS);
         default:
            return Collections.emptyList();
         }
      case 4:
         var3 = args[1].toLowerCase();
         var4 = -1;
         switch(var3.hashCode()) {
         case -1068795718:
            if (var3.equals("modify")) {
               var4 = 1;
            }
            break;
         case 113762:
            if (var3.equals("set")) {
               var4 = 0;
            }
            break;
         case 109532725:
            if (var3.equals("slots")) {
               var4 = 2;
            }
         }

         switch(var4) {
         case 0:
         case 1:
            return CompleteUtility.complete(args[3], new String[]{"1", "2", "3"});
         case 2:
            return CompleteUtility.complete(args[3], Aliases.EQUIPMENT_SLOTS);
         default:
            return Collections.emptyList();
         }
      case 5:
      case 6:
      case 7:
         var3 = args[1].toLowerCase();
         var4 = -1;
         switch(var3.hashCode()) {
         case -1068795718:
            if (var3.equals("modify")) {
               var4 = 1;
            }
            break;
         case 113762:
            if (var3.equals("set")) {
               var4 = 0;
            }
            break;
         case 109532725:
            if (var3.equals("slots")) {
               var4 = 2;
            }
         }

         switch(var4) {
         case 0:
         case 1:
            return CompleteUtility.complete(args[4], Aliases.BOOLEAN);
         case 2:
            return CompleteUtility.complete(args[4], Aliases.EQUIPMENT_SLOTS);
         default:
            return Collections.emptyList();
         }
      case 8:
         if ("slots".equalsIgnoreCase(args[1])) {
            return CompleteUtility.complete(args[4], Aliases.EQUIPMENT_SLOTS);
         }

         return Collections.emptyList();
      default:
         return Collections.emptyList();
      }
   }

   private Map<PotionEffectType, PotionEffect> getPotionEffects(ItemStack item, EquipmentSlot slot, boolean ignoreInstant) {
      if (ItemUtils.isAirOrNull(item)) {
         return Collections.emptyMap();
      } else {
         EffectsInfo info = new EffectsInfo(item);
         if (info.isValidSlot(slot) && info.hasAnyEffects()) {
            if (!ignoreInstant) {
               return info.getEffectsMap();
            } else {
               HashMap<PotionEffectType, PotionEffect> map = new HashMap(info.getEffectsMap());
               map.entrySet().removeIf((e) -> {
                  return ((PotionEffectType)e.getKey()).isInstant();
               });
               return map;
            }
         } else {
            return Collections.emptyMap();
         }
      }
   }

   private int getAmplifier(Map<PotionEffectType, PotionEffect> map, PotionEffectType type) {
      return !map.containsKey(type) ? -1 : ((PotionEffect)map.get(type)).getAmplifier();
   }

   @EventHandler
   public void onEquipChange(EquipmentChangeEvent event) {
      Map<PotionEffectType, PotionEffect> oldEffects = this.getPotionEffects(event.getFrom(), event.getSlotType(), true);
      Map<PotionEffectType, PotionEffect> newEffects = new HashMap(this.getPotionEffects(event.getTo(), event.getSlotType(), false));
      if (!oldEffects.isEmpty() || !newEffects.isEmpty()) {
         Map<PotionEffectType, PotionEffect> equipsEffects = new HashMap();
         Iterator var5 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

         while(var5.hasNext()) {
            EquipmentSlot slot = (EquipmentSlot)var5.next();
            if (slot != event.getSlotType()) {
               this.getPotionEffects(this.getEquip(event.getPlayer(), slot), slot, true).forEach((k, v) -> {
                  if (this.getAmplifier(equipsEffects, k) < v.getAmplifier()) {
                     equipsEffects.put(k, v);
                  }

               });
            }
         }

         HashSet<PotionEffectType> keys = new HashSet(oldEffects.keySet());
         keys.addAll(newEffects.keySet());
         keys.forEach((k) -> {
            if (k.isInstant()) {
               this.addEffect(event.getPlayer(), k, (PotionEffect)newEffects.get(k));
            } else {
               int newAmplifier = this.getAmplifier(newEffects, k);
               int oldAmplifier = this.getAmplifier(oldEffects, k);
               int equipAmplifier = this.getAmplifier(equipsEffects, k);
               PotionEffect max = newAmplifier > equipAmplifier ? (PotionEffect)newEffects.get(k) : (PotionEffect)equipsEffects.get(k);
               int maxAmplifier = Math.max(newAmplifier, equipAmplifier);
               if (oldAmplifier != maxAmplifier) {
                  this.addEffect(event.getPlayer(), k, max);
               }

            }
         });
      }
   }

   @EventHandler
   private void onPlayerRespawn(final PlayerRespawnEvent event) {
      (new BukkitRunnable() {
         public void run() {
            Iterator var1 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

            label70:
            while(true) {
               EquipmentSlot slot;
               EffectsInfo newInfo;
               do {
                  do {
                     ItemStack equip;
                     do {
                        if (!var1.hasNext()) {
                           return;
                        }

                        slot = (EquipmentSlot)var1.next();
                        equip = Effects.this.getEquip(event.getPlayer(), slot);
                     } while(ItemUtils.isAirOrNull(equip));

                     newInfo = new EffectsInfo(equip);
                  } while(!newInfo.isValidSlot(slot));
               } while(!newInfo.hasAnyEffects());

               Iterator var5 = newInfo.getEffects().iterator();

               while(true) {
                  while(true) {
                     if (!var5.hasNext()) {
                        continue label70;
                     }

                     PotionEffect effect = (PotionEffect)var5.next();
                     ItemTag.get().log(effect.getType() + " " + effect.getAmplifier() + 1);
                     if (!effect.getType().isInstant() && event.getPlayer().hasPotionEffect(effect.getType())) {
                        PotionEffect currentEffect = null;
                        if (VersionUtils.isVersionAfter(1, 11)) {
                           currentEffect = event.getPlayer().getPotionEffect(effect.getType());
                        } else {
                           Iterator var8 = event.getPlayer().getActivePotionEffects().iterator();

                           while(var8.hasNext()) {
                              PotionEffect k = (PotionEffect)var8.next();
                              if (k.getType().equals(effect.getType())) {
                                 currentEffect = k;
                                 break;
                              }
                           }
                        }

                        if (currentEffect.getDuration() < 72000 && currentEffect.getDuration() >= 0) {
                           if (VersionUtils.isVersionAfter(1, 16)) {
                              Effects.this.addEffect(event.getPlayer(), effect.getType(), effect);
                           } else if (currentEffect.getAmplifier() <= effect.getAmplifier()) {
                              Effects.this.addEffect(event.getPlayer(), effect.getType(), effect);
                           }
                        }
                     } else {
                        Effects.this.addEffect(event.getPlayer(), effect.getType(), effect);
                     }
                  }
               }
            }
         }
      }).runTaskLater(ItemTag.get(), 1L);
   }

   private void addEffect(@NotNull Player target, @NotNull PotionEffectType type, @Nullable PotionEffect effect) {
      if (effect == null) {
         target.removePotionEffect(type);
      } else if (type.isInstant()) {
         target.addPotionEffect(effect);
      } else {
         if (VersionUtils.isVersionAfter(1, 16)) {
            if (target.hasPotionEffect(effect.getType())) {
               target.removePotionEffect(effect.getType());
            }

            target.addPotionEffect(effect);
         } else {
            target.addPotionEffect(effect, true);
         }

      }
   }

   @EventHandler(
      ignoreCancelled = true
   )
   private void event(PlayerItemConsumeEvent event) {
      if (event.getItem().getType() == Material.MILK_BUCKET) {
         Bukkit.getScheduler().runTaskLater(this.getPlugin(), () -> {
            this.restoreEffects(event.getPlayer());
         }, 1L);
      }

   }

   public void restoreEffects(Player p) {
      if (p.isOnline() && !p.isDead()) {
         HashMap<PotionEffectType, PotionEffect> newEffects = new HashMap();
         Iterator var3 = ItemTagUtility.getPlayerEquipmentSlots().iterator();

         while(var3.hasNext()) {
            EquipmentSlot slot = (EquipmentSlot)var3.next();
            Map<PotionEffectType, PotionEffect> newInfo = this.getPotionEffects(this.getEquip(p, slot), slot, true);
            newInfo.forEach((k, v) -> {
               if (this.getAmplifier(newEffects, k) < v.getAmplifier()) {
                  newEffects.put(k, v);
               }

            });
         }

         newEffects.forEach((k, v) -> {
            this.addEffect(p, k, v);
         });
      }
   }

   protected ItemStack getEquip(Player p, EquipmentSlot slot) {
      switch(slot) {
      case CHEST:
         return p.getEquipment().getChestplate();
      case FEET:
         return p.getEquipment().getBoots();
      case HAND:
         return this.getItemInHand(p);
      case HEAD:
         return p.getEquipment().getHelmet();
      case LEGS:
         return p.getEquipment().getLeggings();
      default:
         return VersionUtils.isVersionAfter(1, 9) && slot == EquipmentSlot.OFF_HAND ? p.getInventory().getItemInOffHand() : null;
      }
   }
}
