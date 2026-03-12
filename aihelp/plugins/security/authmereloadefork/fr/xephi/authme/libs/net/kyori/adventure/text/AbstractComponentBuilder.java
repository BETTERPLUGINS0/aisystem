package fr.xephi.authme.libs.net.kyori.adventure.text;

import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.ClickEvent;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.HoverEventSource;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextColor;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextDecoration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractComponentBuilder<C extends BuildableComponent<C, B>, B extends ComponentBuilder<C, B>> implements ComponentBuilder<C, B> {
   protected List<Component> children = Collections.emptyList();
   @Nullable
   private Style style;
   @Nullable
   private Style.Builder styleBuilder;

   protected AbstractComponentBuilder() {
   }

   protected AbstractComponentBuilder(@NotNull final C component) {
      List<Component> children = component.children();
      if (!children.isEmpty()) {
         this.children = new ArrayList(children);
      }

      if (component.hasStyling()) {
         this.style = component.style();
      }

   }

   @NotNull
   public B append(@NotNull final Component component) {
      if (component == Component.empty()) {
         return this;
      } else {
         this.prepareChildren();
         this.children.add((Component)Objects.requireNonNull(component, "component"));
         return this;
      }
   }

   @NotNull
   public B append(@NotNull final Component... components) {
      return this.append((ComponentLike[])components);
   }

   @NotNull
   public B append(@NotNull final ComponentLike... components) {
      Objects.requireNonNull(components, "components");
      boolean prepared = false;
      int i = 0;

      for(int length = components.length; i < length; ++i) {
         Component component = ((ComponentLike)Objects.requireNonNull(components[i], "components[?]")).asComponent();
         if (component != Component.empty()) {
            if (!prepared) {
               this.prepareChildren();
               prepared = true;
            }

            this.children.add((Component)Objects.requireNonNull(component, "components[?]"));
         }
      }

      return this;
   }

   @NotNull
   public B append(@NotNull final Iterable<? extends ComponentLike> components) {
      Objects.requireNonNull(components, "components");
      boolean prepared = false;
      Iterator var3 = components.iterator();

      while(var3.hasNext()) {
         ComponentLike like = (ComponentLike)var3.next();
         Component component = ((ComponentLike)Objects.requireNonNull(like, "components[?]")).asComponent();
         if (component != Component.empty()) {
            if (!prepared) {
               this.prepareChildren();
               prepared = true;
            }

            this.children.add((Component)Objects.requireNonNull(component, "components[?]"));
         }
      }

      return this;
   }

   private void prepareChildren() {
      if (this.children == Collections.emptyList()) {
         this.children = new ArrayList();
      }

   }

   @NotNull
   public B applyDeep(@NotNull final Consumer<? super ComponentBuilder<?, ?>> consumer) {
      this.apply(consumer);
      if (this.children == Collections.emptyList()) {
         return this;
      } else {
         ListIterator it = this.children.listIterator();

         while(it.hasNext()) {
            Component child = (Component)it.next();
            if (child instanceof BuildableComponent) {
               ComponentBuilder<?, ?> childBuilder = ((BuildableComponent)child).toBuilder();
               childBuilder.applyDeep(consumer);
               it.set(childBuilder.build());
            }
         }

         return this;
      }
   }

   @NotNull
   public B mapChildren(@NotNull final Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
      if (this.children == Collections.emptyList()) {
         return this;
      } else {
         ListIterator it = this.children.listIterator();

         while(it.hasNext()) {
            Component child = (Component)it.next();
            if (child instanceof BuildableComponent) {
               BuildableComponent<?, ?> mappedChild = (BuildableComponent)Objects.requireNonNull((BuildableComponent)function.apply((BuildableComponent)child), "mappedChild");
               if (child != mappedChild) {
                  it.set(mappedChild);
               }
            }
         }

         return this;
      }
   }

   @NotNull
   public B mapChildrenDeep(@NotNull final Function<BuildableComponent<?, ?>, ? extends BuildableComponent<?, ?>> function) {
      if (this.children == Collections.emptyList()) {
         return this;
      } else {
         ListIterator it = this.children.listIterator();

         while(it.hasNext()) {
            Component child = (Component)it.next();
            if (child instanceof BuildableComponent) {
               BuildableComponent<?, ?> mappedChild = (BuildableComponent)Objects.requireNonNull((BuildableComponent)function.apply((BuildableComponent)child), "mappedChild");
               if (mappedChild.children().isEmpty()) {
                  if (child != mappedChild) {
                     it.set(mappedChild);
                  }
               } else {
                  ComponentBuilder<?, ?> builder = mappedChild.toBuilder();
                  builder.mapChildrenDeep(function);
                  it.set(builder.build());
               }
            }
         }

         return this;
      }
   }

   @NotNull
   public List<Component> children() {
      return Collections.unmodifiableList(this.children);
   }

   @NotNull
   public B style(@NotNull final Style style) {
      this.style = style;
      this.styleBuilder = null;
      return this;
   }

   @NotNull
   public B style(@NotNull final Consumer<Style.Builder> consumer) {
      consumer.accept(this.styleBuilder());
      return this;
   }

   @NotNull
   public B font(@Nullable final Key font) {
      this.styleBuilder().font(font);
      return this;
   }

   @NotNull
   public B color(@Nullable final TextColor color) {
      this.styleBuilder().color(color);
      return this;
   }

   @NotNull
   public B colorIfAbsent(@Nullable final TextColor color) {
      this.styleBuilder().colorIfAbsent(color);
      return this;
   }

   @NotNull
   public B decoration(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state) {
      this.styleBuilder().decoration(decoration, state);
      return this;
   }

   @NotNull
   public B decorationIfAbsent(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state) {
      this.styleBuilder().decorationIfAbsent(decoration, state);
      return this;
   }

   @NotNull
   public B clickEvent(@Nullable final ClickEvent event) {
      this.styleBuilder().clickEvent(event);
      return this;
   }

   @NotNull
   public B hoverEvent(@Nullable final HoverEventSource<?> source) {
      this.styleBuilder().hoverEvent(source);
      return this;
   }

   @NotNull
   public B insertion(@Nullable final String insertion) {
      this.styleBuilder().insertion(insertion);
      return this;
   }

   @NotNull
   public B mergeStyle(@NotNull final Component that, @NotNull final Set<Style.Merge> merges) {
      this.styleBuilder().merge(((Component)Objects.requireNonNull(that, "component")).style(), merges);
      return this;
   }

   @NotNull
   public B resetStyle() {
      this.style = null;
      this.styleBuilder = null;
      return this;
   }

   @NotNull
   private Style.Builder styleBuilder() {
      if (this.styleBuilder == null) {
         if (this.style != null) {
            this.styleBuilder = this.style.toBuilder();
            this.style = null;
         } else {
            this.styleBuilder = Style.style();
         }
      }

      return this.styleBuilder;
   }

   protected final boolean hasStyle() {
      return this.styleBuilder != null || this.style != null;
   }

   @NotNull
   protected Style buildStyle() {
      if (this.styleBuilder != null) {
         return this.styleBuilder.build();
      } else {
         return this.style != null ? this.style : Style.empty();
      }
   }
}
