package implementation.v1_21_R10P.service;

import advancedplugins.pm2.cv.util.ReflectionUtil;
import java.util.List;
import java.util.Objects;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.phys.Vec3;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;

public class EntityService implements advancedplugins.pm2.cv.api.service.EntityService {
   public void addPassenger(Entity var1, Entity var2) {
      CraftEntity var3 = (CraftEntity)var1;
      CraftEntity var4 = (CraftEntity)var2;
      net.minecraft.world.entity.Entity var5 = var3.getHandle();
      net.minecraft.world.entity.Entity var6 = var4.getHandle();
      if (var6.isPassenger()) {
         var6.stopRiding();
      }

      var6.setPose(Pose.STANDING);
      ReflectionUtil.setField(net.minecraft.world.entity.Entity.class, var6, "vehicle", var5);
      ReflectionUtil.callMethod(net.minecraft.world.entity.Entity.class, var5, "addPassenger", List.of(net.minecraft.world.entity.Entity.class), var6);
      if (var4 instanceof CraftPlayer) {
         CraftPlayer var7 = (CraftPlayer)var4;
         ServerPlayer var8 = var7.getHandle();
         var5.positionRider(var8);
         var8.connection.teleport(new PositionMoveRotation(var8.position(), Vec3.ZERO, 0.0F, 0.0F), Relative.ROTATION);
         var8.connection.send(new ClientboundSetPassengersPacket(var5));
      }

   }

   public Entity getEntityById(int var1, World var2) {
      CraftWorld var3 = (CraftWorld)var2;
      ServerLevel var4 = var3.getHandle();
      return var4.getEntity(var1) != null ? ((net.minecraft.world.entity.Entity)Objects.requireNonNull(var4.getEntity(var1))).getBukkitEntity() : null;
   }
}
