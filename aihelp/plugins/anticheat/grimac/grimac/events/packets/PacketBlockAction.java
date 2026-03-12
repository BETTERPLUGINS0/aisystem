package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.Vector3i;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockAction;
import ac.grim.grimac.utils.data.ShulkerData;
import ac.grim.grimac.utils.nmsutil.Materials;

public class PacketBlockAction extends PacketListenerAbstract {
   public PacketBlockAction() {
      super(PacketListenerPriority.HIGH);
   }

   public void onPacketSend(PacketSendEvent event) {
      if (event.getPacketType() == PacketType.Play.Server.BLOCK_ACTION) {
         GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         WrapperPlayServerBlockAction blockAction = new WrapperPlayServerBlockAction(event);
         Vector3i blockPos = blockAction.getBlockPosition();
         player.latencyUtils.addRealTimeTask(player.lastTransactionSent.get(), () -> {
            WrappedBlockState existing = player.compensatedWorld.getBlock(blockPos);
            if (Materials.isShulker(existing.getType())) {
               ShulkerData data;
               if (blockAction.getActionData() >= 1) {
                  data = new ShulkerData(blockPos, player.lastTransactionSent.get(), false);
                  player.compensatedWorld.openShulkerBoxes.remove(data);
                  player.compensatedWorld.openShulkerBoxes.add(data);
               } else {
                  data = new ShulkerData(blockPos, player.lastTransactionSent.get(), true);
                  player.compensatedWorld.openShulkerBoxes.remove(data);
                  player.compensatedWorld.openShulkerBoxes.add(data);
               }
            }

         });
      }

   }
}
