package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DialogTypes {
   private static final VersionedRegistry<DialogType<?>> REGISTRY = new VersionedRegistry("dialog_type");
   public static final DialogType<NoticeDialog> NOTICE = define("notice", NoticeDialog::decode, NoticeDialog::encode);
   public static final DialogType<ServerLinksDialog> SERVER_LINKS = define("server_links", ServerLinksDialog::decode, ServerLinksDialog::encode);
   public static final DialogType<DialogListDialog> DIALOG_LIST = define("dialog_list", DialogListDialog::decode, DialogListDialog::encode);
   public static final DialogType<MultiActionDialog> MULTI_ACTION = define("multi_action", MultiActionDialog::decode, MultiActionDialog::encode);
   public static final DialogType<ConfirmationDialog> CONFIRMATION = define("confirmation", ConfirmationDialog::decode, ConfirmationDialog::encode);

   private DialogTypes() {
   }

   public static VersionedRegistry<DialogType<?>> getRegistry() {
      return REGISTRY;
   }

   @ApiStatus.Internal
   public static <T extends Dialog> DialogType<T> define(String name, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
      return (DialogType)REGISTRY.define(name, (data) -> {
         return new StaticDialogType(data, decoder, encoder);
      });
   }

   static {
      REGISTRY.unloadMappings();
   }
}
