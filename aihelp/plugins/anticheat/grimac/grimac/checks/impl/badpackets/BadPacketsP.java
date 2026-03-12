package ac.grim.grimac.checks.impl.badpackets;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;

@CheckData(
   name = "BadPacketsP",
   description = "Invalid click packets",
   experimental = true
)
public class BadPacketsP extends Check implements PacketCheck {
   private int containerType = -1;
   private int containerId = -1;

   public BadPacketsP(GrimPlayer playerData) {
      super(playerData);
   }

   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Play.Server.OPEN_WINDOW) {
         WrapperPlayServerOpenWindow window = new WrapperPlayServerOpenWindow(event);
         this.containerType = window.getType();
         this.containerId = window.getContainerId();
      }

   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
         WrapperPlayClientClickWindow wrapper = new WrapperPlayClientClickWindow(event);
         WrapperPlayClientClickWindow.WindowClickType clickType = wrapper.getWindowClickType();
         int button = wrapper.getButton();
         boolean var10000;
         switch(clickType) {
         case PICKUP:
         case QUICK_MOVE:
         case CLONE:
            var10000 = button > 2 || button < 0;
            break;
         case SWAP:
            var10000 = (button > 8 || button < 0) && button != 40;
            break;
         case THROW:
            var10000 = button != 0 && button != 1;
            break;
         case QUICK_CRAFT:
            var10000 = button == 3 || button == 7 || button > 10 || button < 0;
            break;
         case PICKUP_ALL:
            var10000 = button != 0;
            break;
         case UNKNOWN:
            var10000 = true;
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         boolean flag = var10000;
         if (flag) {
            String var10001 = clickType.toString().toLowerCase();
            if (this.flagAndAlert("clickType=" + var10001 + ", button=" + button + (wrapper.getWindowId() == this.containerId ? ", container=" + this.containerType : "")) && this.shouldModifyPackets()) {
               event.setCancelled(true);
               this.player.onPacketCancel();
            }
         }
      }

   }
}
