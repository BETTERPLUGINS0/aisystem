package implementation.v1_21_R6.service;

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
import implementation.v1_21_R6.util.PacketWritingUtil;
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
         return var8.newInstance(var3);
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

   private Object reflective(Object var1) {
      try {
         Field var2 = ServerboundInteractPacket.class.getDeclaredField("b");
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
         Action var28 = var25.getAction();
         EntityActionWrapper.Action var30 = null;
         switch(var28) {
         case START_SPRINTING:
            var30 = EntityActionWrapper.Action.START_SPRINTING;
            break;
         case STOP_SPRINTING:
            var30 = EntityActionWrapper.Action.STOP_SPRINTING;
            break;
         case STOP_SLEEPING:
            var30 = EntityActionWrapper.Action.LEAVE_BED;
            break;
         case START_RIDING_JUMP:
            var30 = EntityActionWrapper.Action.START_JUMP_HORSE;
            break;
         case STOP_RIDING_JUMP:
            var30 = EntityActionWrapper.Action.STOP_JUMP_HORSE;
            break;
         case OPEN_INVENTORY:
            var30 = EntityActionWrapper.Action.OPEN_VEHICLE_INVENTORY;
            break;
         case START_FALL_FLYING:
            var30 = EntityActionWrapper.Action.START_FLYING_ELYTRA;
         }

         return new EntityActionWrapper(var25.getId(), var30);
      } else if (var1 instanceof ServerboundPlayerInputPacket) {
         ServerboundPlayerInputPacket var24 = (ServerboundPlayerInputPacket)var1;
         return new PlayerInputPacketWrapper(var24.input().left() ? 1.0F : (var24.input().right() ? -1.0F : 0.0F), var24.input().forward() ? 1.0F : (var24.input().backward() ? -1.0F : 0.0F), var24.input().jump(), var24.input().shift());
      } else {
         if (var1 instanceof ServerboundInteractPacket) {
            ServerboundInteractPacket var2 = (ServerboundInteractPacket)var1;
            int var7 = (Integer)this.reflective(var2);

            String var8;
            try {
               Class var9 = Class.forName("net.minecraft.network.protocol.game.PacketPlayInUseEntity");
               Field var10 = var9.getDeclaredField("c");
               var10.setAccessible(true);
               Object var11 = var10.get(var2);
               Method var12 = var11.getClass().getDeclaredMethod("a");
               var12.setAccessible(true);
               var8 = String.valueOf(var12.invoke(var11));
            } catch (IllegalAccessException | NoSuchFieldException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException var22) {
               throw new RuntimeException(var22);
            }

            if (var8.equalsIgnoreCase("ATTACK")) {
               boolean var39 = var2.isUsingSecondaryAction();
               return new InteractPacketWrapper(var7, InteractPacketWrapper.Action.ATTACK, (Double)null, (Double)null, (Double)null, (InteractPacketWrapper.Hand)null, var39);
            }

            if (var8.equalsIgnoreCase("INTERACT_AT")) {
               Vec3 var38 = (Vec3)this.object(var2, "net.minecraft.network.protocol.game.PacketPlayInUseEntity$e", "c", "b");
               InteractionHand var42 = (InteractionHand)this.object(var2, "net.minecraft.network.protocol.game.PacketPlayInUseEntity$e", "c", "a");

               assert var38 != null;

               double var44 = var38.x;
               double var13 = var38.y;
               double var15 = var38.z;

               assert var42 != null;

               int var17 = var42.ordinal();
               boolean var18 = var2.isUsingSecondaryAction();
               return new InteractPacketWrapper(var7, InteractPacketWrapper.Action.INTERACT_AT, var44, var13, var15, var17 == 0 ? InteractPacketWrapper.Hand.MAIN_HAND : InteractPacketWrapper.Hand.OFF_HAND, var18);
            }

            if (var8.equalsIgnoreCase("INTERACT")) {
               InteractionHand var37 = (InteractionHand)this.object(var2, "net.minecraft.network.protocol.game.PacketPlayInUseEntity$d", "c", "a");

               assert var37 != null;

               int var40 = var37.ordinal();
               boolean var43 = var2.isUsingSecondaryAction();
               return new InteractPacketWrapper(var7, InteractPacketWrapper.Action.INTERACT, (Double)null, (Double)null, (Double)null, var40 == 0 ? InteractPacketWrapper.Hand.MAIN_HAND : InteractPacketWrapper.Hand.OFF_HAND, var43);
            }
         } else {
            if (var1 instanceof ServerboundSwingPacket) {
               return new SwingArmPacketWrapper(((ServerboundSwingPacket)var1).getHand() == InteractionHand.MAIN_HAND ? SwingArmPacketWrapper.Hand.MAIN_HAND : SwingArmPacketWrapper.Hand.OFF_HAND);
            }

            if (var1 instanceof ServerboundUseItemPacket) {
               ServerboundUseItemPacket var27 = (ServerboundUseItemPacket)var1;
               return new UseItemPacketWrapper(var27.getHand() == InteractionHand.MAIN_HAND ? UseItemPacketWrapper.Hand.MAIN_HAND : UseItemPacketWrapper.Hand.OFF_HAND, var27.getSequence());
            }

            if (var1 instanceof Rot) {
               Rot var29 = (Rot)var1;
               return new SetPlayerRotationPacketWrapper(var29.yRot, var29.xRot, var29.isOnGround());
            }

            if (var1 instanceof PosRot) {
               PosRot var31 = (PosRot)var1;
               return new SetPlayerPositionRotationPacketWrapper(var31.x, var31.y, var31.z, var31.yRot, var31.xRot, var31.isOnGround());
            }

            if (var1 instanceof ServerboundPlayerActionPacket) {
               ServerboundPlayerActionPacket var34 = (ServerboundPlayerActionPacket)var1;
               BlockPos var35 = var34.getPos();
               PlayerActionPacketWrapper.Direction var10000;
               switch(var34.getDirection()) {
               case DOWN:
                  var10000 = PlayerActionPacketWrapper.Direction.DOWN;
                  break;
               case UP:
                  var10000 = PlayerActionPacketWrapper.Direction.UP;
                  break;
               case NORTH:
                  var10000 = PlayerActionPacketWrapper.Direction.NORTH;
                  break;
               case SOUTH:
                  var10000 = PlayerActionPacketWrapper.Direction.SOUTH;
                  break;
               case WEST:
                  var10000 = PlayerActionPacketWrapper.Direction.WEST;
                  break;
               case EAST:
                  var10000 = PlayerActionPacketWrapper.Direction.EAST;
                  break;
               default:
                  throw new IllegalStateException();
               }

               PlayerActionPacketWrapper.Direction var36 = var10000;
               PlayerActionPacketWrapper.Action var45;
               switch(var34.getAction()) {
               case START_DESTROY_BLOCK:
                  var45 = PlayerActionPacketWrapper.Action.START_DESTROY_BLOCK;
                  break;
               case ABORT_DESTROY_BLOCK:
                  var45 = PlayerActionPacketWrapper.Action.ABORT_DESTROY_BLOCK;
                  break;
               case STOP_DESTROY_BLOCK:
                  var45 = PlayerActionPacketWrapper.Action.STOP_DESTROY_BLOCK;
                  break;
               case DROP_ALL_ITEMS:
                  var45 = PlayerActionPacketWrapper.Action.DROP_ALL_ITEMS;
                  break;
               case DROP_ITEM:
                  var45 = PlayerActionPacketWrapper.Action.DROP_ITEM;
                  break;
               case RELEASE_USE_ITEM:
                  var45 = PlayerActionPacketWrapper.Action.RELEASE_USE_ITEM;
                  break;
               case SWAP_ITEM_WITH_OFFHAND:
                  var45 = PlayerActionPacketWrapper.Action.SWAP_ITEM_WITH_OFFHAND;
                  break;
               default:
                  throw new IllegalStateException();
               }

               PlayerActionPacketWrapper.Action var41 = var45;
               return new PlayerActionPacketWrapper(var35.getX(), var35.getY(), var35.getZ(), var36, var41, var34.getSequence());
            }
         }

         if (var1 instanceof ClientboundTeleportEntityPacket) {
            ClientboundTeleportEntityPacket var23 = (ClientboundTeleportEntityPacket)var1;
            return new TeleportEntityPacketWrapper(var23.id(), var23.change().position().x(), var23.change().position().y(), var23.change().position().z(), (byte)((int)var23.change().yRot()), (byte)((int)var23.change().xRot()), var23.onGround());
         } else {
            boolean var6;
            int var32;
            Field var33;
            if (var1 instanceof Pos) {
               Pos var3 = (Pos)var1;
               var6 = false;

               try {
                  var33 = ClientboundMoveEntityPacket.class.getDeclaredField("entityId");
                  var33.setAccessible(true);
                  var32 = (Integer)var33.get(var1);
               } catch (IllegalAccessException | NoSuchFieldException var19) {
                  throw new RuntimeException(var19);
               }

               return new UpdateEntityPositionPacketWrapper(var32, var3.getXa(), var3.getYa(), var3.getZa(), var3.isOnGround());
            } else if (var1 instanceof net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot) {
               net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot var4 = (net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.Rot)var1;
               var6 = false;

               try {
                  var33 = ClientboundMoveEntityPacket.class.getDeclaredField("entityId");
                  var33.setAccessible(true);
                  var32 = (Integer)var33.get(var1);
               } catch (IllegalAccessException | NoSuchFieldException var20) {
                  throw new RuntimeException(var20);
               }

               return new UpdateEntityRotationPacketWrapper(var32, (byte)((int)var4.getYRot()), (byte)((int)var4.getXRot()), var4.isOnGround());
            } else if (var1 instanceof net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot) {
               net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot var5 = (net.minecraft.network.protocol.game.ClientboundMoveEntityPacket.PosRot)var1;
               var6 = false;

               try {
                  var33 = ClientboundMoveEntityPacket.class.getDeclaredField("entityId");
                  var33.setAccessible(true);
                  var32 = (Integer)var33.get(var1);
               } catch (IllegalAccessException | NoSuchFieldException var21) {
                  throw new RuntimeException(var21);
               }

               return new UpdateEntityPositionRotationPacketWrapper(var32, var5.getXa(), var5.getYa(), var5.getZa(), (byte)((int)var5.getYRot()), (byte)((int)var5.getXRot()), var5.isOnGround());
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
