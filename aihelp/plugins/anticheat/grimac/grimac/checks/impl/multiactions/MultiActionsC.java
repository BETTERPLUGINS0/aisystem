package ac.grim.grimac.checks.impl.multiactions;

import ac.grim.grimac.checks.Check;
import ac.grim.grimac.checks.CheckData;
import ac.grim.grimac.checks.type.PacketCheck;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.StringJoiner;

@CheckData(
   name = "MultiActionsC",
   description = "Clicked in inventory while moving"
)
public class MultiActionsC extends Check implements PacketCheck {
   public MultiActionsC(GrimPlayer player) {
      super(player);
   }

   @Contract(
      pure = true
   )
   public static String getVerbose(@NotNull GrimPlayer player) {
      StringJoiner verbose = new StringJoiner(", ");
      if (player.isSprinting && (!player.isSwimming || !player.clientClaimsLastOnGround)) {
         verbose.add("sprinting");
      }

      if (player.isSneaking && player.getClientVersion().isOlderThan(ClientVersion.V_1_15)) {
         verbose.add("sneaking");
      }

      if (player.supportsEndTick() && player.packetStateData.knownInput.moving()) {
         verbose.add("input");
      }

      return verbose.toString();
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW && !this.player.serverOpenedInventoryThisTick) {
         String verbose = getVerbose(this.player);
         if (!verbose.isEmpty() && this.flagAndAlert(verbose) && this.shouldModifyPackets()) {
            event.setCancelled(true);
            this.player.onPacketCancel();
         }
      }

   }
}
