package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public final class EnchantmentCost {
   private final int base;
   private final int perLevelAboveFirst;

   public EnchantmentCost(int base, int perLevelAboveFirst) {
      this.base = base;
      this.perLevelAboveFirst = perLevelAboveFirst;
   }

   /** @deprecated */
   @Deprecated
   public static EnchantmentCost decode(NBT nbt, ClientVersion version) {
      return decode(nbt, PacketWrapper.createDummyWrapper(version));
   }

   public static EnchantmentCost decode(NBT nbt, PacketWrapper<?> wrapper) {
      NBTCompound compound = (NBTCompound)nbt;
      int base = compound.getNumberTagOrThrow("base").getAsInt();
      int perLevelAboveFirst = compound.getNumberTagOrThrow("per_level_above_first").getAsInt();
      return new EnchantmentCost(base, perLevelAboveFirst);
   }

   /** @deprecated */
   @Deprecated
   public static NBT encode(EnchantmentCost cost, ClientVersion version) {
      return encode(PacketWrapper.createDummyWrapper(version), cost);
   }

   public static NBT encode(PacketWrapper<?> wrapper, EnchantmentCost cost) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("base", new NBTInt(cost.base));
      compound.setTag("per_level_above_first", new NBTInt(cost.perLevelAboveFirst));
      return compound;
   }

   public int getBase() {
      return this.base;
   }

   public int getPerLevelAboveFirst() {
      return this.perLevelAboveFirst;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof EnchantmentCost)) {
         return false;
      } else {
         EnchantmentCost that = (EnchantmentCost)obj;
         if (this.base != that.base) {
            return false;
         } else {
            return this.perLevelAboveFirst == that.perLevelAboveFirst;
         }
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.base, this.perLevelAboveFirst});
   }

   public String toString() {
      return "EnchantmentCost{base=" + this.base + ", perLevelAboveFirst=" + this.perLevelAboveFirst + '}';
   }
}
