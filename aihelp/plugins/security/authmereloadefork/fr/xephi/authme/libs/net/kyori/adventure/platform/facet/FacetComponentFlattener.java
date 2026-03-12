package fr.xephi.authme.libs.net.kyori.adventure.platform.facet;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.TranslatableComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import fr.xephi.authme.libs.net.kyori.adventure.translation.GlobalTranslator;
import fr.xephi.authme.libs.net.kyori.adventure.translation.TranslationRegistry;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class FacetComponentFlattener {
   private static final Pattern LOCALIZATION_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?s");

   private FacetComponentFlattener() {
   }

   public static <V> ComponentFlattener get(final V instance, final Collection<? extends FacetComponentFlattener.Translator<V>> candidates) {
      FacetComponentFlattener.Translator<V> translator = (FacetComponentFlattener.Translator)Facet.of(candidates, instance);
      ComponentFlattener.Builder flattenerBuilder = (ComponentFlattener.Builder)ComponentFlattener.basic().toBuilder();
      flattenerBuilder.complexMapper(TranslatableComponent.class, (translatable, consumer) -> {
         String key = translatable.key();
         Iterator var5 = GlobalTranslator.translator().sources().iterator();

         fr.xephi.authme.libs.net.kyori.adventure.translation.Translator registry;
         do {
            if (!var5.hasNext()) {
               String translated = translator == null ? key : translator.valueOrDefault(instance, key);
               Matcher matcher = LOCALIZATION_PATTERN.matcher(translated);
               List<Component> args = translatable.args();
               int argPosition = 0;
               int lastIdx = 0;

               while(matcher.find()) {
                  if (lastIdx < matcher.start()) {
                     consumer.accept(Component.text(translated.substring(lastIdx, matcher.start())));
                  }

                  lastIdx = matcher.end();
                  String argIdx = matcher.group(1);
                  int idx;
                  if (argIdx != null) {
                     try {
                        idx = Integer.parseInt(argIdx) - 1;
                        if (idx < args.size()) {
                           consumer.accept((Component)args.get(idx));
                        }
                     } catch (NumberFormatException var12) {
                     }
                  } else {
                     idx = argPosition++;
                     if (idx < args.size()) {
                        consumer.accept((Component)args.get(idx));
                     }
                  }
               }

               if (lastIdx < translated.length()) {
                  consumer.accept(Component.text(translated.substring(lastIdx)));
               }

               return;
            }

            registry = (fr.xephi.authme.libs.net.kyori.adventure.translation.Translator)var5.next();
         } while(!(registry instanceof TranslationRegistry) || !((TranslationRegistry)registry).contains(key));

         consumer.accept(GlobalTranslator.render(translatable, Locale.getDefault()));
      });
      return (ComponentFlattener)flattenerBuilder.build();
   }

   public interface Translator<V> extends Facet<V> {
      @NotNull
      String valueOrDefault(@NotNull final V game, @NotNull final String key);
   }
}
