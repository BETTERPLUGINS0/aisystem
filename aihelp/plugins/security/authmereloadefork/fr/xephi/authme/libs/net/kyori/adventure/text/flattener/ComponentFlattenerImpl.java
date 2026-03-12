package fr.xephi.authme.libs.net.kyori.adventure.text.flattener;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.KeybindComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.ScoreComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.SelectorComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.TextComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.TranslatableComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.util.InheritanceAwareMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class ComponentFlattenerImpl implements ComponentFlattener {
   static final ComponentFlattener BASIC = (ComponentFlattener)(new ComponentFlattenerImpl.BuilderImpl()).mapper(KeybindComponent.class, (component) -> {
      return component.keybind();
   }).mapper(ScoreComponent.class, (component) -> {
      String value = component.value();
      return value != null ? value : "";
   }).mapper(SelectorComponent.class, SelectorComponent::pattern).mapper(TextComponent.class, TextComponent::content).mapper(TranslatableComponent.class, (component) -> {
      String fallback = component.fallback();
      return fallback != null ? fallback : component.key();
   }).build();
   static final ComponentFlattener TEXT_ONLY = (ComponentFlattener)(new ComponentFlattenerImpl.BuilderImpl()).mapper(TextComponent.class, TextComponent::content).build();
   private static final int MAX_DEPTH = 512;
   private final InheritanceAwareMap<Component, ComponentFlattenerImpl.Handler> flatteners;
   private final Function<Component, String> unknownHandler;

   ComponentFlattenerImpl(final InheritanceAwareMap<Component, ComponentFlattenerImpl.Handler> flatteners, @Nullable final Function<Component, String> unknownHandler) {
      this.flatteners = flatteners;
      this.unknownHandler = unknownHandler;
   }

   public void flatten(@NotNull final Component input, @NotNull final FlattenerListener listener) {
      this.flatten0(input, listener, 0);
   }

   private void flatten0(@NotNull final Component input, @NotNull final FlattenerListener listener, final int depth) {
      Objects.requireNonNull(input, "input");
      Objects.requireNonNull(listener, "listener");
      if (input != Component.empty()) {
         if (depth > 512) {
            throw new IllegalStateException("Exceeded maximum depth of 512 while attempting to flatten components!");
         } else {
            ComponentFlattenerImpl.Handler flattener = this.flattener(input);
            Style inputStyle = input.style();
            listener.pushStyle(inputStyle);

            try {
               if (flattener != null) {
                  flattener.handle(this, input, listener, depth + 1);
               }

               if (!input.children().isEmpty() && listener.shouldContinue()) {
                  Iterator var6 = input.children().iterator();

                  while(var6.hasNext()) {
                     Component child = (Component)var6.next();
                     this.flatten0(child, listener, depth + 1);
                  }
               }
            } finally {
               listener.popStyle(inputStyle);
            }

         }
      }
   }

   @Nullable
   private <T extends Component> ComponentFlattenerImpl.Handler flattener(final T test) {
      ComponentFlattenerImpl.Handler flattener = (ComponentFlattenerImpl.Handler)this.flatteners.get(test.getClass());
      return flattener == null && this.unknownHandler != null ? (self, component, listener, depth) -> {
         listener.component((String)this.unknownHandler.apply(component));
      } : flattener;
   }

   @NotNull
   public ComponentFlattener.Builder toBuilder() {
      return new ComponentFlattenerImpl.BuilderImpl(this.flatteners, this.unknownHandler);
   }

   static final class BuilderImpl implements ComponentFlattener.Builder {
      private final InheritanceAwareMap.Builder<Component, ComponentFlattenerImpl.Handler> flatteners;
      @Nullable
      private Function<Component, String> unknownHandler;

      BuilderImpl() {
         this.flatteners = InheritanceAwareMap.builder().strict(true);
      }

      BuilderImpl(final InheritanceAwareMap<Component, ComponentFlattenerImpl.Handler> flatteners, @Nullable final Function<Component, String> unknownHandler) {
         this.flatteners = InheritanceAwareMap.builder(flatteners).strict(true);
         this.unknownHandler = unknownHandler;
      }

      @NotNull
      public ComponentFlattener build() {
         return new ComponentFlattenerImpl((InheritanceAwareMap)this.flatteners.build(), this.unknownHandler);
      }

      @NotNull
      public <T extends Component> ComponentFlattener.Builder mapper(@NotNull final Class<T> type, @NotNull final Function<T, String> converter) {
         this.flatteners.put(type, (self, component, listener, depth) -> {
            listener.component((String)converter.apply(component));
         });
         return this;
      }

      @NotNull
      public <T extends Component> ComponentFlattener.Builder complexMapper(@NotNull final Class<T> type, @NotNull final BiConsumer<T, Consumer<Component>> converter) {
         this.flatteners.put(type, (self, component, listener, depth) -> {
            converter.accept(component, (c) -> {
               self.flatten0(c, listener, depth);
            });
         });
         return this;
      }

      @NotNull
      public ComponentFlattener.Builder unknownMapper(@Nullable final Function<Component, String> converter) {
         this.unknownHandler = converter;
         return this;
      }
   }

   @FunctionalInterface
   interface Handler {
      void handle(final ComponentFlattenerImpl self, final Component input, final FlattenerListener listener, final int depth);
   }
}
