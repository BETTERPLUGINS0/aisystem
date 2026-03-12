package ac.grim.grimac.checks.impl.crash;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;

@CheckData(
   name = "CrashF"
)
public class CrashF extends Check implements PacketCheck {
   public CrashF(GrimPlayer playerData) {
      super(playerData);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
         WrapperPlayClientClickWindow click = new WrapperPlayClientClickWindow(event);
         WrapperPlayClientClickWindow.WindowClickType clickType = click.getWindowClickType();
         int button = click.getButton();
         int windowId = click.getWindowId();
         int slot = click.getSlot();
         if ((clickType == WrapperPlayClientClickWindow.WindowClickType.QUICK_MOVE || clickType == WrapperPlayClientClickWindow.WindowClickType.SWAP) && windowId >= 0 && button < 0) {
            String var10001 = String.valueOf(clickType);
            if (this.flagAndAlert("clickType=" + var10001 + " button=" + button)) {
               event.setCancelled(true);
               this.player.onPacketCancel();
            }
         } else if (windowId >= 0 && clickType == WrapperPlayClientClickWindow.WindowClickType.SWAP && slot < 0 && this.flagAndAlert("clickType=" + String.valueOf(clickType) + " button=" + button + " slot=" + slot)) {
            event.setCancelled(true);
            this.player.onPacketCancel();
         }
      }

   }
}
