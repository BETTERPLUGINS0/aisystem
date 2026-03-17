package me.PM2.infinitevehicles.xseries.profiles.gameprofile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;
import java.util.function.Consumer;

public final class OldGameProfile extends MojangGameProfile {
   protected OldGameProfile(GameProfile var1) {
      super(var1);
   }

   public UUID id() {
      return this.object.getId();
   }

   public String name() {
      return this.object.getName();
   }

   public PropertyMap properties() {
      return this.object.getProperties();
   }

   public MojangGameProfile copy(Consumer<PropertyModifier> var1) {
      GameProfile var2 = new GameProfile(this.id(), this.name());
      PropertyMap var3 = var2.getProperties();
      var3.putAll(this.properties());
      if (var1 != null) {
         var1.accept(new PropertyModifier(var3));
      }

      return new OldGameProfile(var2);
   }
}
