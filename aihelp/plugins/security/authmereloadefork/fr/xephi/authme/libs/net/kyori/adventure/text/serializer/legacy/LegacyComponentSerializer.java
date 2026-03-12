package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.legacy;

import fr.xephi.authme.libs.net.kyori.adventure.builder.AbstractBuilder;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.TextComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import fr.xephi.authme.libs.net.kyori.adventure.util.Buildable;
import fr.xephi.authme.libs.net.kyori.adventure.util.PlatformAPI;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LegacyComponentSerializer extends ComponentSerializer<Component, TextComponent, String>, Buildable<LegacyComponentSerializer, LegacyComponentSerializer.Builder> {
   char SECTION_CHAR = '§';
   char AMPERSAND_CHAR = '&';
   char HEX_CHAR = '#';

   @NotNull
   static LegacyComponentSerializer legacySection() {
      return LegacyComponentSerializerImpl.Instances.SECTION;
   }

   @NotNull
   static LegacyComponentSerializer legacyAmpersand() {
      return LegacyComponentSerializerImpl.Instances.AMPERSAND;
   }

   @NotNull
   static LegacyComponentSerializer legacy(final char legacyCharacter) {
      if (legacyCharacter == 167) {
         return legacySection();
      } else {
         return legacyCharacter == '&' ? legacyAmpersand() : builder().character(legacyCharacter).build();
      }
   }

   @Nullable
   static LegacyFormat parseChar(final char character) {
      return LegacyComponentSerializerImpl.legacyFormat(character);
   }

   @NotNull
   static LegacyComponentSerializer.Builder builder() {
      return new LegacyComponentSerializerImpl.BuilderImpl();
   }

   @NotNull
   TextComponent deserialize(@NotNull final String input);

   @NotNull
   String serialize(@NotNull final Component component);

   @PlatformAPI
   @ApiStatus.Internal
   public interface Provider {
      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      LegacyComponentSerializer legacyAmpersand();

      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      LegacyComponentSerializer legacySection();

      @PlatformAPI
      @ApiStatus.Internal
      @NotNull
      Consumer<LegacyComponentSerializer.Builder> legacy();
   }

   public interface Builder extends AbstractBuilder<LegacyComponentSerializer>, Buildable.Builder<LegacyComponentSerializer> {
      @NotNull
      LegacyComponentSerializer.Builder character(final char legacyCharacter);

      @NotNull
      LegacyComponentSerializer.Builder hexCharacter(final char legacyHexCharacter);

      @NotNull
      LegacyComponentSerializer.Builder extractUrls();

      @NotNull
      LegacyComponentSerializer.Builder extractUrls(@NotNull final Pattern pattern);

      @NotNull
      LegacyComponentSerializer.Builder extractUrls(@Nullable final Style style);

      @NotNull
      LegacyComponentSerializer.Builder extractUrls(@NotNull final Pattern pattern, @Nullable final Style style);

      @NotNull
      LegacyComponentSerializer.Builder hexColors();

      @NotNull
      LegacyComponentSerializer.Builder useUnusualXRepeatedCharacterHexFormat();

      @NotNull
      LegacyComponentSerializer.Builder flattener(@NotNull final ComponentFlattener flattener);

      @NotNull
      LegacyComponentSerializer build();
   }
}
