package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapDecoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.util.NbtMapEncoder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ClickEventActions {
   private static final VersionedRegistry<ClickEventAction<?>> REGISTRY = new VersionedRegistry("click_event_action");
   public static final ClickEventAction<OpenUrlClickEvent> OPEN_URL = define("open_url", true, OpenUrlClickEvent::decode, OpenUrlClickEvent::encode);
   public static final ClickEventAction<OpenFileClickEvent> OPEN_FILE = define("open_file", false, OpenFileClickEvent::decode, OpenFileClickEvent::encode);
   public static final ClickEventAction<RunCommandClickEvent> RUN_COMMAND = define("run_command", true, RunCommandClickEvent::decode, RunCommandClickEvent::encode);
   @ApiStatus.Obsolete
   public static final ClickEventAction<TwitchUserInfoClickEvent> TWITCH_USER_INFO = define("twitch_user_info", false, TwitchUserInfoClickEvent::decode, TwitchUserInfoClickEvent::encode);
   public static final ClickEventAction<SuggestCommandClickEvent> SUGGEST_COMMAND = define("suggest_command", true, SuggestCommandClickEvent::decode, SuggestCommandClickEvent::encode);
   public static final ClickEventAction<ChangePageClickEvent> CHANGE_PAGE = define("change_page", true, ChangePageClickEvent::decode, ChangePageClickEvent::encode);
   public static final ClickEventAction<CopyToClipboardClickEvent> COPY_TO_CLIPBOARD = define("copy_to_clipboard", true, CopyToClipboardClickEvent::decode, CopyToClipboardClickEvent::encode);
   public static final ClickEventAction<ShowDialogClickEvent> SHOW_DIALOG = define("show_dialog", true, ShowDialogClickEvent::decode, ShowDialogClickEvent::encode);
   public static final ClickEventAction<CustomClickEvent> CUSTOM = define("custom", true, CustomClickEvent::decode, CustomClickEvent::encode);

   private ClickEventActions() {
   }

   @ApiStatus.Internal
   public static <T extends ClickEvent> ClickEventAction<T> define(String name, boolean allowFromServer, NbtMapDecoder<T> decoder, NbtMapEncoder<T> encoder) {
      return (ClickEventAction)REGISTRY.define(name, (data) -> {
         return new StaticClickEventAction(data, allowFromServer, decoder, encoder);
      });
   }

   public static VersionedRegistry<ClickEventAction<?>> getRegistry() {
      return REGISTRY;
   }

   static {
      REGISTRY.unloadMappings();
   }
}
