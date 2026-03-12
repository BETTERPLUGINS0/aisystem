package ch.jalu.configme.configurationdata;

import ch.jalu.configme.exception.ConfigMeException;
import ch.jalu.configme.properties.Property;
import ch.jalu.configme.properties.convertresult.PropertyValue;
import ch.jalu.configme.resource.PropertyReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class ConfigurationDataImpl implements ConfigurationData {
   @NotNull
   private final List<Property<?>> properties;
   @NotNull
   private final Map<String, List<String>> allComments;
   @NotNull
   private final Map<String, Object> values;
   private boolean allPropertiesValidInResource;

   protected ConfigurationDataImpl(@NotNull List<? extends Property<?>> allProperties, @NotNull Map<String, List<String>> allComments) {
      this.properties = Collections.unmodifiableList(allProperties);
      this.allComments = Collections.unmodifiableMap(allComments);
      this.values = new HashMap();
   }

   @NotNull
   public List<Property<?>> getProperties() {
      return this.properties;
   }

   @NotNull
   public List<String> getCommentsForSection(String path) {
      return (List)this.allComments.getOrDefault(path, Collections.emptyList());
   }

   @NotNull
   public Map<String, List<String>> getAllComments() {
      return this.allComments;
   }

   @NotNull
   public <T> T getValue(@NotNull Property<T> property) {
      Object value = this.values.get(property.getPath());
      return value;
   }

   public <T> void setValue(@NotNull Property<T> property, @NotNull T value) {
      if (property.isValidValue(value)) {
         this.values.put(property.getPath(), value);
      } else {
         throw new ConfigMeException("Invalid value for property '" + property + "': " + value);
      }
   }

   public void initializeValues(@NotNull PropertyReader reader) {
      this.values.clear();
      this.allPropertiesValidInResource = (Boolean)this.getProperties().stream().map((property) -> {
         return this.setValueForProperty(property, reader);
      }).reduce(true, Boolean::logicalAnd);
   }

   protected <T> boolean setValueForProperty(@NotNull Property<T> property, @NotNull PropertyReader reader) {
      PropertyValue<T> propertyValue = property.determineValue(reader);
      this.setValue(property, propertyValue.getValue());
      return propertyValue.isValidInResource();
   }

   public boolean areAllValuesValidInResource() {
      return this.allPropertiesValidInResource;
   }

   @NotNull
   protected Map<String, Object> getValues() {
      return this.values;
   }
}
