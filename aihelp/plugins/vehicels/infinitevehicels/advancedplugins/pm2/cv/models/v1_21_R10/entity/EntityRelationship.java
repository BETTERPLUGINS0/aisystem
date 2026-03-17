package advancedplugins.pm2.cv.models.v1_21_R10.entity;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class EntityRelationship extends Entity {
   private EntityRelationship(int var1) {
      super(EntityType.INTERACTION, MinecraftServer.getServer().overworld());
      this.setId(var1);
      this.setPosRaw(0.0D, 0.0D, 0.0D);
      this.setRot(0.0F, 0.0F);
      this.setOnGround(false);
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
      this.passengers = ImmutableList.copyOf(var1);
   }

   protected void defineSynchedData(@NotNull Builder var1) {
   }

   public boolean hurtServer(ServerLevel var1, DamageSource var2, float var3) {
      return false;
   }

   protected void readAdditionalSaveData(ValueInput var1) {
   }

   protected void addAdditionalSaveData(ValueOutput var1) {
   }

   protected void readAdditionalSaveData(@NotNull CompoundTag var1) {
   }

   protected void addAdditionalSaveData(@NotNull CompoundTag var1) {
   }
}
