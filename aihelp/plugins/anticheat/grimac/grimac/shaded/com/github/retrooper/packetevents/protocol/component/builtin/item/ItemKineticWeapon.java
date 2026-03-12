package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ItemKineticWeapon {
   private int contactCooldownTicks;
   private int delayTicks;
   @Nullable
   private ItemKineticWeapon.Condition dismountConditions;
   @Nullable
   private ItemKineticWeapon.Condition knockbackConditions;
   @Nullable
   private ItemKineticWeapon.Condition damageConditions;
   private float forwardMovement;
   private float damageMultiplier;
   @Nullable
   private Sound sound;
   @Nullable
   private Sound hitSound;

   public ItemKineticWeapon(int contactCooldownTicks, int delayTicks, @Nullable ItemKineticWeapon.Condition dismountConditions, @Nullable ItemKineticWeapon.Condition knockbackConditions, @Nullable ItemKineticWeapon.Condition damageConditions, float forwardMovement, float damageMultiplier, @Nullable Sound sound, @Nullable Sound hitSound) {
      this.contactCooldownTicks = contactCooldownTicks;
      this.delayTicks = delayTicks;
      this.dismountConditions = dismountConditions;
      this.knockbackConditions = knockbackConditions;
      this.damageConditions = damageConditions;
      this.forwardMovement = forwardMovement;
      this.damageMultiplier = damageMultiplier;
      this.sound = sound;
      this.hitSound = hitSound;
   }

   public static ItemKineticWeapon read(PacketWrapper<?> wrapper) {
      int contactCooldownTicks = wrapper.readVarInt();
      int delayTicks = wrapper.readVarInt();
      ItemKineticWeapon.Condition dismountConditions = (ItemKineticWeapon.Condition)wrapper.readOptional(ItemKineticWeapon.Condition::read);
      ItemKineticWeapon.Condition knockbackConditions = (ItemKineticWeapon.Condition)wrapper.readOptional(ItemKineticWeapon.Condition::read);
      ItemKineticWeapon.Condition damageConditions = (ItemKineticWeapon.Condition)wrapper.readOptional(ItemKineticWeapon.Condition::read);
      float forwardMovement = wrapper.readFloat();
      float damageMultiplier = wrapper.readFloat();
      Sound sound = (Sound)wrapper.readOptional(Sound::read);
      Sound hitSound = (Sound)wrapper.readOptional(Sound::read);
      return new ItemKineticWeapon(contactCooldownTicks, delayTicks, dismountConditions, knockbackConditions, damageConditions, forwardMovement, damageMultiplier, sound, hitSound);
   }

   public static void write(PacketWrapper<?> wrapper, ItemKineticWeapon component) {
      wrapper.writeVarInt(component.contactCooldownTicks);
      wrapper.writeVarInt(component.delayTicks);
      wrapper.writeOptional(component.dismountConditions, ItemKineticWeapon.Condition::write);
      wrapper.writeOptional(component.knockbackConditions, ItemKineticWeapon.Condition::write);
      wrapper.writeOptional(component.damageConditions, ItemKineticWeapon.Condition::write);
      wrapper.writeFloat(component.forwardMovement);
      wrapper.writeFloat(component.damageMultiplier);
      wrapper.writeOptional(component.sound, Sound::write);
      wrapper.writeOptional(component.hitSound, Sound::write);
   }

   public int getContactCooldownTicks() {
      return this.contactCooldownTicks;
   }

   public void setContactCooldownTicks(int contactCooldownTicks) {
      this.contactCooldownTicks = contactCooldownTicks;
   }

   public int getDelayTicks() {
      return this.delayTicks;
   }

   public void setDelayTicks(int delayTicks) {
      this.delayTicks = delayTicks;
   }

   @Nullable
   public ItemKineticWeapon.Condition getDismountConditions() {
      return this.dismountConditions;
   }

   public void setDismountConditions(@Nullable ItemKineticWeapon.Condition dismountConditions) {
      this.dismountConditions = dismountConditions;
   }

   @Nullable
   public ItemKineticWeapon.Condition getKnockbackConditions() {
      return this.knockbackConditions;
   }

   public void setKnockbackConditions(@Nullable ItemKineticWeapon.Condition knockbackConditions) {
      this.knockbackConditions = knockbackConditions;
   }

   @Nullable
   public ItemKineticWeapon.Condition getDamageConditions() {
      return this.damageConditions;
   }

   public void setDamageConditions(@Nullable ItemKineticWeapon.Condition damageConditions) {
      this.damageConditions = damageConditions;
   }

   public float getForwardMovement() {
      return this.forwardMovement;
   }

   public void setForwardMovement(float forwardMovement) {
      this.forwardMovement = forwardMovement;
   }

   public float getDamageMultiplier() {
      return this.damageMultiplier;
   }

   public void setDamageMultiplier(float damageMultiplier) {
      this.damageMultiplier = damageMultiplier;
   }

   @Nullable
   public Sound getSound() {
      return this.sound;
   }

   public void setSound(@Nullable Sound sound) {
      this.sound = sound;
   }

   @Nullable
   public Sound getHitSound() {
      return this.hitSound;
   }

   public void setHitSound(@Nullable Sound hitSound) {
      this.hitSound = hitSound;
   }

   public boolean equals(Object obj) {
      if (obj != null && this.getClass() == obj.getClass()) {
         ItemKineticWeapon that = (ItemKineticWeapon)obj;
         if (this.contactCooldownTicks != that.contactCooldownTicks) {
            return false;
         } else if (this.delayTicks != that.delayTicks) {
            return false;
         } else if (Float.compare(that.forwardMovement, this.forwardMovement) != 0) {
            return false;
         } else if (Float.compare(that.damageMultiplier, this.damageMultiplier) != 0) {
            return false;
         } else if (!Objects.equals(this.dismountConditions, that.dismountConditions)) {
            return false;
         } else if (!Objects.equals(this.knockbackConditions, that.knockbackConditions)) {
            return false;
         } else if (!Objects.equals(this.damageConditions, that.damageConditions)) {
            return false;
         } else {
            return !Objects.equals(this.sound, that.sound) ? false : Objects.equals(this.hitSound, that.hitSound);
         }
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.contactCooldownTicks, this.delayTicks, this.dismountConditions, this.knockbackConditions, this.damageConditions, this.forwardMovement, this.damageMultiplier, this.sound, this.hitSound});
   }

   public static class Condition {
      private int maxDurationTicks;
      private float minSpeed;
      private float minRelativeSpeed;

      public Condition(int maxDurationTicks, float minSpeed, float minRelativeSpeed) {
         this.maxDurationTicks = maxDurationTicks;
         this.minSpeed = minSpeed;
         this.minRelativeSpeed = minRelativeSpeed;
      }

      public static ItemKineticWeapon.Condition read(PacketWrapper<?> wrapper) {
         int maxDurationTicks = wrapper.readVarInt();
         float minSpeed = wrapper.readFloat();
         float minRelativeSpeed = wrapper.readFloat();
         return new ItemKineticWeapon.Condition(maxDurationTicks, minSpeed, minRelativeSpeed);
      }

      public static void write(PacketWrapper<?> wrapper, ItemKineticWeapon.Condition condition) {
         wrapper.writeVarInt(condition.maxDurationTicks);
         wrapper.writeFloat(condition.minSpeed);
         wrapper.writeFloat(condition.minRelativeSpeed);
      }

      public int getMaxDurationTicks() {
         return this.maxDurationTicks;
      }

      public void setMaxDurationTicks(int maxDurationTicks) {
         this.maxDurationTicks = maxDurationTicks;
      }

      public float getMinSpeed() {
         return this.minSpeed;
      }

      public void setMinSpeed(float minSpeed) {
         this.minSpeed = minSpeed;
      }

      public float getMinRelativeSpeed() {
         return this.minRelativeSpeed;
      }

      public void setMinRelativeSpeed(float minRelativeSpeed) {
         this.minRelativeSpeed = minRelativeSpeed;
      }

      public boolean equals(Object obj) {
         if (obj != null && this.getClass() == obj.getClass()) {
            ItemKineticWeapon.Condition condition = (ItemKineticWeapon.Condition)obj;
            if (this.maxDurationTicks != condition.maxDurationTicks) {
               return false;
            } else if (Float.compare(condition.minSpeed, this.minSpeed) != 0) {
               return false;
            } else {
               return Float.compare(condition.minRelativeSpeed, this.minRelativeSpeed) == 0;
            }
         } else {
            return false;
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.maxDurationTicks, this.minSpeed, this.minRelativeSpeed});
      }
   }
}
