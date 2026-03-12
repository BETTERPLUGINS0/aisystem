package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.recipe.display.slot;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;

public class SmithingTrimSlotDisplay extends SlotDisplay<SmithingTrimSlotDisplay> {
   private SlotDisplay<?> base;
   private SlotDisplay<?> material;
   private TrimPattern trimPattern;
   @ApiStatus.Obsolete
   private SlotDisplay<?> pattern;

   public SmithingTrimSlotDisplay(SlotDisplay<?> base, SlotDisplay<?> material, TrimPattern trimPattern) {
      super(SlotDisplayTypes.SMITHING_TRIM);
      this.base = base;
      this.material = material;
      this.trimPattern = trimPattern;
   }

   @ApiStatus.Obsolete
   public SmithingTrimSlotDisplay(SlotDisplay<?> base, SlotDisplay<?> material, SlotDisplay<?> pattern) {
      super(SlotDisplayTypes.SMITHING_TRIM);
      this.base = base;
      this.material = material;
      this.pattern = pattern;
   }

   public static SmithingTrimSlotDisplay read(PacketWrapper<?> wrapper) {
      SlotDisplay<?> base = SlotDisplay.read(wrapper);
      SlotDisplay<?> material = SlotDisplay.read(wrapper);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         TrimPattern trimPattern = TrimPattern.read(wrapper);
         return new SmithingTrimSlotDisplay(base, material, trimPattern);
      } else {
         SlotDisplay<?> pattern = SlotDisplay.read(wrapper);
         return new SmithingTrimSlotDisplay(base, material, pattern);
      }
   }

   public static void write(PacketWrapper<?> wrapper, SmithingTrimSlotDisplay display) {
      SlotDisplay.write(wrapper, display.base);
      SlotDisplay.write(wrapper, display.material);
      if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5)) {
         TrimPattern.write(wrapper, display.trimPattern);
      } else {
         SlotDisplay.write(wrapper, display.pattern);
      }

   }

   public SlotDisplay<?> getBase() {
      return this.base;
   }

   public void setBase(SlotDisplay<?> base) {
      this.base = base;
   }

   public SlotDisplay<?> getMaterial() {
      return this.material;
   }

   public void setMaterial(SlotDisplay<?> material) {
      this.material = material;
   }

   @ApiStatus.Obsolete
   public SlotDisplay<?> getPattern() {
      return this.pattern;
   }

   @ApiStatus.Obsolete
   public void setPattern(SlotDisplay<?> pattern) {
      this.pattern = pattern;
   }

   public TrimPattern getTrimPattern() {
      return this.trimPattern;
   }

   public void setTrimPattern(TrimPattern trimPattern) {
      this.trimPattern = trimPattern;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof SmithingTrimSlotDisplay)) {
         return false;
      } else {
         SmithingTrimSlotDisplay that = (SmithingTrimSlotDisplay)obj;
         if (!this.base.equals(that.base)) {
            return false;
         } else if (!this.material.equals(that.material)) {
            return false;
         } else {
            return !Objects.equals(this.pattern, that.pattern) ? false : Objects.equals(this.trimPattern, that.trimPattern);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.base, this.material, this.pattern, this.trimPattern});
   }

   public String toString() {
      return "SmithingTrimSlotDisplay{base=" + this.base + ", material=" + this.material + ", trimPattern=" + this.trimPattern + ", pattern=" + this.pattern + '}';
   }
}
