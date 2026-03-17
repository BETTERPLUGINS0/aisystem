/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.ApiStatus$Internal
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.platform.facet;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.platform.facet.Facet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class FacetComponentFlattener {
    private static final Pattern LOCALIZATION_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?s");

    private FacetComponentFlattener() {
    }

    public static <V> ComponentFlattener get(V v, Collection<? extends Translator<V>> collection) {
        Translator translator = Facet.of(collection, v);
        ComponentFlattener.Builder builder = (ComponentFlattener.Builder)ComponentFlattener.basic().toBuilder();
        builder.complexMapper(TranslatableComponent.class, (translatableComponent, consumer) -> {
            String string = translatableComponent.key();
            for (net.kyori.adventure.translation.Translator object22 : GlobalTranslator.translator().sources()) {
                if (!(object22 instanceof TranslationRegistry) || !((TranslationRegistry)object22).contains(string)) continue;
                consumer.accept(GlobalTranslator.render(translatableComponent, Locale.getDefault()));
                return;
            }
            String string2 = translator == null ? string : translator.valueOrDefault(v, string);
            Matcher matcher = LOCALIZATION_PATTERN.matcher(string2);
            List<Component> list = translatableComponent.args();
            int n = 0;
            int n2 = 0;
            while (matcher.find()) {
                int numberFormatException;
                if (n2 < matcher.start()) {
                    consumer.accept(Component.text(string2.substring(n2, matcher.start())));
                }
                n2 = matcher.end();
                @Nullable String string3 = matcher.group(1);
                if (string3 != null) {
                    try {
                        numberFormatException = Integer.parseInt(string3) - 1;
                        if (numberFormatException >= list.size()) continue;
                        consumer.accept(list.get(numberFormatException));
                    } catch (NumberFormatException numberFormatException2) {}
                    continue;
                }
                if ((numberFormatException = n++) >= list.size()) continue;
                consumer.accept(list.get(numberFormatException));
            }
            if (n2 < string2.length()) {
                consumer.accept(Component.text(string2.substring(n2)));
            }
        });
        return (ComponentFlattener)builder.build();
    }

    public static interface Translator<V>
    extends Facet<V> {
        @NotNull
        public String valueOrDefault(@NotNull V var1, @NotNull String var2);
    }
}

