package advancedplugins.pm2.cv.models.v1_20_R4.network.patch;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.interaction.InteractionTracker;
import lombok.Generated;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketListenerPlayIn;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity.c;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerboundInteractPacketWrapper implements Packet<PacketListenerPlayIn> {
   private final int originalId;
   private final int relayedId;
   private final PacketPlayInUseEntity original;
   private final boolean isAttack;

   public ServerboundInteractPacketWrapper(int originalId, int relayedId, int action, PacketPlayInUseEntity original) {
      this.originalId = var1;
      this.relayedId = var2;
      this.original = var4;
      this.isAttack = var3 == 1;
   }

   public int getEntityId() {
      return this.originalId;
   }

   public boolean isAttack() {
      return this.isAttack;
   }

   public void a(@NotNull PacketDataSerializer friendlyByteBuf) {
      this.original.a(var1);
   }

   public void handle(PacketListenerPlayIn var0) {
      PatchedServerGamePacketListener.handleInteract(this, var1);
   }

   @Nullable
   public Entity getTarget(WorldServer var0) {
      return var1.b(this.relayedId);
   }

   public boolean isFakeInteraction() {
      InteractionTracker var1 = ModelAPI.getInteractionTracker();
      return var1.getModelRelay(this.originalId) != null || var1.getEntityRelay(this.originalId) != null;
   }

   public boolean isUsingSecondaryAction() {
      return this.original.a();
   }

   public void dispatch(c var0) {
      this.original.a(var1);
   }

   @Generated
   public int getOriginalId() {
      return this.originalId;
   }

   @Generated
   public int getRelayedId() {
      return this.relayedId;
   }

   @Generated
   public PacketPlayInUseEntity getOriginal() {
      return this.original;
   }
}
