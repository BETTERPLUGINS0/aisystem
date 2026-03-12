package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.consumables.ConsumeEffect;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class ItemConsumable {
   private float consumeSeconds;
   private ItemConsumable.Animation animation;
   private Sound sound;
   private boolean consumeParticles;
   private List<ConsumeEffect<?>> effects;

   public ItemConsumable(float consumeSeconds, ItemConsumable.Animation animation, Sound sound, boolean consumeParticles, List<ConsumeEffect<?>> effects) {
      this.consumeSeconds = consumeSeconds;
      this.animation = animation;
      this.sound = sound;
      this.consumeParticles = consumeParticles;
      this.effects = effects;
   }

   public static ItemConsumable read(PacketWrapper<?> wrapper) {
      float consumeSeconds = wrapper.readFloat();
      ItemConsumable.Animation animation = (ItemConsumable.Animation)wrapper.readEnum((Enum[])ItemConsumable.Animation.values());
      Sound sound = Sound.read(wrapper);
      boolean consumeParticles = wrapper.readBoolean();
      List<ConsumeEffect<?>> effects = wrapper.readList(ConsumeEffect::readFull);
      return new ItemConsumable(consumeSeconds, animation, sound, consumeParticles, effects);
   }

   public static void write(PacketWrapper<?> wrapper, ItemConsumable consumable) {
      wrapper.writeFloat(consumable.consumeSeconds);
      wrapper.writeEnum(consumable.animation);
      Sound.write(wrapper, consumable.sound);
      wrapper.writeBoolean(consumable.consumeParticles);
      wrapper.writeList(consumable.effects, ConsumeEffect::writeFull);
   }

   public float getConsumeSeconds() {
      return this.consumeSeconds;
   }

   public void setConsumeSeconds(float consumeSeconds) {
      this.consumeSeconds = consumeSeconds;
   }

   public ItemConsumable.Animation getAnimation() {
      return this.animation;
   }

   public void setAnimation(ItemConsumable.Animation animation) {
      this.animation = animation;
   }

   public Sound getSound() {
      return this.sound;
   }

   public void setSound(Sound sound) {
      this.sound = sound;
   }

   public boolean isConsumeParticles() {
      return this.consumeParticles;
   }

   public void setConsumeParticles(boolean consumeParticles) {
      this.consumeParticles = consumeParticles;
   }

   public List<ConsumeEffect<?>> getEffects() {
      return this.effects;
   }

   public void setEffects(List<ConsumeEffect<?>> effects) {
      this.effects = effects;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemConsumable)) {
         return false;
      } else {
         ItemConsumable that = (ItemConsumable)obj;
         if (Float.compare(that.consumeSeconds, this.consumeSeconds) != 0) {
            return false;
         } else if (this.consumeParticles != that.consumeParticles) {
            return false;
         } else if (this.animation != that.animation) {
            return false;
         } else {
            return !this.sound.equals(that.sound) ? false : this.effects.equals(that.effects);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.consumeSeconds, this.animation, this.sound, this.consumeParticles, this.effects});
   }

   public String toString() {
      return "ItemConsumable{consumeSeconds=" + this.consumeSeconds + ", animation=" + this.animation + ", sound=" + this.sound + ", consumeParticles=" + this.consumeParticles + ", effects=" + this.effects + '}';
   }

   public static enum Animation {
      NONE,
      EAT,
      DRINK,
      BLOCK,
      BOW,
      SPEAR,
      CROSSBOW,
      SPYGLASS,
      TOOT_HORN,
      BRUSH,
      BUNDLE;

      // $FF: synthetic method
      private static ItemConsumable.Animation[] $values() {
         return new ItemConsumable.Animation[]{NONE, EAT, DRINK, BLOCK, BOW, SPEAR, CROSSBOW, SPYGLASS, TOOT_HORN, BRUSH, BUNDLE};
      }
   }
}
