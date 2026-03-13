package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.compat.Compat;
import com.nisovin.shopkeepers.util.bukkit.RegistryUtils;
import com.nisovin.shopkeepers.util.bukkit.SoundEffect;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Locale;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ConfigMigration9 implements ConfigMigration {
   public void apply(DataContainer configData) {
      this.migrateSoundEffect(configData, "trade-succeeded-sound");
      this.migrateSoundEffect(configData, "trade-failed-sound");
      this.migrateSoundEffect(configData, "trade-notification-sound");
      this.migrateSoundEffect(configData, "shop-owner-trade-notification-sound");
   }

   private void migrateSoundEffect(DataContainer configData, String key) {
      Object data = configData.get(key);
      if (data == null) {
         Log.info("  Setting '" + key + "': Not found. Skipping migration.");
      } else {
         try {
            SoundEffect soundEffect = (SoundEffect)SoundEffect.SERIALIZER.deserialize(data);
            if (soundEffect.getSound() != null) {
               Log.info("  Setting '" + key + "': Sound value found. Nothing to migrate.");
            } else {
               String soundName = soundEffect.getSoundName();

               assert soundName != null;

               if (soundName.isEmpty()) {
                  Log.info("  Setting '" + key + "': Empty sound name. Nothing to migrate.");
               } else {
                  Sound sound = this.getSoundByEnumName(soundName);
                  if (sound == null) {
                     Log.info("  Setting '" + key + "': Sound enum value not found. Skipping migration.");
                  } else {
                     SoundEffect newSoundEffect = (new SoundEffect(sound)).withCategory(soundEffect.getCategory()).withPitch(soundEffect.getPitch()).withVolume(soundEffect.getVolume());
                     Object newData = SoundEffect.SERIALIZER.serialize(newSoundEffect);

                     assert newData != null;

                     Log.info("  Migrating setting '" + key + "' from sound name '" + soundName + "' to sound key '" + String.valueOf(RegistryUtils.getKeyOrThrow(sound)) + "'.");
                     configData.set(key, newData);
                  }
               }
            }
         } catch (InvalidDataException var9) {
            Log.warning("  Setting '" + key + "': Failed to load. Skipping migration.");
         }
      }
   }

   @Nullable
   private Sound getSoundByEnumName(String name) {
      NamespacedKey key = NamespacedKey.fromString(name.toLowerCase(Locale.ROOT));
      if (key != null) {
         Sound sound = (Sound)Compat.getProvider().getRegistry(Sound.class).get(key);
         if (sound != null) {
            return sound;
         }
      }

      try {
         return (Sound)Sound.class.getField(name).get((Object)null);
      } catch (IllegalAccessException | NoSuchFieldException var4) {
         return null;
      }
   }
}
