package implementation.v1_21_R7.service;

import advancedplugins.pm2.cv.util.ReflectionUtil;
import java.util.List;
import java.util.Objects;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R6.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R6.entity.CraftPlayer;
import org.bukkit.entity.Entity;

public class EntityService implements advancedplugins.pm2.cv.api.service.EntityService {
   public void addPassenger(Entity var1, Entity var2) {
      CraftEntity var3 = (CraftEntity)var1;
      CraftEntity var4 = (CraftEntity)var2;
      net.minecraft.world.entity.Entity var5 = var3.getHandle();
      net.minecraft.world.entity.Entity var6 = var4.getHandle();
      if (var6.cl()) {
         var6.cb();
      }

      var6.c(EntityPose.a);
      ReflectionUtil.setField(net.minecraft.world.entity.Entity.class, var6, "vehicle", var5);
      ReflectionUtil.callMethod(net.minecraft.world.entity.Entity.class, var5, "addPassenger", List.of(net.minecraft.world.entity.Entity.class), var6);
      if (var4 instanceof CraftPlayer) {
         CraftPlayer var7 = (CraftPlayer)var4;
         EntityPlayer var8 = var7.getHandle();
         var5.k(var8);
         var8.g.a(new PositionMoveRotation(var8.dD(), Vec3D.c, 0.0F, 0.0F), Relative.k);
         var8.g.b(new PacketPlayOutMount(var5));
      }

   }

   public Entity getEntityById(int var1, World var2) {
      CraftWorld var3 = (CraftWorld)var2;
      WorldServer var4 = var3.getHandle();
      return var4.M.e().a(var1) != null ? ((net.minecraft.world.entity.Entity)Objects.requireNonNull((net.minecraft.world.entity.Entity)var4.M.e().a(var1))).getBukkitEntity() : null;
   }
}
