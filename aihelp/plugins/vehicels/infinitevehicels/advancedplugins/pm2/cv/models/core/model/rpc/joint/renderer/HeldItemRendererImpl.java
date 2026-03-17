package advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.HeldItemRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.HeldItem;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.nms.RenderParsers;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.CollectionDataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.UpdateDataTracker;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.entity.ItemDisplay.ItemDisplayTransform;
import org.bukkit.inventory.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class HeldItemRendererImpl extends AbstractBehaviorRenderer implements HeldItemRenderer {
   private final int id;
   private final UUID uuid;
   private final Map<String, HeldItemRenderer.Item> spawnQueue = new ConcurrentHashMap();
   private final Map<String, HeldItemRenderer.Item> rendered = new ConcurrentHashMap();
   private final Map<String, HeldItemRenderer.Item> destroyQueue = new ConcurrentHashMap();
   private final CollectionDataTracker<Integer> passengers = new CollectionDataTracker(new HashSet());
   private boolean initialized;

   public HeldItemRendererImpl(IVisualModel var1) {
      super(var1);
      this.id = this.nmsHandler.getEntityHandler().getNextEntityId();
      this.uuid = UUID.randomUUID();
   }

   public void initialize() {
      Iterator var1 = this.visualModel.getJoints().entrySet().iterator();

      while(var1.hasNext()) {
         Entry var2 = (Entry)var1.next();
         String var3 = (String)var2.getKey();
         IJoint var4 = (IJoint)var2.getValue();
         this.create(var3, var4);
      }

      this.initialized = true;
   }

   private void create(String var1, IJoint var2) {
      Optional var3 = var2.getJointAction(JointBehaviorTypes.ITEM);
      if (var3.isPresent()) {
         JointAction var4 = (JointAction)var3.get();
         HeldItemRendererImpl.ItemImpl var5 = new HeldItemRendererImpl.ItemImpl(this.nmsHandler.getEntityHandler().getNextEntityId(), UUID.randomUUID());
         var5.position.set(((HeldItem)var4).getLocation());
         var5.scale.set(var2.getGlobalScale());
         var5.rotation.set(((HeldItem)var4).getRotation());
         var5.model.set(var2.isVisible() ? var2.getModel() : null);
         var5.display.set(((HeldItem)var4).getDisplay());
         var5.glowing.set(var2.isGlowing());
         var5.glowColor.set(var2.getGlowColor());
         this.spawnQueue.put(var1, var5);
         this.destroyQueue.remove(var1);
         this.passengers.add(var5.id);
      }

   }

   public void readJointData() {
      if (this.initialized) {
         this.destroyQueue.putAll(this.rendered);
         Iterator var1 = this.visualModel.getJoints().entrySet().iterator();

         while(var1.hasNext()) {
            Entry var2 = (Entry)var1.next();
            String var3 = (String)var2.getKey();
            IJoint var4 = (IJoint)var2.getValue();
            HeldItemRenderer.Item var5 = (HeldItemRenderer.Item)this.getQueued(var3);
            if (var5 != null) {
               this.read(var3, var5, var4);
            } else {
               this.create(var3, var4);
            }
         }

         this.destroyQueue.forEach((var1x, var2x) -> {
            this.passengers.remove(var2x.getId());
         });
      }

   }

   private void read(String var1, HeldItemRenderer.Item var2, IJoint var3) {
      Optional var4 = var3.getJointAction(JointBehaviorTypes.ITEM);
      var4.ifPresent((var4x) -> {
         var2.getPosition().set(var4x.getLocation());
         var2.getScale().set(var3.getGlobalScale());
         var2.getRotation().set(var4x.getRotation());
         var2.getGlowing().set(var3.isGlowing());
         var2.getGlowColor().set(var3.getGlowColor());
         if (!var3.isVisible()) {
            var2.getModel().set((Object)null);
         } else {
            var2.getModel().set(var3.getModel());
            if (var3.getModelTracker().isDirty()) {
               var3.getModelTracker().clearDirty();
               var2.getModel().markDirty();
            }
         }

         var2.getDisplay().set(var4x.getDisplay());
         this.destroyQueue.remove(var1);
      });
   }

   public void sendToClient(RenderParsers var1) {
      if (this.initialized) {
         Set var2 = this.destroyQueue.keySet();
         Map var3 = this.rendered;
         Objects.requireNonNull(var3);
         Objects.requireNonNull(var3);
         var2.forEach(var3::remove);
         var1.getBehaviorParser(this).sendToClients(this);
         this.rendered.putAll(this.spawnQueue);
         this.spawnQueue.clear();
         this.destroyQueue.clear();
      }

   }

   public void destroy(RenderParsers var1) {
      if (this.initialized) {
         var1.getBehaviorParser(this).destroy(this);
      }

   }

   @Generated
   public int getId() {
      return this.id;
   }

   @Generated
   public UUID getUuid() {
      return this.uuid;
   }

   @Generated
   public Map<String, HeldItemRenderer.Item> getSpawnQueue() {
      return this.spawnQueue;
   }

   @Generated
   public Map<String, HeldItemRenderer.Item> getRendered() {
      return this.rendered;
   }

   @Generated
   public Map<String, HeldItemRenderer.Item> getDestroyQueue() {
      return this.destroyQueue;
   }

   @Generated
   public CollectionDataTracker<Integer> getPassengers() {
      return this.passengers;
   }

   @Generated
   public boolean isInitialized() {
      return this.initialized;
   }

   @Generated
   public void setInitialized(boolean var1) {
      this.initialized = var1;
   }

   public static class ItemImpl implements HeldItemRenderer.Item {
      private final int id;
      private final UUID uuid;
      private final DataTracker<Vector3f> position = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<Vector3f> scale = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<Quaternionf> rotation = new UpdateDataTracker(new Quaternionf(), Quaternionf::set);
      private final DataTracker<ItemStack> model = new DataTracker();
      private final DataTracker<ItemDisplayTransform> display;
      private final DataTracker<Boolean> glowing;
      private final DataTracker<Integer> glowColor;

      public ItemImpl(int var1, UUID var2) {
         this.id = var1;
         this.uuid = var2;
         this.display = new DataTracker(ItemDisplayTransform.NONE);
         this.glowing = new DataTracker(false);
         this.glowColor = new DataTracker(0);
      }

      @Generated
      public int getId() {
         return this.id;
      }

      @Generated
      public UUID getUuid() {
         return this.uuid;
      }

      @Generated
      public DataTracker<Vector3f> getPosition() {
         return this.position;
      }

      @Generated
      public DataTracker<Vector3f> getScale() {
         return this.scale;
      }

      @Generated
      public DataTracker<Quaternionf> getRotation() {
         return this.rotation;
      }

      @Generated
      public DataTracker<ItemStack> getModel() {
         return this.model;
      }

      @Generated
      public DataTracker<ItemDisplayTransform> getDisplay() {
         return this.display;
      }

      @Generated
      public DataTracker<Boolean> getGlowing() {
         return this.glowing;
      }

      @Generated
      public DataTracker<Integer> getGlowColor() {
         return this.glowColor;
      }

      @Generated
      public ItemImpl(int var1, UUID var2, DataTracker<ItemDisplayTransform> var3, DataTracker<Boolean> var4, DataTracker<Integer> var5) {
         this.id = var1;
         this.uuid = var2;
         this.display = var3;
         this.glowing = var4;
         this.glowColor = var5;
      }
   }
}
