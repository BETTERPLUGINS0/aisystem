package advancedplugins.pm2.cv.models.api.model.rpc.entity.data;

import advancedplugins.pm2.cv.models.api.model.rpc.IVisualModel;
import advancedplugins.pm2.cv.models.api.model.rpc.entity.CullType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.math.Box;
import advancedplugins.pm2.cv.models.api.utils.math.MathUtils;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractEntityData implements IEntityData {
   private static int CULL_INTERVAL;
   private static boolean CULL_VERTICAL;
   private static double CULL_VERTICAL_DISTANCE;
   private static CullType CULL_VERTICAL_TYPE;
   private static boolean CULL_BEHIND;
   private static double VIEW_ANGLE;
   private static double UPDATE_BEHIND_RADIUS_SQR;
   private static CullType CULL_BEHIND_TYPE;
   private static boolean CULL_BLOCKED;
   private static double FORCE_UNBLOCKED_RADIUS_SQR;
   private static CullType CULL_BLOCKED_TYPE;
   private final Set<IVisualModel> glowingModel = new HashSet();
   private final Set<IJoint> glowingJoint = new HashSet();
   private Box cullHitbox;
   private Integer cullInterval;
   private Boolean verticalCull;
   private Double verticalCullDistance;
   private CullType verticalCullType;
   private Boolean backCull;
   private Double backCullAngle;
   private Double backCullIgnoreRadius;
   private CullType backCullType;
   private Boolean blockedCull;
   private Double blockedCullIgnoreRadius;
   private CullType blockedCullType;

   public static void updateConfig() {
      CULL_INTERVAL = Math.max(ConfigProperty.CULL_INTERVAL.getInt(), 1);
      CULL_VERTICAL = ConfigProperty.VERTICAL_CULL_ENABLE.getBoolean();
      CULL_VERTICAL_DISTANCE = ConfigProperty.VERTICAL_CULL_DISTANCE.getDouble();
      CULL_VERTICAL_TYPE = ConfigProperty.VERTICAL_CULL_TYPE.getCullType();
      CULL_BEHIND = ConfigProperty.BACKWARDS_CULL_ENABLED.getBoolean();
      VIEW_ANGLE = Math.cos(MathUtils.clamp(ConfigProperty.BACKWARDS_CULL_ANGLE.getDouble(), 0.0D, 360.0D) * 0.5D * 0.01745329238474369D);
      UPDATE_BEHIND_RADIUS_SQR = ConfigProperty.BACKWARDS_CULL_IGNORE_RADIUS.getDouble();
      UPDATE_BEHIND_RADIUS_SQR *= UPDATE_BEHIND_RADIUS_SQR;
      CULL_BEHIND_TYPE = ConfigProperty.BACKWARDS_CULL_TYPE.getCullType();
      CULL_BLOCKED = ConfigProperty.BLOCK_CULL_ENABLE.getBoolean();
      FORCE_UNBLOCKED_RADIUS_SQR = ConfigProperty.BLOCK_CULL_IGNORE_RADIUS.getDouble();
      FORCE_UNBLOCKED_RADIUS_SQR *= FORCE_UNBLOCKED_RADIUS_SQR;
      CULL_BLOCKED_TYPE = ConfigProperty.BLOCK_CULL_TYPE.getCullType();
   }

   public int cullInterval() {
      return this.cullInterval == null ? CULL_INTERVAL : this.cullInterval;
   }

   public boolean verticalCull() {
      return this.verticalCull == null ? CULL_VERTICAL : this.verticalCull;
   }

   public double verticalCullDistance() {
      return this.verticalCullDistance == null ? CULL_VERTICAL_DISTANCE : this.verticalCullDistance;
   }

   public CullType verticalCullType() {
      return this.verticalCullType == null ? CULL_VERTICAL_TYPE : this.verticalCullType;
   }

   public boolean backCull() {
      return this.backCull == null ? CULL_BEHIND : this.backCull;
   }

   public double backCullAngle() {
      return this.backCullAngle == null ? VIEW_ANGLE : this.backCullAngle;
   }

   public double backCullIgnoreRadius() {
      return this.backCullIgnoreRadius == null ? UPDATE_BEHIND_RADIUS_SQR : this.backCullIgnoreRadius * this.backCullIgnoreRadius;
   }

   public CullType backCullType() {
      return this.backCullType == null ? CULL_BEHIND_TYPE : this.backCullType;
   }

   public boolean blockedCull() {
      return this.blockedCull == null ? CULL_BLOCKED : this.blockedCull;
   }

   public double blockedCullIgnoreRadius() {
      return this.blockedCullIgnoreRadius == null ? FORCE_UNBLOCKED_RADIUS_SQR : this.blockedCullIgnoreRadius * this.blockedCullIgnoreRadius;
   }

   public CullType blockedCullType() {
      return this.blockedCullType == null ? CULL_BLOCKED_TYPE : this.blockedCullType;
   }

   public void markModelGlowing(IVisualModel var1, boolean var2) {
      if (var2) {
         this.glowingModel.add(var1);
      } else {
         this.glowingModel.remove(var1);
      }

   }

   public void markJointGlowing(IJoint var1, boolean var2) {
      if (var2) {
         this.glowingJoint.add(var1);
      } else {
         this.glowingJoint.remove(var1);
      }

   }

   public boolean isModelGlowing() {
      return !this.glowingJoint.isEmpty() || !this.glowingModel.isEmpty();
   }

   public Box getCullHitbox() {
      return this.cullHitbox;
   }

   public void setCullHitbox(Box var1) {
      this.cullHitbox = var1;
   }

   public Integer getCullInterval() {
      return this.cullInterval;
   }

   public void setCullInterval(Integer var1) {
      this.cullInterval = var1;
   }

   public Boolean getVerticalCull() {
      return this.verticalCull;
   }

   public void setVerticalCull(Boolean var1) {
      this.verticalCull = var1;
   }

   public Double getVerticalCullDistance() {
      return this.verticalCullDistance;
   }

   public void setVerticalCullDistance(Double var1) {
      this.verticalCullDistance = var1;
   }

   public CullType getVerticalCullType() {
      return this.verticalCullType;
   }

   public void setVerticalCullType(CullType var1) {
      this.verticalCullType = var1;
   }

   public Boolean getBackCull() {
      return this.backCull;
   }

   public void setBackCull(Boolean var1) {
      this.backCull = var1;
   }

   public Double getBackCullAngle() {
      return this.backCullAngle;
   }

   public void setBackCullAngle(Double var1) {
      this.backCullAngle = var1;
   }

   public Double getBackCullIgnoreRadius() {
      return this.backCullIgnoreRadius;
   }

   public void setBackCullIgnoreRadius(Double var1) {
      this.backCullIgnoreRadius = var1;
   }

   public CullType getBackCullType() {
      return this.backCullType;
   }

   public void setBackCullType(CullType var1) {
      this.backCullType = var1;
   }

   public Boolean getBlockedCull() {
      return this.blockedCull;
   }

   public void setBlockedCull(Boolean var1) {
      this.blockedCull = var1;
   }

   public Double getBlockedCullIgnoreRadius() {
      return this.blockedCullIgnoreRadius;
   }

   public void setBlockedCullIgnoreRadius(Double var1) {
      this.blockedCullIgnoreRadius = var1;
   }

   public CullType getBlockedCullType() {
      return this.blockedCullType;
   }

   public void setBlockedCullType(CullType var1) {
      this.blockedCullType = var1;
   }
}
