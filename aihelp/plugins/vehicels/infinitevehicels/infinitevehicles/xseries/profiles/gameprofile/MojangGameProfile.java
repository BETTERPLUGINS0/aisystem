package me.PM2.infinitevehicles.xseries.profiles.gameprofile;

import com.google.common.collect.Iterables;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;
import java.util.function.Consumer;
import me.PM2.infinitevehicles.xseries.AbstractReferencedClass;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class MojangGameProfile extends AbstractReferencedClass<GameProfile> {
   protected final GameProfile object;

   protected MojangGameProfile(GameProfile var1) {
      this.object = var1;
   }

   public final GameProfile object() {
      return this.object;
   }

   public abstract UUID id();

   public abstract String name();

   public abstract PropertyMap properties();

   public final MojangGameProfile copy() {
      return this.copy((Consumer)null);
   }

   public abstract MojangGameProfile copy(@Nullable Consumer<PropertyModifier> var1);

   @Nullable
   public Property getProperty(String var1) {
      return (Property)Iterables.getFirst(this.properties().get(var1), (Object)null);
   }
}
