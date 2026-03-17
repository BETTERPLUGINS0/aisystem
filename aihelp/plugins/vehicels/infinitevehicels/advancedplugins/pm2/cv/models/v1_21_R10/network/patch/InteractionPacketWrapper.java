package advancedplugins.pm2.cv.models.v1_21_R10.network.patch;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.interaction.InteractionTracker;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import net.minecraft.network.protocol.game.GamePacketTypes;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket.Handler;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InteractionPacketWrapper implements Packet<ServerGamePacketListener> {
   private final int sourceEntityId;
   private final int targetEntityId;
   private final ServerboundInteractPacket wrappedPacket;
   private final boolean attackAction;

   public InteractionPacketWrapper(int var1, int var2, int var3, ServerboundInteractPacket var4) {
      this.sourceEntityId = var1;
      this.targetEntityId = var2;
      this.wrappedPacket = var4;
      this.attackAction = var3 == 1;
   }

   @NotNull
   public PacketType<? extends Packet<ServerGamePacketListener>> type() {
      return GamePacketTypes.SERVERBOUND_INTERACT;
   }

   public int getEntityId() {
      return this.sourceEntityId;
   }

   public boolean isAttack() {
      return this.attackAction;
   }

   public void handle(ServerGamePacketListener var1) {
      ServerInteractionProcessor.handleInteract(this, var1);
   }

   @Nullable
   public Entity getTarget(ServerLevel var1) {
      return var1.getEntityOrPart(this.targetEntityId);
   }

   public boolean isFakeInteraction() {
      InteractionTracker var1 = ModelAPI.getInteractionTracker();
      return var1.getModelRelay(this.sourceEntityId) != null || var1.getEntityRelay(this.sourceEntityId) != null;
   }

   public boolean isUsingSecondaryAction() {
      return this.wrappedPacket.isUsingSecondaryAction();
   }

   public void dispatch(Handler var1) {
      this.wrappedPacket.dispatch(var1);
   }

   public int getOriginalId() {
      return this.sourceEntityId;
   }

   public int getRelayedId() {
      return this.targetEntityId;
   }

   public ServerboundInteractPacket getOriginal() {
      return this.wrappedPacket;
   }
}
