package advancedplugins.pm2.cv.models.v1_21_R5.network;

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
import advancedplugins.pm2.cv.models.v1_21_R5.entity.EntityContainer;
import advancedplugins.pm2.cv.models.v1_21_R5.network.patch.ServerboundInteractPacketWrapper;
import advancedplugins.pm2.cv.models.v1_21_R5.network.utils.NetworkUtils;
import advancedplugins.pm2.cv.models.v1_21_R5.network.utils.PacketInterceptor;
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
import net.minecraft.network.RegistryFriendlyByteBuf;
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
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InfiniteModelChannelHandler extends ChannelDuplexHandler {
   private final Player player;
   private final ServerPlayer serverPlayer;
   private final ModelUpdaters updaters;
   private final EntityHandler entityHandler;
   private final ClientDesyncMonitor desyncMonitor;
   private final PacketInterceptor<ClientGamePacketListener> writeInterceptors;
   private final PacketInterceptor<ServerGamePacketListener> readInterceptors;

   public InfiniteModelChannelHandler(Player var1, PipelineWrapper var2) {
      this.player = var1;
      this.serverPlayer = ((CraftPlayer)var1).getHandle();
      this.updaters = ModelAPI.getAPI().getModelUpdaters();
      this.entityHandler = ModelAPI.getEntityHandler();
      this.desyncMonitor = var2.getDesyncMonitor();
      this.writeInterceptors = new PacketInterceptor();
      this.writeInterceptors.register(ClientboundAddEntityPacket.class, this::handleAddEntity).register(ClientboundRemoveEntitiesPacket.class, this::handleRemoveEntities).register(Pos.class, this::handleEntityId).register(Rot.class, this::handleEntityId).register(PosRot.class, this::handleEntityId).register(ClientboundRotateHeadPacket.class, this::handleEntityId).register(ClientboundEntityEventPacket.class, this::handleEntityId).register(ClientboundSetEntityMotionPacket.class, this::handleEntityMotion).register(ClientboundTeleportEntityPacket.class, this::handleTeleportEntity).register(ClientboundAnimatePacket.class, this::handleAnimate).register(ClientboundSetEntityDataPacket.class, this::handleEntityData).register(ClientboundSetEquipmentPacket.class, this::handleSetEquipment).register(ClientboundRemoveMobEffectPacket.class, this::handleRemoveMobEffect).register(ClientboundUpdateMobEffectPacket.class, this::handleUpdateMobEffect).register(ClientboundKeepAlivePacket.class, this::handleKeepAlive).registerPost(ClientboundAddEntityPacket.class, this::handleAddEntityPost);
      this.readInterceptors = new PacketInterceptor();
      this.readInterceptors.register(ServerboundInteractPacket.class, this::handleInteract).register(ServerboundPlayerInputPacket.class, this::handlePlayerInput).register(ServerboundPongPacket.class, this::handlePong).register(net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos.class, this::handlePlayerMove).register(net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot.class, this::handlePlayerMove).register(ServerboundClientTickEndPacket.class, this::handleClientTickEnd);
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) {
      if (!(var2 instanceof Packet)) {
         super.write(var1, var2, var3);
      } else {
         Packet var4 = (Packet)var2;

         try {
            ArrayList var5;
            ClientboundBundlePacket var11;
            if (var4 instanceof ClientboundBundlePacket) {
               ClientboundBundlePacket var6 = (ClientboundBundlePacket)var4;
               var5 = new ArrayList();
               Iterator var7 = var6.subPackets().iterator();

               while(var7.hasNext()) {
                  Packet var8 = (Packet)var7.next();
                  Packet var9 = this.writeInterceptors.accept(var8);
                  if (var9 != null) {
                     var5.add(var9);
                     var5.addAll(this.writeInterceptors.acceptPost(var9));
                  }
               }

               if (!var5.isEmpty()) {
                  var11 = new ClientboundBundlePacket(var5);
                  super.write(var1, var11, var3);
               }
            } else {
               var4 = this.writeInterceptors.accept(var4);
               if (var4 == null) {
                  return;
               }

               var5 = new ArrayList();
               var5.add(var4);
               var5.addAll(this.writeInterceptors.acceptPost(var4));
               if (var5.size() == 1) {
                  super.write(var1, var4, var3);
               } else {
                  var11 = new ClientboundBundlePacket(var5);
                  super.write(var1, var11, var3);
               }
            }
         } catch (Throwable var10) {
            var10.printStackTrace();
         }
      }

   }

   public void channelRead(@NotNull ChannelHandlerContext var1, @NotNull Object var2) {
      if (!(var2 instanceof Packet)) {
         super.channelRead(var1, var2);
      } else {
         Packet var3 = this.readInterceptors.accept((Packet)var2);
         if (var3 != null) {
            super.channelRead(var1, var3);
            this.readInterceptors.acceptPost(var3);
         }
      }

   }

   private ClientboundAddEntityPacket handleAddEntity(ClientboundAddEntityPacket var1) {
      return this.shouldShow(var1.getId()) ? var1 : null;
   }

   private List<Packet<? super ClientGamePacketListener>> handleAddEntityPost(ClientboundAddEntityPacket var1) {
      return this.handleMount(var1.getUUID());
   }

   private List<Packet<? super ClientGamePacketListener>> handleMount(UUID var1) {
      Pair var2 = ModelAPI.getMountPairManager().get(var1);
      if (var2 == null) {
         return null;
      } else {
         IVisualModel var3 = (IVisualModel)var2.left();
         ArrayList var4 = null;
         Optional var5 = var3.getBehaviorRenderer(JointBehaviorTypes.MOUNT);
         if (var5.isPresent()) {
            Object var6 = var5.get();
            if (var6 instanceof MountRenderer) {
               MountRenderer var7 = (MountRenderer)var6;
               MountController var8 = (MountController)var2.right();
               Mount var9 = var8.getMount();
               if (var9 instanceof JointAction) {
                  JointAction var10 = (JointAction)var9;
                  MountRenderer.Mount var11 = (MountRenderer.Mount)var7.getRendered().get(var10.getJoint().getJointId());
                  if (var11 == null) {
                     return null;
                  }

                  CollectionDataTracker var12 = var11.getPassengers();
                  var4 = new ArrayList();
                  var4.add(new ClientboundSetPassengersPacket(EntityContainer.of(var11.getMountId(), (Collection)var12)));
               }
            }
         }

         return var4;
      }
   }

   private ClientboundRemoveEntitiesPacket handleRemoveEntities(ClientboundRemoveEntitiesPacket var1) {
      int[] var2 = var1.getEntityIds().intStream().filter(this::shouldShow).toArray();
      return var2.length == var1.getEntityIds().size() ? var1 : new ClientboundRemoveEntitiesPacket(IntArrayList.wrap(var2));
   }

   private <T extends Packet<? super ClientGamePacketListener>> T handleEntityId(T var1) {
      FriendlyByteBuf var2 = NetworkUtils.readClientbound(var1);
      int var3 = var2.readVarInt();
      return this.shouldShow(var3) ? var1 : null;
   }

   private ClientboundSetEntityMotionPacket handleEntityMotion(ClientboundSetEntityMotionPacket var1) {
      return this.shouldShow(var1.getId()) ? var1 : null;
   }

   private ClientboundTeleportEntityPacket handleTeleportEntity(ClientboundTeleportEntityPacket var1) {
      return this.shouldShow(var1.id()) ? var1 : null;
   }

   private ClientboundAnimatePacket handleAnimate(ClientboundAnimatePacket var1) {
      return this.shouldShow(var1.getId()) ? var1 : null;
   }

   private ClientboundSetEntityDataPacket handleEntityData(ClientboundSetEntityDataPacket var1) {
      if (!this.shouldShow(var1.id())) {
         return null;
      } else if (var1.id() != this.player.getEntityId()) {
         return var1;
      } else {
         if (this.entityHandler.isForcedInvisible(this.player)) {
            ArrayList var2 = new ArrayList();
            RegistryFriendlyByteBuf var3 = NetworkUtils.createByteBuf();
            var3.writeVarInt(var1.id());
            Iterator var4 = var1.packedItems().iterator();

            while(var4.hasNext()) {
               DataValue var5 = (DataValue)var4.next();
               if (var5.id() == 0) {
                  byte var6 = (Byte)var5.value();
                  var6 = MathUtils.setBit(var6, 5, true);
                  var2.add(new DataValue(0, EntityDataSerializers.BYTE, var6));
               } else {
                  var2.add(var5);
               }
            }

            var3.writeByte(255);
            var1 = new ClientboundSetEntityDataPacket(var1.id(), var2);
         }

         return var1;
      }
   }

   private ClientboundSetEquipmentPacket handleSetEquipment(ClientboundSetEquipmentPacket var1) {
      return this.shouldShow(var1.getEntity()) ? var1 : null;
   }

   private ClientboundRemoveMobEffectPacket handleRemoveMobEffect(ClientboundRemoveMobEffectPacket var1) {
      int var2 = var1.entityId();
      return this.shouldShow(var2) ? var1 : null;
   }

   private ClientboundUpdateMobEffectPacket handleUpdateMobEffect(ClientboundUpdateMobEffectPacket var1) {
      return this.shouldShow(var1.getEntityId()) ? var1 : null;
   }

   private boolean shouldShow(int var1) {
      if (ModelAPI.isRenderCanceled(var1)) {
         return false;
      } else if (this.player.getEntityId() == var1) {
         return true;
      } else {
         IModelContainer var2 = this.updaters.getModeledEntity(var1);
         return var2 == null || var2.isBaseEntityVisible();
      }
   }

   private ClientboundKeepAlivePacket handleKeepAlive(ClientboundKeepAlivePacket var1) {
      if (this.desyncMonitor.clientTickShifted() || this.desyncMonitor.shouldRetest()) {
         this.desyncMonitor.startTest();
      }

      return var1;
   }

   private Packet<? super ServerGamePacketListener> handleInteract(ServerboundInteractPacket var1) {
      FriendlyByteBuf var2 = NetworkUtils.readServerbound(var1, this.serverPlayer);
      int var3 = var2.readVarInt();
      int var4 = var2.readVarInt();
      if (var3 == DynamicHitbox.getHitboxId()) {
         DynamicHitbox var5 = ModelAPI.getInteractionTracker().getDynamicHitbox(this.player.getUniqueId());
         if (var5 != null) {
            return new ServerboundInteractPacketWrapper(var3, var5.getTarget(), var4, var1);
         }
      }

      IVisualModel var8 = ModelAPI.getInteractionTracker().getModelRelay(var3);
      if (var8 != null) {
         IModelContainer var9 = var8.getModeledEntity();
         if (var9 == null) {
            return var1;
         } else {
            int var7 = var9.getBase().getEntityId();
            return new ServerboundInteractPacketWrapper(var3, var7, var4, var1);
         }
      } else {
         Integer var6 = ModelAPI.getInteractionTracker().getEntityRelay(var3);
         return (Packet)(var6 != null ? new ServerboundInteractPacketWrapper(var3, var6, var4, var1) : var1);
      }
   }

   private ServerboundPlayerInputPacket handlePlayerInput(ServerboundPlayerInputPacket var1) {
      MountController var2 = ModelAPI.getMountPairManager().getController(this.player.getUniqueId());
      if (var2 != null) {
         MountController.MountInput var3 = var2.getInput();
         if (var3 == null) {
            var2.setInput(new MountController.MountInput(var1.input().forward(), var1.input().backward(), var1.input().left(), var1.input().right(), var1.input().jump(), var1.input().shift(), var1.input().sprint()));
         } else {
            var3.setForward(var1.input().forward());
            var3.setBackward(var1.input().backward());
            var3.setLeft(var1.input().left());
            var3.setRight(var1.input().right());
            var3.setJump(var1.input().jump());
            var3.setSneak(var1.input().shift());
            var3.setSprint(var1.input().sprint());
         }
      }

      return var1;
   }

   private ServerboundPongPacket handlePong(ServerboundPongPacket var1) {
      this.desyncMonitor.recordPongTime(System.currentTimeMillis());
      return null;
   }

   private ServerboundMovePlayerPacket handlePlayerMove(ServerboundMovePlayerPacket var1) {
      if (!ConfigProperty.SYNC_CLIENT_TICK_END.getBoolean()) {
         this.desyncMonitor.recordClientSyncTime(System.currentTimeMillis());
      }

      return var1;
   }

   private ServerboundClientTickEndPacket handleClientTickEnd(ServerboundClientTickEndPacket var1) {
      if (ConfigProperty.SYNC_CLIENT_TICK_END.getBoolean()) {
         this.desyncMonitor.recordClientSyncTime(System.currentTimeMillis());
      }

      return var1;
   }
}
