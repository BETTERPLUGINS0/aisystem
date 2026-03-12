package fr.xephi.authme.libs.ch.jalu.configme.configurationdata;

import fr.xephi.authme.libs.ch.jalu.configme.SettingsHolder;
import fr.xephi.authme.libs.ch.jalu.configme.exception.ConfigMeException;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.PropertyValue;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationDataImpl implements ConfigurationData {
   private final List<Property<?>> properties;
   private final Map<String, List<String>> allComments;
   private final Map<String, Object> values;
   private boolean allPropertiesValidInResource;

   protected ConfigurationDataImpl(List<? extends Property<?>> allProperties, Map<String, List<String>> allComments) {
      this.properties = Collections.unmodifiableList(allProperties);
      this.allComments = Collections.unmodifiableMap(allComments);
      this.values = new HashMap();
   }

   public List<Property<?>> getProperties() {
      return this.properties;
   }

   public List<String> getCommentsForSection(String path) {
      return (List)this.allComments.getOrDefault(path, Collections.emptyList());
   }

   public Map<String, List<String>> getAllComments() {
      return this.allComments;
   }

   public <T> T getValue(Property<T> property) {
      Object value = this.values.get(property.getPath());
      if (value == null) {
         throw new ConfigMeException(String.format("No value exists for property with path '%s'. This may happen if the property belongs to a %s class which was not passed to the settings manager.", property.getPath(), SettingsHolder.class.getSimpleName()));
      } else {
         return value;
      }
   }

   public <T> void setValue(Property<T> property, T value) {
      if (property.isValidValue(value)) {
         this.values.put(property.getPath(), value);
      } else {
         throw new ConfigMeException("Invalid value for property '" + property + "': " + value);
      }
   }

   public void initializeValues(PropertyReader reader) {
      this.values.clear();
      this.allPropertiesValidInResource = (Boolean)this.getProperties().stream().map((property) -> {
         return this.setValueForProperty(property, reader);
      }).reduce(true, Boolean::logicalAnd);
   }

   protected <T> boolean setValueForProperty(Property<T> property, PropertyReader reader) {
      PropertyValue<T> propertyValue = property.determineValue(reader);
      this.setValue(property, propertyValue.getValue());
      return propertyValue.isValidInResource();
   }

   public boolean areAllValuesValidInResource() {
      return this.allPropertiesValidInResource;
   }

   protected Map<String, Object> getValues() {
      return this.values;
   }
}
