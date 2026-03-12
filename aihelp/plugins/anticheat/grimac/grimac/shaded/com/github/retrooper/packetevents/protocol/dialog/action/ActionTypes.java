package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ActionTypes {
   private static final VersionedRegistry<ActionType<?>> REGISTRY = new VersionedRegistry("dialog_action_type");
   public static final ActionType<StaticAction> OPEN_URL = define("open_url", StaticAction::decode, StaticAction::encode);
   public static final ActionType<StaticAction> RUN_COMMAND = define("run_command", StaticAction::decode, StaticAction::encode);
   public static final ActionType<StaticAction> SUGGEST_COMMAND = define("suggest_command", StaticAction::decode, StaticAction::encode);
   public static final ActionType<StaticAction> SHOW_DIALOG = define("show_dialog", StaticAction::decode, StaticAction::encode);
   public static final ActionType<StaticAction> CHANGE_PAGE = define("change_page", StaticAction::decode, StaticAction::encode);
   public static final ActionType<StaticAction> COPY_TO_CLIPBOARD = define("copy_to_clipboard", StaticAction::decode, StaticAction::encode);
   public static final ActionType<StaticAction> CUSTOM = define("custom", StaticAction::decode, StaticAction::encode);
   public static final ActionType<DynamicRunCommandAction> DYNAMIC_RUN_COMMAND = define("dynamic/run_command", DynamicRunCommandAction::decode, DynamicRunCommandAction::encode);
   public static final ActionType<DynamicCustomAction> DYNAMIC_CUSTOM = define("dynamic/custom", DynamicCustomAction::decode, DynamicCustomAction::encode);

   private ActionTypes() {
   }

   @ApiStatus.Internal
   public static <T extends Action> ActionType<T> define(String name, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
      return (ActionType)REGISTRY.define(name, (data) -> {
         return new StaticActionType(data, decoder, encoder);
      });
   }

   public static VersionedRegistry<ActionType<?>> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
