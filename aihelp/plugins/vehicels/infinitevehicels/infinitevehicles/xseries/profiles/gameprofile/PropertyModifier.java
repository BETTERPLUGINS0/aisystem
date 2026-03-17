package me.PM2.infinitevehicles.xseries.profiles.gameprofile;

import com.google.common.collect.Multimap;
import com.mojang.authlib.properties.Property;

public final class PropertyModifier {
   private final Multimap<String, Property> properties;

   public PropertyModifier(Multimap<String, Property> var1) {
      this.properties = var1;
   }

   public void setProperty(String var1, String var2) {
      this.properties.removeAll(var1);
      this.properties.put(var1, new Property(var1, var2));
   }

   public void setProperty(String var1, Property var2) {
      this.properties.removeAll(var1);
      this.properties.put(var1, var2);
   }

   public void removeProperty(String var1) {
      this.properties.removeAll(var1);
   }

   public Multimap<String, Property> getProperties() {
      return this.properties;
   }
}
