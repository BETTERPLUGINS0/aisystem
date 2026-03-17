package implementation.v1_20_R1.fake;

import advancedplugins.pm2.cv.enums.EnumDisplayProperty;
import com.mojang.math.Transformation;
import net.minecraft.world.entity.Display;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftDisplay;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public abstract class FakeDisplayHandle<D extends Display> extends FakeEntityHandle<D, EnumDisplayProperty> implements advancedplugins.pm2.cv.fake.display.FakeDisplayHandle {
   public FakeDisplayHandle(@NotNull World var1) {
      super(var1);
   }

   public void applyProperty(@NotNull EnumDisplayProperty var1, @NotNull Object var2) {
      switch(var1) {
      case VISIBILITY:
         ((Display)this.handle).j(!(Boolean)var2);
         break;
      case GLOWING:
         ((Display)this.handle).i((Boolean)var2);
         break;
      case GLOWING_COLOR:
         ((CraftDisplay)((Display)this.handle).getBukkitEntity()).setGlowColorOverride((Color)var2);
         break;
      case TRANSFORM_INTERPOLATION_DELAY:
         ((Display)this.handle).c((Integer)var2);
         break;
      case TRANSFORM_INTERPOLATION_DURATION:
         ((Display)this.handle).b((Integer)var2);
         break;
      case VIEW_RANGE:
         ((Display)this.handle).s((Float)var2);
         break;
      case LEFT_ROTATION:
         ((Display)this.handle).a(new Transformation((Vector3f)null, (Quaternionf)var2, (Vector3f)null, (Quaternionf)null));
         break;
      case TRANSFORMATION:
         org.bukkit.util.Transformation var3 = (org.bukkit.util.Transformation)var2;
         ((Display)this.handle).a(new Transformation(var3.getTranslation(), var3.getLeftRotation(), var3.getScale(), var3.getRightRotation()));
      }

   }

   public void applyTransformation(@Nullable Matrix4f var1) {
      ((Display)this.handle).a(new Transformation(var1));
   }
}
