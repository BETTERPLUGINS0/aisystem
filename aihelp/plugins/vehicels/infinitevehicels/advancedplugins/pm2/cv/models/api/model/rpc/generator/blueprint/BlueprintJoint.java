package advancedplugins.pm2.cv.models.api.model.rpc.generator.blueprint;

import advancedplugins.pm2.cv.models.api.model.rpc.generator.BaseItemEnum;
import advancedplugins.pm2.cv.models.api.model.rpc.generator.assets.ItemModelData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.core.model.rpc.generator.processed.ProcessedJoint;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Generated;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class BlueprintJoint {
   private final Map<String, BlueprintJoint> children = new LinkedHashMap();
   private final Map<String, Map<String, Object>> behaviors = new LinkedHashMap();
   private final transient Map<JointActionType<?>, JointActionType.CachedProvider<?>> cachedBehaviorProvider = new LinkedHashMap();
   private final ItemModelData modelData = new ItemModelData();
   private ProcessedJoint processedJoint;
   private String name;
   private UUID uuid;
   private boolean isRenderer;
   private int scale = 1;
   /** @deprecated */
   @Deprecated
   private BaseItemEnum baseItem;
   /** @deprecated */
   @Deprecated
   private int dataId;
   private Vector3f localPosition;
   private Vector3f localRotation;
   private Quaternionf localQuaternion = new Quaternionf();
   private Vector3f globalPosition;
   private Vector3f rotatedGlobalPosition;
   private Vector3f globalRotation;
   private Quaternionf globalQuaternion = new Quaternionf();
   private BlueprintJoint parent;
   private Vector3f modelScale = new Vector3f(1.0F);
   private BlueprintJoint dupeTarget;
   private boolean renderByDefault = true;

   /** @deprecated */
   @Deprecated
   public BaseItemEnum getBaseItem() {
      return this.baseItem;
   }

   /** @deprecated */
   @Deprecated
   public void setBaseItem(BaseItemEnum var1) {
      this.baseItem = var1;
   }

   /** @deprecated */
   @Deprecated
   public int getDataId() {
      return this.dataId;
   }

   /** @deprecated */
   @Deprecated
   public void setDataId(int var1) {
      this.dataId = var1;
   }

   @Generated
   public void setProcessedJoint(ProcessedJoint var1) {
      this.processedJoint = var1;
   }

   @Generated
   public void setName(String var1) {
      this.name = var1;
   }

   @Generated
   public void setUuid(UUID var1) {
      this.uuid = var1;
   }

   @Generated
   public void setRenderer(boolean var1) {
      this.isRenderer = var1;
   }

   @Generated
   public void setScale(int var1) {
      this.scale = var1;
   }

   @Generated
   public void setLocalPosition(Vector3f var1) {
      this.localPosition = var1;
   }

   @Generated
   public void setLocalRotation(Vector3f var1) {
      this.localRotation = var1;
   }

   @Generated
   public void setLocalQuaternion(Quaternionf var1) {
      this.localQuaternion = var1;
   }

   @Generated
   public void setGlobalPosition(Vector3f var1) {
      this.globalPosition = var1;
   }

   @Generated
   public void setRotatedGlobalPosition(Vector3f var1) {
      this.rotatedGlobalPosition = var1;
   }

   @Generated
   public void setGlobalRotation(Vector3f var1) {
      this.globalRotation = var1;
   }

   @Generated
   public void setGlobalQuaternion(Quaternionf var1) {
      this.globalQuaternion = var1;
   }

   @Generated
   public void setParent(BlueprintJoint var1) {
      this.parent = var1;
   }

   @Generated
   public void setModelScale(Vector3f var1) {
      this.modelScale = var1;
   }

   @Generated
   public void setDupeTarget(BlueprintJoint var1) {
      this.dupeTarget = var1;
   }

   @Generated
   public void setRenderByDefault(boolean var1) {
      this.renderByDefault = var1;
   }

   @Generated
   public Map<String, BlueprintJoint> getChildren() {
      return this.children;
   }

   @Generated
   public Map<String, Map<String, Object>> getBehaviors() {
      return this.behaviors;
   }

   @Generated
   public Map<JointActionType<?>, JointActionType.CachedProvider<?>> getCachedBehaviorProvider() {
      return this.cachedBehaviorProvider;
   }

   @Generated
   public ItemModelData getModelData() {
      return this.modelData;
   }

   @Generated
   public ProcessedJoint getProcessedJoint() {
      return this.processedJoint;
   }

   @Generated
   public String getName() {
      return this.name;
   }

   @Generated
   public UUID getUuid() {
      return this.uuid;
   }

   @Generated
   public boolean isRenderer() {
      return this.isRenderer;
   }

   @Generated
   public int getScale() {
      return this.scale;
   }

   @Generated
   public Vector3f getLocalPosition() {
      return this.localPosition;
   }

   @Generated
   public Vector3f getLocalRotation() {
      return this.localRotation;
   }

   @Generated
   public Quaternionf getLocalQuaternion() {
      return this.localQuaternion;
   }

   @Generated
   public Vector3f getGlobalPosition() {
      return this.globalPosition;
   }

   @Generated
   public Vector3f getRotatedGlobalPosition() {
      return this.rotatedGlobalPosition;
   }

   @Generated
   public Vector3f getGlobalRotation() {
      return this.globalRotation;
   }

   @Generated
   public Quaternionf getGlobalQuaternion() {
      return this.globalQuaternion;
   }

   @Generated
   public BlueprintJoint getParent() {
      return this.parent;
   }

   @Generated
   public Vector3f getModelScale() {
      return this.modelScale;
   }

   @Generated
   public BlueprintJoint getDupeTarget() {
      return this.dupeTarget;
   }

   @Generated
   public boolean isRenderByDefault() {
      return this.renderByDefault;
   }
}
