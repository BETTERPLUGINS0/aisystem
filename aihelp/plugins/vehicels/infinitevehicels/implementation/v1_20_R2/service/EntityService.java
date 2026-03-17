package implementation.v1_20_R2.service;

import java.util.Objects;
import net.minecraft.server.level.WorldServer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.entity.Entity;

public class EntityService implements advancedplugins.pm2.cv.api.service.EntityService {
   public void addPassenger(Entity var1, Entity var2) {
      var1.addPassenger(var2);
   }

   public Entity getEntityById(int var1, World var2) {
      CraftWorld var3 = (CraftWorld)var2;
      WorldServer var4 = var3.getHandle();
      return var4.M.d().a(var1) != null ? ((net.minecraft.world.entity.Entity)Objects.requireNonNull((net.minecraft.world.entity.Entity)var4.M.d().a(var1))).getBukkitEntity() : null;
   }
}
