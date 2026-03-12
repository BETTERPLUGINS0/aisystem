package ac.grim.grimac.checks.impl.crash;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.PacketEvents;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.manager.server.ServerVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerOpenWindow;
import ac.grim.grimac.utils.inventory.inventory.MenuType;

@CheckData(
   name = "CrashD",
   description = "Clicking slots in lectern window"
)
public class CrashD extends Check implements PacketCheck {
   private MenuType type;
   private int lecternId;

   public CrashD(GrimPlayer playerData) {
      super(playerData);
      this.type = MenuType.UNKNOWN;
      this.lecternId = -1;
   }

   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Play.Server.OPEN_WINDOW && this.isSupportedVersion()) {
         WrapperPlayServerOpenWindow window = new WrapperPlayServerOpenWindow(event);
         this.type = MenuType.getMenuType(window.getType());
         if (this.type == MenuType.LECTERN) {
            this.lecternId = window.getContainerId();
         }
      }

   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW && this.isSupportedVersion()) {
         WrapperPlayClientClickWindow click = new WrapperPlayClientClickWindow(event);
         int clickType = click.getWindowClickType().ordinal();
         int button = click.getButton();
         int windowId = click.getWindowId();
         if (this.type == MenuType.LECTERN && windowId > 0 && windowId == this.lecternId && this.flagAndAlert("clickType=" + clickType + " button=" + button)) {
            event.setCancelled(true);
            this.player.onPacketCancel();
         }
      }

   }

   private boolean isSupportedVersion() {
      return PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_14);
   }
}
