package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.instrument;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public final class Instruments {
   private static final VersionedRegistry<Instrument> REGISTRY = new VersionedRegistry("instrument");
   public static final Instrument PONDER_GOAT_HORN;
   public static final Instrument SING_GOAT_HORN;
   public static final Instrument SEEK_GOAT_HORN;
   public static final Instrument FEEL_GOAT_HORN;
   public static final Instrument ADMIRE_GOAT_HORN;
   public static final Instrument CALL_GOAT_HORN;
   public static final Instrument YEARN_GOAT_HORN;
   public static final Instrument DREAM_GOAT_HORN;

   private Instruments() {
   }

   @ApiStatus.Internal
   public static Instrument define(String key, Sound sound) {
      return define(key, sound, 140, 256.0F);
   }

   @ApiStatus.Internal
   public static Instrument define(String key, Sound sound, int useDuration, float range) {
      return (Instrument)REGISTRY.define(key, (data) -> {
         return new StaticInstrument(data, sound, (float)useDuration, range, Component.translatable("instrument.minecraft." + key));
      });
   }

   public static VersionedRegistry<Instrument> getRegistry() {
      return REGISTRY;
   }

   public static Instrument getByName(String name) {
      return (Instrument)REGISTRY.getByName(name);
   }

   public static Instrument getById(ClientVersion version, int id) {
      return (Instrument)REGISTRY.getById(version, id);
   }

   static {
      PONDER_GOAT_HORN = define("ponder_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_0);
      SING_GOAT_HORN = define("sing_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_1);
      SEEK_GOAT_HORN = define("seek_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_2);
      FEEL_GOAT_HORN = define("feel_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_3);
      ADMIRE_GOAT_HORN = define("admire_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_4);
      CALL_GOAT_HORN = define("call_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_5);
      YEARN_GOAT_HORN = define("yearn_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_6);
      DREAM_GOAT_HORN = define("dream_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_7);
      REGISTRY.unloadMappings();
   }
}
