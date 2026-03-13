package libs.com.ryderbelserion.vital.common.api.interfaces;

public interface IModule {
   default boolean isEnabled() {
      return false;
   }

   default void enable() {
   }

   default void reload() {
   }

   default void disable() {
   }

   String getName();
}
