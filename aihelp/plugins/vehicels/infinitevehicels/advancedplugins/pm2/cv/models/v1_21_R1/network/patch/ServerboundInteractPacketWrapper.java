package advancedplugins.pm2.cv.models.v1_21_R1.network.patch;

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

   @NotNull
   public PacketType<? extends Packet<PacketListenerPlayIn>> a() {
      return GamePacketTypes.by;
   }

   public int getEntityId() {
      return this.originalId;
   }

   public boolean isAttack() {
      return this.isAttack;
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
      return this.original.b();
   }

   public void dispatch(c var0) {
      this.original.a(var1);
   }

   public int getOriginalId() {
      return this.originalId;
   }

   public int getRelayedId() {
      return this.relayedId;
   }

   public PacketPlayInUseEntity getOriginal() {
      return this.original;
   }
}
