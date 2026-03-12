package fr.xephi.authme.libs.net.kyori.adventure.text;

import fr.xephi.authme.libs.net.kyori.adventure.text.event.HoverEvent;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
@ApiStatus.NonExtendable
public interface ComponentIteratorType {
   ComponentIteratorType DEPTH_FIRST = (component, deque, flags) -> {
      List children;
      int i;
      if (flags.contains(ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS) && component instanceof TranslatableComponent) {
         TranslatableComponent translatable = (TranslatableComponent)component;
         children = translatable.arguments();

         for(i = children.size() - 1; i >= 0; --i) {
            deque.addFirst(((ComponentLike)children.get(i)).asComponent());
         }
      }

      HoverEvent<?> hoverEvent = component.hoverEvent();
      if (hoverEvent != null) {
         HoverEvent.Action<?> action = hoverEvent.action();
         if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME) && action == HoverEvent.Action.SHOW_ENTITY) {
            deque.addFirst(((HoverEvent.ShowEntity)hoverEvent.value()).name());
         } else if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_TEXT_COMPONENT) && action == HoverEvent.Action.SHOW_TEXT) {
            deque.addFirst((Component)hoverEvent.value());
         }
      }

      children = component.children();

      for(i = children.size() - 1; i >= 0; --i) {
         deque.addFirst((Component)children.get(i));
      }

   };
   ComponentIteratorType BREADTH_FIRST = (component, deque, flags) -> {
      if (flags.contains(ComponentIteratorFlag.INCLUDE_TRANSLATABLE_COMPONENT_ARGUMENTS) && component instanceof TranslatableComponent) {
         Iterator var3 = ((TranslatableComponent)component).arguments().iterator();

         while(var3.hasNext()) {
            TranslationArgument argument = (TranslationArgument)var3.next();
            deque.add(argument.asComponent());
         }
      }

      HoverEvent<?> hoverEvent = component.hoverEvent();
      if (hoverEvent != null) {
         HoverEvent.Action<?> action = hoverEvent.action();
         if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_ENTITY_NAME) && action == HoverEvent.Action.SHOW_ENTITY) {
            deque.addLast(((HoverEvent.ShowEntity)hoverEvent.value()).name());
         } else if (flags.contains(ComponentIteratorFlag.INCLUDE_HOVER_SHOW_TEXT_COMPONENT) && action == HoverEvent.Action.SHOW_TEXT) {
            deque.addLast((Component)hoverEvent.value());
         }
      }

      deque.addAll(component.children());
   };

   void populate(@NotNull final Component component, @NotNull final Deque<Component> deque, @NotNull final Set<ComponentIteratorFlag> flags);
}
