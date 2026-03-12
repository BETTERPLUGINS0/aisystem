package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tree.Node;
import org.jetbrains.annotations.NotNull;

public final class RootNode extends ElementNode implements Node.Root {
   private final String beforePreprocessing;

   public RootNode(@NotNull final String sourceMessage, @NotNull final String beforePreprocessing) {
      super((ElementNode)null, (Token)null, sourceMessage);
      this.beforePreprocessing = beforePreprocessing;
   }

   @NotNull
   public String input() {
      return this.beforePreprocessing;
   }
}
