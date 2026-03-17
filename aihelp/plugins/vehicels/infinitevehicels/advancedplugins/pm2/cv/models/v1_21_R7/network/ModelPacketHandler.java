package advancedplugins.pm2.cv.models.v1_21_R7.network;

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
import advancedplugins.pm2.cv.models.v1_21_R7.entity.EntityRelationship;
import advancedplugins.pm2.cv.models.v1_21_R7.network.patch.InteractionPacketWrapper;
import advancedplugins.pm2.cv.models.v1_21_R7.network.utils.PacketInterceptor;
import advancedplugins.pm2.cv.models.v1_21_R7.network.utils.PacketTransmissionUtility;
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
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.PacketListenerPlayIn;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import net.minecraft.network.protocol.game.PacketPlayInFlying;
import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEffect;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityHeadRotation;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutEntityStatus;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutEntityVelocity;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayOutRemoveEntityEffect;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket;
import net.minecraft.network.protocol.game.PacketPlayInFlying.PacketPlayInPosition;
import net.minecraft.network.protocol.game.PacketPlayInFlying.PacketPlayInPositionLook;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcher.c;
import net.minecraft.server.level.EntityPlayer;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ModelPacketHandler extends ChannelDuplexHandler {
   private final Player bukkitPlayer;
   private final EntityPlayer nmsPlayer;
   private final ModelUpdaters modelManager;
   private final EntityHandler entityManager;
   private final ClientDesyncMonitor syncMonitor;
   private final PacketInterceptor<PacketListenerPlayOut> outgoingInterceptor;
   private final PacketInterceptor<PacketListenerPlayIn> incomingInterceptor;

   public ModelPacketHandler(Player var1, PipelineWrapper var2) {
      this.bukkitPlayer = var1;
      this.nmsPlayer = ((CraftPlayer)var1).getHandle();
      this.modelManager = ModelAPI.getAPI().getModelUpdaters();
      this.entityManager = ModelAPI.getEntityHandler();
      this.syncMonitor = var2.getDesyncMonitor();
      this.outgoingInterceptor = this.buildOutgoingInterceptor();
      this.incomingInterceptor = this.buildIncomingInterceptor();
   }

   private PacketInterceptor<PacketListenerPlayOut> buildOutgoingInterceptor() {
      return (new PacketInterceptor()).register(PacketPlayOutSpawnEntity.class, this::processEntitySpawn).register(PacketPlayOutEntityDestroy.class, this::processEntityRemoval).register(PacketPlayOutRelEntityMove.class, this::filterByEntityId).register(PacketPlayOutEntityLook.class, this::filterByEntityId).register(PacketPlayOutRelEntityMoveLook.class, this::filterByEntityId).register(PacketPlayOutEntityHeadRotation.class, this::filterByEntityId).register(PacketPlayOutEntityStatus.class, this::filterByEntityId).register(PacketPlayOutEntityVelocity.class, this::processVelocityUpdate).register(PacketPlayOutEntityTeleport.class, this::processTeleportation).register(PacketPlayOutAnimation.class, this::processAnimation).register(PacketPlayOutEntityMetadata.class, this::processMetadataUpdate).register(PacketPlayOutEntityEquipment.class, this::processEquipmentChange).register(PacketPlayOutRemoveEntityEffect.class, this::processEffectRemoval).register(PacketPlayOutEntityEffect.class, this::processEffectApplication).register(ClientboundKeepAlivePacket.class, this::processKeepAlive).registerPost(PacketPlayOutSpawnEntity.class, this::postProcessEntitySpawn);
   }

   private PacketInterceptor<PacketListenerPlayIn> buildIncomingInterceptor() {
      return (new PacketInterceptor()).register(PacketPlayInUseEntity.class, this::processInteraction).register(PacketPlayInSteerVehicle.class, this::processPlayerControls).register(ServerboundPongPacket.class, this::processPongResponse).register(PacketPlayInPosition.class, this::processMovement).register(PacketPlayInPositionLook.class, this::processMovement).register(ServerboundClientTickEndPacket.class, this::processTickEnd);
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
      Iterator var3 = var1.b().iterator();

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

   private PacketPlayOutSpawnEntity processEntitySpawn(PacketPlayOutSpawnEntity var1) {
      return this.isEntityVisible(var1.b()) ? var1 : null;
   }

   private List<Packet<? super PacketListenerPlayOut>> postProcessEntitySpawn(PacketPlayOutSpawnEntity var1) {
      return this.generateMountPackets(var1.e());
   }

   private List<Packet<? super PacketListenerPlayOut>> generateMountPackets(UUID var1) {
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
                  var11.add(new PacketPlayOutMount(EntityRelationship.of(var9.getMountId(), (Collection)var10)));
                  return var11;
               }
            }
         } else {
            return null;
         }
      }
   }

   private PacketPlayOutEntityDestroy processEntityRemoval(PacketPlayOutEntityDestroy var1) {
      int[] var2 = var1.b().intStream().filter(this::isEntityVisible).toArray();
      return var2.length == var1.b().size() ? var1 : new PacketPlayOutEntityDestroy(IntArrayList.wrap(var2));
   }

   private <T extends Packet<? super PacketListenerPlayOut>> T filterByEntityId(T var1) {
      PacketDataSerializer var2 = PacketTransmissionUtility.decodeOutgoingPacket(var1);
      int var3 = var2.l();
      return this.isEntityVisible(var3) ? var1 : null;
   }

   private PacketPlayOutEntityVelocity processVelocityUpdate(PacketPlayOutEntityVelocity var1) {
      return this.isEntityVisible(var1.b()) ? var1 : null;
   }

   private PacketPlayOutEntityTeleport processTeleportation(PacketPlayOutEntityTeleport var1) {
      return this.isEntityVisible(var1.b()) ? var1 : null;
   }

   private PacketPlayOutAnimation processAnimation(PacketPlayOutAnimation var1) {
      return this.isEntityVisible(var1.b()) ? var1 : null;
   }

   private PacketPlayOutEntityMetadata processMetadataUpdate(PacketPlayOutEntityMetadata var1) {
      if (!this.isEntityVisible(var1.b())) {
         return null;
      } else {
         return var1.b() == this.bukkitPlayer.getEntityId() && this.entityManager.isForcedInvisible(this.bukkitPlayer) ? this.createInvisibilityPacket(var1) : var1;
      }
   }

   private PacketPlayOutEntityMetadata createInvisibilityPacket(PacketPlayOutEntityMetadata var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = var1.e().iterator();

      while(var3.hasNext()) {
         c var4 = (c)var3.next();
         if (var4.a() == 0) {
            byte var5 = (Byte)var4.c();
            var5 = MathUtils.setBit(var5, 5, true);
            var2.add(new c(0, DataWatcherRegistry.a, var5));
         } else {
            var2.add(var4);
         }
      }

      return new PacketPlayOutEntityMetadata(var1.b(), var2);
   }

   private PacketPlayOutEntityEquipment processEquipmentChange(PacketPlayOutEntityEquipment var1) {
      return this.isEntityVisible(var1.b()) ? var1 : null;
   }

   private PacketPlayOutRemoveEntityEffect processEffectRemoval(PacketPlayOutRemoveEntityEffect var1) {
      return this.isEntityVisible(var1.b()) ? var1 : null;
   }

   private PacketPlayOutEntityEffect processEffectApplication(PacketPlayOutEntityEffect var1) {
      return this.isEntityVisible(var1.b()) ? var1 : null;
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

   private Packet<? super PacketListenerPlayIn> processInteraction(PacketPlayInUseEntity var1) {
      PacketDataSerializer var2 = PacketTransmissionUtility.decodeIncomingPacket(var1, this.nmsPlayer);
      int var3 = var2.l();
      int var4 = var2.l();
      return this.resolveInteractionTarget(var3, var4, var1);
   }

   private Packet<? super PacketListenerPlayIn> resolveInteractionTarget(int var1, int var2, PacketPlayInUseEntity var3) {
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

   private PacketPlayInSteerVehicle processPlayerControls(PacketPlayInSteerVehicle var1) {
      MountController var2 = ModelAPI.getMountPairManager().getController(this.bukkitPlayer.getUniqueId());
      if (var2 != null) {
         this.updateMountInput(var2, var1);
      }

      return var1;
   }

   private void updateMountInput(MountController var1, PacketPlayInSteerVehicle var2) {
      MountController.MountInput var3 = var1.getInput();
      if (var3 == null) {
         var3 = new MountController.MountInput(var2.b().a(), var2.b().b(), var2.b().c(), var2.b().d(), var2.b().e(), var2.b().f(), var2.b().g());
         var1.setInput(var3);
      } else {
         var3.setForward(var2.b().a());
         var3.setBackward(var2.b().b());
         var3.setLeft(var2.b().c());
         var3.setRight(var2.b().d());
         var3.setJump(var2.b().e());
         var3.setSneak(var2.b().f());
         var3.setSprint(var2.b().g());
      }

   }

   private ServerboundPongPacket processPongResponse(ServerboundPongPacket var1) {
      this.syncMonitor.recordPongTime(System.currentTimeMillis());
      return null;
   }

   private PacketPlayInFlying processMovement(PacketPlayInFlying var1) {
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
