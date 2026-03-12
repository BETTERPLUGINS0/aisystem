package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.jetbrains.annotations.VisibleForTesting;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class ComponentCompaction {
   @VisibleForTesting
   static final boolean SIMPLIFY_STYLE_FOR_BLANK_COMPONENTS = false;

   private ComponentCompaction() {
   }

   static Component compact(@NotNull final Component self, @Nullable final Style parentStyle) {
      List<Component> children = self.children();
      Component optimized = self.children(Collections.emptyList());
      if (parentStyle != null) {
         optimized = ((Component)optimized).style(self.style().unmerge(parentStyle));
      }

      int childrenSize = children.size();
      if (childrenSize == 0) {
         if (isBlank((Component)optimized)) {
            optimized = ((Component)optimized).style(simplifyStyleForBlank(((Component)optimized).style(), parentStyle));
         }

         return (Component)optimized;
      } else {
         if (childrenSize == 1 && isText((Component)optimized)) {
            TextComponent textComponent = (TextComponent)optimized;
            if (textComponent.content().isEmpty()) {
               Component child = (Component)children.get(0);
               return child.style(child.style().merge(((Component)optimized).style(), Style.Merge.Strategy.IF_ABSENT_ON_TARGET)).compact();
            }
         }

         Style childParentStyle = ((Component)optimized).style();
         if (parentStyle != null) {
            childParentStyle = childParentStyle.merge(parentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
         }

         List<Component> childrenToAppend = new ArrayList(children.size());

         int i;
         Component child;
         for(i = 0; i < children.size(); ++i) {
            child = (Component)children.get(i);
            child = compact(child, childParentStyle);
            if (child.children().isEmpty() && isText(child)) {
               TextComponent textComponent = (TextComponent)child;
               if (textComponent.content().isEmpty()) {
                  continue;
               }
            }

            childrenToAppend.add(child);
         }

         if (isText((Component)optimized)) {
            while(!childrenToAppend.isEmpty()) {
               Component child = (Component)childrenToAppend.get(0);
               Style childStyle = child.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
               if (!isText(child) || !Objects.equals(childStyle, childParentStyle)) {
                  break;
               }

               optimized = joinText((TextComponent)optimized, (TextComponent)child);
               childrenToAppend.remove(0);
               childrenToAppend.addAll(0, child.children());
            }
         }

         i = 0;

         while(true) {
            while(i + 1 < childrenToAppend.size()) {
               child = (Component)childrenToAppend.get(i);
               Component neighbor = (Component)childrenToAppend.get(i + 1);
               if (child.children().isEmpty() && isText(child) && isText(neighbor)) {
                  Style childStyle = child.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                  Style neighborStyle = neighbor.style().merge(childParentStyle, Style.Merge.Strategy.IF_ABSENT_ON_TARGET);
                  if (childStyle.equals(neighborStyle)) {
                     Component combined = joinText((TextComponent)child, (TextComponent)neighbor);
                     childrenToAppend.set(i, combined);
                     childrenToAppend.remove(i + 1);
                     continue;
                  }
               }

               ++i;
            }

            if (childrenToAppend.isEmpty() && isBlank((Component)optimized)) {
               optimized = ((Component)optimized).style(simplifyStyleForBlank(((Component)optimized).style(), parentStyle));
            }

            return ((Component)optimized).children(childrenToAppend);
         }
      }
   }

   private static boolean isBlank(final Component component) {
      if (isText(component)) {
         TextComponent textComponent = (TextComponent)component;
         String content = textComponent.content();

         for(int i = 0; i < content.length(); ++i) {
            char c = content.charAt(i);
            if (c != ' ') {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @NotNull
   private static Style simplifyStyleForBlank(@NotNull final Style style, @Nullable final Style parentStyle) {
      return style;
   }

   private static TextComponent joinText(final TextComponent one, final TextComponent two) {
      return TextComponentImpl.create(two.children(), one.style(), one.content() + two.content());
   }

   private static boolean isText(final Component component) {
      return component instanceof TextComponent && !(component instanceof VirtualComponent);
   }
}
