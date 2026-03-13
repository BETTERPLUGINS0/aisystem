package libs.com.ryderbelserion.vital.common.api.interfaces;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public interface IPlugin {
   default boolean isEnabled() {
      return false;
   }

   @NotNull
   default String getName() {
      return "";
   }

   default void init() {
   }

   default void stop() {
   }

   default boolean isVanished(@NotNull UUID uuid) {
      return false;
   }

   default boolean isIgnored(@NotNull UUID sender, @NotNull UUID target) {
      return false;
   }

   default boolean isMuted(@NotNull UUID uuid) {
      return false;
   }

   default boolean isAfk(@NotNull UUID uuid) {
      return false;
   }

   default <T> T get() {
      return null;
   }
}
