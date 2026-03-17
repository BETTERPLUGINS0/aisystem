/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.kyori.adventure.text.renderer;

import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.BuildableComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.EntityNBTComponent;
import net.kyori.adventure.text.KeybindComponent;
import net.kyori.adventure.text.NBTComponent;
import net.kyori.adventure.text.NBTComponentBuilder;
import net.kyori.adventure.text.ScoreComponent;
import net.kyori.adventure.text.SelectorComponent;
import net.kyori.adventure.text.StorageNBTComponent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.TranslationArgument;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.renderer.AbstractComponentRenderer;
import net.kyori.adventure.translation.Translator;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class TranslatableComponentRenderer<C>
extends AbstractComponentRenderer<C> {
    private static final Set<Style.Merge> MERGES = Style.Merge.merges(Style.Merge.COLOR, Style.Merge.DECORATIONS, Style.Merge.INSERTION, Style.Merge.FONT);

    @NotNull
    public static TranslatableComponentRenderer<Locale> usingTranslationSource(final @NotNull Translator translator) {
        Objects.requireNonNull(translator, "source");
        return new TranslatableComponentRenderer<Locale>(){

            @Override
            @Nullable
            protected MessageFormat translate(@NotNull String string, @NotNull Locale locale) {
                return translator.translate(string, locale);
            }

            @Override
            @NotNull
            protected Component renderTranslatable(@NotNull TranslatableComponent translatableComponent, @NotNull Locale locale) {
                TriState triState = translator.hasAnyTranslations();
                if (triState == TriState.TRUE || triState == TriState.NOT_SET) {
                    @Nullable Component component = translator.translate(translatableComponent, locale);
                    if (component != null) {
                        return component;
                    }
                    return super.renderTranslatable(translatableComponent, locale);
                }
                return translatableComponent;
            }
        };
    }

    @Nullable
    protected MessageFormat translate(@NotNull String string, @NotNull C c) {
        return null;
    }

    @Nullable
    protected MessageFormat translate(@NotNull String string, @Nullable String string2, @NotNull C c) {
        return this.translate(string, c);
    }

    @Override
    @NotNull
    protected Component renderBlockNbt(@NotNull BlockNBTComponent blockNBTComponent, @NotNull C c) {
        BlockNBTComponent.Builder builder = this.nbt(c, Component.blockNBT(), blockNBTComponent).pos(blockNBTComponent.pos());
        return this.mergeStyleAndOptionallyDeepRender(blockNBTComponent, builder, c);
    }

    @Override
    @NotNull
    protected Component renderEntityNbt(@NotNull EntityNBTComponent entityNBTComponent, @NotNull C c) {
        EntityNBTComponent.Builder builder = this.nbt(c, Component.entityNBT(), entityNBTComponent).selector(entityNBTComponent.selector());
        return this.mergeStyleAndOptionallyDeepRender(entityNBTComponent, builder, c);
    }

    @Override
    @NotNull
    protected Component renderStorageNbt(@NotNull StorageNBTComponent storageNBTComponent, @NotNull C c) {
        StorageNBTComponent.Builder builder = this.nbt(c, Component.storageNBT(), storageNBTComponent).storage(storageNBTComponent.storage());
        return this.mergeStyleAndOptionallyDeepRender(storageNBTComponent, builder, c);
    }

    protected <O extends NBTComponent<O, B>, B extends NBTComponentBuilder<O, B>> B nbt(@NotNull C c, B b, O o) {
        b.nbtPath(o.nbtPath()).interpret(o.interpret());
        @Nullable Component component = o.separator();
        if (component != null) {
            b.separator(this.render(component, c));
        }
        return b;
    }

    @Override
    @NotNull
    protected Component renderKeybind(@NotNull KeybindComponent keybindComponent, @NotNull C c) {
        KeybindComponent.Builder builder = Component.keybind().keybind(keybindComponent.keybind());
        return this.mergeStyleAndOptionallyDeepRender(keybindComponent, builder, c);
    }

    @Override
    @NotNull
    protected Component renderScore(@NotNull ScoreComponent scoreComponent, @NotNull C c) {
        ScoreComponent.Builder builder = Component.score().name(scoreComponent.name()).objective(scoreComponent.objective()).value(scoreComponent.value());
        return this.mergeStyleAndOptionallyDeepRender(scoreComponent, builder, c);
    }

    @Override
    @NotNull
    protected Component renderSelector(@NotNull SelectorComponent selectorComponent, @NotNull C c) {
        SelectorComponent.Builder builder = Component.selector().pattern(selectorComponent.pattern());
        return this.mergeStyleAndOptionallyDeepRender(selectorComponent, builder, c);
    }

    @Override
    @NotNull
    protected Component renderText(@NotNull TextComponent textComponent, @NotNull C c) {
        TextComponent.Builder builder = Component.text().content(textComponent.content());
        return this.mergeStyleAndOptionallyDeepRender(textComponent, builder, c);
    }

    @Override
    @NotNull
    protected Component renderTranslatable(@NotNull TranslatableComponent translatableComponent, @NotNull C c) {
        @Nullable MessageFormat messageFormat = this.translate(translatableComponent.key(), translatableComponent.fallback(), c);
        if (messageFormat == null) {
            TranslatableComponent.Builder builder = Component.translatable().key(translatableComponent.key()).fallback(translatableComponent.fallback());
            if (!translatableComponent.arguments().isEmpty()) {
                ArrayList<TranslationArgument> arrayList = new ArrayList<TranslationArgument>(translatableComponent.arguments());
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    TranslationArgument translationArgument = (TranslationArgument)arrayList.get(i);
                    if (!(translationArgument.value() instanceof Component)) continue;
                    arrayList.set(i, TranslationArgument.component(this.render((Component)translationArgument.value(), c)));
                }
                builder.arguments(arrayList);
            }
            return this.mergeStyleAndOptionallyDeepRender(translatableComponent, builder, c);
        }
        List<TranslationArgument> list = translatableComponent.arguments();
        TextComponent.Builder builder = Component.text();
        this.mergeStyle(translatableComponent, builder, c);
        if (list.isEmpty()) {
            builder.content(messageFormat.format(null, new StringBuffer(), null).toString());
            return this.optionallyRenderChildrenAppendAndBuild(translatableComponent.children(), builder, c);
        }
        Object[] objectArray = new Object[list.size()];
        StringBuffer stringBuffer = messageFormat.format(objectArray, new StringBuffer(), (FieldPosition)null);
        AttributedCharacterIterator attributedCharacterIterator = messageFormat.formatToCharacterIterator(objectArray);
        while (attributedCharacterIterator.getIndex() < attributedCharacterIterator.getEndIndex()) {
            int n = attributedCharacterIterator.getRunLimit();
            Integer n2 = (Integer)attributedCharacterIterator.getAttribute(MessageFormat.Field.ARGUMENT);
            if (n2 != null) {
                TranslationArgument translationArgument = list.get(n2);
                if (translationArgument.value() instanceof Component) {
                    builder.append(this.render(translationArgument.asComponent(), c));
                } else {
                    builder.append(translationArgument.asComponent());
                }
            } else {
                builder.append((Component)Component.text(stringBuffer.substring(attributedCharacterIterator.getIndex(), n)));
            }
            attributedCharacterIterator.setIndex(n);
        }
        return this.optionallyRenderChildrenAppendAndBuild(translatableComponent.children(), builder, c);
    }

    protected <O extends BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O mergeStyleAndOptionallyDeepRender(Component component, B b, C c) {
        this.mergeStyle(component, b, c);
        return this.optionallyRenderChildrenAppendAndBuild(component.children(), b, c);
    }

    protected <O extends BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O optionallyRenderChildrenAppendAndBuild(List<Component> list, B b, C c) {
        if (!list.isEmpty()) {
            list.forEach(component -> b.append(this.render((Component)component, c)));
        }
        return (O)b.build();
    }

    protected <B extends ComponentBuilder<?, ?>> void mergeStyle(Component component, B b, C c) {
        b.mergeStyle(component, MERGES);
        b.clickEvent(component.clickEvent());
        @Nullable HoverEvent<?> hoverEvent = component.hoverEvent();
        if (hoverEvent != null) {
            b.hoverEvent(hoverEvent.withRenderedValue(this, c));
        }
    }
}

