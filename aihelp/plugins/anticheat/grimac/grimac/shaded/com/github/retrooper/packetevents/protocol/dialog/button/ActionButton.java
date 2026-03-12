package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.button;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.Action;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBT;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ActionButton {
   private final CommonButtonData button;
   @Nullable
   private final Action action;

   public ActionButton(CommonButtonData button, @Nullable Action action) {
      this.button = button;
      this.action = action;
   }

   public static ActionButton decode(NBT nbt, PacketWrapper<?> wrapper) {
      NBTCompound compound = (NBTCompound)nbt;
      CommonButtonData button = CommonButtonData.decode(compound, wrapper);
      Action action = (Action)compound.getOrNull("action", Action::decode, wrapper);
      return new ActionButton(button, action);
   }

   public static NBT encode(PacketWrapper<?> wrapper, ActionButton button) {
      NBTCompound compound = new NBTCompound();
      CommonButtonData.encode(compound, wrapper, button.button);
      if (button.action != null) {
         compound.set("action", button.action, Action::encode, wrapper);
      }

      return compound;
   }

   public CommonButtonData getButton() {
      return this.button;
   }

   @Nullable
   public Action getAction() {
      return this.action;
   }
}
