package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.Action;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class NoticeDialog extends AbstractMappedEntity implements Dialog {
   public static final ActionButton DEFAULT_ACTION = new ActionButton(new CommonButtonData(Component.translatable("gui.ok"), (Component)null, 150), (Action)null);
   private final CommonDialogData common;
   private final ActionButton action;

   public NoticeDialog(CommonDialogData common, ActionButton action) {
      this((TypesBuilderData)null, common, action);
   }

   @ApiStatus.Internal
   public NoticeDialog(@Nullable TypesBuilderData data, CommonDialogData common, ActionButton action) {
      super(data);
      this.common = common;
      this.action = action;
   }

   public static NoticeDialog decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      CommonDialogData common = CommonDialogData.decode(compound, wrapper);
      ActionButton action = (ActionButton)compound.getOr("action", ActionButton::decode, DEFAULT_ACTION, wrapper);
      return new NoticeDialog((TypesBuilderData)null, common, action);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, NoticeDialog dialog) {
      CommonDialogData.encode(compound, wrapper, dialog.common);
      if (dialog.action != DEFAULT_ACTION) {
         compound.set("action", dialog.action, ActionButton::encode, wrapper);
      }

   }

   public Dialog copy(@Nullable TypesBuilderData newData) {
      return new NoticeDialog(newData, this.common, this.action);
   }

   public CommonDialogData getCommon() {
      return this.common;
   }

   public ActionButton getAction() {
      return this.action;
   }

   public DialogType<?> getType() {
      return DialogTypes.NOTICE;
   }

   public boolean deepEquals(@Nullable Object obj) {
      if (!(obj instanceof NoticeDialog)) {
         return false;
      } else {
         NoticeDialog that = (NoticeDialog)obj;
         return !this.common.equals(that.common) ? false : this.action.equals(that.action);
      }
   }

   public int deepHashCode() {
      return Objects.hash(new Object[]{this.common, this.action});
   }
}
