package advancedplugins.pm2.cv.fake.display;

import advancedplugins.pm2.cv.enums.EnumDisplayProperty;
import advancedplugins.pm2.cv.fake.FakeEntityHandle;
import advancedplugins.pm2.cv.fake.FakeEntityLinker;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public interface FakeDisplayHandle extends FakeEntityHandle<EnumDisplayProperty> {
   void applyTransformation(@Nullable Matrix4f transformation);

   void trickySetInvisibleTo(@NotNull Player player, boolean invisible, @Nullable FakeEntityLinker.Generic linker);

   default void trickySetInvisibleTo(@NotNull Player player, boolean invisible) {
      this.trickySetInvisibleTo(player, invisible, (FakeEntityLinker.Generic)null);
   }
}
