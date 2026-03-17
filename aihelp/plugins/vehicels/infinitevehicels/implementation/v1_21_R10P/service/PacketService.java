package implementation.v1_21_R10P.service;

import advancedplugins.pm2.cv.api.interfaces.VersionSensible;
import advancedplugins.pm2.cv.packet.PacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.EntityActionWrapper;
import advancedplugins.pm2.cv.packet.incoming.InteractPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.PlayerActionPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.PlayerInputPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.SetPlayerPositionRotationPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.SetPlayerRotationPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.SwingArmPacketWrapper;
import advancedplugins.pm2.cv.packet.incoming.UseItemPacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.SetEntityPassengersWrapper;
import advancedplugins.pm2.cv.packet.outgoing.TeleportEntityPacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.UpdateEntityPositionPacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.UpdateEntityPositionRotationPacketWrapper;
import advancedplugins.pm2.cv.packet.outgoing.UpdateEntityRotationPacketWrapper;
import gnu.trove.list.array.TIntArrayList;
import implementation.v1_21_R10P.util.PacketWritingUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Pos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Rot;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec3;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketService implements advancedplugins.pm2.cv.service.PacketService {
   @VersionSensible
   private Connection getNetworkManager(@NotNull ServerGamePacketListenerImpl var1) {
      try {
         Field var2 = ServerCommonPacketListenerImpl.class.getDeclaredField("connection");
         var2.setAccessible(true);
         return (Connection)var2.get(var1);
      } catch (IllegalAccessException | NoSuchFieldException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   public ChannelPipeline getChannelPipeline(@NotNull Player var1) {
      return this.getNetworkManager(((CraftPlayer)var1).getHandle().connection).channel.pipeline();
   }

   @NotNull
   public Object createPassengersPacket(int var1, List<Integer> var2) {
      FriendlyByteBuf var3 = new FriendlyByteBuf(Unpooled.buffer());
      TIntArrayList var4 = new TIntArrayList();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         Integer var6 = (Integer)var5.next();
         var4.add(var6);
      }

      var3.writeVarInt(var1);
      var3.writeVarIntArray(var4.toArray());

      try {
         Constructor var8 = ClientboundSetPassengersPacket.class.getDeclaredConstructor(FriendlyByteBuf.class);
         var8.setAccessible(true);
         return (ClientboundSetPassengersPacket)var8.newInstance(var3);
      } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException var7) {
         throw new RuntimeException(var7);
      }
   }

   public void sendPacket(@NotNull Player var1, @NotNull Object var2, boolean var3) {
      if (var3) {
         Packet var4 = (Packet)var2;
         ChannelPipeline var5 = this.getChannelPipeline(var1);

         try {
            PacketWritingUtil.compressAndWriteToPipeline(var4, var5);
         } catch (Exception var7) {
            var7.printStackTrace();
         }
      } else {
         this.getNetworkManager(((CraftPlayer)var1).getHandle().connection).send((Packet)var2);
      }

   }

   private Object reflectiveEntityID(Object var1) {
      try {
         Field var2 = ServerboundInteractPacket.class.getDeclaredField("entityId");
         var2.setAccessible(true);
         return var2.get(var1);
      } catch (IllegalAccessException | NoSuchFieldException var3) {
         throw new RuntimeException(var3);
      }
   }

   private Object object(Packet<?> var1, String var2, String var3, String var4) {
      try {
         Class var5 = Class.forName(var2);
         Field var6 = var1.getClass().getDeclaredField(var3);
         var6.setAccessible(true);
         Object var7 = var6.get(var1);
         Field var8 = var5.getDeclaredField(var4);
         var8.setAccessible(true);
         return var8.get(var7);
      } catch (Exception var9) {
         var9.printStackTrace();
         return null;
      }
   }

   @Nullable
   public PacketWrapper read(@NotNull Object var1) {
      if (var1 instanceof ClientboundSetPassengersPacket) {
         ClientboundSetPassengersPacket var26 = (ClientboundSetPassengersPacket)var1;
         return new SetEntityPassengersWrapper(var26.getVehicle(), var26.getPassengers());
      } else if (var1 instanceof ServerboundPlayerCommandPacket) {
         ServerboundPlayerCommandPacket var25 = (ServerboundPlayerCommandPacket)var1;
         Action var39 = var25.getAction();
         EntityActionWrapper.Action var31 = null;
         switch(var39) {
         case START_SPRINTING:
            var31 = EntityActionWrapper.Action.START_SPRINTING;
            break;
         case STOP_SPRINTING:
            var31 = EntityActionWrapper.Action.STOP_SPRINTING;
            break;
         case STOP_SLEEPING:
            var31 = EntityActionWrapper.Action.LEAVE_BED;
            break;
         case START_RIDING_JUMP:
            var31 = EntityActionWrapper.Action.START_JUMP_HORSE;
            break;
         case STOP_RIDING_JUMP:
            var31 = EntityActionWrapper.Action.STOP_JUMP_HORSE;
            break;
         case OPEN_INVENTORY:
            var31 = EntityActionWrapper.Action.OPEN_VEHICLE_INVENTORY;
            break;
         case START_FALL_FLYING:
            var31 = EntityActionWrapper.Action.START_FLYING_ELYTRA;
         }

         return new EntityActionWrapper(var25.getId(), var31);
      } else if (var1 instanceof ServerboundPlayerInputPacket) {
         ServerboundPlayerInputPacket var24 = (ServerboundPlayerInputPacket)var1;
         return new PlayerInputPacketWrapper(var24.input().left() ? 1.0F : (var24.input().right() ? -1.0F : 0.0F), var24.input().forward() ? 1.0F : (var24.input().backward() ? -1.0F : 0.0F), var24.input().jump(), var24.input().shift());
      } else {
         int var3;
         if (var1 instanceof ServerboundInteractPacket) {
            ServerboundInteractPacket var2 = (ServerboundInteractPacket)var1;
            var3 = var2.getEntityId();

            String var4;
            try {
               Field var5 = var2.getClass().getDeclaredField("action");
               var5.setAccessible(true);
               Object var6 = var5.get(var2);
               Method var7 = var6.getClass().getDeclaredMethod("getType");
               var7.setAccessible(true);
               var4 = ((Enum)var7.invoke(var6)).name();
            } catch (NoSuchFieldException | InvocationTargetException | NoSuchMethodException | IllegalAccessException var18) {
               throw new RuntimeException(var18);
            }

            if (var4.equalsIgnoreCase("ATTACK")) {
               boolean var34 = var2.isUsingSecondaryAction();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.ATTACK, (Double)null, (Double)null, (Double)null, (InteractPacketWrapper.Hand)null, var34);
            }

            if (var4.equalsIgnoreCase("INTERACT_AT")) {
               Vec3 var32 = (Vec3)this.object(var2, "net.minecraft.network.protocol.game.ServerboundInteractPacket$InteractionAtLocationAction", "action", "location");
               InteractionHand var40 = (InteractionHand)this.object(var2, "net.minecraft.network.protocol.game.ServerboundInteractPacket$InteractionAtLocationAction", "action", "hand");

               assert var32 != null;

               double var43 = var32.x;
               double var9 = var32.y;
               double var11 = var32.z;

               assert var40 != null;

               int var13 = var40.ordinal();
               boolean var14 = var2.isUsingSecondaryAction();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.INTERACT_AT, var43, var9, var11, var13 == 0 ? InteractPacketWrapper.Hand.MAIN_HAND : InteractPacketWrapper.Hand.OFF_HAND, var14);
            }

            if (var4.equalsIgnoreCase("INTERACT")) {
               InteractionHand var28 = (InteractionHand)this.object(var2, "net.minecraft.network.protocol.game.ServerboundInteractPacket$InteractionAction", "action", "hand");

               assert var28 != null;

               int var37 = var28.ordinal();
               boolean var42 = var2.isUsingSecondaryAction();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.INTERACT, (Double)null, (Double)null, (Double)null, var37 == 0 ? InteractPacketWrapper.Hand.MAIN_HAND : InteractPacketWrapper.Hand.OFF_HAND, var42);
            }
         } else {
            if (var1 instanceof ServerboundSwingPacket) {
               return new SwingArmPacketWrapper(((ServerboundSwingPacket)var1).getHand() == InteractionHand.MAIN_HAND ? SwingArmPacketWrapper.Hand.MAIN_HAND : SwingArmPacketWrapper.Hand.OFF_HAND);
            }

            if (var1 instanceof ServerboundUseItemPacket) {
               ServerboundUseItemPacket var36 = (ServerboundUseItemPacket)var1;
               return new UseItemPacketWrapper(var36.getHand() == InteractionHand.MAIN_HAND ? UseItemPacketWrapper.Hand.MAIN_HAND : UseItemPacketWrapper.Hand.OFF_HAND, var36.getSequence());
            }

            if (var1 instanceof Rot) {
               Rot var35 = (Rot)var1;
               return new SetPlayerRotationPacketWrapper(var35.yRot, var35.xRot, var35.isOnGround());
            }

            if (var1 instanceof PosRot) {
               PosRot var33 = (PosRot)var1;
               return new SetPlayerPositionRotationPacketWrapper(var33.x, var33.y, var33.z, var33.yRot, var33.xRot, var33.isOnGround());
            }

            if (var1 instanceof ServerboundPlayerActionPacket) {
               ServerboundPlayerActionPacket var29 = (ServerboundPlayerActionPacket)var1;
               BlockPos var30 = var29.getPos();
               PlayerActionPacketWrapper.Direction var38;
               switch(var29.getDirection()) {
               case DOWN:
                  var38 = PlayerActionPacketWrapper.Direction.DOWN;
                  break;
               case UP:
                  var38 = PlayerActionPacketWrapper.Direction.UP;
                  break;
               case NORTH:
                  var38 = PlayerActionPacketWrapper.Direction.NORTH;
                  break;
               case SOUTH:
                  var38 = PlayerActionPacketWrapper.Direction.SOUTH;
                  break;
               case WEST:
                  var38 = PlayerActionPacketWrapper.Direction.WEST;
                  break;
               case EAST:
                  var38 = PlayerActionPacketWrapper.Direction.EAST;
                  break;
               default:
                  throw new IllegalStateException();
               }

               PlayerActionPacketWrapper.Action var41;
               switch(var29.getAction()) {
               case START_DESTROY_BLOCK:
                  var41 = PlayerActionPacketWrapper.Action.START_DESTROY_BLOCK;
                  break;
               case ABORT_DESTROY_BLOCK:
                  var41 = PlayerActionPacketWrapper.Action.ABORT_DESTROY_BLOCK;
                  break;
               case STOP_DESTROY_BLOCK:
                  var41 = PlayerActionPacketWrapper.Action.STOP_DESTROY_BLOCK;
                  break;
               case DROP_ALL_ITEMS:
                  var41 = PlayerActionPacketWrapper.Action.DROP_ALL_ITEMS;
                  break;
               case DROP_ITEM:
                  var41 = PlayerActionPacketWrapper.Action.DROP_ITEM;
                  break;
               case RELEASE_USE_ITEM:
                  var41 = PlayerActionPacketWrapper.Action.RELEASE_USE_ITEM;
                  break;
               case SWAP_ITEM_WITH_OFFHAND:
                  var41 = PlayerActionPacketWrapper.Action.SWAP_ITEM_WITH_OFFHAND;
                  break;
               default:
                  throw new IllegalStateException();
               }

               return new PlayerActionPacketWrapper(var30.getX(), var30.getY(), var30.getZ(), var38, var41, var29.getSequence());
            }
         }

         if (var1 instanceof ClientboundTeleportEntityPacket) {
            ClientboundTeleportEntityPacket var22 = (ClientboundTeleportEntityPacket)var1;
            return new TeleportEntityPacketWrapper(var22.id(), var22.change().position().x(), var22.change().position().y(), var22.change().position().z(), (byte)((int)var22.change().yRot()), (byte)((int)var22.change().xRot()), var22.onGround());
         } else {
            boolean var23;
            Field var27;
            if (var1 instanceof Pos) {
               Pos var21 = (Pos)var1;
               var23 = false;

               try {
                  var27 = ClientboundMoveEntityPacket.class.getDeclaredField("entityId");
                  var27.setAccessible(true);
                  var3 = (Integer)var27.get(var1);
               } catch (NoSuchFieldException | IllegalAccessException var15) {
                  throw new RuntimeException(var15);
               }

               return new UpdateEntityPositionPacketWrapper(var3, var21.getXa(), var21.getYa(), var21.getZa(), var21.isOnGround());
            } else if (var1 instanceof net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot) {
               net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot var20 = (net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot)var1;
               var23 = false;

               try {
                  var27 = ClientboundMoveEntityPacket.class.getDeclaredField("entityId");
                  var27.setAccessible(true);
                  var3 = (Integer)var27.get(var1);
               } catch (IllegalAccessException | NoSuchFieldException var16) {
                  throw new RuntimeException(var16);
               }

               return new UpdateEntityRotationPacketWrapper(var3, (byte)((int)var20.getYRot()), (byte)((int)var20.getXRot()), var20.isOnGround());
            } else if (var1 instanceof net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot) {
               net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot var19 = (net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot)var1;
               var23 = false;

               try {
                  var27 = ClientboundMoveEntityPacket.class.getDeclaredField("entityId");
                  var27.setAccessible(true);
                  var3 = (Integer)var27.get(var1);
               } catch (IllegalAccessException | NoSuchFieldException var17) {
                  throw new RuntimeException(var17);
               }

               return new UpdateEntityPositionRotationPacketWrapper(var3, var19.getXa(), var19.getYa(), var19.getZa(), (byte)((int)var19.getYRot()), (byte)((int)var19.getXRot()), var19.isOnGround());
            } else {
               return null;
            }
         }
      }
   }

   @NotNull
   public Object createInstance(@NotNull PacketWrapper var1) {
      if (var1 instanceof PlayerInputPacketWrapper) {
         PlayerInputPacketWrapper var13 = (PlayerInputPacketWrapper)var1;
         boolean var21 = var13.forward > 0.0F;
         boolean var22 = var13.forward < 0.0F;
         boolean var25 = var13.jump;
         boolean var27 = var13.sideways > 0.0F;
         boolean var28 = var13.sideways < 0.0F;
         boolean var8 = var13.unmount;
         boolean var9 = false;
         Input var10 = new Input(var21, var22, var27, var28, var25, var8, var9);
         return new ServerboundPlayerInputPacket(var10);
      } else if (var1 instanceof InteractPacketWrapper) {
         InteractPacketWrapper var19 = (InteractPacketWrapper)var1;
         FriendlyByteBuf var20 = new FriendlyByteBuf(Unpooled.buffer());
         var20.writeVarInt(var19.entityId);
         switch(var19.action) {
         case INTERACT:
            var20.writeVarInt(0);
            var20.writeVarInt(var19.hand == InteractPacketWrapper.Hand.MAIN_HAND ? 0 : 1);
            break;
         case ATTACK:
            var20.writeVarInt(1);
            break;
         case INTERACT_AT:
            var20.writeVarInt(2);
            var20.writeFloat(((Double)Objects.requireNonNull(var19.targetX)).floatValue());
            var20.writeFloat(((Double)Objects.requireNonNull(var19.targetY)).floatValue());
            var20.writeFloat(((Double)Objects.requireNonNull(var19.targetZ)).floatValue());
            var20.writeVarInt(var19.hand == InteractPacketWrapper.Hand.MAIN_HAND ? 0 : 1);
         }

         var20.writeBoolean(var19.sneaking);

         try {
            Constructor var24 = ServerboundInteractPacket.class.getDeclaredConstructor(FriendlyByteBuf.class);
            var24.setAccessible(true);
            return var24.newInstance(var20);
         } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException var11) {
            throw new RuntimeException(var11);
         }
      } else if (var1 instanceof SwingArmPacketWrapper) {
         return new ServerboundSwingPacket(((SwingArmPacketWrapper)var1).hand == SwingArmPacketWrapper.Hand.MAIN_HAND ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
      } else if (var1 instanceof UseItemPacketWrapper) {
         UseItemPacketWrapper var17 = (UseItemPacketWrapper)var1;
         return new ServerboundUseItemPacket(var17.hand == UseItemPacketWrapper.Hand.MAIN_HAND ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, var17.sequence, 0.0F, 0.0F);
      } else if (var1 instanceof SetPlayerRotationPacketWrapper) {
         SetPlayerRotationPacketWrapper var16 = (SetPlayerRotationPacketWrapper)var1;
         return new Rot(var16.yRot, var16.xRot, var16.onGround, false);
      } else if (var1 instanceof SetPlayerPositionRotationPacketWrapper) {
         SetPlayerPositionRotationPacketWrapper var15 = (SetPlayerPositionRotationPacketWrapper)var1;
         return new PosRot(var15.x, var15.y, var15.z, var15.yRot, var15.xRot, var15.onGround, false);
      } else if (var1 instanceof PlayerActionPacketWrapper) {
         PlayerActionPacketWrapper var14 = (PlayerActionPacketWrapper)var1;
         BlockPos var18 = new BlockPos(var14.positionX, var14.positionY, var14.positionZ);
         Direction var10000;
         switch(var14.direction) {
         case DOWN:
            var10000 = Direction.DOWN;
            break;
         case UP:
            var10000 = Direction.UP;
            break;
         case NORTH:
            var10000 = Direction.NORTH;
            break;
         case SOUTH:
            var10000 = Direction.SOUTH;
            break;
         case WEST:
            var10000 = Direction.WEST;
            break;
         case EAST:
            var10000 = Direction.EAST;
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         Direction var23 = var10000;
         net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action var29;
         switch(var14.action) {
         case START_DESTROY_BLOCK:
            var29 = net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK;
            break;
         case ABORT_DESTROY_BLOCK:
            var29 = net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK;
            break;
         case STOP_DESTROY_BLOCK:
            var29 = net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK;
            break;
         case DROP_ALL_ITEMS:
            var29 = net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.DROP_ALL_ITEMS;
            break;
         case DROP_ITEM:
            var29 = net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.DROP_ITEM;
            break;
         case RELEASE_USE_ITEM:
            var29 = net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.RELEASE_USE_ITEM;
            break;
         case SWAP_ITEM_WITH_OFFHAND:
            var29 = net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action.SWAP_ITEM_WITH_OFFHAND;
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action var26 = var29;
         return new ServerboundPlayerActionPacket(var26, var18, var23, var14.sequence);
      } else if (var1 instanceof TeleportEntityPacketWrapper) {
         TeleportEntityPacketWrapper var2 = (TeleportEntityPacketWrapper)var1;
         FriendlyByteBuf var6 = new FriendlyByteBuf(Unpooled.buffer());
         var6.writeVarInt(var2.entityId);
         var6.writeDouble(var2.x);
         var6.writeDouble(var2.y);
         var6.writeDouble(var2.z);
         var6.writeByte((byte)((int)((float)var2.yRot * 256.0F / 360.0F)));
         var6.writeByte((byte)((int)((float)var2.xRot * 256.0F / 360.0F)));
         var6.writeBoolean(var2.onGround);

         try {
            Constructor var7 = ClientboundTeleportEntityPacket.class.getDeclaredConstructor(FriendlyByteBuf.class);
            var7.setAccessible(true);
            return var7.newInstance(var6);
         } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException var12) {
            throw new RuntimeException(var12);
         }
      } else if (var1 instanceof UpdateEntityPositionPacketWrapper) {
         UpdateEntityPositionPacketWrapper var3 = (UpdateEntityPositionPacketWrapper)var1;
         return new Pos(var3.entityId, var3.deltaX, var3.deltaY, var3.deltaZ, var3.onGround);
      } else if (var1 instanceof UpdateEntityRotationPacketWrapper) {
         UpdateEntityRotationPacketWrapper var4 = (UpdateEntityRotationPacketWrapper)var1;
         return new net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot(var4.entityId, var4.yRot, var4.xRot, var4.onGround);
      } else if (var1 instanceof UpdateEntityPositionRotationPacketWrapper) {
         UpdateEntityPositionRotationPacketWrapper var5 = (UpdateEntityPositionRotationPacketWrapper)var1;
         return new net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot(var5.entityId, var5.deltaX, var5.deltaY, var5.deltaZ, var5.yRot, var5.xRot, var5.onGround);
      } else {
         throw new IllegalArgumentException();
      }
   }
}
