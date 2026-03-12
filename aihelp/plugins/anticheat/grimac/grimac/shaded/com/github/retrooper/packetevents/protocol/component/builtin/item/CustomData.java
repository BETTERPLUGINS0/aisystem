package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.component.builtin.item;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureNbtUtil;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class CustomData {
   private CustomData() {
   }

   public static NBTCompound read(PacketWrapper<?> wrapper) {
      NBT nbt = wrapper.readNBTRaw();
      if (nbt instanceof NBTCompound) {
         return (NBTCompound)nbt;
      } else if (nbt instanceof NBTString) {
         String nbtString = ((NBTString)nbt).getValue();
         return (NBTCompound)AdventureNbtUtil.fromString(nbtString);
      } else {
         throw new UnsupportedOperationException("Unsupported custom data nbt type: " + nbt.getType());
      }
   }

   public static void write(PacketWrapper<?> wrapper, NBTCompound compound) {
      wrapper.writeNBT(compound);
   }
}
