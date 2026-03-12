package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.body;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class DialogBodyTypes {
   private static final VersionedRegistry<DialogBodyType<?>> REGISTRY = new VersionedRegistry("dialog_body_type");
   public static final DialogBodyType<ItemDialogBody> ITEM = define("item", ItemDialogBody::decode, ItemDialogBody::encode);
   public static final DialogBodyType<PlainMessageDialogBody> PLAIN_MESSAGE = define("plain_message", PlainMessageDialogBody::decode, PlainMessageDialogBody::encode);

   private DialogBodyTypes() {
   }

   @ApiStatus.Internal
   public static <T extends DialogBody> DialogBodyType<T> define(String name, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
      return (DialogBodyType)REGISTRY.define(name, (data) -> {
         return new StaticDialogBodyType(data, decoder, encoder);
      });
   }

   public static VersionedRegistry<DialogBodyType<?>> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
