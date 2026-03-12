package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTLong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;

public class ItemContainerLoot {
   private ResourceLocation lootTable;
   private long seed;

   public ItemContainerLoot(ResourceLocation lootTable, long seed) {
      this.lootTable = lootTable;
      this.seed = seed;
   }

   public static ItemContainerLoot read(PacketWrapper<?> wrapper) {
      NBTCompound compound = wrapper.readNBT();
      ResourceLocation lootTable = new ResourceLocation(compound.getStringTagValueOrThrow("loot_table"));
      NBTNumber seedTag = compound.getNumberTagOrNull("seed");
      long seed = seedTag == null ? 0L : seedTag.getAsLong();
      return new ItemContainerLoot(lootTable, seed);
   }

   public static void write(PacketWrapper<?> wrapper, ItemContainerLoot loot) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("loot_table", new NBTString(loot.lootTable.toString()));
      if (loot.seed != 0L) {
         compound.setTag("seed", new NBTLong(loot.seed));
      }

      wrapper.writeNBT(compound);
   }

   public ResourceLocation getLootTable() {
      return this.lootTable;
   }

   public void setLootTable(ResourceLocation lootTable) {
      this.lootTable = lootTable;
   }

   public long getSeed() {
      return this.seed;
   }

   public void setSeed(long seed) {
      this.seed = seed;
   }

   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof ItemContainerLoot)) {
         return false;
      } else {
         ItemContainerLoot that = (ItemContainerLoot)obj;
         return this.seed != that.seed ? false : this.lootTable.equals(that.lootTable);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.lootTable, this.seed});
   }
}
