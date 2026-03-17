package implementation.v1_19_R3.fake;

import advancedplugins.pm2.cv.enums.EnumDisplayProperty;
import advancedplugins.pm2.cv.enums.EnumDisplayTextAlignment;
import advancedplugins.pm2.cv.fake.FakeEntityLinker;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Display.TextDisplay;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftTextDisplay;
import org.bukkit.craftbukkit.v1_19_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FakeDisplayTextHandle extends FakeDisplayHandle<TextDisplay> implements advancedplugins.pm2.cv.fake.display.FakeDisplayTextHandle {
   public FakeDisplayTextHandle(@NotNull World var1) {
      super(var1);
   }

   protected TextDisplay createHandleInstance(@NotNull World var1) {
      return (TextDisplay)EntityTypes.aX.a(((CraftWorld)var1).getHandle());
   }

   public void applyProperty(@NotNull EnumDisplayProperty var1, @NotNull Object var2) {
      super.applyProperty(var1, var2);
      CraftTextDisplay var3 = (CraftTextDisplay)((TextDisplay)this.handle).getBukkitEntity();
      switch(var1) {
      case TEXT:
         ((TextDisplay)this.handle).c(CraftChatMessage.fromString((String)var2, true)[0]);
         break;
      case TEXT_LINE_WIDTH:
         ((TextDisplay)this.handle).b((Integer)var2);
         break;
      case TEXT_OPACITY:
         ((TextDisplay)this.handle).c((Byte)var2);
         break;
      case TEXT_SHADOWED:
         var3.setShadowed((Boolean)var2);
         break;
      case TEXT_CAN_SEE_THROUGH:
         var3.setSeeThrough((Boolean)var2);
         break;
      case TEXT_DEFAULT_BACKGROUND:
         var3.setDefaultBackground((Boolean)var2);
         break;
      case TEXT_ALIGNMENT:
         var3.setAlignment(this.toTextAlignment((EnumDisplayTextAlignment)var2));
      }

   }

   private TextAlignment toTextAlignment(EnumDisplayTextAlignment var1) {
      switch(var1) {
      case CENTER:
         return TextAlignment.CENTER;
      case LEFT:
         return TextAlignment.LEFT;
      case RIGHT:
         return TextAlignment.RIGHT;
      default:
         throw new IllegalStateException();
      }
   }

   public void trickySetInvisibleTo(@NotNull Player var1, boolean var2, @Nullable FakeEntityLinker.Generic var3) {
   }
}
