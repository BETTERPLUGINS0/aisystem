package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import java.util.Arrays;

@ApiStatus.Internal
public class ParsingExceptionImpl extends ParsingException {
   private static final long serialVersionUID = 2507190809441787202L;
   private final String originalText;
   @NotNull
   private Token[] tokens;

   public ParsingExceptionImpl(final String message, @Nullable final String originalText, @NotNull final Token... tokens) {
      super(message, (Throwable)null, true, false);
      this.tokens = tokens;
      this.originalText = originalText;
   }

   public ParsingExceptionImpl(final String message, @Nullable final String originalText, @Nullable final Throwable cause, final boolean withStackTrace, @NotNull final Token... tokens) {
      super(message, cause, true, withStackTrace);
      this.tokens = tokens;
      this.originalText = originalText;
   }

   public String getMessage() {
      String arrowInfo = this.tokens().length != 0 ? "\n\t" + this.arrow() : "";
      String messageInfo = this.originalText() != null ? "\n\t" + this.originalText() + arrowInfo : "";
      return super.getMessage() + messageInfo;
   }

   @Nullable
   public String detailMessage() {
      return super.getMessage();
   }

   @Nullable
   public String originalText() {
      return this.originalText;
   }

   @NotNull
   public Token[] tokens() {
      return this.tokens;
   }

   public void tokens(@NotNull final Token[] tokens) {
      this.tokens = tokens;
   }

   private String arrow() {
      Token[] ts = this.tokens();
      char[] chars = new char[ts[ts.length - 1].endIndex()];
      int i = 0;
      Token[] var4 = ts;
      int var5 = ts.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Token t = var4[var6];
         Arrays.fill(chars, i, t.startIndex(), ' ');
         chars[t.startIndex()] = '^';
         if (Math.abs(t.startIndex() - t.endIndex()) > 1) {
            Arrays.fill(chars, t.startIndex() + 1, t.endIndex() - 1, '~');
         }

         chars[t.endIndex() - 1] = '^';
         i = t.endIndex();
      }

      return new String(chars);
   }

   public int startIndex() {
      return this.tokens.length == 0 ? -1 : this.tokens[0].startIndex();
   }

   public int endIndex() {
      return this.tokens.length == 0 ? -1 : this.tokens[this.tokens.length - 1].endIndex();
   }
}
