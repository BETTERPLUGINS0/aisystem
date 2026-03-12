package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.TextComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.QuotingOverride;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class MiniMessageSerializer {
   private MiniMessageSerializer() {
   }

   @NotNull
   static String serialize(@NotNull final Component component, @NotNull final SerializableResolver resolver, final boolean strict) {
      StringBuilder sb = new StringBuilder();
      MiniMessageSerializer.Collector emitter = new MiniMessageSerializer.Collector(resolver, strict, sb);
      emitter.mark();
      visit(component, emitter, resolver, true);
      if (strict) {
         emitter.popAll();
      } else {
         emitter.completeTag();
      }

      return sb.toString();
   }

   private static void visit(@NotNull final Component component, final MiniMessageSerializer.Collector emitter, final SerializableResolver resolver, final boolean lastChild) {
      resolver.handle(component, emitter);
      emitter.flushClaims(component);
      Iterator it = component.children().iterator();

      while(it.hasNext()) {
         emitter.mark();
         visit((Component)it.next(), emitter, resolver, lastChild && !it.hasNext());
      }

      if (!lastChild) {
         emitter.popToMark();
      }

   }

   static final class Collector implements TokenEmitter, ClaimConsumer {
      private static final String MARK = "__<'\"\\MARK__";
      private static final char[] TEXT_ESCAPES = new char[]{'\\', '<'};
      private static final char[] TAG_TOKENS = new char[]{'>', ':'};
      private static final char[] SINGLE_QUOTED_ESCAPES = new char[]{'\\', '\''};
      private static final char[] DOUBLE_QUOTED_ESCAPES = new char[]{'\\', '"'};
      private final SerializableResolver resolver;
      private final boolean strict;
      private final StringBuilder consumer;
      private String[] activeTags = new String[4];
      private int tagLevel = 0;
      private MiniMessageSerializer.Collector.TagState tagState;
      @Nullable
      Emitable componentClaim;
      final Set<String> claimedStyleElements;

      Collector(final SerializableResolver resolver, final boolean strict, final StringBuilder consumer) {
         this.tagState = MiniMessageSerializer.Collector.TagState.TEXT;
         this.claimedStyleElements = new HashSet();
         this.resolver = resolver;
         this.strict = strict;
         this.consumer = consumer;
      }

      private void pushActiveTag(final String tag) {
         if (this.tagLevel >= this.activeTags.length) {
            this.activeTags = (String[])Arrays.copyOf(this.activeTags, this.activeTags.length * 2);
         }

         this.activeTags[this.tagLevel++] = tag;
      }

      private String popTag(final boolean allowMarks) {
         if (this.tagLevel-- <= 0) {
            throw new IllegalStateException("Unbalanced tags, tried to pop below depth");
         } else {
            String tag = this.activeTags[this.tagLevel];
            if (!allowMarks && tag == "__<'\"\\MARK__") {
               throw new IllegalStateException("Tried to pop past mark, tag stack: " + Arrays.toString(this.activeTags) + " @ " + this.tagLevel);
            } else {
               return tag;
            }
         }
      }

      void mark() {
         this.pushActiveTag("__<'\"\\MARK__");
      }

      void popToMark() {
         if (this.tagLevel != 0) {
            String tag;
            while((tag = this.popTag(true)) != "__<'\"\\MARK__") {
               this.emitClose(tag);
            }

         }
      }

      void popAll() {
         while(this.tagLevel > 0) {
            String tag = this.activeTags[--this.tagLevel];
            if (tag != "__<'\"\\MARK__") {
               this.emitClose(tag);
            }
         }

      }

      void completeTag() {
         if (this.tagState.isTag) {
            this.consumer.append('>');
            this.tagState = MiniMessageSerializer.Collector.TagState.TEXT;
         }

      }

      @NotNull
      public MiniMessageSerializer.Collector tag(@NotNull final String token) {
         this.completeTag();
         this.consumer.append('<');
         this.escapeTagContent(token, QuotingOverride.UNQUOTED);
         this.tagState = MiniMessageSerializer.Collector.TagState.MID;
         this.pushActiveTag(token);
         return this;
      }

      @NotNull
      public TokenEmitter selfClosingTag(@NotNull final String token) {
         this.completeTag();
         this.consumer.append('<');
         this.escapeTagContent(token, QuotingOverride.UNQUOTED);
         this.tagState = MiniMessageSerializer.Collector.TagState.MID_SELF_CLOSING;
         return this;
      }

      @NotNull
      public TokenEmitter argument(@NotNull final String arg) {
         if (!this.tagState.isTag) {
            throw new IllegalStateException("Not within a tag!");
         } else {
            this.consumer.append(':');
            this.escapeTagContent(arg, (QuotingOverride)null);
            return this;
         }
      }

      @NotNull
      public TokenEmitter argument(@NotNull final String arg, @NotNull final QuotingOverride quotingPreference) {
         if (!this.tagState.isTag) {
            throw new IllegalStateException("Not within a tag!");
         } else {
            this.consumer.append(':');
            this.escapeTagContent(arg, (QuotingOverride)Objects.requireNonNull(quotingPreference, "quotingPreference"));
            return this;
         }
      }

      @NotNull
      public TokenEmitter argument(@NotNull final Component arg) {
         String serialized = MiniMessageSerializer.serialize(arg, this.resolver, this.strict);
         return this.argument(serialized, QuotingOverride.QUOTED);
      }

      @NotNull
      public MiniMessageSerializer.Collector text(@NotNull final String text) {
         this.completeTag();
         appendEscaping(this.consumer, text, TEXT_ESCAPES, true);
         return this;
      }

      private void escapeTagContent(final String content, @Nullable final QuotingOverride preference) {
         boolean mustBeQuoted = preference == QuotingOverride.QUOTED;
         boolean hasSingleQuote = false;
         boolean hasDoubleQuote = false;

         for(int i = 0; i < content.length(); ++i) {
            char active = content.charAt(i);
            if (active != '>' && active != ':' && active != ' ') {
               if (active == '\'') {
                  hasSingleQuote = true;
                  break;
               }

               if (active == '"') {
                  hasDoubleQuote = true;
                  if (mustBeQuoted && hasSingleQuote) {
                     break;
                  }
               }
            } else {
               mustBeQuoted = true;
               if (hasSingleQuote && hasDoubleQuote) {
                  break;
               }
            }
         }

         if (hasSingleQuote) {
            this.consumer.append('"');
            appendEscaping(this.consumer, content, DOUBLE_QUOTED_ESCAPES, true);
            this.consumer.append('"');
         } else if (!hasDoubleQuote && !mustBeQuoted) {
            appendEscaping(this.consumer, content, TAG_TOKENS, false);
         } else {
            this.consumer.append('\'');
            appendEscaping(this.consumer, content, SINGLE_QUOTED_ESCAPES, true);
            this.consumer.append('\'');
         }

      }

      static void appendEscaping(final StringBuilder builder, final String text, final char[] escapeChars, final boolean allowEscapes) {
         int startIdx = 0;
         boolean unescapedFound = false;

         for(int i = 0; i < text.length(); ++i) {
            char test = text.charAt(i);
            boolean escaped = false;
            char[] var9 = escapeChars;
            int var10 = escapeChars.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               char c = var9[var11];
               if (test == c) {
                  if (!allowEscapes) {
                     throw new IllegalArgumentException("Invalid escapable character '" + test + "' found at index " + i + " in string '" + text + "'");
                  }

                  escaped = true;
                  break;
               }
            }

            if (escaped) {
               if (unescapedFound) {
                  builder.append(text, startIdx, i);
               }

               startIdx = i + 1;
               builder.append('\\').append(test);
            } else {
               unescapedFound = true;
            }
         }

         if (startIdx < text.length() && unescapedFound) {
            builder.append(text, startIdx, text.length());
         }

      }

      @NotNull
      public MiniMessageSerializer.Collector pop() {
         this.emitClose(this.popTag(false));
         return this;
      }

      private void emitClose(@NotNull final String tag) {
         if (this.tagState.isTag) {
            if (this.tagState == MiniMessageSerializer.Collector.TagState.MID) {
               this.consumer.append('/');
            }

            this.consumer.append('>');
            this.tagState = MiniMessageSerializer.Collector.TagState.TEXT;
         } else {
            this.consumer.append('<').append('/');
            this.escapeTagContent(tag, QuotingOverride.UNQUOTED);
            this.consumer.append('>');
         }

      }

      public void style(@NotNull final String claimKey, @NotNull final Emitable styleClaim) {
         if (this.claimedStyleElements.add((String)Objects.requireNonNull(claimKey, "claimKey"))) {
            styleClaim.emit(this);
         }

      }

      public boolean component(@NotNull final Emitable componentClaim) {
         if (this.componentClaim != null) {
            return false;
         } else {
            this.componentClaim = (Emitable)Objects.requireNonNull(componentClaim, "componentClaim");
            return true;
         }
      }

      public boolean componentClaimed() {
         return this.componentClaim != null;
      }

      public boolean styleClaimed(@NotNull final String claimId) {
         return this.claimedStyleElements.contains(claimId);
      }

      void flushClaims(final Component component) {
         if (this.componentClaim != null) {
            this.componentClaim.emit(this);
            this.componentClaim = null;
         } else {
            if (!(component instanceof TextComponent)) {
               throw new IllegalStateException("Unclaimed component " + component);
            }

            this.text(((TextComponent)component).content());
         }

         this.claimedStyleElements.clear();
      }

      static enum TagState {
         TEXT(false),
         MID(true),
         MID_SELF_CLOSING(true);

         final boolean isTag;

         private TagState(final boolean isTag) {
            this.isTag = isTag;
         }
      }
   }
}
