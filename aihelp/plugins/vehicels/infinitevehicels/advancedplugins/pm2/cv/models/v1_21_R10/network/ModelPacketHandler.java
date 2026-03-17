package advancedplugins.pm2.cv.models.v1_21_R10.network;

import advancedplugins.pm2.cv.models.api.ModelAPI;
import advancedplugins.pm2.cv.models.api.model.rpc.IModelContainer;
import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.ModelUpdaters;
import advancedplugins.pm2.cv.models.api.model.rpc.interaction.DynamicHitbox;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.MountRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.Mount;
import advancedplugins.pm2.cv.models.api.model.rpc.mount.controller.MountController;
import advancedplugins.pm2.cv.models.api.nms.entity.EntityHandler;
import advancedplugins.pm2.cv.models.api.nms.network.ClientDesyncMonitor;
import advancedplugins.pm2.cv.models.api.nms.network.PipelineWrapper;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.CollectionDataTracker;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import advancedplugins.pm2.cv.models.v1_21_R10.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R10.network.patch.InteractionPacketWrapper;
import advancedplugins.pm2.cv.models.v1_21_R10.network.utils.PacketInterceptor;
import advancedplugins.pm2.cv.models.v1_21_R10.network.utils.PacketTransmissionUtility;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundAnimatePacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Pos;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData.DataValue;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.v1_21_R7.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ModelPacketHandler extends ChannelDuplexHandler {
   private final Player bukkitPlayer;
   private final ServerPlayer nmsPlayer;
   private final ModelUpdaters modelManager;
   private final EntityHandler entityManager;
   private final ClientDesyncMonitor syncMonitor;
   private final PacketInterceptor<ClientGamePacketListener> outgoingInterceptor;
   private final PacketInterceptor<ServerGamePacketListener> incomingInterceptor;

   public ModelPacketHandler(Player var1, PipelineWrapper var2) {
      this.bukkitPlayer = var1;
      this.nmsPlayer = ((CraftPlayer)var1).getHandle();
      this.modelManager = ModelAPI.getAPI().getModelUpdaters();
      this.entityManager = ModelAPI.getEntityHandler();
      this.syncMonitor = var2.getDesyncMonitor();
      this.outgoingInterceptor = this.buildOutgoingInterceptor();
      this.incomingInterceptor = this.buildIncomingInterceptor();
   }

   private PacketInterceptor<ClientGamePacketListener> buildOutgoingInterceptor() {
      return (new PacketInterceptor()).register(ClientboundAddEntityPacket.class, this::processEntitySpawn).register(ClientboundRemoveEntitiesPacket.class, this::processEntityRemoval).register(Pos.class, this::filterByEntityId).register(Rot.class, this::filterByEntityId).register(PosRot.class, this::filterByEntityId).register(ClientboundRotateHeadPacket.class, this::filterByEntityId).register(ClientboundEntityEventPacket.class, this::filterByEntityId).register(ClientboundSetEntityMotionPacket.class, this::processVelocityUpdate).register(ClientboundTeleportEntityPacket.class, this::processTeleportation).register(ClientboundAnimatePacket.class, this::processAnimation).register(ClientboundSetEntityDataPacket.class, this::processMetadataUpdate).register(ClientboundSetEquipmentPacket.class, this::processEquipmentChange).register(ClientboundRemoveMobEffectPacket.class, this::processEffectRemoval).register(ClientboundUpdateMobEffectPacket.class, this::processEffectApplication).register(ClientboundKeepAlivePacket.class, this::processKeepAlive).registerPost(ClientboundAddEntityPacket.class, this::postProcessEntitySpawn);
   }

   private PacketInterceptor<ServerGamePacketListener> buildIncomingInterceptor() {
      return (new PacketInterceptor()).register(ServerboundInteractPacket.class, this::processInteraction).register(ServerboundPlayerInputPacket.class, this::processPlayerControls).register(ServerboundPongPacket.class, this::processPongResponse).register(net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos.class, this::processMovement).register(net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot.class, this::processMovement).register(ServerboundClientTickEndPacket.class, this::processTickEnd);
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) {
      if (!(var2 instanceof Packet)) {
         super.write(var1, var2, var3);
      } else {
         try {
            Packet var4 = this.processOutgoingPacket((Packet)var2);
            if (var4 != null) {
               super.write(var1, var4, var3);
            }
         } catch (Throwable var5) {
            var5.printStackTrace();
         }

      }
   }

   private Packet processOutgoingPacket(Packet var1) {
      return (Packet)(var1 instanceof ClientboundBundlePacket ? this.processBundledPackets((ClientboundBundlePacket)var1) : this.processSinglePacket(var1));
   }

   private ClientboundBundlePacket processBundledPackets(ClientboundBundlePacket var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.subPackets().iterator();

      while(var3.hasNext()) {
         Packet var4 = (Packet)var3.next();
         Packet var5 = this.outgoingInterceptor.accept(var4);
         if (var5 != null) {
            var2.add(var5);
            var2.addAll(this.outgoingInterceptor.acceptPost(var5));
         }
      }

      return var2.isEmpty() ? null : new ClientboundBundlePacket(var2);
   }

   private Packet processSinglePacket(Packet var1) {
      var1 = this.outgoingInterceptor.accept(var1);
      if (var1 == null) {
         return null;
      } else {
         ArrayList var2 = new ArrayList();
         var2.add(var1);
         var2.addAll(this.outgoingInterceptor.acceptPost(var1));
         return (Packet)(var2.size() == 1 ? var1 : new ClientboundBundlePacket(var2));
      }
   }

   public void channelRead(@NotNull ChannelHandlerContext var1, @NotNull Object var2) {
      if (!(var2 instanceof Packet)) {
         super.channelRead(var1, var2);
      } else {
         Packet var3 = this.incomingInterceptor.accept((Packet)var2);
         if (var3 != null) {
            super.channelRead(var1, var3);
            this.incomingInterceptor.acceptPost(var3);
         }

      }
   }

   private ClientboundAddEntityPacket processEntitySpawn(ClientboundAddEntityPacket var1) {
      return this.isEntityVisible(var1.getId()) ? var1 : null;
   }

   private List<Packet<? super ClientGamePacketListener>> postProcessEntitySpawn(ClientboundAddEntityPacket var1) {
      return this.generateMountPackets(var1.getUUID());
   }

   private List<Packet<? super ClientGamePacketListener>> generateMountPackets(UUID var1) {
      Pair var2 = ModelAPI.getMountPairManager().get(var1);
      if (var2 == null) {
         return null;
      } else {
         IVisualModel var3 = (IVisualModel)var2.left();
         Optional var4 = var3.getBehaviorRenderer(JointBehaviorTypes.MOUNT);
         if (var4.isPresent() && var4.get() instanceof MountRenderer) {
            MountRenderer var5 = (MountRenderer)var4.get();
            MountController var6 = (MountController)var2.right();
            Mount var7 = var6.getMount();
            if (!(var7 instanceof JointAction)) {
               return null;
            } else {
               JointAction var8 = (JointAction)var7;
               MountRenderer.Mount var9 = (MountRenderer.Mount)var5.getRendered().get(var8.getJoint().getJointId());
               if (var9 == null) {
                  return null;
               } else {
                  CollectionDataTracker var10 = var9.getPassengers();
                  ArrayList var11 = new ArrayList();
                  var11.add(new ClientboundSetPassengersPacket(EntityRelationship.of(var9.getMountId(), (Collection)var10)));
                  return var11;
               }
            }
         } else {
            return null;
         }
      }
   }

   private ClientboundRemoveEntitiesPacket processEntityRemoval(ClientboundRemoveEntitiesPacket var1) {
      int[] var2 = var1.getEntityIds().intStream().filter(this::isEntityVisible).toArray();
      return var2.length == var1.getEntityIds().size() ? var1 : new ClientboundRemoveEntitiesPacket(IntArrayList.wrap(var2));
   }

   private <T extends Packet<? super ClientGamePacketListener>> T filterByEntityId(T var1) {
      FriendlyByteBuf var2 = PacketTransmissionUtility.decodeOutgoingPacket(var1);
      int var3 = var2.readVarInt();
      return this.isEntityVisible(var3) ? var1 : null;
   }

   private ClientboundSetEntityMotionPacket processVelocityUpdate(ClientboundSetEntityMotionPacket var1) {
      return this.isEntityVisible(var1.getId()) ? var1 : null;
   }

   private ClientboundTeleportEntityPacket processTeleportation(ClientboundTeleportEntityPacket var1) {
      return this.isEntityVisible(var1.id()) ? var1 : null;
   }

   private ClientboundAnimatePacket processAnimation(ClientboundAnimatePacket var1) {
      return this.isEntityVisible(var1.getId()) ? var1 : null;
   }

   private ClientboundSetEntityDataPacket processMetadataUpdate(ClientboundSetEntityDataPacket var1) {
      if (!this.isEntityVisible(var1.id())) {
         return null;
      } else {
         return var1.id() == this.bukkitPlayer.getEntityId() && this.entityManager.isForcedInvisible(this.bukkitPlayer) ? this.createInvisibilityPacket(var1) : var1;
      }
   }

   private ClientboundSetEntityDataPacket createInvisibilityPacket(ClientboundSetEntityDataPacket var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.packedItems().iterator();

      while(var3.hasNext()) {
         DataValue var4 = (DataValue)var3.next();
         if (var4.id() == 0) {
            byte var5 = (Byte)var4.value();
            var5 = MathUtils.setBit(var5, 5, true);
            var2.add(new DataValue(0, EntityDataSerializers.BYTE, var5));
         } else {
            var2.add(var4);
         }
      }

      return new ClientboundSetEntityDataPacket(var1.id(), var2);
   }

   private ClientboundSetEquipmentPacket processEquipmentChange(ClientboundSetEquipmentPacket var1) {
      return this.isEntityVisible(var1.getEntity()) ? var1 : null;
   }

   private ClientboundRemoveMobEffectPacket processEffectRemoval(ClientboundRemoveMobEffectPacket var1) {
      return this.isEntityVisible(var1.entityId()) ? var1 : null;
   }

   private ClientboundUpdateMobEffectPacket processEffectApplication(ClientboundUpdateMobEffectPacket var1) {
      return this.isEntityVisible(var1.getEntityId()) ? var1 : null;
   }

   private boolean isEntityVisible(int var1) {
      if (ModelAPI.isRenderCanceled(var1)) {
         return false;
      } else if (this.bukkitPlayer.getEntityId() == var1) {
         return true;
      } else {
         IModelContainer var2 = this.modelManager.getModeledEntity(var1);
         return var2 == null || var2.isBaseEntityVisible();
      }
   }

   private ClientboundKeepAlivePacket processKeepAlive(ClientboundKeepAlivePacket var1) {
      if (this.syncMonitor.clientTickShifted() || this.syncMonitor.shouldRetest()) {
         this.syncMonitor.startTest();
      }

      return var1;
   }

   private Packet<? super ServerGamePacketListener> processInteraction(ServerboundInteractPacket var1) {
      FriendlyByteBuf var2 = PacketTransmissionUtility.decodeIncomingPacket(var1, this.nmsPlayer);
      int var3 = var2.readVarInt();
      int var4 = var2.readVarInt();
      return this.resolveInteractionTarget(var3, var4, var1);
   }

   private Packet<? super ServerGamePacketListener> resolveInteractionTarget(int var1, int var2, ServerboundInteractPacket var3) {
      if (var1 == DynamicHitbox.getHitboxId()) {
         DynamicHitbox var4 = ModelAPI.getInteractionTracker().getDynamicHitbox(this.bukkitPlayer.getUniqueId());
         if (var4 != null) {
            return new InteractionPacketWrapper(var1, var4.getTarget(), var2, var3);
         }
      }

      IVisualModel var8 = ModelAPI.getInteractionTracker().getModelRelay(var1);
      if (var8 != null) {
         IModelContainer var5 = var8.getModeledEntity();
         if (var5 != null) {
            int var6 = var5.getBase().getEntityId();
            return new InteractionPacketWrapper(var1, var6, var2, var3);
         }
      }

      Integer var7 = ModelAPI.getInteractionTracker().getEntityRelay(var1);
      return (Packet)(var7 != null ? new InteractionPacketWrapper(var1, var7, var2, var3) : var3);
   }

   private ServerboundPlayerInputPacket processPlayerControls(ServerboundPlayerInputPacket var1) {
      MountController var2 = ModelAPI.getMountPairManager().getController(this.bukkitPlayer.getUniqueId());
      if (var2 != null) {
         this.updateMountInput(var2, var1);
      }

      return var1;
   }

   private void updateMountInput(MountController var1, ServerboundPlayerInputPacket var2) {
      MountController.MountInput var3 = var1.getInput();
      if (var3 == null) {
         var3 = new MountController.MountInput(var2.input().forward(), var2.input().backward(), var2.input().left(), var2.input().right(), var2.input().jump(), var2.input().shift(), var2.input().sprint());
         var1.setInput(var3);
      } else {
         var3.setForward(var2.input().forward());
         var3.setBackward(var2.input().backward());
         var3.setLeft(var2.input().left());
         var3.setRight(var2.input().right());
         var3.setJump(var2.input().jump());
         var3.setSneak(var2.input().shift());
         var3.setSprint(var2.input().sprint());
      }

   }

   private ServerboundPongPacket processPongResponse(ServerboundPongPacket var1) {
      this.syncMonitor.recordPongTime(System.currentTimeMillis());
      return null;
   }

   private ServerboundMovePlayerPacket processMovement(ServerboundMovePlayerPacket var1) {
      if (!ConfigProperty.SYNC_CLIENT_TICK_END.getBoolean()) {
         this.syncMonitor.recordClientSyncTime(System.currentTimeMillis());
      }

      return var1;
   }

   private ServerboundClientTickEndPacket processTickEnd(ServerboundClientTickEndPacket var1) {
      if (ConfigProperty.SYNC_CLIENT_TICK_END.getBoolean()) {
         this.syncMonitor.recordClientSyncTime(System.currentTimeMillis());
      }

      return var1;
   }
}
