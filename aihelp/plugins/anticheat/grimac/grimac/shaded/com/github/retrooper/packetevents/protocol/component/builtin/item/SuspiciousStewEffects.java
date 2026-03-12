package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.List;
import java.util.Objects;

public class SuspiciousStewEffects {
   private List<SuspiciousStewEffects.EffectEntry> effects;

   public SuspiciousStewEffects(List<SuspiciousStewEffects.EffectEntry> effects) {
      this.effects = effects;
   }

   public static SuspiciousStewEffects read(PacketWrapper<?> wrapper) {
      List<SuspiciousStewEffects.EffectEntry> effects = wrapper.readList(SuspiciousStewEffects.EffectEntry::read);
      return new SuspiciousStewEffects(effects);
   }

   public static void write(PacketWrapper<?> wrapper, SuspiciousStewEffects effects) {
      wrapper.writeList(effects.effects, SuspiciousStewEffects.EffectEntry::write);
   }

   public void addEffect(SuspiciousStewEffects.EffectEntry effect) {
      this.effects.add(effect);
   }

   public List<SuspiciousStewEffects.EffectEntry> getEffects() {
      return this.effects;
   }

   public void setEffects(List<SuspiciousStewEffects.EffectEntry> effects) {
      this.effects = effects;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof SuspiciousStewEffects)) {
         return false;
      } else {
         SuspiciousStewEffects that = (SuspiciousStewEffects)obj;
         return this.effects.equals(that.effects);
      }
   }

   public int hashCode() {
      return Objects.hashCode(this.effects);
   }

   public static class EffectEntry {
      private PotionType type;
      private int duration;

      public EffectEntry(PotionType type, int duration) {
         this.type = type;
         this.duration = duration;
      }

      public static SuspiciousStewEffects.EffectEntry read(PacketWrapper<?> wrapper) {
         PotionType type = (PotionType)wrapper.readMappedEntity(PotionTypes::getById);
         int duration = wrapper.readVarInt();
         return new SuspiciousStewEffects.EffectEntry(type, duration);
      }

      public static void write(PacketWrapper<?> wrapper, SuspiciousStewEffects.EffectEntry effect) {
         wrapper.writeMappedEntity(effect.type);
         wrapper.writeVarInt(effect.duration);
      }

      public PotionType getType() {
         return this.type;
      }

      public void setType(PotionType type) {
         this.type = type;
      }

      public int getDuration() {
         return this.duration;
      }

      public void setDuration(int duration) {
         this.duration = duration;
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (!(obj instanceof SuspiciousStewEffects.EffectEntry)) {
            return false;
         } else {
            SuspiciousStewEffects.EffectEntry that = (SuspiciousStewEffects.EffectEntry)obj;
            return this.duration != that.duration ? false : this.type.equals(that.type);
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.type, this.duration});
      }
   }
}
