package implementation.v1_21_R1.service;

import advancedplugins.pm2.cv.util.ReflectionUtil;
import java.util.List;
import java.util.Objects;
import net.minecraft.network.protocol.game.PacketPlayOutMount;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_21_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_21_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;

public class EntityService implements advancedplugins.pm2.cv.api.service.EntityService {
   public void addPassenger(Entity var1, Entity var2) {
      CraftEntity var3 = (CraftEntity)var1;
      CraftEntity var4 = (CraftEntity)var2;
      net.minecraft.world.entity.Entity var5 = var3.getHandle();
      net.minecraft.world.entity.Entity var6 = var4.getHandle();
      if (var6.bS()) {
         var6.ad();
      }

      var6.b(EntityPose.a);
      ReflectionUtil.setField(net.minecraft.world.entity.Entity.class, var6, "vehicle", var5);
      ReflectionUtil.callMethod(net.minecraft.world.entity.Entity.class, var5, "addPassenger", List.of(net.minecraft.world.entity.Entity.class), var6);
      if (var4 instanceof CraftPlayer) {
         CraftPlayer var7 = (CraftPlayer)var4;
         EntityPlayer var8 = var7.getHandle();
         var8.b(Vec3D.b);
         var5.j(var8);
         var8.c.a(var8.dt(), var8.dv(), var8.dz(), var8.dE(), var8.dG());
         var8.c.b(new PacketPlayOutMount(var5));
      }

   }

   public Entity getEntityById(int var1, World var2) {
      CraftWorld var3 = (CraftWorld)var2;
      WorldServer var4 = var3.getHandle();
      return var4.N.d().a(var1) != null ? ((net.minecraft.world.entity.Entity)Objects.requireNonNull((net.minecraft.world.entity.Entity)var4.N.d().a(var1))).getBukkitEntity() : null;
   }
}
