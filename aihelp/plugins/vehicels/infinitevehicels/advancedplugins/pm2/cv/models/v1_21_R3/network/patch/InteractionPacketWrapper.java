package advancedplugins.pm2.cv.models.v1_21_R3.network.patch;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.interaction.InteractionTracker;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.game.GamePacketTypes;
import net.minecraft.network.protocol.game.PacketListenerPlayIn;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity.c;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InteractionPacketWrapper implements Packet<PacketListenerPlayIn> {
   private final int sourceEntityId;
   private final int targetEntityId;
   private final PacketPlayInUseEntity wrappedPacket;
   private final boolean attackAction;

   public InteractionPacketWrapper(int sourceId, int targetId, int actionType, PacketPlayInUseEntity original) {
      this.sourceEntityId = var1;
      this.targetEntityId = var2;
      this.wrappedPacket = var4;
      this.attackAction = var3 == 1;
   }

   @NotNull
   public PacketType<? extends Packet<PacketListenerPlayIn>> a() {
      return GamePacketTypes.bF;
   }

   public int getEntityId() {
      return this.sourceEntityId;
   }

   public boolean isAttack() {
      return this.attackAction;
   }

   public void handle(PacketListenerPlayIn listener) {
      ServerInteractionProcessor.handleInteract(this, var1);
   }

   @Nullable
   public Entity getTarget(WorldServer level) {
      return var1.b(this.targetEntityId);
   }

   public boolean isFakeInteraction() {
      InteractionTracker var1 = ModelAPI.getInteractionTracker();
      return var1.getModelRelay(this.sourceEntityId) != null || var1.getEntityRelay(this.sourceEntityId) != null;
   }

   public boolean isUsingSecondaryAction() {
      return this.wrappedPacket.b();
   }

   public void dispatch(c handler) {
      this.wrappedPacket.a(var1);
   }

   public int getOriginalId() {
      return this.sourceEntityId;
   }

   public int getRelayedId() {
      return this.targetEntityId;
   }

   public PacketPlayInUseEntity getOriginal() {
      return this.wrappedPacket;
   }
}
