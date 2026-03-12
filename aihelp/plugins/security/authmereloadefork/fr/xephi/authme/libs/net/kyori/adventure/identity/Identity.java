package fr.xephi.authme.libs.net.kyori.adventure.identity;

import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.pointer.Pointer;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public interface Identity extends Examinable, Identified {
   Pointer<String> NAME = Pointer.pointer(String.class, Key.key("adventure", "name"));
   Pointer<UUID> UUID = Pointer.pointer(UUID.class, Key.key("adventure", "uuid"));
   Pointer<Component> DISPLAY_NAME = Pointer.pointer(Component.class, Key.key("adventure", "display_name"));
   Pointer<Locale> LOCALE = Pointer.pointer(Locale.class, Key.key("adventure", "locale"));

   @NotNull
   static Identity nil() {
      return NilIdentity.INSTANCE;
   }

   @NotNull
   static Identity identity(@NotNull final UUID uuid) {
      return (Identity)(uuid.equals(NilIdentity.NIL_UUID) ? NilIdentity.INSTANCE : new IdentityImpl(uuid));
   }

   @NotNull
   UUID uuid();

   @NotNull
   default Identity identity() {
      return this;
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("uuid", (Object)this.uuid()));
   }
}
