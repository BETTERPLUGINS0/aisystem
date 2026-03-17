package me.PM2.infinitevehicles.xseries.profiles.gameprofile.property;

import com.mojang.authlib.properties.Property;

public final class OldProperty extends MojangProperty {
   protected OldProperty(Property var1) {
      super(var1);
   }

   public String name() {
      return this.object.getName();
   }

   public String value() {
      return this.object.getValue();
   }

   public String signature() {
      return this.object.getSignature();
   }
}
