package advancedplugins.pm2.cv.models.v1_21_R3.entity;

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
public class EntityRelationship extends Entity {
   private EntityRelationship(int entityId) {
      super(EntityTypes.ao, MinecraftServer.getServer().J());
      this.e(var1);
      this.o(0.0D, 0.0D, 0.0D);
      this.b(0.0F, 0.0F);
      this.d(false);
   }

   public static EntityRelationship of(int parentId) {
      return new EntityRelationship(var0);
   }

   public static Entity of(int parentId, int... childIds) {
      EntityRelationship var2 = of(var0);
      var2.attachChildren(Arrays.stream(var1).mapToObj(EntityRelationship::new).toList());
      return var2;
   }

   public static Entity of(int parentId, Collection<Integer> childIds) {
      EntityRelationship var2 = of(var0);
      var2.attachChildren(var1.stream().map(EntityRelationship::new).toList());
      return var2;
   }

   protected void attachChildren(List<? extends Entity> children) {
      this.q = ImmutableList.copyOf(var1);
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
