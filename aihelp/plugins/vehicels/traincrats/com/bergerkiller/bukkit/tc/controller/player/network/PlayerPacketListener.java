package com.bergerkiller.bukkit.tc.controller.player.network;

import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.common.protocol.PacketListener;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.tc.TrainCarts;
import org.bukkit.entity.Player;

public interface PlayerPacketListener<L extends PacketListener> {
   static <L extends PacketListener> PlayerPacketListener<L> createNoOp(final Player player, final L packetListener) {
      return new PlayerPacketListener<L>() {
         public Player getPlayer() {
            return player;
         }

         public L getListener() {
            return packetListener;
         }

         public boolean isEnabled() {
            return false;
         }

         public PlayerPacketListener<L> enable() {
            return this;
         }

         public PlayerPacketListener<L> disable() {
            return this;
         }

         public void terminate() {
         }
      };
   }

   Player getPlayer();

   L getListener();

   boolean isEnabled();

   PlayerPacketListener<L> enable();

   PlayerPacketListener<L> disable();

   void terminate();

   public interface Provider extends LibraryComponent {
      static PlayerPacketListener.Provider create(TrainCarts traincarts) {
         return new PlayerPacketListenerProviderImpl(traincarts);
      }

      <L extends PacketListener> PlayerPacketListener<L> create(Player var1, L var2, PacketType... var3);

      void enable();

      void disable();
   }
}
