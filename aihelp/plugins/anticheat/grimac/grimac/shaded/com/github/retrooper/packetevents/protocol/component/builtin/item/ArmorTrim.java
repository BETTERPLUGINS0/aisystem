package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimmaterial.TrimMaterial;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.trimpattern.TrimPattern;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;

public class ArmorTrim {
   private TrimMaterial material;
   private TrimPattern pattern;
   @ApiStatus.Obsolete
   private boolean showInTooltip;

   public ArmorTrim(TrimMaterial material, TrimPattern pattern) {
      this(material, pattern, true);
   }

   @ApiStatus.Obsolete
   public ArmorTrim(TrimMaterial material, TrimPattern pattern, boolean showInTooltip) {
      this.material = material;
      this.pattern = pattern;
      this.showInTooltip = showInTooltip;
   }

   public static ArmorTrim read(PacketWrapper<?> wrapper) {
      TrimMaterial material = TrimMaterial.read(wrapper);
      TrimPattern pattern = TrimPattern.read(wrapper);
      boolean showInTooltip = wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_5) || wrapper.readBoolean();
      return new ArmorTrim(material, pattern, showInTooltip);
   }

   public static void write(PacketWrapper<?> wrapper, ArmorTrim trim) {
      TrimMaterial.write(wrapper, trim.material);
      TrimPattern.write(wrapper, trim.pattern);
      if (wrapper.getServerVersion().isOlderThan(ServerVersion.V_1_21_5)) {
         wrapper.writeBoolean(trim.showInTooltip);
      }

   }

   public TrimMaterial getMaterial() {
      return this.material;
   }

   public void setMaterial(TrimMaterial material) {
      this.material = material;
   }

   public TrimPattern getPattern() {
      return this.pattern;
   }

   public void setPattern(TrimPattern pattern) {
      this.pattern = pattern;
   }

   @ApiStatus.Obsolete
   public boolean isShowInTooltip() {
      return this.showInTooltip;
   }

   @ApiStatus.Obsolete
   public void setShowInTooltip(boolean showInTooltip) {
      this.showInTooltip = showInTooltip;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ArmorTrim)) {
         return false;
      } else {
         ArmorTrim armorTrim = (ArmorTrim)obj;
         if (this.showInTooltip != armorTrim.showInTooltip) {
            return false;
         } else {
            return !this.material.equals(armorTrim.material) ? false : this.pattern.equals(armorTrim.pattern);
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.material, this.pattern, this.showInTooltip});
   }

   public String toString() {
      return "ArmorTrim{material=" + this.material + ", pattern=" + this.pattern + ", showInTooltip=" + this.showInTooltip + '}';
   }
}
