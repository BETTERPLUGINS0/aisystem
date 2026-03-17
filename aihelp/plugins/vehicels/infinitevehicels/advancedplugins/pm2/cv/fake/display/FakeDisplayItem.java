package advancedplugins.pm2.cv.fake.display;

import advancedplugins.pm2.cv.enums.EnumDisplayEntity;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public class FakeDisplayItem extends FakeDisplay {
   public FakeDisplayItem(@NotNull World world) {
      super(var1);
   }

   @NotNull
   public EnumDisplayEntity getKind() {
      return EnumDisplayEntity.ITEM;
   }
}
