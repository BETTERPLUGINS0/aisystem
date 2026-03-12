package ac.grim.grimac.events.packets;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.player.GrimPlayer;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketListenerPriority;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.event.PacketSendEvent;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPong;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientWindowConfirmation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import ac.grim.grimac.utils.data.Pair;

public class PacketPingListener extends PacketListenerAbstract {
   public PacketPingListener() {
      super(PacketListenerPriority.LOWEST);
   }

   public void onPacketReceive(PacketReceiveEvent event) {
      if (event.getPacketType() == PacketType.Play.Client.WINDOW_CONFIRMATION) {
         WrapperPlayClientWindowConfirmation transaction = new WrapperPlayClientWindowConfirmation(event);
         short id = transaction.getActionId();
         GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         player.packetStateData.lastTransactionPacketWasValid = false;
         if (id <= 0 && player.addTransactionResponse(id)) {
            player.packetStateData.lastTransactionPacketWasValid = true;
            event.setCancelled(true);
         }
      }

      if (event.getPacketType() == PacketType.Play.Client.PONG) {
         WrapperPlayClientPong pong = new WrapperPlayClientPong(event);
         GrimPlayer player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         player.packetStateData.lastTransactionPacketWasValid = false;
         int id = pong.getId();
         if (id == (short)id) {
            short shortID = (short)id;
            if (player.addTransactionResponse(shortID)) {
               player.packetStateData.lastTransactionPacketWasValid = true;
               event.setCancelled(!GrimAPI.INSTANCE.getConfigManager().isDisablePongCancelling());
            }
         }
      }

   }

   public void onPacketSend(PacketSendEvent event) {
      GrimPlayer player;
      if (event.getPacketType() == PacketType.Play.Server.WINDOW_CONFIRMATION) {
         WrapperPlayServerWindowConfirmation confirmation = new WrapperPlayServerWindowConfirmation(event);
         short id = confirmation.getActionId();
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         player.packetStateData.lastServerTransWasValid = false;
         if (id <= 0 && player.didWeSendThatTrans.remove(id)) {
            player.packetStateData.lastServerTransWasValid = true;
            player.transactionsSent.add(new Pair(id, System.nanoTime()));
            player.lastTransactionSent.getAndIncrement();
         }
      }

      if (event.getPacketType() == PacketType.Play.Server.PING) {
         WrapperPlayServerPing pong = new WrapperPlayServerPing(event);
         int id = pong.getId();
         player = GrimAPI.INSTANCE.getPlayerDataManager().getPlayer(event.getUser());
         if (player == null) {
            return;
         }

         player.packetStateData.lastServerTransWasValid = false;
         if (id == (short)id) {
            Short shortID = (short)id;
            if (player.didWeSendThatTrans.remove(shortID)) {
               player.packetStateData.lastServerTransWasValid = true;
               player.transactionsSent.add(new Pair(shortID, System.nanoTime()));
               player.lastTransactionSent.getAndIncrement();
            }
         }
      }

   }
}
