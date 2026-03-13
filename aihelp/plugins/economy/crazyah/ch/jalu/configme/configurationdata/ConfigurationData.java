package ch.jalu.configme.configurationdata;

import ch.jalu.configme.properties.Property;
import ch.jalu.configme.resource.PropertyReader;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public interface ConfigurationData {
   @NotNull
   List<Property<?>> getProperties();

   @NotNull
   List<String> getCommentsForSection(String var1);

   @NotNull
   Map<String, List<String>> getAllComments();

   void initializeValues(@NotNull PropertyReader var1);

   @NotNull
   <T> T getValue(@NotNull Property<T> var1);

   <T> void setValue(@NotNull Property<T> var1, @NotNull T var2);

   boolean areAllValuesValidInResource();
}
