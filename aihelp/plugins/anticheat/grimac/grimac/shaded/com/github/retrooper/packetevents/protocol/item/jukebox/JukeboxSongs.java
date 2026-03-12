package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.jukebox;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sound;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.sound.Sounds;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.resources.ResourceLocation;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.util.Collection;

public final class JukeboxSongs {
   private static final VersionedRegistry<IJukeboxSong> REGISTRY = new VersionedRegistry("jukebox_song");
   public static final IJukeboxSong THIRTEEN;
   public static final IJukeboxSong CAT;
   public static final IJukeboxSong BLOCKS;
   public static final IJukeboxSong CHIRP;
   public static final IJukeboxSong FAR;
   public static final IJukeboxSong MALL;
   public static final IJukeboxSong MELLOHI;
   public static final IJukeboxSong STAL;
   public static final IJukeboxSong STRAD;
   public static final IJukeboxSong WARD;
   public static final IJukeboxSong ELEVEN;
   public static final IJukeboxSong WAIT;
   public static final IJukeboxSong PIGSTEP;
   public static final IJukeboxSong OTHERSIDE;
   public static final IJukeboxSong FIVE;
   public static final IJukeboxSong RELIC;
   public static final IJukeboxSong PRECIPICE;
   public static final IJukeboxSong CREATOR;
   public static final IJukeboxSong CREATOR_MUSIC_BOX;
   public static final IJukeboxSong TEARS;
   public static final IJukeboxSong LAVA_CHICKEN;

   private JukeboxSongs() {
   }

   private static String makeDescriptionId(String var0, @Nullable ResourceLocation var1) {
      return var1 == null ? var0 + ".unregistered_sadface" : var0 + "." + var1.getNamespace() + "." + var1.getKey().replace('/', '.');
   }

   @ApiStatus.Internal
   public static IJukeboxSong define(String key, Sound sound, float lengthInSeconds, int comparatorOutput) {
      return (IJukeboxSong)REGISTRY.define(key, (data) -> {
         return new JukeboxSong(data, sound, Component.translatable(makeDescriptionId("jukebox_song", data.getName())), lengthInSeconds, comparatorOutput);
      });
   }

   public static VersionedRegistry<IJukeboxSong> getRegistry() {
      return REGISTRY;
   }

   public static IJukeboxSong getByName(String name) {
      return (IJukeboxSong)REGISTRY.getByName(name);
   }

   public static IJukeboxSong getById(ClientVersion version, int id) {
      return (IJukeboxSong)REGISTRY.getById(version, id);
   }

   public static Collection<IJukeboxSong> values() {
      return REGISTRY.getEntries();
   }

   static {
      THIRTEEN = define("13", Sounds.MUSIC_DISC_13, 178.0F, 1);
      CAT = define("cat", Sounds.MUSIC_DISC_CAT, 185.0F, 2);
      BLOCKS = define("blocks", Sounds.MUSIC_DISC_BLOCKS, 345.0F, 3);
      CHIRP = define("chirp", Sounds.MUSIC_DISC_CHIRP, 185.0F, 4);
      FAR = define("far", Sounds.MUSIC_DISC_FAR, 174.0F, 5);
      MALL = define("mall", Sounds.MUSIC_DISC_MALL, 197.0F, 6);
      MELLOHI = define("mellohi", Sounds.MUSIC_DISC_MELLOHI, 96.0F, 7);
      STAL = define("stal", Sounds.MUSIC_DISC_STAL, 150.0F, 8);
      STRAD = define("strad", Sounds.MUSIC_DISC_STRAD, 188.0F, 9);
      WARD = define("ward", Sounds.MUSIC_DISC_WARD, 251.0F, 10);
      ELEVEN = define("11", Sounds.MUSIC_DISC_11, 71.0F, 11);
      WAIT = define("wait", Sounds.MUSIC_DISC_WAIT, 238.0F, 12);
      PIGSTEP = define("pigstep", Sounds.MUSIC_DISC_PIGSTEP, 149.0F, 13);
      OTHERSIDE = define("otherside", Sounds.MUSIC_DISC_OTHERSIDE, 195.0F, 14);
      FIVE = define("5", Sounds.MUSIC_DISC_5, 178.0F, 15);
      RELIC = define("relic", Sounds.MUSIC_DISC_RELIC, 218.0F, 14);
      PRECIPICE = define("precipice", Sounds.MUSIC_DISC_PRECIPICE, 299.0F, 13);
      CREATOR = define("creator", Sounds.MUSIC_DISC_CREATOR, 176.0F, 12);
      CREATOR_MUSIC_BOX = define("creator_music_box", Sounds.MUSIC_DISC_CREATOR_MUSIC_BOX, 73.0F, 11);
      TEARS = define("tears", Sounds.MUSIC_DISC_TEARS, 175.0F, 10);
      LAVA_CHICKEN = define("lava_chicken", Sounds.MUSIC_DISC_LAVA_CHICKEN, 134.0F, 9);
      REGISTRY.unloadMappings();
   }
}
