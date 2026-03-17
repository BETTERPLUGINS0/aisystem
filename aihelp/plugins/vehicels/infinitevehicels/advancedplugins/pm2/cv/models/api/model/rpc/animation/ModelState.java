package advancedplugins.pm2.cv.models.api.model.rpc.animation;

import advancedplugins.pm2.cv.models.api.utils.config.ConfigProperty;
import advancedplugins.pm2.cv.models.api.utils.config.Property;
import java.util.Locale;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public enum ModelState implements Property {
   IDLE(BlueprintAnimation.LoopMode.LOOP, false),
   WALK(BlueprintAnimation.LoopMode.LOOP, false),
   STRAFE(BlueprintAnimation.LoopMode.LOOP, false),
   JUMP_START(BlueprintAnimation.LoopMode.ONCE, true),
   JUMP(BlueprintAnimation.LoopMode.LOOP, true),
   JUMP_END(BlueprintAnimation.LoopMode.ONCE, true),
   HOVER(BlueprintAnimation.LoopMode.LOOP, true),
   FLY(BlueprintAnimation.LoopMode.LOOP, true),
   SPAWN(BlueprintAnimation.LoopMode.ONCE, true),
   DEATH(BlueprintAnimation.LoopMode.HOLD, true);

   private final String path;
   private final Object def;
   private final BlueprintAnimation.LoopMode loopMode;
   private final boolean override;

   private ModelState(BlueprintAnimation.LoopMode param3, boolean param4) {
      String var5 = ConfigProperty.DEFAULT_NAMES.getPath();
      this.path = var5 + "." + this.name();
      this.def = this.name().toLowerCase(Locale.ENGLISH);
      this.loopMode = var3;
      this.override = var4;
   }

   @Nullable
   public static ModelState get(String var0) {
      try {
         return valueOf(var0.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException var2) {
         return null;
      }
   }

   @Generated
   public String getPath() {
      return this.path;
   }

   @Generated
   public Object getDef() {
      return this.def;
   }

   @Generated
   public BlueprintAnimation.LoopMode getLoopMode() {
      return this.loopMode;
   }

   @Generated
   public boolean isOverride() {
      return this.override;
   }

   // $FF: synthetic method
   private static ModelState[] $values() {
      return new ModelState[]{IDLE, WALK, STRAFE, JUMP_START, JUMP, JUMP_END, HOVER, FLY, SPAWN, DEATH};
   }
}
