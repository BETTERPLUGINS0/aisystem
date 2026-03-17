package advancedplugins.pm2.cv.enums;

import advancedplugins.pm2.cv.util.ConvertUtil;
import advancedplugins.pm2.cv.util.math.Matrix4;
import advancedplugins.pm2.cv.util.math.TransformUtil;
import me.PM2.infinitevehicles.math.geometry.euclidean.threed.Vector3D;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public enum EnumAxis {
   X {
      public Vector3D getDirectionVector() {
         return new Vector3D(1.0D, 0.0D, 0.0D);
      }

      public Color getColor() {
         return Color.fromRGB(255, 0, 0);
      }

      public ChatColor getChatColor() {
         return ChatColor.DARK_RED;
      }
   },
   Y {
      public Vector3D getDirectionVector() {
         return new Vector3D(0.0D, 1.0D, 0.0D);
      }

      public Color getColor() {
         return Color.fromRGB(0, 255, 0);
      }

      public ChatColor getChatColor() {
         return ChatColor.GREEN;
      }
   },
   Z {
      public Vector3D getDirectionVector() {
         return new Vector3D(0.0D, 0.0D, 1.0D);
      }

      public Color getColor() {
         return Color.fromRGB(0, 0, 255);
      }

      public ChatColor getChatColor() {
         return ChatColor.DARK_BLUE;
      }
   };

   public abstract Color getColor();

   public abstract ChatColor getChatColor();

   public abstract Vector3D getDirectionVector();

   public Vector3D getDirectionVector(@NotNull Matrix4f transformation) {
      Vector3f var2 = ConvertUtil.toVector3f(this.getDirectionVector());
      return ConvertUtil.toVector3D(var2.mulProject(var1));
   }

   public Vector3D getDirectionVector(@NotNull Matrix4 transformation) {
      return TransformUtil.transform(this.getDirectionVector(), var1);
   }

   // $FF: synthetic method
   private static EnumAxis[] $values() {
      return new EnumAxis[]{X, Y, Z};
   }
}
