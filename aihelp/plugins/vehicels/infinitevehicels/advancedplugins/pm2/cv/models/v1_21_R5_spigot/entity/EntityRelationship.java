package advancedplugins.pm2.cv.models.v1_21_R5_spigot.entity;

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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class EntityRelationship extends Entity {
   private EntityRelationship(int var1) {
      super(EntityTypes.aq, MinecraftServer.getServer().J());
      this.e(var1);
      this.o(0.0D, 0.0D, 0.0D);
      this.b(0.0F, 0.0F);
      this.e(false);
   }

   public static EntityRelationship of(int var0) {
      return new EntityRelationship(var0);
   }

   public static Entity of(int var0, int... var1) {
      EntityRelationship var2 = of(var0);
      var2.attachChildren(Arrays.stream(var1).mapToObj(EntityRelationship::new).toList());
      return var2;
   }

   public static Entity of(int var0, Collection<Integer> var1) {
      EntityRelationship var2 = of(var0);
      var2.attachChildren(var1.stream().map(EntityRelationship::new).toList());
      return var2;
   }

   protected void attachChildren(List<? extends Entity> var1) {
      this.aR = ImmutableList.copyOf(var1);
   }

   protected void a(@NotNull a var1) {
   }

   public boolean a(WorldServer var1, DamageSource var2, float var3) {
      return false;
   }

   protected void a(ValueInput var1) {
   }

   protected void a(ValueOutput var1) {
   }

   protected void readAdditionalSaveData(@NotNull NBTTagCompound var1) {
   }

   protected void addAdditionalSaveData(@NotNull NBTTagCompound var1) {
   }
}
