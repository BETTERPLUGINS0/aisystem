package fr.xephi.authme.libs.net.kyori.adventure.text.renderer;

import fr.xephi.authme.libs.net.kyori.adventure.text.BlockNBTComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.BuildableComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.ComponentBuilder;
import fr.xephi.authme.libs.net.kyori.adventure.text.EntityNBTComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.KeybindComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.NBTComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.NBTComponentBuilder;
import fr.xephi.authme.libs.net.kyori.adventure.text.ScoreComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.SelectorComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.StorageNBTComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.TextComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.TranslatableComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.TranslationArgument;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.HoverEvent;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.translation.Translator;
import fr.xephi.authme.libs.net.kyori.adventure.util.TriState;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.text.MessageFormat.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class TranslatableComponentRenderer<C> extends AbstractComponentRenderer<C> {
   private static final Set<Style.Merge> MERGES;

   @NotNull
   public static TranslatableComponentRenderer<Locale> usingTranslationSource(@NotNull final Translator source) {
      Objects.requireNonNull(source, "source");
      return new TranslatableComponentRenderer<Locale>() {
         @Nullable
         protected MessageFormat translate(@NotNull final String key, @NotNull final Locale context) {
            return source.translate(key, context);
         }

         @NotNull
         protected Component renderTranslatable(@NotNull final TranslatableComponent component, @NotNull final Locale context) {
            TriState anyTranslations = source.hasAnyTranslations();
            if (anyTranslations != TriState.TRUE && anyTranslations != TriState.NOT_SET) {
               return component;
            } else {
               Component translated = source.translate(component, context);
               return translated != null ? translated : super.renderTranslatable(component, context);
            }
         }
      };
   }

   @Nullable
   protected MessageFormat translate(@NotNull final String key, @NotNull final C context) {
      return null;
   }

   @Nullable
   protected MessageFormat translate(@NotNull final String key, @Nullable final String fallback, @NotNull final C context) {
      return this.translate(key, context);
   }

   @NotNull
   protected Component renderBlockNbt(@NotNull final BlockNBTComponent component, @NotNull final C context) {
      BlockNBTComponent.Builder builder = ((BlockNBTComponent.Builder)this.nbt(context, Component.blockNBT(), component)).pos(component.pos());
      return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
   }

   @NotNull
   protected Component renderEntityNbt(@NotNull final EntityNBTComponent component, @NotNull final C context) {
      EntityNBTComponent.Builder builder = ((EntityNBTComponent.Builder)this.nbt(context, Component.entityNBT(), component)).selector(component.selector());
      return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
   }

   @NotNull
   protected Component renderStorageNbt(@NotNull final StorageNBTComponent component, @NotNull final C context) {
      StorageNBTComponent.Builder builder = ((StorageNBTComponent.Builder)this.nbt(context, Component.storageNBT(), component)).storage(component.storage());
      return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
   }

   protected <O extends NBTComponent<O, B>, B extends NBTComponentBuilder<O, B>> B nbt(@NotNull final C context, final B builder, final O oldComponent) {
      builder.nbtPath(oldComponent.nbtPath()).interpret(oldComponent.interpret());
      Component separator = oldComponent.separator();
      if (separator != null) {
         builder.separator(this.render(separator, context));
      }

      return builder;
   }

   @NotNull
   protected Component renderKeybind(@NotNull final KeybindComponent component, @NotNull final C context) {
      KeybindComponent.Builder builder = Component.keybind().keybind(component.keybind());
      return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
   }

   @NotNull
   protected Component renderScore(@NotNull final ScoreComponent component, @NotNull final C context) {
      ScoreComponent.Builder builder = Component.score().name(component.name()).objective(component.objective()).value(component.value());
      return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
   }

   @NotNull
   protected Component renderSelector(@NotNull final SelectorComponent component, @NotNull final C context) {
      SelectorComponent.Builder builder = Component.selector().pattern(component.pattern());
      return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
   }

   @NotNull
   protected Component renderText(@NotNull final TextComponent component, @NotNull final C context) {
      TextComponent.Builder builder = Component.text().content(component.content());
      return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
   }

   @NotNull
   protected Component renderTranslatable(@NotNull final TranslatableComponent component, @NotNull final C context) {
      MessageFormat format = this.translate(component.key(), component.fallback(), context);
      if (format != null) {
         List<TranslationArgument> args = component.arguments();
         TextComponent.Builder builder = Component.text();
         this.mergeStyle(component, builder, context);
         if (args.isEmpty()) {
            builder.content(format.format((Object[])null, new StringBuffer(), (FieldPosition)null).toString());
            return this.optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
         } else {
            Object[] nulls = new Object[args.size()];
            StringBuffer sb = format.format(nulls, new StringBuffer(), (FieldPosition)null);

            int end;
            for(AttributedCharacterIterator it = format.formatToCharacterIterator(nulls); it.getIndex() < it.getEndIndex(); it.setIndex(end)) {
               end = it.getRunLimit();
               Integer index = (Integer)it.getAttribute(Field.ARGUMENT);
               if (index != null) {
                  TranslationArgument arg = (TranslationArgument)args.get(index);
                  if (arg.value() instanceof Component) {
                     builder.append(this.render(arg.asComponent(), context));
                  } else {
                     builder.append(arg.asComponent());
                  }
               } else {
                  builder.append(Component.text(sb.substring(it.getIndex(), end)));
               }
            }

            return this.optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
         }
      } else {
         TranslatableComponent.Builder builder = Component.translatable().key(component.key()).fallback(component.fallback());
         if (!component.arguments().isEmpty()) {
            List<TranslationArgument> args = new ArrayList(component.arguments());
            int i = 0;

            for(int size = args.size(); i < size; ++i) {
               TranslationArgument arg = (TranslationArgument)args.get(i);
               if (arg.value() instanceof Component) {
                  args.set(i, TranslationArgument.component(this.render((Component)arg.value(), context)));
               }
            }

            builder.arguments((List)args);
         }

         return this.mergeStyleAndOptionallyDeepRender(component, builder, context);
      }
   }

   protected <O extends BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O mergeStyleAndOptionallyDeepRender(final Component component, final B builder, final C context) {
      this.mergeStyle(component, builder, context);
      return this.optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
   }

   protected <O extends BuildableComponent<O, B>, B extends ComponentBuilder<O, B>> O optionallyRenderChildrenAppendAndBuild(final List<Component> children, final B builder, final C context) {
      if (!children.isEmpty()) {
         children.forEach((child) -> {
            builder.append(this.render(child, context));
         });
      }

      return builder.build();
   }

   protected <B extends ComponentBuilder<?, ?>> void mergeStyle(final Component component, final B builder, final C context) {
      builder.mergeStyle(component, MERGES);
      builder.clickEvent(component.clickEvent());
      HoverEvent<?> hoverEvent = component.hoverEvent();
      if (hoverEvent != null) {
         builder.hoverEvent(hoverEvent.withRenderedValue(this, context));
      }

   }

   static {
      MERGES = Style.Merge.merges(Style.Merge.COLOR, Style.Merge.DECORATIONS, Style.Merge.INSERTION, Style.Merge.FONT);
   }
}
