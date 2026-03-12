package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.Dialog;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTEnd;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.adventure.NbtTagHolder;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ClickEvent {
   static ClickEvent decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      String actionName = compound.getStringTagValueOrThrow("action");
      ClickEventAction<?> action = (ClickEventAction)ClickEventActions.getRegistry().getByNameOrThrow(actionName);
      return action.decode(compound, wrapper);
   }

   static void encode(NBTCompound compound, PacketWrapper<?> wrapper, ClickEvent clickEvent) {
      compound.set("action", clickEvent.getAction().getName(), ResourceLocation::encode, wrapper);
      clickEvent.getAction().encode(compound, wrapper, clickEvent);
   }

   ClickEventAction<?> getAction();

   static ClickEvent fromAdventure(ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent clickEvent) {
      switch(clickEvent.action()) {
      case OPEN_URL:
         return new OpenUrlClickEvent(clickEvent.value());
      case OPEN_FILE:
         return new OpenFileClickEvent(clickEvent.value());
      case RUN_COMMAND:
         return new RunCommandClickEvent(clickEvent.value());
      case SUGGEST_COMMAND:
         return new SuggestCommandClickEvent(clickEvent.value());
      case CHANGE_PAGE:
         return new ChangePageClickEvent(clickEvent.value());
      case COPY_TO_CLIPBOARD:
         return new CopyToClipboardClickEvent(clickEvent.value());
      case SHOW_DIALOG:
         return new ShowDialogClickEvent((Dialog)((ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent.Payload.Dialog)clickEvent.payload()).dialog());
      case CUSTOM:
         ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent.Payload.Custom payload = (ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent.Payload.Custom)clickEvent.payload();
         NbtTagHolder nbtTag = (NbtTagHolder)payload.nbt();
         return new CustomClickEvent(new ResourceLocation(payload.key()), nbtTag.getTag() instanceof NBTEnd ? null : nbtTag.getTag());
      default:
         throw new UnsupportedOperationException("Unsupported clickevent: " + clickEvent);
      }
   }

   ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent asAdventure();
}
