package advancedplugins.pm2.cv.models.core.model.rpc.joint.manager.behavior;

import advancedplugins.pm2.cv.models.api.model.rpc.joint.IJoint;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.AbstractJointAction;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointActionType;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.behavior.JointBehaviorData;
import advancedplugins.pm2.cv.models.api.model.rpc.joint.type.NameTag;
import java.util.function.Supplier;
import lombok.Generated;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public class NameTagImpl extends AbstractJointAction<NameTagImpl> implements NameTag {
   private final Vector3f location = new Vector3f();
   private String jsonString;
   private Supplier<String> jsonStringSupplier;
   private boolean visible;
   private int backgroundColor = 1073741824;
   private boolean useDefaultBackgroundColor = true;
   private TextAlignment alignment;
   private int lineWidth;
   private byte textOpacity;
   private boolean shadow;
   private boolean seeThrough;
   private Billboard billboard;
   private Vector3f scale;

   public NameTagImpl(IJoint var1, JointActionType<NameTagImpl> var2, JointBehaviorData var3) {
      super(var1, var2, var3);
      this.alignment = TextAlignment.CENTER;
      this.textOpacity = -1;
      this.shadow = false;
      this.seeThrough = true;
      this.billboard = Billboard.CENTER;
      this.scale = new Vector3f(1.0F);
   }

   public void onApply() {
      Location var1 = this.joint.calculatePivotLocation();
      this.joint.getBlueprintJoint().getLocalPosition().rotateY((180.0F - this.joint.getYaw()) * 0.017453292F, this.location).add((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
   }

   public void onFinalize() {
      if (this.jsonStringSupplier != null) {
         this.setJsonString((String)this.jsonStringSupplier.get());
      }

      Location var1 = this.joint.calculatePivotLocation();
      this.joint.getGlobalPosition().rotateY((180.0F - this.joint.getYaw()) * 0.017453292F, this.location).add((float)var1.getX(), (float)var1.getY(), (float)var1.getZ());
   }

   public void setString(String var1) {
      this.setComponent(Component.text(var1));
   }

   public void setComponent(Component var1) {
      this.setJsonString((String)GsonComponentSerializer.gson().serialize(var1));
   }

   public void setComponentSupplier(@Nullable Supplier<Component> var1) {
      this.jsonStringSupplier = var1 == null ? null : () -> {
         return (String)GsonComponentSerializer.gson().serialize((Component)var1.get());
      };
   }

   public void setUseDefaultBackgroundColor(boolean var1) {
      this.useDefaultBackgroundColor = var1;
      if (var1) {
         this.backgroundColor = 1073741824;
      }

   }

   @Generated
   public Vector3f getLocation() {
      return this.location;
   }

   @Generated
   public String getJsonString() {
      return this.jsonString;
   }

   @Generated
   public Supplier<String> getJsonStringSupplier() {
      return this.jsonStringSupplier;
   }

   @Generated
   public boolean isVisible() {
      return this.visible;
   }

   @Generated
   public int getBackgroundColor() {
      return this.backgroundColor;
   }

   @Generated
   public boolean isUseDefaultBackgroundColor() {
      return this.useDefaultBackgroundColor;
   }

   @Generated
   public TextAlignment getAlignment() {
      return this.alignment;
   }

   @Generated
   public int getLineWidth() {
      return this.lineWidth;
   }

   @Generated
   public byte getTextOpacity() {
      return this.textOpacity;
   }

   @Generated
   public boolean isShadow() {
      return this.shadow;
   }

   @Generated
   public boolean isSeeThrough() {
      return this.seeThrough;
   }

   @Generated
   public Billboard getBillboard() {
      return this.billboard;
   }

   @Generated
   public Vector3f getScale() {
      return this.scale;
   }

   @Generated
   public void setJsonString(String var1) {
      this.jsonString = var1;
   }

   @Generated
   public void setJsonStringSupplier(Supplier<String> var1) {
      this.jsonStringSupplier = var1;
   }

   @Generated
   public void setVisible(boolean var1) {
      this.visible = var1;
   }

   @Generated
   public void setBackgroundColor(int var1) {
      this.backgroundColor = var1;
   }

   @Generated
   public void setAlignment(TextAlignment var1) {
      this.alignment = var1;
   }

   @Generated
   public void setLineWidth(int var1) {
      this.lineWidth = var1;
   }

   @Generated
   public void setTextOpacity(byte var1) {
      this.textOpacity = var1;
   }

   @Generated
   public void setShadow(boolean var1) {
      this.shadow = var1;
   }

   @Generated
   public void setSeeThrough(boolean var1) {
      this.seeThrough = var1;
   }

   @Generated
   public void setBillboard(Billboard var1) {
      this.billboard = var1;
   }

   @Generated
   public void setScale(Vector3f var1) {
      this.scale = var1;
   }
}
