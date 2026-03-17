package advancedplugins.pm2.cv.models.core.model.rpc.visual;

import advancedplugins.pm2.cv.models.api.model.rpc.entity.BaseEntity;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.BaseItemEnum;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.Visual;
import advancedplugins.pm2.cv.models.api.model.rpc.visual.renderer.VisualRenderer;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.core.model.rpc.Position;
import advancedplugins.pm2.cv.models.core.model.rpc.visual.renderer.VisualDisplayRendererImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.Generated;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class VisualImpl extends Visual {
   private final BaseEntity<?> base;
   private final VisualRenderer renderer;
   private final Vector3f modelScale = new Vector3f();
   private final boolean initialized;
   private final List<Runnable> queuedTask = new ArrayList();
   private final DataTracker<ItemStack> modelTracker;
   private final Position position;
   private boolean isBaseEntityVisible;
   private boolean destroyed;
   private boolean removed;
   private Color color;
   private boolean enchanted;
   private boolean visible;

   public VisualImpl(@NotNull BaseEntity<?> var1, @Nullable Function<Visual, VisualRenderer> var2, @Nullable Consumer<Visual> var3) {
      this.modelTracker = new DataTracker(new ItemStack(Material.AIR));
      this.isBaseEntityVisible = true;
      this.position = new Position(new Vector3f(1.0F));
      this.visible = true;
      this.base = var1;
      this.registerSelf();
      this.getPosition().setOrigin(var1.getLocation().toVector().toVector3f());
      Object var4 = var2 == null ? new VisualDisplayRendererImpl(this) : (VisualRenderer)var2.apply(this);
      this.renderer = (VisualRenderer)(var4 == null ? new VisualDisplayRendererImpl(this) : var4);
      if (var3 != null) {
         var3.accept(this);
      }

      this.renderer.init();
      synchronized(this.queuedTask) {
         this.queuedTask.forEach(Runnable::run);
         this.initialized = true;
      }
   }

   public boolean tick() {
      if (!this.initialized) {
         return true;
      } else {
         this.getPosition().setOrigin(this.base.getLocation().toVector().toVector3f());
         this.getPosition().setYaw(this.base.getYHeadRot());
         this.getPosition().setPitch(this.base.getXHeadRot());
         this.renderer.readData();
         return !this.removed && !this.base.isRemoved();
      }
   }

   public void dispose() {
      this.destroyed = true;
   }

   public void setRemoved() {
      this.removed = true;
   }

   public ItemStack getModel() {
      return (ItemStack)this.modelTracker.get();
   }

   public void setModel(ItemStack var1) {
      this.modelTracker.set(var1);
   }

   public BaseEntity<?> getOriginal() {
      return this.base;
   }

   public void setModelScale(int var1) {
      this.modelScale.set((float)var1);
   }

   public boolean isReady() {
      return this.initialized;
   }

   public boolean isOriginalVisible() {
      return this.isBaseEntityVisible;
   }

   public void setOriginalVisible(boolean var1) {
      if (this.isOriginalVisible() != var1) {
         this.isBaseEntityVisible = var1;
         this.base.setVisible(var1);
      }

   }

   public boolean isDisposed() {
      return this.destroyed;
   }

   public void setColor(Color var1) {
      this.color = var1;
      ItemStack var2 = (ItemStack)this.modelTracker.get();
      BaseItemEnum var3 = BaseItemEnum.fromMaterial(var2.getType());
      if (var3 != null) {
         ItemMeta var4 = var2.getItemMeta();
         var3.color(var4, var1);
         var2.setItemMeta(var4);
         this.modelTracker.markDirty();
      }

   }

   public void setEnchanted(boolean var1) {
      if (this.isEnchanted() != var1) {
         this.enchanted = var1;
         ItemStack var2 = (ItemStack)this.modelTracker.get();
         if (var1) {
            var2.addUnsafeEnchantment(Enchantment.VANISHING_CURSE, 1);
         } else {
            var2.removeEnchantment(Enchantment.VANISHING_CURSE);
         }

         this.modelTracker.markDirty();
      }

   }

   @Generated
   public BaseEntity<?> getBase() {
      return this.base;
   }

   @Generated
   public VisualRenderer getRenderer() {
      return this.renderer;
   }

   @Generated
   public Vector3f getModelScale() {
      return this.modelScale;
   }

   @Generated
   public boolean isInitialized() {
      return this.initialized;
   }

   @Generated
   public List<Runnable> getQueuedTask() {
      return this.queuedTask;
   }

   @Generated
   public DataTracker<ItemStack> getModelTracker() {
      return this.modelTracker;
   }

   @Generated
   public Position getPosition() {
      return this.position;
   }

   @Generated
   public boolean isBaseEntityVisible() {
      return this.isBaseEntityVisible;
   }

   @Generated
   public boolean isDestroyed() {
      return this.destroyed;
   }

   @Generated
   public boolean isRemoved() {
      return this.removed;
   }

   @Generated
   public Color getColor() {
      return this.color;
   }

   @Generated
   public boolean isEnchanted() {
      return this.enchanted;
   }

   @Generated
   public boolean isVisible() {
      return this.visible;
   }

   @Generated
   public void setBaseEntityVisible(boolean var1) {
      this.isBaseEntityVisible = var1;
   }

   @Generated
   public void setDestroyed(boolean var1) {
      this.destroyed = var1;
   }

   @Generated
   public void setRemoved(boolean var1) {
      this.removed = var1;
   }

   @Generated
   public void setVisible(boolean var1) {
      this.visible = var1;
   }
}
