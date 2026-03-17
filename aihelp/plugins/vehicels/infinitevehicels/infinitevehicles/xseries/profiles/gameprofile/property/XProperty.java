package me.PM2.infinitevehicles.xseries.profiles.gameprofile.property;

import com.google.common.collect.Multimap;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.lang.invoke.MethodHandle;
import java.util.Objects;
import me.PM2.infinitevehicles.xseries.reflection.XReflection;
import org.jetbrains.annotations.NotNull;

public class XProperty {
   private static final boolean USE_RECORDS = XReflection.isRecord(Property.class);
   private static final MethodHandle PropertyMap$ctor = (MethodHandle)XReflection.of(PropertyMap.class).constructor().reflectOrNull();

   private XProperty() {
   }

   @NotNull
   public static MojangProperty of(Property var0) {
      Objects.requireNonNull(var0, "Property is null");
      return (MojangProperty)(USE_RECORDS ? new NewProperty(var0) : new OldProperty(var0));
   }

   public static MojangProperty create(String var0, String var1) {
      return of(new Property(var0, var1));
   }

   public static MojangProperty create(String var0, String var1, String var2) {
      return of(new Property(var0, var1, var2));
   }

   public static PropertyMap createPropertyMap(Multimap<String, Property> var0) {
      if (PropertyMap$ctor == null) {
         return new PropertyMap(var0);
      } else {
         try {
            PropertyMap var1 = PropertyMap$ctor.invokeExact();
            var1.putAll(var0);
            return var1;
         } catch (Throwable var2) {
            throw new RuntimeException(var2);
         }
      }
   }
}
