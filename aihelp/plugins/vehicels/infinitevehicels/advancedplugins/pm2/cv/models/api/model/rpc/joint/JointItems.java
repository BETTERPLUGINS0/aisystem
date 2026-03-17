package advancedplugins.pm2.cv.models.api.model.rpc.joint;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ItemModelData;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint.BlueprintJoint;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Generated;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class JointItems {
   private final Int2ObjectOpenHashMap<ItemStack> stacks = new Int2ObjectOpenHashMap();
   private final Object2IntOpenHashMap<ItemStack> toHash = new Object2IntOpenHashMap();
   private final DataTracker<JointItems> tracker = new DataTracker(this);

   public ItemStack getFirst() {
      this.prepStacks();
      return (ItemStack)((Entry)this.stacks.int2ObjectEntrySet().iterator().next()).getValue();
   }

   public Map<Integer, ItemStack> getItems() {
      return new Int2ObjectOpenHashMap(this.stacks);
   }

   public void forEach(BiConsumer<Integer, ItemStack> var1) {
      this.stacks.forEach(var1);
   }

   public void forEach(Consumer<ItemStack> var1) {
      this.forEach(var1, false);
   }

   public void forEach(Consumer<ItemStack> var1, boolean var2) {
      this.forEach(var1, () -> {
         return var2;
      });
   }

   public void forEach(Consumer<ItemStack> var1, Supplier<Boolean> var2) {
      this.stacks.forEach((var1x, var2x) -> {
         var1.accept(var2x);
      });
      if ((Boolean)var2.get()) {
         this.toHash.clear();
         this.stacks.forEach((var1x, var2x) -> {
            this.toHash.put(var2x, var2x.hashCode());
         });
         this.stacks.clear();
         this.toHash.forEach((var1x, var2x) -> {
            this.stacks.put(var1x.hashCode(), var1x);
         });
         this.markDirty();
      }

   }

   public void update(BlueprintJoint var1, Color var2) {
      HashSet var3 = new HashSet(var1.getModelData().createItemStack(ItemModelData.context().color(var2).build()));
      if (!var3.equals(this.stacks.keySet())) {
         this.stacks.clear();
         this.toHash.clear();
         var3.forEach(this::add);
      }

   }

   public void clear() {
      if (!this.stacks.isEmpty()) {
         this.stacks.clear();
         this.toHash.clear();
         this.markDirty();
      }

   }

   public void add(ItemStack var1) {
      if (!this.toHash.containsKey(var1)) {
         this.stacks.put(var1.hashCode(), var1);
         this.toHash.put(var1, var1.hashCode());
         this.markDirty();
      }

   }

   public void remove(ItemStack var1) {
      if (this.toHash.containsKey(var1)) {
         this.stacks.remove(this.toHash.removeInt(var1));
         this.markDirty();
      }

   }

   public void markDirty() {
      this.tracker.markDirty();
   }

   public void clearDirty() {
      this.tracker.clearDirty();
   }

   public boolean isDirty() {
      return this.tracker.isDirty();
   }

   public boolean isEqual(Set<ItemStack> var1) {
      return var1.equals(this.toHash.keySet());
   }

   private void prepStacks() {
      if (this.stacks.isEmpty()) {
         this.add(new ItemStack(Material.AIR));
      }

   }

   @Generated
   public Int2ObjectOpenHashMap<ItemStack> getStacks() {
      return this.stacks;
   }

   @Generated
   public Object2IntOpenHashMap<ItemStack> getToHash() {
      return this.toHash;
   }

   @Generated
   public DataTracker<JointItems> getTracker() {
      return this.tracker;
   }
}
