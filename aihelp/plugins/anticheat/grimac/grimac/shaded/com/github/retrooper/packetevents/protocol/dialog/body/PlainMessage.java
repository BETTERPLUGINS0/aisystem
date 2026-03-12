package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PlainMessage {
   private final Component contents;
   private final int width;

   public PlainMessage(Component contents, int width) {
      this.contents = contents;
      this.width = width;
   }

   public static PlainMessage decode(NBT nbt, PacketWrapper<?> wrapper) {
      return decode((NBTCompound)nbt, wrapper);
   }

   public static PlainMessage decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      Component contents = (Component)compound.getOrThrow("contents", AdventureSerializer.serializer(wrapper), wrapper);
      int width = compound.getNumberTagValueOrDefault("width", 200).intValue();
      return new PlainMessage(contents, width);
   }

   public static NBT encode(PacketWrapper<?> wrapper, PlainMessage message) {
      NBTCompound compound = new NBTCompound();
      encode(compound, wrapper, message);
      return compound;
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, PlainMessage message) {
      compound.set("contents", message.contents, AdventureSerializer.serializer(wrapper), wrapper);
      if (message.width != 200) {
         compound.setTag("width", new NBTInt(message.width));
      }

   }

   public Component getContents() {
      return this.contents;
   }

   public int getWidth() {
      return this.width;
   }
}
