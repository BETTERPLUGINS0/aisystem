package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tree.Node;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementNode implements Node {
   @Nullable
   private final ElementNode parent;
   @Nullable
   private final Token token;
   private final String sourceMessage;
   private final List<ElementNode> children = new ArrayList();

   ElementNode(@Nullable final ElementNode parent, @Nullable final Token token, @NotNull final String sourceMessage) {
      this.parent = parent;
      this.token = token;
      this.sourceMessage = sourceMessage;
   }

   @Nullable
   public ElementNode parent() {
      return this.parent;
   }

   @Nullable
   public Token token() {
      return this.token;
   }

   @NotNull
   public String sourceMessage() {
      return this.sourceMessage;
   }

   @NotNull
   public List<ElementNode> children() {
      return Collections.unmodifiableList(this.children);
   }

   @NotNull
   public List<ElementNode> unsafeChildren() {
      return this.children;
   }

   public void addChild(@NotNull final ElementNode childNode) {
      int last = this.children.size() - 1;
      if (childNode instanceof TextNode && !this.children.isEmpty() && this.children.get(last) instanceof TextNode) {
         TextNode lastNode = (TextNode)this.children.remove(last);
         if (lastNode.token().endIndex() == childNode.token().startIndex()) {
            Token replace = new Token(lastNode.token().startIndex(), childNode.token().endIndex(), TokenType.TEXT);
            this.children.add(new TextNode(this, replace, lastNode.sourceMessage()));
         } else {
            this.children.add(lastNode);
            this.children.add(childNode);
         }
      } else {
         this.children.add(childNode);
      }

   }

   @NotNull
   public StringBuilder buildToString(@NotNull final StringBuilder sb, final int indent) {
      char[] in = this.ident(indent);
      sb.append(in).append("Node {\n");
      Iterator var4 = this.children.iterator();

      while(var4.hasNext()) {
         ElementNode child = (ElementNode)var4.next();
         child.buildToString(sb, indent + 1);
      }

      sb.append(in).append("}\n");
      return sb;
   }

   @NotNull
   char[] ident(final int indent) {
      char[] c = new char[indent * 2];
      Arrays.fill(c, ' ');
      return c;
   }

   @NotNull
   public String toString() {
      return this.buildToString(new StringBuilder(), 0).toString();
   }
}
