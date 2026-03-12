package fr.xephi.authme.message.updater;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationDataImpl;
import fr.xephi.authme.libs.ch.jalu.configme.properties.Property;
import fr.xephi.authme.libs.ch.jalu.configme.properties.convertresult.PropertyValue;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import fr.xephi.authme.message.MessageKey;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MessageKeyConfigurationData extends ConfigurationDataImpl {
   public MessageKeyConfigurationData(MessageUpdater.MessageKeyPropertyListBuilder propertyListBuilder, Map<String, List<String>> allComments) {
      super(propertyListBuilder.getAllProperties(), allComments);
   }

   public void initializeValues(PropertyReader reader) {
      Iterator var2 = this.getAllMessageProperties().iterator();

      while(var2.hasNext()) {
         Property<String> property = (Property)var2.next();
         PropertyValue<String> value = property.determineValue(reader);
         if (value.isValidInResource()) {
            this.setValue(property, (String)value.getValue());
         }
      }

   }

   public <T> T getValue(Property<T> property) {
      return this.getValues().get(property.getPath());
   }

   public List<Property<String>> getAllMessageProperties() {
      return this.getProperties();
   }

   public String getMessage(MessageKey messageKey) {
      return (String)this.getValue(new MessageUpdater.MessageKeyProperty(messageKey));
   }

   public void setMessage(MessageKey messageKey, String message) {
      this.setValue(new MessageUpdater.MessageKeyProperty(messageKey), message);
   }
}
