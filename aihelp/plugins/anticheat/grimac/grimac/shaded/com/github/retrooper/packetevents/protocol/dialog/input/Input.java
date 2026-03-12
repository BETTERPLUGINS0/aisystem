package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Input {
   private final String key;
   private final InputControl control;

   public Input(String key, InputControl control) {
      this.key = key;
      this.control = control;
   }

   public static Input decode(NBT nbt, PacketWrapper<?> wrapper) {
      NBTCompound compound = (NBTCompound)nbt;
      String key = compound.getStringTagValueOrThrow("key");
      InputControl control = InputControl.decode(compound, wrapper);
      return new Input(key, control);
   }

   public static NBT encode(PacketWrapper<?> wrapper, Input input) {
      NBTCompound compound = new NBTCompound();
      compound.setTag("key", new NBTString(input.key));
      InputControl.encode(compound, wrapper, input.control);
      return compound;
   }

   public String getKey() {
      return this.key;
   }

   public InputControl getControl() {
      return this.control;
   }
}
