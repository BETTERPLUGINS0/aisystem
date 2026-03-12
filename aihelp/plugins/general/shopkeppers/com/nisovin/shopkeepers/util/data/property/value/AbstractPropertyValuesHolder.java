package com.nisovin.shopkeepers.util.data.property.value;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractPropertyValuesHolder implements PropertyValuesHolder {
   private final List<PropertyValue<?>> propertyValues = new ArrayList();
   private final List<? extends PropertyValue<?>> propertyValuesView;

   public AbstractPropertyValuesHolder() {
      this.propertyValuesView = Collections.unmodifiableList(this.propertyValues);
   }

   final void add(PropertyValue<?> propertyValue) {
      Validate.notNull(propertyValue, (String)"propertyValue is null");
      Validate.isTrue(propertyValue.getHolder() == this, "propertyValue has already been added to another holder");
      String propertyName = propertyValue.getProperty().getName();
      Iterator var3 = this.propertyValuesView.iterator();

      while(var3.hasNext()) {
         PropertyValue<?> otherPropertyValue = (PropertyValue)var3.next();
         if (propertyName.equalsIgnoreCase(otherPropertyValue.getProperty().getName())) {
            Validate.error("Another PropertyValue with the same property name has already been added: " + propertyName);
         }
      }

      this.propertyValues.add(propertyValue);
   }

   public final List<? extends PropertyValue<?>> getPropertyValues() {
      return this.propertyValuesView;
   }

   public abstract String getLogPrefix();

   public abstract void markDirty();
}
