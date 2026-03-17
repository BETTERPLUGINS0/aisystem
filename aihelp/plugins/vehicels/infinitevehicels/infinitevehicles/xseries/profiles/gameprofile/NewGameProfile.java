package me.PM2.infinitevehicles.xseries.profiles.gameprofile;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;
import java.util.function.Consumer;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class NewGameProfile extends MojangGameProfile {
   protected NewGameProfile(GameProfile var1) {
      super(var1);
   }

   public UUID id() {
      return this.object.id();
   }

   public String name() {
      return this.object.name();
   }

   public PropertyMap properties() {
      return this.object.properties();
   }

   public MojangGameProfile copy(@Nullable Consumer<PropertyModifier> var1) {
      PropertyMap var2 = this.properties();
      ListMultimap var3 = MultimapBuilder.hashKeys(var2.size()).arrayListValues(1).build();
      var3.putAll(var2);
      if (var1 != null) {
         var1.accept(new PropertyModifier(var3));
      }

      GameProfile var4 = new GameProfile(this.id(), this.name(), new PropertyMap(var3));
      return new NewGameProfile(var4);
   }
}
