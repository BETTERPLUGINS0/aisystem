package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEventSource;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import ac.grim.grimac.shaded.kyori.adventure.text.renderer.ComponentRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class TextReplacementRenderer implements ComponentRenderer<TextReplacementRenderer.State> {
   static final TextReplacementRenderer INSTANCE = new TextReplacementRenderer();

   private TextReplacementRenderer() {
   }

   @NotNull
   public Component render(@NotNull final Component component, @NotNull final TextReplacementRenderer.State state) {
      if (!state.running) {
         return component;
      } else {
         boolean prevFirstMatch = state.firstMatch;
         state.firstMatch = true;
         List<Component> oldChildren = component.children();
         int oldChildrenSize = oldChildren.size();
         Style oldStyle = component.style();
         List<Component> children = null;
         Component modified = component;
         int replacedUntil;
         if (component instanceof TextComponent) {
            String content = ((TextComponent)component).content();
            Matcher matcher = state.pattern.matcher(content);
            replacedUntil = 0;

            while(matcher.find()) {
               PatternReplacementResult result = state.continuer.shouldReplace(matcher, ++state.matchCount, state.replaceCount);
               if (result != PatternReplacementResult.CONTINUE) {
                  if (result == PatternReplacementResult.STOP) {
                     state.running = false;
                     break;
                  }

                  ComponentLike builder;
                  if (matcher.start() == 0) {
                     if (matcher.end() == content.length()) {
                        builder = (ComponentLike)state.replacement.apply(matcher, (TextComponent.Builder)Component.text().content(matcher.group()).style(component.style()));
                        modified = builder == null ? Component.empty() : builder.asComponent();
                        if (((Component)modified).style().hoverEvent() != null) {
                           oldStyle = oldStyle.hoverEvent((HoverEventSource)null);
                        }

                        modified = ((Component)modified).style(((Component)modified).style().merge(component.style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET));
                        if (children == null) {
                           children = new ArrayList(oldChildrenSize + ((Component)modified).children().size());
                           children.addAll(((Component)modified).children());
                        }
                     } else {
                        modified = Component.text("", component.style());
                        builder = (ComponentLike)state.replacement.apply(matcher, Component.text().content(matcher.group()));
                        if (builder != null) {
                           if (children == null) {
                              children = new ArrayList(oldChildrenSize + 1);
                           }

                           children.add(builder.asComponent());
                        }
                     }
                  } else {
                     if (children == null) {
                        children = new ArrayList(oldChildrenSize + 2);
                     }

                     if (state.firstMatch) {
                        modified = ((TextComponent)component).content(content.substring(0, matcher.start()));
                     } else if (replacedUntil < matcher.start()) {
                        children.add(Component.text(content.substring(replacedUntil, matcher.start())));
                     }

                     builder = (ComponentLike)state.replacement.apply(matcher, Component.text().content(matcher.group()));
                     if (builder != null) {
                        children.add(builder.asComponent());
                     }
                  }

                  ++state.replaceCount;
                  state.firstMatch = false;
                  replacedUntil = matcher.end();
               }
            }

            if (replacedUntil < content.length() && replacedUntil > 0) {
               if (children == null) {
                  children = new ArrayList(oldChildrenSize);
               }

               children.add(Component.text(content.substring(replacedUntil)));
            }
         } else if (component instanceof TranslatableComponent) {
            List<TranslationArgument> args = ((TranslatableComponent)component).arguments();
            List<TranslationArgument> newArgs = null;
            replacedUntil = 0;

            for(int size = args.size(); replacedUntil < size; ++replacedUntil) {
               TranslationArgument original = (TranslationArgument)args.get(replacedUntil);
               TranslationArgument replaced = original.value() instanceof Component ? TranslationArgument.component(this.render((Component)original.value(), state)) : original;
               if (replaced != original && newArgs == null) {
                  newArgs = new ArrayList(size);
                  if (replacedUntil > 0) {
                     newArgs.addAll(args.subList(0, replacedUntil));
                  }
               }

               if (newArgs != null) {
                  newArgs.add(replaced);
               }
            }

            if (newArgs != null) {
               modified = ((TranslatableComponent)component).arguments((List)newArgs);
            }
         }

         if (state.running) {
            if (state.replaceInsideHoverEvents) {
               HoverEvent<?> event = oldStyle.hoverEvent();
               if (event != null) {
                  HoverEvent<?> rendered = event.withRenderedValue(this, state);
                  if (event != rendered) {
                     modified = ((Component)modified).style((s) -> {
                        s.hoverEvent(rendered);
                     });
                  }
               }
            }

            boolean first = true;

            for(int i = 0; i < oldChildrenSize; ++i) {
               Component child = (Component)oldChildren.get(i);
               Component replaced = this.render(child, state);
               if (replaced != child) {
                  if (children == null) {
                     children = new ArrayList(oldChildrenSize);
                  }

                  if (first) {
                     children.addAll(oldChildren.subList(0, i));
                  }

                  first = false;
               }

               if (children != null) {
                  children.add(replaced);
                  first = false;
               }
            }
         } else if (children != null) {
            children.addAll(oldChildren);
         }

         state.firstMatch = prevFirstMatch;
         return (Component)(children != null ? ((Component)modified).children(children) : modified);
      }
   }

   static final class State {
      final Pattern pattern;
      final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement;
      final TextReplacementConfig.Condition continuer;
      final boolean replaceInsideHoverEvents;
      boolean running = true;
      int matchCount = 0;
      int replaceCount = 0;
      boolean firstMatch = true;

      State(@NotNull final Pattern pattern, @NotNull final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement, @NotNull final TextReplacementConfig.Condition continuer, final boolean replaceInsideHoverEvents) {
         this.pattern = pattern;
         this.replacement = replacement;
         this.continuer = continuer;
         this.replaceInsideHoverEvents = replaceInsideHoverEvents;
      }
   }
}
