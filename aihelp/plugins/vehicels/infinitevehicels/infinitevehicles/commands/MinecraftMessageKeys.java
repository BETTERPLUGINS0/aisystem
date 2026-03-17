package me.PM2.infinitevehicles.commands;

import java.util.Locale;
import me.PM2.infinitevehicles.locales.MessageKey;
import me.PM2.infinitevehicles.locales.MessageKeyProvider;

public enum MinecraftMessageKeys implements MessageKeyProvider {
   INVALID_WORLD,
   YOU_MUST_BE_HOLDING_ITEM,
   PLAYER_IS_VANISHED_CONFIRM,
   USERNAME_TOO_SHORT,
   IS_NOT_A_VALID_NAME,
   MULTIPLE_PLAYERS_MATCH,
   NO_PLAYER_FOUND_SERVER,
   NO_PLAYER_FOUND_OFFLINE,
   NO_PLAYER_FOUND,
   LOCATION_PLEASE_SPECIFY_WORLD,
   LOCATION_PLEASE_SPECIFY_XYZ,
   LOCATION_CONSOLE_NOT_RELATIVE;

   private final MessageKey key;

   private MinecraftMessageKeys() {
      this.key = MessageKey.of("acf-minecraft." + this.name().toLowerCase(Locale.ENGLISH));
   }

   public MessageKey getMessageKey() {
      return this.key;
   }

   // $FF: synthetic method
   private static MinecraftMessageKeys[] $values() {
      return new MinecraftMessageKeys[]{INVALID_WORLD, YOU_MUST_BE_HOLDING_ITEM, PLAYER_IS_VANISHED_CONFIRM, USERNAME_TOO_SHORT, IS_NOT_A_VALID_NAME, MULTIPLE_PLAYERS_MATCH, NO_PLAYER_FOUND_SERVER, NO_PLAYER_FOUND_OFFLINE, NO_PLAYER_FOUND, LOCATION_PLEASE_SPECIFY_WORLD, LOCATION_PLEASE_SPECIFY_XYZ, LOCATION_CONSOLE_NOT_RELATIVE};
   }
}
