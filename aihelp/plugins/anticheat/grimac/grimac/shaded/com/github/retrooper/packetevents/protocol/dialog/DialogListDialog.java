package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntityRefSet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class DialogListDialog extends AbstractMappedEntity implements Dialog {
   private final CommonDialogData common;
   private final MappedEntityRefSet<Dialog> dialogs;
   @Nullable
   private final ActionButton exitAction;
   private final int columns;
   private final int buttonWidth;

   public DialogListDialog(CommonDialogData common, MappedEntityRefSet<Dialog> dialogs, @Nullable ActionButton exitAction, int columns, int buttonWidth) {
      this((TypesBuilderData)null, common, dialogs, exitAction, columns, buttonWidth);
   }

   @ApiStatus.Internal
   public DialogListDialog(@Nullable TypesBuilderData data, CommonDialogData common, MappedEntityRefSet<Dialog> dialogs, @Nullable ActionButton exitAction, int columns, int buttonWidth) {
      super(data);
      this.common = common;
      this.dialogs = dialogs;
      this.exitAction = exitAction;
      this.columns = columns;
      this.buttonWidth = buttonWidth;
   }

   public static DialogListDialog decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      CommonDialogData common = CommonDialogData.decode(compound, wrapper);
      MappedEntityRefSet<Dialog> dialogs = (MappedEntityRefSet)compound.getOrThrow("dialogs", MappedEntitySet::decodeRefSet, wrapper);
      ActionButton action = (ActionButton)compound.getOrNull("exit_action", ActionButton::decode, wrapper);
      int columns = compound.getNumberTagValueOrDefault("columns", 2).intValue();
      int buttonWidth = compound.getNumberTagValueOrDefault("button_width", 150).intValue();
      return new DialogListDialog((TypesBuilderData)null, common, dialogs, action, columns, buttonWidth);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, DialogListDialog dialog) {
      CommonDialogData.encode(compound, wrapper, dialog.common);
      compound.set("dialogs", dialog.dialogs, MappedEntitySet::encodeRefSet, wrapper);
      if (dialog.exitAction != null) {
         compound.set("exit_action", dialog.exitAction, ActionButton::encode, wrapper);
      }

      if (dialog.columns != 2) {
         compound.setTag("columns", new NBTInt(dialog.columns));
      }

      if (dialog.buttonWidth != 150) {
         compound.setTag("button_width", new NBTInt(dialog.buttonWidth));
      }

   }

   public Dialog copy(@Nullable TypesBuilderData newData) {
      return new DialogListDialog(newData, this.common, this.dialogs, this.exitAction, this.columns, this.buttonWidth);
   }

   public CommonDialogData getCommon() {
      return this.common;
   }

   public MappedEntityRefSet<Dialog> getDialogs() {
      return this.dialogs;
   }

   @Nullable
   public ActionButton getExitAction() {
      return this.exitAction;
   }

   public int getColumns() {
      return this.columns;
   }

   public int getButtonWidth() {
      return this.buttonWidth;
   }

   public DialogType<?> getType() {
      return DialogTypes.DIALOG_LIST;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (!(obj instanceof DialogListDialog)) {
         return false;
      } else {
         DialogListDialog that = (DialogListDialog)obj;
         if (this.columns != that.columns) {
            return false;
         } else if (this.buttonWidth != that.buttonWidth) {
            return false;
         } else if (!this.common.equals(that.common)) {
            return false;
         } else {
            return !this.dialogs.equals(that.dialogs) ? false : Objects.equals(this.exitAction, that.exitAction);
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.common, this.dialogs, this.exitAction, this.columns, this.buttonWidth});
   }
}
