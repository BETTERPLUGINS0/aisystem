package ac.grim.grimac.shaded.incendo.cloud.setting;

import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.common.returnsreceiver.qual.This;

@API(
   status = Status.STABLE
)
public interface Configurable<S extends Setting> {
   @NonNull
   static <E extends Enum<E> & Setting> Configurable<E> enumConfigurable(@NonNull final Class<E> enumClass) {
      return new EnumConfigurable(enumClass);
   }

   @This
   @NonNull
   Configurable<S> set(@NonNull S setting, boolean value);

   boolean get(@NonNull S setting);
}
