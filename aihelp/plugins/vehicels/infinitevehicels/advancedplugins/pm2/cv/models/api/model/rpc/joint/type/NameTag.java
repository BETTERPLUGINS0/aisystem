package advancedplugins.pm2.cv.models.api.model.rpc.joint.type;

import java.util.function.Supplier;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Display.Billboard;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

public interface NameTag {
   Vector3f getLocation();

   void setString(String var1);

   void setComponent(Component var1);

   String getJsonString();

   void setJsonString(String var1);

   void setComponentSupplier(@Nullable Supplier<Component> var1);

   @Nullable
   Supplier<String> getJsonStringSupplier();

   void setJsonStringSupplier(@Nullable Supplier<String> var1);

   boolean isVisible();

   void setVisible(boolean var1);

   int getLineWidth();

   void setLineWidth(int var1);

   int getBackgroundColor();

   void setBackgroundColor(int var1);

   boolean isUseDefaultBackgroundColor();

   void setUseDefaultBackgroundColor(boolean var1);

   TextAlignment getAlignment();

   void setAlignment(TextAlignment var1);

   byte getTextOpacity();

   void setTextOpacity(byte var1);

   boolean isShadow();

   void setShadow(boolean var1);

   boolean isSeeThrough();

   void setSeeThrough(boolean var1);

   Billboard getBillboard();

   void setBillboard(Billboard var1);

   Vector3f getScale();

   void setScale(Vector3f var1);
}
