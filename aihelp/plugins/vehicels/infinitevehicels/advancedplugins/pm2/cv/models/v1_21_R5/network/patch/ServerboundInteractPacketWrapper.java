package advancedplugins.pm2.cv.models.v1_21_R5.network.patch;

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

public class ServerboundInteractPacketWrapper implements Packet<ServerGamePacketListener> {
   private final int originalId;
   private final int relayedId;
   private final ServerboundInteractPacket original;
   private final boolean isAttack;

   public ServerboundInteractPacketWrapper(int var1, int var2, int var3, ServerboundInteractPacket var4) {
      this.originalId = var1;
      this.relayedId = var2;
      this.original = var4;
      this.isAttack = var3 == 1;
   }

   @NotNull
   public PacketType<? extends Packet<ServerGamePacketListener>> type() {
      return GamePacketTypes.SERVERBOUND_INTERACT;
   }

   public int getEntityId() {
      return this.originalId;
   }

   public boolean isAttack() {
      return this.isAttack;
   }

   public void handle(ServerGamePacketListener var1) {
      PatchedServerGamePacketListener.handleInteract(this, var1);
   }

   @Nullable
   public Entity getTarget(ServerLevel var1) {
      return var1.getEntityOrPart(this.relayedId);
   }

   public boolean isFakeInteraction() {
      InteractionTracker var1 = ModelAPI.getInteractionTracker();
      return var1.getModelRelay(this.originalId) != null || var1.getEntityRelay(this.originalId) != null;
   }

   public boolean isUsingSecondaryAction() {
      return this.original.isUsingSecondaryAction();
   }

   public void dispatch(Handler var1) {
      this.original.dispatch(var1);
   }

   public int getOriginalId() {
      return this.originalId;
   }

   public int getRelayedId() {
      return this.relayedId;
   }

   public ServerboundInteractPacket getOriginal() {
      return this.original;
   }
}
