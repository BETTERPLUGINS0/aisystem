package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.match;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

public abstract class MatchedTokenConsumer<T> {
   protected final String input;
   private int lastIndex = -1;

   public MatchedTokenConsumer(@NotNull final String input) {
      this.input = input;
   }

   @MustBeInvokedByOverriders
   public void accept(final int start, final int end, @NotNull final TokenType tokenType) {
      this.lastIndex = end;
   }

   @UnknownNullability
   public abstract T result();

   public final int lastEndIndex() {
      return this.lastIndex;
   }
}
