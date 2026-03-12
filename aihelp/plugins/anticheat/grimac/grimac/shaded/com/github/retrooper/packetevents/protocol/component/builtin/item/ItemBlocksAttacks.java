package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype.DamageType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.damagetype.DamageTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemBlocksAttacks {
   private float blockDelaySeconds;
   private float disableCooldownScale;
   private List<ItemBlocksAttacks.DamageReduction> damageReductions;
   private ItemBlocksAttacks.ItemDamageFunction itemDamage;
   @Nullable
   private ResourceLocation bypassedBy;
   @Nullable
   private Sound blockSound;
   @Nullable
   private Sound disableSound;

   public ItemBlocksAttacks(float blockDelaySeconds, float disableCooldownScale, List<ItemBlocksAttacks.DamageReduction> damageReductions, ItemBlocksAttacks.ItemDamageFunction itemDamage, @Nullable ResourceLocation bypassedBy, @Nullable Sound blockSound, @Nullable Sound disableSound) {
      this.blockDelaySeconds = blockDelaySeconds;
      this.disableCooldownScale = disableCooldownScale;
      this.damageReductions = damageReductions;
      this.itemDamage = itemDamage;
      this.bypassedBy = bypassedBy;
      this.blockSound = blockSound;
      this.disableSound = disableSound;
   }

   public static ItemBlocksAttacks read(PacketWrapper<?> wrapper) {
      float blockDelaySeconds = wrapper.readFloat();
      float disableCooldownScale = wrapper.readFloat();
      List<ItemBlocksAttacks.DamageReduction> damageReductions = wrapper.readList(ItemBlocksAttacks.DamageReduction::read);
      ItemBlocksAttacks.ItemDamageFunction itemDamage = ItemBlocksAttacks.ItemDamageFunction.read(wrapper);
      ResourceLocation bypassedBy = (ResourceLocation)wrapper.readOptional(PacketWrapper::readIdentifier);
      Sound blockSound = (Sound)wrapper.readOptional(Sound::read);
      Sound disableSound = (Sound)wrapper.readOptional(Sound::read);
      return new ItemBlocksAttacks(blockDelaySeconds, disableCooldownScale, damageReductions, itemDamage, bypassedBy, blockSound, disableSound);
   }

   public static void write(PacketWrapper<?> wrapper, ItemBlocksAttacks attack) {
      wrapper.writeFloat(attack.blockDelaySeconds);
      wrapper.writeFloat(attack.disableCooldownScale);
      wrapper.writeList(attack.damageReductions, ItemBlocksAttacks.DamageReduction::write);
      ItemBlocksAttacks.ItemDamageFunction.write(wrapper, attack.itemDamage);
      wrapper.writeOptional(attack.bypassedBy, PacketWrapper::writeIdentifier);
      wrapper.writeOptional(attack.blockSound, Sound::write);
      wrapper.writeOptional(attack.disableSound, Sound::write);
   }

   public float getBlockDelaySeconds() {
      return this.blockDelaySeconds;
   }

   public void setBlockDelaySeconds(float blockDelaySeconds) {
      this.blockDelaySeconds = blockDelaySeconds;
   }

   public float getDisableCooldownScale() {
      return this.disableCooldownScale;
   }

   public void setDisableCooldownScale(float disableCooldownScale) {
      this.disableCooldownScale = disableCooldownScale;
   }

   public List<ItemBlocksAttacks.DamageReduction> getDamageReductions() {
      return this.damageReductions;
   }

   public void setDamageReductions(List<ItemBlocksAttacks.DamageReduction> damageReductions) {
      this.damageReductions = damageReductions;
   }

   public ItemBlocksAttacks.ItemDamageFunction getItemDamage() {
      return this.itemDamage;
   }

   public void setItemDamage(ItemBlocksAttacks.ItemDamageFunction itemDamage) {
      this.itemDamage = itemDamage;
   }

   @Nullable
   public ResourceLocation getBypassedBy() {
      return this.bypassedBy;
   }

   public void setBypassedBy(@Nullable ResourceLocation bypassedBy) {
      this.bypassedBy = bypassedBy;
   }

   @Nullable
   public Sound getBlockSound() {
      return this.blockSound;
   }

   public void setBlockSound(@Nullable Sound blockSound) {
      this.blockSound = blockSound;
   }

   @Nullable
   public Sound getDisableSound() {
      return this.disableSound;
   }

   public void setDisableSound(@Nullable Sound disableSound) {
      this.disableSound = disableSound;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof ItemBlocksAttacks)) {
         return false;
      } else {
         ItemBlocksAttacks that = (ItemBlocksAttacks)obj;
         if (Float.compare(that.blockDelaySeconds, this.blockDelaySeconds) != 0) {
            return false;
         } else if (Float.compare(that.disableCooldownScale, this.disableCooldownScale) != 0) {
            return false;
         } else if (!this.damageReductions.equals(that.damageReductions)) {
            return false;
         } else if (!this.itemDamage.equals(that.itemDamage)) {
            return false;
         } else if (!Objects.equals(this.bypassedBy, that.bypassedBy)) {
            return false;
         } else {
            return !Objects.equals(this.blockSound, that.blockSound) ? false : Objects.equals(this.disableSound, that.disableSound);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.blockDelaySeconds, this.disableCooldownScale, this.damageReductions, this.itemDamage, this.bypassedBy, this.blockSound, this.disableSound});
   }

   public static final class ItemDamageFunction {
      private float threshold;
      private float base;
      private float factor;

      public ItemDamageFunction(float threshold, float base, float factor) {
         this.threshold = threshold;
         this.base = base;
         this.factor = factor;
      }

      public static ItemBlocksAttacks.ItemDamageFunction read(PacketWrapper<?> wrapper) {
         float threshold = wrapper.readFloat();
         float base = wrapper.readFloat();
         float factor = wrapper.readFloat();
         return new ItemBlocksAttacks.ItemDamageFunction(threshold, base, factor);
      }

      public static void write(PacketWrapper<?> wrapper, ItemBlocksAttacks.ItemDamageFunction function) {
         wrapper.writeFloat(function.threshold);
         wrapper.writeFloat(function.base);
         wrapper.writeFloat(function.factor);
      }

      public float getThreshold() {
         return this.threshold;
      }

      public void setThreshold(float threshold) {
         this.threshold = threshold;
      }

      public float getBase() {
         return this.base;
      }

      public void setBase(float base) {
         this.base = base;
      }

      public float getFactor() {
         return this.factor;
      }

      public void setFactor(float factor) {
         this.factor = factor;
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof ItemBlocksAttacks.ItemDamageFunction)) {
            return false;
         } else {
            ItemBlocksAttacks.ItemDamageFunction that = (ItemBlocksAttacks.ItemDamageFunction)obj;
            if (Float.compare(that.threshold, this.threshold) != 0) {
               return false;
            } else if (Float.compare(that.base, this.base) != 0) {
               return false;
            } else {
               return Float.compare(that.factor, this.factor) == 0;
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.threshold, this.base, this.factor});
      }
   }

   public static final class DamageReduction {
      private float horizontalBlockingAngle;
      @Nullable
      private MappedEntitySet<DamageType> type;
      private float base;
      private float factor;

      public DamageReduction(float horizontalBlockingAngle, @Nullable MappedEntitySet<DamageType> type, float base, float factor) {
         this.horizontalBlockingAngle = horizontalBlockingAngle;
         this.type = type;
         this.base = base;
         this.factor = factor;
      }

      public static ItemBlocksAttacks.DamageReduction read(PacketWrapper<?> wrapper) {
         float horizontalBlockingAngle = wrapper.readFloat();
         MappedEntitySet<DamageType> type = (MappedEntitySet)wrapper.readOptional((ew) -> {
            return MappedEntitySet.read(ew, DamageTypes.getRegistry());
         });
         float base = wrapper.readFloat();
         float factor = wrapper.readFloat();
         return new ItemBlocksAttacks.DamageReduction(horizontalBlockingAngle, type, base, factor);
      }

      public static void write(PacketWrapper<?> wrapper, ItemBlocksAttacks.DamageReduction reduction) {
         wrapper.writeFloat(reduction.horizontalBlockingAngle);
         wrapper.writeOptional(reduction.type, MappedEntitySet::write);
         wrapper.writeFloat(reduction.base);
         wrapper.writeFloat(reduction.factor);
      }

      public float getHorizontalBlockingAngle() {
         return this.horizontalBlockingAngle;
      }

      public void setHorizontalBlockingAngle(float horizontalBlockingAngle) {
         this.horizontalBlockingAngle = horizontalBlockingAngle;
      }

      @Nullable
      public MappedEntitySet<DamageType> getType() {
         return this.type;
      }

      public void setType(@Nullable MappedEntitySet<DamageType> type) {
         this.type = type;
      }

      public float getBase() {
         return this.base;
      }

      public void setBase(float base) {
         this.base = base;
      }

      public float getFactor() {
         return this.factor;
      }

      public void setFactor(float factor) {
         this.factor = factor;
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof ItemBlocksAttacks.DamageReduction)) {
            return false;
         } else {
            ItemBlocksAttacks.DamageReduction that = (ItemBlocksAttacks.DamageReduction)obj;
            if (Float.compare(that.horizontalBlockingAngle, this.horizontalBlockingAngle) != 0) {
               return false;
            } else if (Float.compare(that.base, this.base) != 0) {
               return false;
            } else {
               return Float.compare(that.factor, this.factor) != 0 ? false : Objects.equals(this.type, that.type);
            }
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.horizontalBlockingAngle, this.type, this.base, this.factor});
      }
   }
}
