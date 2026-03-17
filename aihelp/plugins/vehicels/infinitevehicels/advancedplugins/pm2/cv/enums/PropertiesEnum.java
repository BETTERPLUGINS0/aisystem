package advancedplugins.pm2.cv.enums;

import org.jetbrains.annotations.NotNull;

public interface PropertiesEnum {
   @NotNull
   Class<?> getValueType();

   @NotNull
   Object getDefaultValueRaw();

   @NotNull
   default <T> T getDefaultValue(Class<T> type) {
      return type.cast(this.getDefaultValueRaw());
   }
}
