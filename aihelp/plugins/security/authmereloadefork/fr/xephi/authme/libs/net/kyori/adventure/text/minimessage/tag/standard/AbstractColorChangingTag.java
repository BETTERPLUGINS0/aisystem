package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.standard;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.TextComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.TextColor;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Inserting;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Modifying;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tree.Node;
import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.Collections;
import java.util.PrimitiveIterator.OfInt;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract class AbstractColorChangingTag implements Modifying, Examinable {
   private static final ComponentFlattener LENGTH_CALCULATOR = (ComponentFlattener)ComponentFlattener.builder().mapper(TextComponent.class, TextComponent::content).unknownMapper((x) -> {
      return "_";
   }).build();
   private boolean visited;
   private int size = 0;
   private int disableApplyingColorDepth = -1;

   protected final int size() {
      return this.size;
   }

   public final void visit(@NotNull final Node current, final int depth) {
      if (this.visited) {
         throw new IllegalStateException("Color changing tag instances cannot be re-used, return a new one for each resolve");
      } else {
         if (current instanceof ValueNode) {
            String value = ((ValueNode)current).value();
            this.size += value.codePointCount(0, value.length());
         } else if (current instanceof TagNode) {
            TagNode tag = (TagNode)current;
            if (tag.tag() instanceof Inserting) {
               LENGTH_CALCULATOR.flatten(((Inserting)tag.tag()).value(), (s) -> {
                  this.size += s.codePointCount(0, s.length());
               });
            }
         }

      }
   }

   public final void postVisit() {
      this.visited = true;
      this.init();
   }

   public final Component apply(@NotNull final Component current, final int depth) {
      if ((this.disableApplyingColorDepth == -1 || depth <= this.disableApplyingColorDepth) && current.style().color() == null) {
         this.disableApplyingColorDepth = -1;
         if (current instanceof TextComponent && ((TextComponent)current).content().length() > 0) {
            TextComponent textComponent = (TextComponent)current;
            String content = textComponent.content();
            TextComponent.Builder parent = Component.text();
            int[] holder = new int[1];
            OfInt it = content.codePoints().iterator();

            while(it.hasNext()) {
               holder[0] = it.nextInt();
               Component comp = Component.text(new String(holder, 0, 1), current.style().color(this.color()));
               this.advanceColor();
               parent.append(comp);
            }

            return parent.build();
         } else if (!(current instanceof TextComponent)) {
            Component ret = current.children(Collections.emptyList()).colorIfAbsent(this.color());
            this.advanceColor();
            return ret;
         } else {
            return Component.empty().mergeStyle(current);
         }
      } else {
         if (this.disableApplyingColorDepth == -1 || depth < this.disableApplyingColorDepth) {
            this.disableApplyingColorDepth = depth;
         }

         if (current instanceof TextComponent) {
            String content = ((TextComponent)current).content();
            int len = content.codePointCount(0, content.length());

            for(int i = 0; i < len; ++i) {
               this.advanceColor();
            }
         }

         return current.children(Collections.emptyList());
      }
   }

   protected abstract void init();

   protected abstract void advanceColor();

   protected abstract TextColor color();

   @NotNull
   public abstract Stream<? extends ExaminableProperty> examinableProperties();

   @NotNull
   public final String toString() {
      return Internals.toString(this);
   }

   public abstract boolean equals(@Nullable final Object other);

   public abstract int hashCode();
}
