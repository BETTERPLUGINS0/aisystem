package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PlainMessageDialogBody implements DialogBody {
   private final PlainMessage message;

   public PlainMessageDialogBody(PlainMessage message) {
      this.message = message;
   }

   public static PlainMessageDialogBody decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      return new PlainMessageDialogBody(PlainMessage.decode(compound, wrapper));
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, PlainMessageDialogBody body) {
      PlainMessage.encode(compound, wrapper, body.message);
   }

   public DialogBodyType<?> getType() {
      return DialogBodyTypes.PLAIN_MESSAGE;
   }

   public PlainMessage getMessage() {
      return this.message;
   }
}
