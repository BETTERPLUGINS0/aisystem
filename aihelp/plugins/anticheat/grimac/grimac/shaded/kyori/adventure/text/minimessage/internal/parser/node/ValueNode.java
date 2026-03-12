package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import java.util.Objects;

public abstract class ValueNode extends ElementNode {
   private final String value;

   ValueNode(@Nullable final ElementNode parent, @Nullable final Token token, @NotNull final String sourceMessage, @NotNull final String value) {
      super(parent, token, sourceMessage);
      this.value = value;
   }

   abstract String valueName();

   @NotNull
   public String value() {
      return this.value;
   }

   @NotNull
   public Token token() {
      return (Token)Objects.requireNonNull(super.token(), "token is not set");
   }

   @NotNull
   public StringBuilder buildToString(@NotNull final StringBuilder sb, final int indent) {
      char[] in = this.ident(indent);
      sb.append(in).append(this.valueName()).append("('").append(this.value).append("')\n");
      return sb;
   }
}
