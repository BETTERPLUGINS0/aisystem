package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action.Action;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Collections;
import java.util.Objects;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class Dialogs {
   private static final ActionButton DEFAULT_BACK_BUTTON = new ActionButton(new CommonButtonData(Component.translatable("gui.back"), (Component)null, 200), (Action)null);
   private static final VersionedRegistry<Dialog> REGISTRY = new VersionedRegistry("dialog");
   public static final Dialog SERVER_LINKS;
   public static final Dialog CUSTOM_OPTIONS;
   public static final Dialog QUICK_ACTIONS;

   private Dialogs() {
   }

   @ApiStatus.Internal
   public static Dialog define(String name, Dialog dialog) {
      VersionedRegistry var10000 = REGISTRY;
      Objects.requireNonNull(dialog);
      return (Dialog)var10000.define(name, dialog::copy);
   }

   public static VersionedRegistry<Dialog> getRegistry() {
      return REGISTRY;
   }

   static {
      SERVER_LINKS = define("server_links", new ServerLinksDialog(new CommonDialogData(Component.translatable("menu.server_links.title"), Component.translatable("menu.server_links"), true, true, DialogAction.CLOSE, Collections.emptyList(), Collections.emptyList()), DEFAULT_BACK_BUTTON, 1, 310));
      CUSTOM_OPTIONS = define("custom_options", new DialogListDialog(new CommonDialogData(Component.translatable("menu.custom_options.title"), Component.translatable("menu.custom_options"), true, true, DialogAction.CLOSE, Collections.emptyList(), Collections.emptyList()), new MappedEntitySet(new ResourceLocation("pause_screen_additions")), DEFAULT_BACK_BUTTON, 1, 310));
      QUICK_ACTIONS = define("quick_actions", new DialogListDialog(new CommonDialogData(Component.translatable("menu.quick_actions.title"), Component.translatable("menu.quick_actions"), true, true, DialogAction.CLOSE, Collections.emptyList(), Collections.emptyList()), new MappedEntitySet(new ResourceLocation("quick_actions")), DEFAULT_BACK_BUTTON, 1, 310));
      REGISTRY.unloadMappings();
   }
}
