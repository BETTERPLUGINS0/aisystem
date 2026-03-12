package ac.grim.grimac.shaded.incendo.cloud.setting;

import java.util.EnumSet;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;

final class EnumConfigurable<S extends Enum<S> & Setting> implements Configurable<S> {
   private final EnumSet<S> settings;

   EnumConfigurable(@NonNull final Class<S> settingClass) {
      this.settings = EnumSet.noneOf(settingClass);
   }

   EnumConfigurable(@NonNull final S defaultSetting) {
      this.settings = EnumSet.of(defaultSetting);
   }

   @This
   @NonNull
   public EnumConfigurable<S> set(@NonNull final S setting, final boolean value) {
      if (value) {
         this.settings.add(setting);
      } else {
         this.settings.remove(setting);
      }

      return this;
   }

   public boolean get(@NonNull final S setting) {
      return this.settings.contains(setting);
   }
}
