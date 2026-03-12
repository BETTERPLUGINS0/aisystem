package ac.grim.grimac.shaded.kyori.adventure.text.renderer;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.BlockNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.BuildableComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.EntityNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.KeybindComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.NBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.NBTComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.ScoreComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.SelectorComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.StorageNBTComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.TextComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslationArgument;
import ac.grim.grimac.shaded.kyori.adventure.text.VirtualComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.translation.Translator;
import ac.grim.grimac.shaded.kyori.adventure.util.TriState;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.text.MessageFormat.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

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
         protected Component renderTranslatableInner(@NotNull final TranslatableComponent component, @NotNull final Locale context) {
            TriState anyTranslations = source.hasAnyTranslations();
            if (anyTranslations == TriState.FALSE) {
               return component;
            } else {
               Component translated;
               if (source.canTranslate(component.key(), context)) {
                  translated = source.translate(component, context);
               } else {
                  translated = null;
               }

               return translated != null ? this.render(translated, context) : super.renderTranslatableInner(component, context);
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
   protected Component renderTranslatable(@NotNull TranslatableComponent component, @NotNull final C context) {
      List<TranslationArgument> arguments = component.arguments();
      List<Component> children = component.children();
      if (!arguments.isEmpty() || !children.isEmpty()) {
         TranslatableComponent.Builder builder = (TranslatableComponent.Builder)component.toBuilder();
         if (!arguments.isEmpty()) {
            List<TranslationArgument> translatedArguments = new ArrayList(arguments);

            for(int i = 0; i < translatedArguments.size(); ++i) {
               TranslationArgument arg = (TranslationArgument)translatedArguments.get(i);
               if (arg.value() instanceof Component && !(arg.value() instanceof VirtualComponent)) {
                  translatedArguments.set(i, TranslationArgument.component(this.render((Component)arg.value(), context)));
               }
            }

            builder.arguments((List)translatedArguments);
         }

         component = (TranslatableComponent)builder.build();
      }

      return this.renderTranslatableInner(component, context);
   }

   @NotNull
   protected Component renderTranslatableInner(@NotNull final TranslatableComponent component, @NotNull final C context) {
      MessageFormat format = this.translate(component.key(), component.fallback(), context);
      if (format == null) {
         return this.optionallyRenderChildrenAndStyle(component, context);
      } else {
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
                  builder.append(arg.asComponent());
               } else {
                  builder.append(Component.text(sb.substring(it.getIndex(), end)));
               }
            }

            return this.optionallyRenderChildrenAppendAndBuild(component.children(), builder, context);
         }
      }
   }

   protected Component optionallyRenderChildrenAndStyle(Component component, final C context) {
      HoverEvent<?> hoverEvent = component.hoverEvent();
      if (hoverEvent != null) {
         component = component.hoverEvent(hoverEvent.withRenderedValue(this, context));
      }

      List<Component> children = component.children();
      if (children.isEmpty()) {
         return component;
      } else {
         List<Component> rendered = new ArrayList(children.size());
         children.forEach((child) -> {
            rendered.add(this.render(child, context));
         });
         return component.children(rendered);
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
      Set<Style.Merge> merges = EnumSet.allOf(Style.Merge.class);
      merges.remove(Style.Merge.EVENTS);
      MERGES = Collections.unmodifiableSet(merges);
   }
}
