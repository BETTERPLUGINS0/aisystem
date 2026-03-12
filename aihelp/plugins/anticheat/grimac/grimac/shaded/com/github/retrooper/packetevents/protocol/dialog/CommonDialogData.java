package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.input.Input;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTByte;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.List;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class CommonDialogData {
   private final Component title;
   @Nullable
   private final Component externalTitle;
   private final boolean canCloseWithEscape;
   private final boolean pause;
   private final DialogAction afterAction;
   private final List<DialogBody> body;
   private final List<Input> inputs;

   public CommonDialogData(Component title, @Nullable Component externalTitle, boolean canCloseWithEscape, boolean pause, DialogAction afterAction, List<DialogBody> body, List<Input> inputs) {
      if (pause && !afterAction.isWillUnpause()) {
         throw new IllegalArgumentException("Dialogs that pause the game must use after_action values that unpause it after user action!");
      } else {
         this.title = title;
         this.externalTitle = externalTitle;
         this.canCloseWithEscape = canCloseWithEscape;
         this.pause = pause;
         this.afterAction = afterAction;
         this.body = body;
         this.inputs = inputs;
      }
   }

   public static CommonDialogData decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      Component title = (Component)compound.getOrThrow("title", AdventureSerializer.serializer(wrapper), wrapper);
      Component externalTitle = (Component)compound.getOrNull("external_title", AdventureSerializer.serializer(wrapper), wrapper);
      boolean canCloseWithEscape = compound.getBooleanOr("can_close_with_escape", true);
      boolean pause = compound.getBooleanOr("pause", true);
      DialogAction afterAction = (DialogAction)compound.getOr("after_action", DialogAction::decode, DialogAction.CLOSE, wrapper);
      List<DialogBody> body = compound.getListOrEmpty("body", DialogBody::decode, wrapper);
      List<Input> inputs = compound.getListOrEmpty("inputs", Input::decode, wrapper);
      return new CommonDialogData(title, externalTitle, canCloseWithEscape, pause, afterAction, body, inputs);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, CommonDialogData data) {
      compound.set("title", data.title, AdventureSerializer.serializer(wrapper), wrapper);
      if (data.externalTitle != null) {
         compound.set("external_title", data.externalTitle, AdventureSerializer.serializer(wrapper), wrapper);
      }

      if (!data.canCloseWithEscape) {
         compound.setTag("can_close_with_escape", new NBTByte(false));
      }

      if (!data.pause) {
         compound.setTag("pause", new NBTByte(false));
      }

      if (data.afterAction != DialogAction.CLOSE) {
         compound.set("after_action", data.afterAction, DialogAction::encode, wrapper);
      }

      if (!data.body.isEmpty()) {
         compound.setCompactList("body", data.body, DialogBody::encode, wrapper);
      }

      if (!data.inputs.isEmpty()) {
         compound.setList("inputs", data.inputs, Input::encode, wrapper);
      }

   }

   public Component getTitle() {
      return this.title;
   }

   @Nullable
   public Component getExternalTitle() {
      return this.externalTitle;
   }

   public boolean isCanCloseWithEscape() {
      return this.canCloseWithEscape;
   }

   public boolean isPause() {
      return this.pause;
   }

   public DialogAction getAfterAction() {
      return this.afterAction;
   }

   public List<DialogBody> getBody() {
      return this.body;
   }

   public List<Input> getInputs() {
      return this.inputs;
   }
}
