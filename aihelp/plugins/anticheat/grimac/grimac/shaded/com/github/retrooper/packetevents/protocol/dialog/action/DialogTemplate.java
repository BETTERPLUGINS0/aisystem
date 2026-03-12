package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTString;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DialogTemplate {
   private final String raw;

   public DialogTemplate(String raw) {
      this.raw = raw;
   }

   public static DialogTemplate decode(NBT nbt, PacketWrapper<?> wrapper) {
      return new DialogTemplate(((NBTString)nbt).getValue());
   }

   public static NBT encode(PacketWrapper<?> wrapper, DialogTemplate template) {
      return new NBTString(template.raw);
   }

   public String getRaw() {
      return this.raw;
   }
}
