package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.dialog.action;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventAction;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.chat.clickevent.ClickEventActions;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class StaticAction implements Action {
   private final ActionType<?> actionType;
   private final ClickEvent clickEvent;

   public StaticAction(ClickEvent clickEvent) {
      if (!clickEvent.getAction().isAllowFromServer()) {
         throw new IllegalArgumentException("Can't create action for unreadable click event with action " + clickEvent.getAction());
      } else {
         this.actionType = (ActionType)ActionTypes.getRegistry().getByNameOrThrow(clickEvent.getAction().getName());
         this.clickEvent = clickEvent;
      }
   }

   public static StaticAction decode(NBTCompound compound, PacketWrapper<?> wrapper) {
      String actionName = compound.getStringTagValueOrThrow("type");
      ClickEventAction<?> action = (ClickEventAction)ClickEventActions.getRegistry().getByNameOrThrow(actionName);
      ClickEvent clickEvent = action.decode(compound, wrapper);
      return new StaticAction(clickEvent);
   }

   public static void encode(NBTCompound compound, PacketWrapper<?> wrapper, StaticAction action) {
      action.clickEvent.getAction().encode(compound, wrapper, action.clickEvent);
   }

   public ActionType<?> getType() {
      return this.actionType;
   }

   public ClickEvent getClickEvent() {
      return this.clickEvent;
   }
}
