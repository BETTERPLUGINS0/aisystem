package emanondev.itemtag;

import emanondev.itemedit.utility.VersionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.Nullable;

public class EffectsInfo {
   private static final String EFFECTS_LIST_KEY;
   private static final String EFFECTS_EQUIPS_KEY;
   private final EnumSet<EquipmentSlot> slots = EnumSet.noneOf(EquipmentSlot.class);
   private final HashMap<PotionEffectType, PotionEffect> effects = new HashMap();
   private final ItemStack item;
   private final TagItem tagItem;

   public EffectsInfo(@Nullable ItemStack item) {
      this.item = item;
      this.tagItem = ItemTag.getTagItem(this.item);
      if (this.tagItem.hasStringTag(EFFECTS_LIST_KEY)) {
         Iterator var2 = this.stringToEffects(this.tagItem.getString(EFFECTS_LIST_KEY)).iterator();

         while(var2.hasNext()) {
            PotionEffect effect = (PotionEffect)var2.next();
            this.effects.put(effect.getType(), effect);
         }
      }

      if (this.tagItem.hasStringTag(EFFECTS_EQUIPS_KEY)) {
         this.slots.addAll(this.stringToEquips(this.tagItem.getString(EFFECTS_EQUIPS_KEY)));
      }

      if (this.slots.isEmpty()) {
         this.slots.addAll(ItemTagUtility.getPlayerEquipmentSlots());
      }

   }

   public static PotionEffect craftPotionEffect(PotionEffectType type, int amplifier, boolean ambient, boolean particles, boolean icon) {
      int duration = type.isInstant() ? 1 : (VersionUtils.isVersionUpTo(1, 19, 3) ? 864000 : -1);
      return VersionUtils.isVersionAfter(1, 13) ? new PotionEffect(type, duration, amplifier, ambient, particles, icon) : new PotionEffect(type, duration, amplifier, ambient, particles);
   }

   private String effectsToString() {
      if (this.effects.isEmpty()) {
         return null;
      } else {
         List<PotionEffect> list = new ArrayList(this.effects.values());
         StringBuilder str = (new StringBuilder()).append(((PotionEffect)list.get(0)).getType().getName()).append(",").append(((PotionEffect)list.get(0)).getAmplifier()).append(",").append(((PotionEffect)list.get(0)).isAmbient()).append(",").append(((PotionEffect)list.get(0)).hasParticles());
         if (VersionUtils.isVersionAfter(1, 13)) {
            str.append(",").append(((PotionEffect)list.get(0)).hasIcon());
         } else {
            str.append(",true");
         }

         for(int i = 1; i < list.size(); ++i) {
            str.append(";").append(((PotionEffect)list.get(i)).getType().getName()).append(",").append(((PotionEffect)list.get(i)).getAmplifier()).append(",").append(((PotionEffect)list.get(i)).isAmbient()).append(",").append(((PotionEffect)list.get(i)).hasParticles());
            if (VersionUtils.isVersionAfter(1, 13)) {
               str.append(",").append(((PotionEffect)list.get(i)).hasIcon());
            } else {
               str.append(",true");
            }
         }

         return str.toString();
      }
   }

   private List<PotionEffect> stringToEffects(String txt) {
      List<PotionEffect> list = new ArrayList();
      if (txt != null && !txt.isEmpty()) {
         String[] effects = txt.split(";");
         String[] var4 = effects;
         int var5 = effects.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String rawEffect = var4[var6];
            String[] args = rawEffect.split(",");
            PotionEffectType type = PotionEffectType.getByName(args[0]);
            if (type != null) {
               list.add(craftPotionEffect(type, Integer.parseInt(args[1]), Boolean.parseBoolean(args[2]), Boolean.parseBoolean(args[3]), Boolean.parseBoolean(args[4])));
            }
         }

         return list;
      } else {
         return list;
      }
   }

   private String equipsToString() {
      if (this.slots.size() == ItemTagUtility.getPlayerEquipmentSlots().size()) {
         return null;
      } else {
         List<EquipmentSlot> list = new ArrayList(this.slots);
         StringBuilder str = new StringBuilder();
         str.append(((EquipmentSlot)list.get(0)).name());

         for(int i = 1; i < list.size(); ++i) {
            str.append(";").append(((EquipmentSlot)list.get(i)).name());
         }

         return str.toString();
      }
   }

   private Collection<EquipmentSlot> stringToEquips(String txt) {
      EnumSet<EquipmentSlot> equips = EnumSet.noneOf(EquipmentSlot.class);
      if (txt != null && !txt.isEmpty()) {
         String[] eq = txt.split(";");
         String[] var4 = eq;
         int var5 = eq.length;

         for(int var6 = 0; var6 < var5; ++var6) {
            String rawEq = var4[var6];
            equips.add(EquipmentSlot.valueOf(rawEq));
         }

         return equips;
      } else {
         return equips;
      }
   }

   public PotionEffect getEffect(PotionEffectType type) {
      return (PotionEffect)this.effects.get(type);
   }

   public boolean hasEffect(PotionEffectType type) {
      return this.effects.containsKey(type);
   }

   public Collection<PotionEffect> getEffects() {
      return this.effects.values();
   }

   public Map<PotionEffectType, PotionEffect> getEffectsMap() {
      return Collections.unmodifiableMap(this.effects);
   }

   public boolean isValidSlot(EquipmentSlot slot) {
      return this.slots.contains(slot);
   }

   public EnumSet<EquipmentSlot> getValidSlots() {
      return this.slots;
   }

   public void addEffect(PotionEffect effect) {
      this.effects.put(effect.getType(), effect);
   }

   public void removeEffect(PotionEffectType type) {
      this.effects.remove(type);
   }

   public void toggleSlot(EquipmentSlot slot) {
      if (this.slots.contains(slot)) {
         this.slots.remove(slot);
         if (this.slots.isEmpty()) {
            this.slots.addAll(ItemTagUtility.getPlayerEquipmentSlots());
         }

      } else {
         this.slots.add(slot);
      }
   }

   public void update() {
      if (this.effects.isEmpty()) {
         this.tagItem.removeTag(EFFECTS_LIST_KEY);
      } else {
         this.tagItem.setTag(EFFECTS_LIST_KEY, this.effectsToString());
      }

      if (this.slots.size() == ItemTagUtility.getPlayerEquipmentSlots().size()) {
         this.tagItem.removeTag(EFFECTS_EQUIPS_KEY);
      } else {
         this.tagItem.setTag(EFFECTS_EQUIPS_KEY, this.equipsToString());
      }

   }

   public boolean hasAnyEffects() {
      return !this.effects.isEmpty();
   }

   public ItemStack getItem() {
      return this.item;
   }

   static {
      EFFECTS_LIST_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":effects_list";
      EFFECTS_EQUIPS_KEY = ItemTag.get().getName().toLowerCase(Locale.ENGLISH) + ":effects_equips";
   }
}
