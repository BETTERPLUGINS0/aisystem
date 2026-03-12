package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public final class TagNode extends ElementNode {
   private final List<TagPart> parts;
   @Nullable
   private Tag tag = null;

   public TagNode(@NotNull final ElementNode parent, @NotNull final Token token, @NotNull final String sourceMessage, @NotNull final TokenParser.TagProvider tagProvider) {
      super(parent, token, sourceMessage);
      this.parts = genParts(token, sourceMessage, tagProvider);
      if (this.parts.isEmpty()) {
         throw new ParsingExceptionImpl("Tag has no parts? " + this, this.sourceMessage(), new Token[]{this.token()});
      }
   }

   @NotNull
   private static List<TagPart> genParts(@NotNull final Token token, @NotNull final String sourceMessage, @NotNull final TokenParser.TagProvider tagProvider) {
      ArrayList<TagPart> parts = new ArrayList();
      if (token.childTokens() != null) {
         Iterator var4 = token.childTokens().iterator();

         while(var4.hasNext()) {
            Token childToken = (Token)var4.next();
            parts.add(new TagPart(sourceMessage, childToken, tagProvider));
         }
      }

      return parts;
   }

   @NotNull
   public List<TagPart> parts() {
      return this.parts;
   }

   @NotNull
   public String name() {
      return ((TagPart)this.parts.get(0)).value();
   }

   @NotNull
   public Token token() {
      return (Token)Objects.requireNonNull(super.token(), "token is not set");
   }

   @NotNull
   public Tag tag() {
      return (Tag)Objects.requireNonNull(this.tag, "no tag set");
   }

   public void tag(@NotNull final Tag tag) {
      this.tag = tag;
   }

   @NotNull
   public StringBuilder buildToString(@NotNull final StringBuilder sb, final int indent) {
      char[] in = this.ident(indent);
      sb.append(in).append("TagNode(");
      int size = this.parts.size();

      for(int i = 0; i < size; ++i) {
         TagPart part = (TagPart)this.parts.get(i);
         sb.append('\'').append(part.value()).append('\'');
         if (i != size - 1) {
            sb.append(", ");
         }
      }

      sb.append(") {\n");
      Iterator var7 = this.children().iterator();

      while(var7.hasNext()) {
         ElementNode child = (ElementNode)var7.next();
         child.buildToString(sb, indent + 1);
      }

      sb.append(in).append("}\n");
      return sb;
   }
}
