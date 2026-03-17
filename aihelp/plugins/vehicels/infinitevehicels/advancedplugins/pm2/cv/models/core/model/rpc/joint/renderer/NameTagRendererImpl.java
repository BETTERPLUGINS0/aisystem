package advancedplugins.pm2.cv.models.core.model.rpc.joint.renderer;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.renderer.renderer.NameTagRenderer;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.JointBehaviorTypes;
import advancedplugins.pm2.cv.models.api.nms.RenderParsers;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.DataTracker;
import advancedplugins.pm2.cv.models.api.utils.data.tracker.UpdateDataTracker;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.entity.Display.Billboard;
import org.joml.Vector3f;

public class NameTagRendererImpl extends AbstractBehaviorRenderer implements NameTagRenderer {
   private final Map<String, NameTagRenderer.NameTag> spawnQueue = new ConcurrentHashMap();
   private final Map<String, NameTagRenderer.NameTag> rendered = new ConcurrentHashMap();
   private final Map<String, NameTagRenderer.NameTag> destroyQueue = new ConcurrentHashMap();
   private boolean initialized;

   public NameTagRendererImpl(IVisualModel var1) {
      super(var1);
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
      Optional var3 = var2.getJointAction(JointBehaviorTypes.NAMETAG);
      if (var3.isPresent()) {
         JointAction var4 = (JointAction)var3.get();
         NameTagRendererImpl.NameTagImpl var5 = new NameTagRendererImpl.NameTagImpl(this.nmsHandler.getEntityHandler().getNextEntityId(), UUID.randomUUID(), this.nmsHandler.getEntityHandler().getNextEntityId(), UUID.randomUUID());
         var5.position.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag)var4).getLocation());
         var5.jsonString.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag)var4).getJsonString());
         var5.visibility.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag)var4).isVisible());
         var5.backgroundColor.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag)var4).isUseDefaultBackgroundColor() ? 1073741824 : ((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag)var4).getBackgroundColor());
         var5.billboard.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag)var4).getBillboard());
         var5.textOpacity.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag)var4).getTextOpacity());
         var5.lineWidth.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag)var4).getLineWidth());
         var5.scale.set(((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag)var4).getScale());
         var5.getStyle().set(this.getStyle((advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag)var4));
         this.spawnQueue.put(var1, var5);
         this.destroyQueue.remove(var1);
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
            NameTagRenderer.NameTag var5 = (NameTagRenderer.NameTag)this.getQueued(var3);
            if (var5 != null) {
               this.read(var3, var5, var4);
            } else {
               this.create(var3, var4);
            }
         }
      }

   }

   private void read(String var1, NameTagRenderer.NameTag var2, IJoint var3) {
      Optional var4 = var3.getJointAction(JointBehaviorTypes.NAMETAG);
      var4.ifPresent((var3x) -> {
         var2.getPosition().set(var3x.getLocation());
         var2.getJsonString().set(var3x.getJsonString());
         var2.getVisibility().set(var3x.isVisible());
         var2.getBillboard().set(var3x.getBillboard());
         var2.getBackgroundColor().set(var3x.isUseDefaultBackgroundColor() ? 1073741824 : var3x.getBackgroundColor());
         var2.getTextOpacity().set(var3x.getTextOpacity());
         var2.getLineWidth().set(var3x.getLineWidth());
         var2.getScale().set(var3x.getScale());
         var2.getStyle().set(this.getStyle(var3x));
         this.destroyQueue.remove(var1);
      });
   }

   private byte getStyle(advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag var1) {
      byte var2 = MathUtils.setBit((byte)0, 0, var1.isShadow());
      var2 = MathUtils.setBit(var2, 1, var1.isSeeThrough());
      var2 = MathUtils.setBit(var2, 2, var1.isUseDefaultBackgroundColor());
      byte var10000;
      switch(var1.getAlignment()) {
      case CENTER:
         var10000 = var2;
         break;
      case LEFT:
         var10000 = (byte)(var2 | 8);
         break;
      case RIGHT:
         var10000 = (byte)(var2 | 16);
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      var2 = var10000;
      return var2;
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
   public Map<String, NameTagRenderer.NameTag> getSpawnQueue() {
      return this.spawnQueue;
   }

   @Generated
   public Map<String, NameTagRenderer.NameTag> getRendered() {
      return this.rendered;
   }

   @Generated
   public Map<String, NameTagRenderer.NameTag> getDestroyQueue() {
      return this.destroyQueue;
   }

   @Generated
   public boolean isInitialized() {
      return this.initialized;
   }

   @Generated
   public void setInitialized(boolean var1) {
      this.initialized = var1;
   }

   public static class NameTagImpl implements NameTagRenderer.NameTag {
      private final int pivotId;
      private final UUID pivotUuid;
      private final int tagId;
      private final UUID tagUuid;
      private final DataTracker<Vector3f> position = new UpdateDataTracker(new Vector3f(), Vector3f::set);
      private final DataTracker<String> jsonString = new DataTracker();
      private final DataTracker<Boolean> visibility = new DataTracker(true);
      private final DataTracker<Integer> backgroundColor = new DataTracker(1073741824);
      private final DataTracker<Billboard> billboard;
      private final DataTracker<Byte> textOpacity;
      private final DataTracker<Integer> lineWidth;
      private final DataTracker<Vector3f> scale;
      private final DataTracker<Byte> style;

      public NameTagImpl(int var1, UUID var2, int var3, UUID var4) {
         this.billboard = new DataTracker(Billboard.CENTER);
         this.textOpacity = new DataTracker((byte)0);
         this.lineWidth = new DataTracker(0);
         this.scale = new DataTracker(new Vector3f(1.0F));
         this.style = new DataTracker((byte)0);
         this.pivotId = var1;
         this.pivotUuid = var2;
         this.tagId = var3;
         this.tagUuid = var4;
      }

      @Generated
      public int getPivotId() {
         return this.pivotId;
      }

      @Generated
      public UUID getPivotUuid() {
         return this.pivotUuid;
      }

      @Generated
      public int getTagId() {
         return this.tagId;
      }

      @Generated
      public UUID getTagUuid() {
         return this.tagUuid;
      }

      @Generated
      public DataTracker<Vector3f> getPosition() {
         return this.position;
      }

      @Generated
      public DataTracker<String> getJsonString() {
         return this.jsonString;
      }

      @Generated
      public DataTracker<Boolean> getVisibility() {
         return this.visibility;
      }

      @Generated
      public DataTracker<Integer> getBackgroundColor() {
         return this.backgroundColor;
      }

      @Generated
      public DataTracker<Billboard> getBillboard() {
         return this.billboard;
      }

      @Generated
      public DataTracker<Byte> getTextOpacity() {
         return this.textOpacity;
      }

      @Generated
      public DataTracker<Integer> getLineWidth() {
         return this.lineWidth;
      }

      @Generated
      public DataTracker<Vector3f> getScale() {
         return this.scale;
      }

      @Generated
      public DataTracker<Byte> getStyle() {
         return this.style;
      }
   }
}
