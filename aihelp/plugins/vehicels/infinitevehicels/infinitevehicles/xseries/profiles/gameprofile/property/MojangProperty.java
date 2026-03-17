package me.PM2.infinitevehicles.xseries.profiles.gameprofile.property;

import com.mojang.authlib.properties.Property;
import me.PM2.infinitevehicles.xseries.AbstractReferencedClass;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class MojangProperty extends AbstractReferencedClass<Property> {
   protected final Property object;

   protected MojangProperty(Property var1) {
      this.object = var1;
   }

   public final Property object() {
      return this.object;
   }

   @NonNull
   public abstract String name();

   @NonNull
   public abstract String value();

   @Nullable
   public abstract String signature();

   @NonNull
   public final Property copy() {
      return new Property(this.name(), this.value(), this.signature());
   }
}
