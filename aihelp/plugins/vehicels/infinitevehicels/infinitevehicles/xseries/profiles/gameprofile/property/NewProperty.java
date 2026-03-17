package me.PM2.infinitevehicles.xseries.profiles.gameprofile.property;

import com.mojang.authlib.properties.Property;

public final class NewProperty extends MojangProperty {
   protected NewProperty(Property var1) {
      super(var1);
   }

   public String name() {
      return this.object.name();
   }

   public String value() {
      return this.object.value();
   }

   public String signature() {
      return this.object.signature();
   }
}
