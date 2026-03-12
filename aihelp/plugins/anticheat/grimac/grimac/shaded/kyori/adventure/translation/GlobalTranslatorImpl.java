package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import ac.grim.grimac.shaded.kyori.adventure.util.TriState;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

final class GlobalTranslatorImpl implements GlobalTranslator {
   private static final Key NAME = Key.key("adventure", "global");
   static final GlobalTranslatorImpl INSTANCE = new GlobalTranslatorImpl();
   final TranslatableComponentRenderer<Locale> renderer = TranslatableComponentRenderer.usingTranslationSource(this);
   private final Set<Translator> sources = Collections.newSetFromMap(new ConcurrentHashMap());

   private GlobalTranslatorImpl() {
   }

   @NotNull
   public Key name() {
      return NAME;
   }

   @NotNull
   public Iterable<? extends Translator> sources() {
      return Collections.unmodifiableSet(this.sources);
   }

   public boolean addSource(@NotNull final Translator source) {
      Objects.requireNonNull(source, "source");
      if (source == this) {
         throw new IllegalArgumentException("GlobalTranslationSource");
      } else {
         return this.sources.add(source);
      }
   }

   public boolean removeSource(@NotNull final Translator source) {
      Objects.requireNonNull(source, "source");
      return this.sources.remove(source);
   }

   @NotNull
   public TriState hasAnyTranslations() {
      return !this.sources.isEmpty() ? TriState.TRUE : TriState.FALSE;
   }

   public boolean canTranslate(@NotNull final String key, @NotNull final Locale locale) {
      Objects.requireNonNull(key, "key");
      Objects.requireNonNull(locale, "locale");
      Iterator var3 = this.sources.iterator();

      Translator source;
      do {
         if (!var3.hasNext()) {
            return false;
         }

         source = (Translator)var3.next();
      } while(!source.canTranslate(key, locale));

      return true;
   }

   @Nullable
   public MessageFormat translate(@NotNull final String key, @NotNull final Locale locale) {
      Objects.requireNonNull(key, "key");
      Objects.requireNonNull(locale, "locale");
      Iterator var3 = this.sources.iterator();

      MessageFormat translation;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         Translator source = (Translator)var3.next();
         translation = source.translate(key, locale);
      } while(translation == null);

      return translation;
   }

   @Nullable
   public Component translate(@NotNull final TranslatableComponent component, @NotNull final Locale locale) {
      Objects.requireNonNull(component, "component");
      Objects.requireNonNull(locale, "locale");
      Iterator var3 = this.sources.iterator();

      Component translation;
      do {
         if (!var3.hasNext()) {
            return null;
         }

         Translator source = (Translator)var3.next();
         translation = source.translate(component, locale);
      } while(translation == null);

      return translation;
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("sources", (Object)this.sources));
   }
}
