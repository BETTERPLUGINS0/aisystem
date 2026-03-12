package fr.xephi.authme.libs.ch.jalu.configme.configurationdata;

import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import java.util.List;
import java.util.Map;

public interface ConfigurationData {
   List<Property<?>> getProperties();

   List<String> getCommentsForSection(String var1);

   Map<String, List<String>> getAllComments();

   void initializeValues(PropertyReader var1);

   <T> T getValue(Property<T> var1);

   <T> void setValue(Property<T> var1, T var2);

   boolean areAllValuesValidInResource();
}
