package advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.type;

import advancedplugins.pm2.cv.models.api.model.rpc.animation.keyframe.data.IKeyframeData;
import advancedplugins.pm2.cv.models.api.model.rpc.animation.property.IAnimationProperty;
import org.joml.Vector3f;

public class VectorKeyframe extends AbstractKeyframe<Vector3f> implements IVectorType {
   private final IKeyframeData[] preVector = new IKeyframeData[3];
   private final IKeyframeData[] postVector = new IKeyframeData[3];
   private boolean isDiscontinuous;
   private float xFactor = 1.0F;
   private float yFactor = 1.0F;
   private float zFactor = 1.0F;

   public VectorKeyframe setX(IKeyframeData var1) {
      this.preVector[0] = var1;
      return this;
   }

   public VectorKeyframe setY(IKeyframeData var1) {
      this.preVector[1] = var1;
      return this;
   }

   public VectorKeyframe setZ(IKeyframeData var1) {
      this.preVector[2] = var1;
      return this;
   }

   public VectorKeyframe setPostX(IKeyframeData var1) {
      this.postVector[0] = var1;
      return this;
   }

   public VectorKeyframe setPostY(IKeyframeData var1) {
      this.postVector[1] = var1;
      return this;
   }

   public VectorKeyframe setPostZ(IKeyframeData var1) {
      this.postVector[2] = var1;
      return this;
   }

   public IVectorType setXFactor(float var1) {
      this.xFactor = var1;
      return this;
   }

   public IVectorType setYFactor(float var1) {
      this.yFactor = var1;
      return this;
   }

   public IVectorType setZFactor(float var1) {
      this.zFactor = var1;
      return this;
   }

   public Vector3f getValue(int var1, IAnimationProperty var2) {
      return var1 != 0 && this.isDiscontinuous ? new Vector3f((float)this.postVector[0].getValue(var2) * this.xFactor, (float)this.postVector[1].getValue(var2) * this.yFactor, (float)this.postVector[2].getValue(var2) * this.zFactor) : new Vector3f((float)this.preVector[0].getValue(var2) * this.xFactor, (float)this.preVector[1].getValue(var2) * this.yFactor, (float)this.preVector[2].getValue(var2) * this.zFactor);
   }

   public IKeyframeData[] getPreVector() {
      return this.preVector;
   }

   public IKeyframeData[] getPostVector() {
      return this.postVector;
   }

   public boolean isDiscontinuous() {
      return this.isDiscontinuous;
   }

   public void setDiscontinuous(boolean var1) {
      this.isDiscontinuous = var1;
   }

   public float getXFactor() {
      return this.xFactor;
   }

   public float getYFactor() {
      return this.yFactor;
   }

   public float getZFactor() {
      return this.zFactor;
   }
}
