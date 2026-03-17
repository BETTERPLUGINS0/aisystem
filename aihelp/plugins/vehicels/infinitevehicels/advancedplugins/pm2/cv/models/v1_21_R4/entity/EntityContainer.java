package advancedplugins.pm2.cv.models.v1_21_R4.entity;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.syncher.DataWatcher.a;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class EntityContainer extends Entity {
   private EntityContainer(int id) {
      super(EntityTypes.ap, MinecraftServer.getServer().J());
      this.e(var1);
      this.o(0.0D, 0.0D, 0.0D);
      this.b(0.0F, 0.0F);
      this.d(false);
   }

   public static EntityContainer of(int id) {
      return new EntityContainer(var0);
   }

   public static Entity of(int id, int... passengerIds) {
      EntityContainer var2 = of(var0);
      var2.setPassengers(Arrays.stream(var1).mapToObj(EntityContainer::new).toList());
      return var2;
   }

   public static Entity of(int id, Collection<Integer> passengerIds) {
      EntityContainer var2 = of(var0);
      var2.setPassengers(var1.stream().map(EntityContainer::new).toList());
      return var2;
   }

   protected void setPassengers(List<? extends Entity> passengers) {
      this.u = ImmutableList.copyOf(var1);
   }

   protected void a(@NotNull a builder) {
   }

   public boolean a(WorldServer world, DamageSource source, float amount) {
      return false;
   }

   protected void a(@NotNull NBTTagCompound nbt) {
   }

   protected void b(@NotNull NBTTagCompound nbt) {
   }
}
