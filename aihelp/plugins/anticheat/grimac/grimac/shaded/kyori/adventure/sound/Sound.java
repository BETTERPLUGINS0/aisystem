package ac.grim.grimac.shaded.kyori.adventure.sound;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Range;
import ac.grim.grimac.shaded.kyori.adventure.builder.AbstractBuilder;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.key.Keyed;
import ac.grim.grimac.shaded.kyori.adventure.util.Index;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.function.Consumer;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface Sound extends Examinable {
   @NotNull
   static Sound.Builder sound() {
      return new SoundImpl.BuilderImpl();
   }

   @NotNull
   static Sound.Builder sound(@NotNull final Sound existing) {
      return new SoundImpl.BuilderImpl(existing);
   }

   @NotNull
   static Sound sound(@NotNull final Consumer<Sound.Builder> configurer) {
      return (Sound)AbstractBuilder.configureAndBuild(sound(), configurer);
   }

   @NotNull
   static Sound sound(@NotNull final Key name, @NotNull final Sound.Source source, final float volume, final float pitch) {
      return (Sound)sound().type(name).source(source).volume(volume).pitch(pitch).build();
   }

   @NotNull
   static Sound sound(@NotNull final Sound.Type type, @NotNull final Sound.Source source, final float volume, final float pitch) {
      Objects.requireNonNull(type, "type");
      return sound(type.key(), source, volume, pitch);
   }

   @NotNull
   static Sound sound(@NotNull final Supplier<? extends Sound.Type> type, @NotNull final Sound.Source source, final float volume, final float pitch) {
      return (Sound)sound().type(type).source(source).volume(volume).pitch(pitch).build();
   }

   @NotNull
   static Sound sound(@NotNull final Key name, @NotNull final Sound.Source.Provider source, final float volume, final float pitch) {
      return sound(name, source.soundSource(), volume, pitch);
   }

   @NotNull
   static Sound sound(@NotNull final Sound.Type type, @NotNull final Sound.Source.Provider source, final float volume, final float pitch) {
      return sound(type, source.soundSource(), volume, pitch);
   }

   @NotNull
   static Sound sound(@NotNull final Supplier<? extends Sound.Type> type, @NotNull final Sound.Source.Provider source, final float volume, final float pitch) {
      return sound(type, source.soundSource(), volume, pitch);
   }

   @NotNull
   Key name();

   @NotNull
   Sound.Source source();

   float volume();

   float pitch();

   @NotNull
   OptionalLong seed();

   @NotNull
   SoundStop asStop();

   public interface Builder extends AbstractBuilder<Sound> {
      @NotNull
      Sound.Builder type(@NotNull final Key type);

      @NotNull
      Sound.Builder type(@NotNull final Sound.Type type);

      @NotNull
      Sound.Builder type(@NotNull final Supplier<? extends Sound.Type> typeSupplier);

      @NotNull
      Sound.Builder source(@NotNull final Sound.Source source);

      @NotNull
      Sound.Builder source(@NotNull final Sound.Source.Provider source);

      @NotNull
      Sound.Builder volume(@Range(from = 0L,to = 2147483647L) final float volume);

      @NotNull
      Sound.Builder pitch(@Range(from = -1L,to = 1L) final float pitch);

      @NotNull
      Sound.Builder seed(final long seed);

      @NotNull
      Sound.Builder seed(@NotNull final OptionalLong seed);
   }

   public static enum Source {
      MASTER("master"),
      MUSIC("music"),
      RECORD("record"),
      WEATHER("weather"),
      BLOCK("block"),
      HOSTILE("hostile"),
      NEUTRAL("neutral"),
      PLAYER("player"),
      AMBIENT("ambient"),
      VOICE("voice"),
      UI("ui");

      public static final Index<String, Sound.Source> NAMES = Index.create(Sound.Source.class, (source) -> {
         return source.name;
      });
      private final String name;

      private Source(final String name) {
         this.name = name;
      }

      // $FF: synthetic method
      private static Sound.Source[] $values() {
         return new Sound.Source[]{MASTER, MUSIC, RECORD, WEATHER, BLOCK, HOSTILE, NEUTRAL, PLAYER, AMBIENT, VOICE, UI};
      }

      public interface Provider {
         @NotNull
         Sound.Source soundSource();
      }
   }

   public interface Type extends Keyed {
      @NotNull
      Key key();
   }

   public interface Emitter {
      @NotNull
      static Sound.Emitter self() {
         return SoundImpl.EMITTER_SELF;
      }
   }
}
