package fr.xephi.authme.libs.net.kyori.adventure.text;

import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import org.jetbrains.annotations.NotNull;

public final class LinearComponents {
   private LinearComponents() {
   }

   @NotNull
   public static Component linear(@NotNull final ComponentBuilderApplicable... applicables) {
      int length = applicables.length;
      if (length == 0) {
         return Component.empty();
      } else if (length == 1) {
         ComponentBuilderApplicable ap0 = applicables[0];
         if (ap0 instanceof ComponentLike) {
            return ((ComponentLike)ap0).asComponent();
         } else {
            throw nothingComponentLike();
         }
      } else {
         TextComponentImpl.BuilderImpl builder = new TextComponentImpl.BuilderImpl();
         Style.Builder style = null;

         int size;
         for(size = 0; size < length; ++size) {
            ComponentBuilderApplicable applicable = applicables[size];
            if (applicable instanceof StyleBuilderApplicable) {
               if (style == null) {
                  style = Style.style();
               }

               style.apply((StyleBuilderApplicable)applicable);
            } else if (style != null && applicable instanceof ComponentLike) {
               builder.applicableApply(((ComponentLike)applicable).asComponent().style(style));
            } else {
               builder.applicableApply(applicable);
            }
         }

         size = builder.children.size();
         if (size == 0) {
            throw nothingComponentLike();
         } else if (size == 1 && !builder.hasStyle()) {
            return (Component)builder.children.get(0);
         } else {
            return builder.build();
         }
      }
   }

   private static IllegalStateException nothingComponentLike() {
      return new IllegalStateException("Cannot build component linearly - nothing component-like was given");
   }
}
