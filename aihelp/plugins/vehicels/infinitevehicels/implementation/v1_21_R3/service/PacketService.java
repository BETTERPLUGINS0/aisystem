package implementation.v1_21_R3.service;

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
import implementation.v1_21_R3.util.PacketWritingUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelPipeline;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.EnumDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketPlayInArmAnimation;
import net.minecraft.network.protocol.game.PacketPlayInBlockDig;
import net.minecraft.network.protocol.game.PacketPlayInBlockPlace;
import net.minecraft.network.protocol.game.PacketPlayInEntityAction;
import net.minecraft.network.protocol.game.PacketPlayInSteerVehicle;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntity;
import net.minecraft.network.protocol.game.PacketPlayOutEntityTeleport;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.network.protocol.game.PacketPlayInBlockDig.EnumPlayerDigType;
import net.minecraft.network.protocol.game.PacketPlayInEntityAction.EnumPlayerAction;
import net.minecraft.network.protocol.game.PacketPlayInFlying.PacketPlayInLook;
import net.minecraft.network.protocol.game.PacketPlayInFlying.PacketPlayInPositionLook;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMove;
import net.minecraft.network.protocol.game.PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.world.EnumHand;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_21_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PacketService implements advancedplugins.pm2.cv.service.PacketService {
   @VersionSensible
   private NetworkManager getNetworkManager(@NotNull PlayerConnection var1) {
      try {
         Field var2 = ServerCommonPacketListenerImpl.class.getDeclaredField("e");
         var2.setAccessible(true);
         return (NetworkManager)var2.get(var1);
      } catch (IllegalAccessException | NoSuchFieldException var3) {
         throw new IllegalArgumentException(var3);
      }
   }

   public ChannelPipeline getChannelPipeline(@NotNull Player var1) {
      return this.getNetworkManager(((CraftPlayer)var1).getHandle().f).n.pipeline();
   }

   @NotNull
   public Object createPassengersPacket(int var1, List<Integer> var2) {
      PacketDataSerializer var3 = new PacketDataSerializer(Unpooled.buffer());
      TIntArrayList var4 = new TIntArrayList();
      Iterator var5 = var2.iterator();

      while(var5.hasNext()) {
         Integer var6 = (Integer)var5.next();
         var4.add(var6);
      }

      var3.c(var1);
      var3.a(var4.toArray());

      try {
         Constructor var8 = PacketPlayOutMount.class.getDeclaredConstructor(PacketDataSerializer.class);
         var8.setAccessible(true);
         return (PacketPlayOutMount)var8.newInstance(var3);
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
         this.getNetworkManager(((CraftPlayer)var1).getHandle().f).a((Packet)var2);
      }

   }

   private Object reflective(Object var1) {
      try {
         Field var2 = PacketPlayInUseEntity.class.getDeclaredField("b");
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
      if (var1 instanceof PacketPlayOutMount) {
         PacketPlayOutMount var26 = (PacketPlayOutMount)var1;
         return new SetEntityPassengersWrapper(var26.e(), var26.b());
      } else if (var1 instanceof PacketPlayInEntityAction) {
         PacketPlayInEntityAction var25 = (PacketPlayInEntityAction)var1;
         EnumPlayerAction var39 = var25.e();
         EntityActionWrapper.Action var31 = null;
         switch(var39) {
         case a:
            var31 = EntityActionWrapper.Action.START_SNEAKING;
            break;
         case b:
            var31 = EntityActionWrapper.Action.STOP_SNEAKING;
            break;
         case d:
            var31 = EntityActionWrapper.Action.START_SPRINTING;
            break;
         case e:
            var31 = EntityActionWrapper.Action.STOP_SPRINTING;
            break;
         case c:
            var31 = EntityActionWrapper.Action.LEAVE_BED;
            break;
         case f:
            var31 = EntityActionWrapper.Action.START_JUMP_HORSE;
            break;
         case g:
            var31 = EntityActionWrapper.Action.STOP_JUMP_HORSE;
            break;
         case h:
            var31 = EntityActionWrapper.Action.OPEN_VEHICLE_INVENTORY;
            break;
         case i:
            var31 = EntityActionWrapper.Action.START_FLYING_ELYTRA;
         }

         return new EntityActionWrapper(var25.b(), var31);
      } else if (var1 instanceof PacketPlayInSteerVehicle) {
         PacketPlayInSteerVehicle var24 = (PacketPlayInSteerVehicle)var1;
         return new PlayerInputPacketWrapper(var24.b().c() ? 1.0F : (var24.b().d() ? -1.0F : 0.0F), var24.b().a() ? 1.0F : (var24.b().b() ? -1.0F : 0.0F), var24.b().e(), var24.b().f());
      } else {
         int var3;
         if (var1 instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity var2 = (PacketPlayInUseEntity)var1;
            var3 = (Integer)this.reflective(var2);

            String var4;
            try {
               Class var5 = Class.forName("net.minecraft.network.protocol.game.PacketPlayInUseEntity");
               Field var6 = var5.getDeclaredField("c");
               var6.setAccessible(true);
               Object var7 = var6.get(var2);
               Method var8 = var7.getClass().getDeclaredMethod("a");
               var8.setAccessible(true);
               var4 = String.valueOf(var8.invoke(var7));
            } catch (IllegalAccessException | NoSuchFieldException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException var18) {
               throw new RuntimeException(var18);
            }

            if (var4.equalsIgnoreCase("ATTACK")) {
               boolean var34 = var2.b();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.ATTACK, (Double)null, (Double)null, (Double)null, (InteractPacketWrapper.Hand)null, var34);
            }

            if (var4.equalsIgnoreCase("INTERACT_AT")) {
               Vec3D var32 = (Vec3D)this.object(var2, "net.minecraft.network.protocol.game.PacketPlayInUseEntity$e", "c", "b");
               EnumHand var40 = (EnumHand)this.object(var2, "net.minecraft.network.protocol.game.PacketPlayInUseEntity$e", "c", "a");

               assert var32 != null;

               double var43 = var32.d;
               double var9 = var32.e;
               double var11 = var32.f;

               assert var40 != null;

               int var13 = var40.ordinal();
               boolean var14 = var2.b();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.INTERACT_AT, var43, var9, var11, var13 == 0 ? InteractPacketWrapper.Hand.MAIN_HAND : InteractPacketWrapper.Hand.OFF_HAND, var14);
            }

            if (var4.equalsIgnoreCase("INTERACT")) {
               EnumHand var28 = (EnumHand)this.object(var2, "net.minecraft.network.protocol.game.PacketPlayInUseEntity$d", "c", "a");

               assert var28 != null;

               int var37 = var28.ordinal();
               boolean var42 = var2.b();
               return new InteractPacketWrapper(var3, InteractPacketWrapper.Action.INTERACT, (Double)null, (Double)null, (Double)null, var37 == 0 ? InteractPacketWrapper.Hand.MAIN_HAND : InteractPacketWrapper.Hand.OFF_HAND, var42);
            }
         } else {
            if (var1 instanceof PacketPlayInArmAnimation) {
               return new SwingArmPacketWrapper(((PacketPlayInArmAnimation)var1).b() == EnumHand.a ? SwingArmPacketWrapper.Hand.MAIN_HAND : SwingArmPacketWrapper.Hand.OFF_HAND);
            }

            if (var1 instanceof PacketPlayInBlockPlace) {
               PacketPlayInBlockPlace var36 = (PacketPlayInBlockPlace)var1;
               return new UseItemPacketWrapper(var36.b() == EnumHand.a ? UseItemPacketWrapper.Hand.MAIN_HAND : UseItemPacketWrapper.Hand.OFF_HAND, var36.e());
            }

            if (var1 instanceof PacketPlayInLook) {
               PacketPlayInLook var35 = (PacketPlayInLook)var1;
               return new SetPlayerRotationPacketWrapper(var35.d, var35.e, var35.b());
            }

            if (var1 instanceof PacketPlayInPositionLook) {
               PacketPlayInPositionLook var33 = (PacketPlayInPositionLook)var1;
               return new SetPlayerPositionRotationPacketWrapper(var33.a, var33.b, var33.c, var33.d, var33.e, var33.b());
            }

            if (var1 instanceof PacketPlayInBlockDig) {
               PacketPlayInBlockDig var29 = (PacketPlayInBlockDig)var1;
               BlockPosition var30 = var29.b();
               PlayerActionPacketWrapper.Direction var38;
               switch(var29.e()) {
               case a:
                  var38 = PlayerActionPacketWrapper.Direction.DOWN;
                  break;
               case b:
                  var38 = PlayerActionPacketWrapper.Direction.UP;
                  break;
               case c:
                  var38 = PlayerActionPacketWrapper.Direction.NORTH;
                  break;
               case d:
                  var38 = PlayerActionPacketWrapper.Direction.SOUTH;
                  break;
               case e:
                  var38 = PlayerActionPacketWrapper.Direction.WEST;
                  break;
               case f:
                  var38 = PlayerActionPacketWrapper.Direction.EAST;
                  break;
               default:
                  throw new IllegalStateException();
               }

               PlayerActionPacketWrapper.Action var41;
               switch(var29.f()) {
               case a:
                  var41 = PlayerActionPacketWrapper.Action.START_DESTROY_BLOCK;
                  break;
               case b:
                  var41 = PlayerActionPacketWrapper.Action.ABORT_DESTROY_BLOCK;
                  break;
               case c:
                  var41 = PlayerActionPacketWrapper.Action.STOP_DESTROY_BLOCK;
                  break;
               case d:
                  var41 = PlayerActionPacketWrapper.Action.DROP_ALL_ITEMS;
                  break;
               case e:
                  var41 = PlayerActionPacketWrapper.Action.DROP_ITEM;
                  break;
               case f:
                  var41 = PlayerActionPacketWrapper.Action.RELEASE_USE_ITEM;
                  break;
               case g:
                  var41 = PlayerActionPacketWrapper.Action.SWAP_ITEM_WITH_OFFHAND;
                  break;
               default:
                  throw new IllegalStateException();
               }

               return new PlayerActionPacketWrapper(var30.u(), var30.v(), var30.w(), var38, var41, var29.g());
            }
         }

         if (var1 instanceof PacketPlayOutEntityTeleport) {
            PacketPlayOutEntityTeleport var22 = (PacketPlayOutEntityTeleport)var1;
            return new TeleportEntityPacketWrapper(var22.b(), var22.e().a().a(), var22.e().a().b(), var22.e().a().c(), (byte)((int)var22.e().c()), (byte)((int)var22.e().d()), var22.g());
         } else {
            boolean var23;
            Field var27;
            if (var1 instanceof PacketPlayOutRelEntityMove) {
               PacketPlayOutRelEntityMove var21 = (PacketPlayOutRelEntityMove)var1;
               var23 = false;

               try {
                  var27 = PacketPlayOutEntity.class.getDeclaredField("a");
                  var27.setAccessible(true);
                  var3 = (Integer)var27.get(var1);
               } catch (IllegalAccessException | NoSuchFieldException var15) {
                  throw new RuntimeException(var15);
               }

               return new UpdateEntityPositionPacketWrapper(var3, var21.b(), var21.e(), var21.f(), var21.k());
            } else if (var1 instanceof PacketPlayOutEntityLook) {
               PacketPlayOutEntityLook var20 = (PacketPlayOutEntityLook)var1;
               var23 = false;

               try {
                  var27 = PacketPlayOutEntity.class.getDeclaredField("a");
                  var27.setAccessible(true);
                  var3 = (Integer)var27.get(var1);
               } catch (IllegalAccessException | NoSuchFieldException var16) {
                  throw new RuntimeException(var16);
               }

               return new UpdateEntityRotationPacketWrapper(var3, (byte)((int)var20.g()), (byte)((int)var20.h()), var20.k());
            } else if (var1 instanceof PacketPlayOutRelEntityMoveLook) {
               PacketPlayOutRelEntityMoveLook var19 = (PacketPlayOutRelEntityMoveLook)var1;
               var23 = false;

               try {
                  var27 = PacketPlayOutEntity.class.getDeclaredField("a");
                  var27.setAccessible(true);
                  var3 = (Integer)var27.get(var1);
               } catch (IllegalAccessException | NoSuchFieldException var17) {
                  throw new RuntimeException(var17);
               }

               return new UpdateEntityPositionRotationPacketWrapper(var3, var19.b(), var19.e(), var19.f(), (byte)((int)var19.g()), (byte)((int)var19.h()), var19.k());
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
         return new PacketPlayInSteerVehicle(var10);
      } else if (var1 instanceof InteractPacketWrapper) {
         InteractPacketWrapper var19 = (InteractPacketWrapper)var1;
         PacketDataSerializer var20 = new PacketDataSerializer(Unpooled.buffer());
         var20.c(var19.entityId);
         switch(var19.action) {
         case INTERACT:
            var20.c(0);
            var20.c(var19.hand == InteractPacketWrapper.Hand.MAIN_HAND ? 0 : 1);
            break;
         case ATTACK:
            var20.c(1);
            break;
         case INTERACT_AT:
            var20.c(2);
            var20.a(((Double)Objects.requireNonNull(var19.targetX)).floatValue());
            var20.a(((Double)Objects.requireNonNull(var19.targetY)).floatValue());
            var20.a(((Double)Objects.requireNonNull(var19.targetZ)).floatValue());
            var20.c(var19.hand == InteractPacketWrapper.Hand.MAIN_HAND ? 0 : 1);
         }

         var20.a(var19.sneaking);

         try {
            Constructor var24 = PacketPlayInUseEntity.class.getDeclaredConstructor(PacketDataSerializer.class);
            var24.setAccessible(true);
            return var24.newInstance(var20);
         } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException var11) {
            throw new RuntimeException(var11);
         }
      } else if (var1 instanceof SwingArmPacketWrapper) {
         return new PacketPlayInArmAnimation(((SwingArmPacketWrapper)var1).hand == SwingArmPacketWrapper.Hand.MAIN_HAND ? EnumHand.a : EnumHand.b);
      } else if (var1 instanceof UseItemPacketWrapper) {
         UseItemPacketWrapper var17 = (UseItemPacketWrapper)var1;
         return new PacketPlayInBlockPlace(var17.hand == UseItemPacketWrapper.Hand.MAIN_HAND ? EnumHand.a : EnumHand.b, var17.sequence, 0.0F, 0.0F);
      } else if (var1 instanceof SetPlayerRotationPacketWrapper) {
         SetPlayerRotationPacketWrapper var16 = (SetPlayerRotationPacketWrapper)var1;
         return new PacketPlayInLook(var16.yRot, var16.xRot, var16.onGround, false);
      } else if (var1 instanceof SetPlayerPositionRotationPacketWrapper) {
         SetPlayerPositionRotationPacketWrapper var15 = (SetPlayerPositionRotationPacketWrapper)var1;
         return new PacketPlayInPositionLook(var15.x, var15.y, var15.z, var15.yRot, var15.xRot, var15.onGround, false);
      } else if (var1 instanceof PlayerActionPacketWrapper) {
         PlayerActionPacketWrapper var14 = (PlayerActionPacketWrapper)var1;
         BlockPosition var18 = new BlockPosition(var14.positionX, var14.positionY, var14.positionZ);
         EnumDirection var10000;
         switch(var14.direction) {
         case DOWN:
            var10000 = EnumDirection.a;
            break;
         case UP:
            var10000 = EnumDirection.b;
            break;
         case NORTH:
            var10000 = EnumDirection.c;
            break;
         case SOUTH:
            var10000 = EnumDirection.d;
            break;
         case WEST:
            var10000 = EnumDirection.e;
            break;
         case EAST:
            var10000 = EnumDirection.f;
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         EnumDirection var23 = var10000;
         EnumPlayerDigType var29;
         switch(var14.action) {
         case START_DESTROY_BLOCK:
            var29 = EnumPlayerDigType.a;
            break;
         case ABORT_DESTROY_BLOCK:
            var29 = EnumPlayerDigType.b;
            break;
         case STOP_DESTROY_BLOCK:
            var29 = EnumPlayerDigType.c;
            break;
         case DROP_ALL_ITEMS:
            var29 = EnumPlayerDigType.d;
            break;
         case DROP_ITEM:
            var29 = EnumPlayerDigType.e;
            break;
         case RELEASE_USE_ITEM:
            var29 = EnumPlayerDigType.f;
            break;
         case SWAP_ITEM_WITH_OFFHAND:
            var29 = EnumPlayerDigType.g;
            break;
         default:
            throw new IncompatibleClassChangeError();
         }

         EnumPlayerDigType var26 = var29;
         return new PacketPlayInBlockDig(var26, var18, var23, var14.sequence);
      } else if (var1 instanceof TeleportEntityPacketWrapper) {
         TeleportEntityPacketWrapper var2 = (TeleportEntityPacketWrapper)var1;
         PacketDataSerializer var6 = new PacketDataSerializer(Unpooled.buffer());
         var6.c(var2.entityId);
         var6.a(var2.x);
         var6.a(var2.y);
         var6.a(var2.z);
         var6.l((byte)((int)((float)var2.yRot * 256.0F / 360.0F)));
         var6.l((byte)((int)((float)var2.xRot * 256.0F / 360.0F)));
         var6.a(var2.onGround);

         try {
            Constructor var7 = PacketPlayOutEntityTeleport.class.getDeclaredConstructor(PacketDataSerializer.class);
            var7.setAccessible(true);
            return var7.newInstance(var6);
         } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException var12) {
            throw new RuntimeException(var12);
         }
      } else if (var1 instanceof UpdateEntityPositionPacketWrapper) {
         UpdateEntityPositionPacketWrapper var3 = (UpdateEntityPositionPacketWrapper)var1;
         return new PacketPlayOutRelEntityMove(var3.entityId, var3.deltaX, var3.deltaY, var3.deltaZ, var3.onGround);
      } else if (var1 instanceof UpdateEntityRotationPacketWrapper) {
         UpdateEntityRotationPacketWrapper var4 = (UpdateEntityRotationPacketWrapper)var1;
         return new PacketPlayOutEntityLook(var4.entityId, var4.yRot, var4.xRot, var4.onGround);
      } else if (var1 instanceof UpdateEntityPositionRotationPacketWrapper) {
         UpdateEntityPositionRotationPacketWrapper var5 = (UpdateEntityPositionRotationPacketWrapper)var1;
         return new PacketPlayOutRelEntityMoveLook(var5.entityId, var5.deltaX, var5.deltaY, var5.deltaZ, var5.yRot, var5.xRot, var5.onGround);
      } else {
         throw new IllegalArgumentException();
      }
   }
}
