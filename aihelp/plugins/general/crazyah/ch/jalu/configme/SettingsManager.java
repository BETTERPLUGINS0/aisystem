package ch.jalu.configme;

import ch.jalu.configme.properties.Property;
import org.jetbrains.annotations.NotNull;

public interface SettingsManager {
   @NotNull
   <T> T getProperty(@NotNull Property<T> var1);

   <T> void setProperty(@NotNull Property<T> var1, @NotNull T var2);

   void reload();

   void save();
}
