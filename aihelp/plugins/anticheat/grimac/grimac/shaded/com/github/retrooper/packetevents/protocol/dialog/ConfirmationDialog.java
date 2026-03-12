package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ConfirmationDialog extends AbstractMappedEntity implements Dialog {
   private final CommonDialogData common;
   private final ActionButton yesButton;
   private final ActionButton noButton;

   public ConfirmationDialog(CommonDialogData common, ActionButton yesButton, ActionButton noButton) {
      this((TypesBuilderData)null, common, yesButton, noButton);
   }

   @ApiStatus.Internal
   public ConfirmationDialog(@Nullable TypesBuilderData data, CommonDialogData common, ActionButton yesButton, ActionButton noButton) {
      super(data);
      this.common = common;
      this.yesButton = yesButton;
      this.noButton = noButton;
   }

   public static ConfirmationDialog decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      CommonDialogData common = CommonDialogData.decode(compound, wrapper);
      ActionButton yesButton = (ActionButton)compound.getOrThrow("yes", ActionButton::decode, wrapper);
      ActionButton noButton = (ActionButton)compound.getOrThrow("no", ActionButton::decode, wrapper);
      return new ConfirmationDialog((TypesBuilderData)null, common, yesButton, noButton);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ConfirmationDialog dialog) {
      CommonDialogData.encode(compound, wrapper, dialog.common);
      compound.set("yes", dialog.yesButton, ActionButton::encode, wrapper);
      compound.set("no", dialog.noButton, ActionButton::encode, wrapper);
   }

   public Dialog copy(@Nullable TypesBuilderData newData) {
      return new ConfirmationDialog(newData, this.common, this.yesButton, this.noButton);
   }

   public CommonDialogData getCommon() {
      return this.common;
   }

   public ActionButton getYesButton() {
      return this.yesButton;
   }

   public ActionButton getNoButton() {
      return this.noButton;
   }

   public DialogType<?> getType() {
      return DialogTypes.CONFIRMATION;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (!(obj instanceof ConfirmationDialog)) {
         return false;
      } else {
         ConfirmationDialog that = (ConfirmationDialog)obj;
         if (!this.common.equals(that.common)) {
            return false;
         } else {
            return !this.yesButton.equals(that.yesButton) ? false : this.noButton.equals(that.noButton);
         }
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.common, this.yesButton, this.noButton});
   }
}
