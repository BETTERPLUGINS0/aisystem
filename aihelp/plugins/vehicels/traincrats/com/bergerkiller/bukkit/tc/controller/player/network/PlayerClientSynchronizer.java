package com.bergerkiller.bukkit.tc.controller.player.network;

import com.bergerkiller.bukkit.common.Common;
import com.bergerkiller.bukkit.common.component.LibraryComponent;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.attachments.api.AttachmentViewer;
import com.bergerkiller.generated.net.minecraft.network.protocol.PacketHandle;
import com.bergerkiller.generated.net.minecraft.network.protocol.game.PacketPlayOutPositionHandle;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import org.bukkit.entity.Player;

public interface PlayerClientSynchronizer {
   Player getPlayer();

   void synchronize(IntFunction<PacketPlayOutPositionHandle> var1, Consumer<PacketPlayOutPositionHandle> var2);

   void synchronizeBundle(List<? extends PacketHandle> var1, Runnable var2, Runnable var3);

   void synchronize(Runnable var1);

   public interface Provider extends LibraryComponent {
      PlayerClientSynchronizer forViewer(AttachmentViewer var1);

      default PlayerClientSynchronizer forPlayer(Player player) {
         return this.forViewer(AttachmentViewer.forPlayer(player));
      }

      default PlayerClientSynchronizer createNoOp(final Player player) {
         return new PlayerClientSynchronizer() {
            public Player getPlayer() {
               return player;
            }

            public void synchronize(IntFunction<PacketPlayOutPositionHandle> positionPacketMaker, Consumer<PacketPlayOutPositionHandle> callback) {
            }

            public void synchronizeBundle(List<? extends PacketHandle> packets, Runnable startCallback, Runnable endCallback) {
            }

            public void synchronize(Runnable callback) {
            }
         };
      }

      static PlayerClientSynchronizer.Provider create(TrainCarts traincarts) {
         return (PlayerClientSynchronizer.Provider)(Common.evaluateMCVersion(">=", "1.9") ? new PlayerClientSynchronizerProviderModernImpl(traincarts) : new PlayerClientSynchronizerProviderLegacyImpl(traincarts));
      }

      void enable();

      void disable();
   }
}
