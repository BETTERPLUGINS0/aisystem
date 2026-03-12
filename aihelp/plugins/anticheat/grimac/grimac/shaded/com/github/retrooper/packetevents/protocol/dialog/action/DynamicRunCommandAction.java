package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DynamicRunCommandAction implements Action {
   private final DialogTemplate template;

   public DynamicRunCommandAction(DialogTemplate template) {
      this.template = template;
   }

   public static DynamicRunCommandAction decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      DialogTemplate template = (DialogTemplate)compound.getOrThrow("template", DialogTemplate::decode, wrapper);
      return new DynamicRunCommandAction(template);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, DynamicRunCommandAction action) {
      compound.set("template", action.template, DialogTemplate::encode, wrapper);
   }

   public DialogTemplate getTemplate() {
      return this.template;
   }

   public ActionType<?> getType() {
      return ActionTypes.DYNAMIC_RUN_COMMAND;
   }
}
