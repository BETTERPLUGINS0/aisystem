package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Objects;

public final class TagStringIO {
   private static final TagStringIO INSTANCE = new TagStringIO(new TagStringIO.Builder());
   private final boolean acceptLegacy;
   private final boolean emitLegacy;
   private final boolean acceptHeterogeneousLists;
   private final boolean emitHeterogeneousLists;
   private final String indent;

   /** @deprecated */
   @Deprecated
   @NotNull
   public static TagStringIO get() {
      return tagStringIO();
   }

   @NotNull
   public static TagStringIO tagStringIO() {
      return INSTANCE;
   }

   @NotNull
   public static TagStringIO.Builder builder() {
      return new TagStringIO.Builder();
   }

   private TagStringIO(@NotNull final TagStringIO.Builder builder) {
      this.acceptLegacy = builder.acceptLegacy;
      this.emitLegacy = builder.emitLegacy;
      this.acceptHeterogeneousLists = builder.acceptHeterogeneousLists;
      this.emitHeterogeneousLists = builder.emitHeterogeneousLists;
      this.indent = builder.indent;
   }

   @NotNull
   public CompoundBinaryTag asCompound(@NotNull final String input) throws IOException {
      Objects.requireNonNull(input, "input");

      try {
         CharBuffer buffer = new CharBuffer(input);
         TagStringReader parser = new TagStringReader(buffer);
         parser.legacy(this.acceptLegacy);
         parser.heterogeneousLists(this.acceptHeterogeneousLists);
         CompoundBinaryTag tag = parser.compound();
         if (buffer.skipWhitespace().hasMore()) {
            throw new IOException("Document had trailing content after first CompoundTag");
         } else {
            return tag;
         }
      } catch (StringTagParseException var5) {
         throw new IOException(var5);
      }
   }

   @NotNull
   public BinaryTag asTag(@NotNull final String input) throws IOException {
      Objects.requireNonNull(input, "input");

      try {
         CharBuffer buffer = new CharBuffer(input);
         TagStringReader parser = new TagStringReader(buffer);
         parser.legacy(this.acceptLegacy);
         parser.heterogeneousLists(this.acceptHeterogeneousLists);
         BinaryTag tag = parser.tag();
         if (buffer.skipWhitespace().hasMore()) {
            throw new IOException("Document had trailing content after first Tag");
         } else {
            return tag;
         }
      } catch (StringTagParseException var5) {
         throw new IOException(var5);
      }
   }

   @NotNull
   public CompoundBinaryTag asCompound(@NotNull final String input, @NotNull final Appendable remainder) throws IOException {
      Objects.requireNonNull(input, "input");
      Objects.requireNonNull(remainder, "remainder");

      try {
         CharBuffer buffer = new CharBuffer(input);
         TagStringReader parser = new TagStringReader(buffer);
         parser.legacy(this.acceptLegacy);
         parser.heterogeneousLists(this.acceptHeterogeneousLists);
         CompoundBinaryTag tag = parser.compound();
         remainder.append(buffer.takeRest());
         return tag;
      } catch (StringTagParseException var6) {
         throw new IOException(var6);
      }
   }

   @NotNull
   public BinaryTag asTag(@NotNull final String input, @NotNull final Appendable remainder) throws IOException {
      Objects.requireNonNull(input, "input");
      Objects.requireNonNull(remainder, "remainder");

      try {
         CharBuffer buffer = new CharBuffer(input);
         TagStringReader parser = new TagStringReader(buffer);
         parser.legacy(this.acceptLegacy);
         parser.heterogeneousLists(this.acceptHeterogeneousLists);
         BinaryTag tag = parser.tag();
         remainder.append(buffer.takeRest());
         return tag;
      } catch (StringTagParseException var6) {
         throw new IOException(var6);
      }
   }

   @NotNull
   public String asString(@NotNull final CompoundBinaryTag input) throws IOException {
      return this.asString((BinaryTag)input);
   }

   @NotNull
   public String asString(@NotNull final BinaryTag input) throws IOException {
      Objects.requireNonNull(input, "input");
      StringBuilder sb = new StringBuilder();
      TagStringWriter emit = new TagStringWriter(sb, this.indent);

      try {
         emit.legacy(this.emitLegacy);
         emit.heterogeneousLists(this.emitHeterogeneousLists);
         emit.writeTag(input);
      } catch (Throwable var7) {
         try {
            emit.close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      emit.close();
      return sb.toString();
   }

   public void toWriter(@NotNull final CompoundBinaryTag input, @NotNull final Writer dest) throws IOException {
      this.toWriter((BinaryTag)input, dest);
   }

   public void toWriter(@NotNull final BinaryTag input, @NotNull final Writer dest) throws IOException {
      Objects.requireNonNull(input, "input");
      Objects.requireNonNull(dest, "dest");
      TagStringWriter emit = new TagStringWriter(dest, this.indent);

      try {
         emit.legacy(this.emitLegacy);
         emit.heterogeneousLists(this.emitHeterogeneousLists);
         emit.writeTag(input);
      } catch (Throwable var7) {
         try {
            emit.close();
         } catch (Throwable var6) {
            var7.addSuppressed(var6);
         }

         throw var7;
      }

      emit.close();
   }

   // $FF: synthetic method
   TagStringIO(TagStringIO.Builder x0, Object x1) {
      this(x0);
   }

   public static class Builder {
      private boolean acceptLegacy = true;
      private boolean emitLegacy = false;
      private boolean acceptHeterogeneousLists = false;
      private boolean emitHeterogeneousLists = false;
      private String indent = "";

      Builder() {
      }

      @NotNull
      public TagStringIO.Builder indent(final int spaces) {
         if (spaces == 0) {
            this.indent = "";
         } else if (!this.indent.isEmpty() && this.indent.charAt(0) != ' ' || spaces != this.indent.length()) {
            char[] indent = new char[spaces];
            Arrays.fill(indent, ' ');
            this.indent = String.copyValueOf(indent);
         }

         return this;
      }

      @NotNull
      public TagStringIO.Builder indentTab(final int tabs) {
         if (tabs == 0) {
            this.indent = "";
         } else if (!this.indent.isEmpty() && this.indent.charAt(0) != '\t' || tabs != this.indent.length()) {
            char[] indent = new char[tabs];
            Arrays.fill(indent, '\t');
            this.indent = String.copyValueOf(indent);
         }

         return this;
      }

      @NotNull
      public TagStringIO.Builder acceptLegacy(final boolean legacy) {
         this.acceptLegacy = legacy;
         return this;
      }

      @NotNull
      public TagStringIO.Builder emitLegacy(final boolean legacy) {
         this.emitLegacy = legacy;
         return this;
      }

      @NotNull
      public TagStringIO.Builder acceptHeterogeneousLists(final boolean heterogeneous) {
         this.acceptHeterogeneousLists = heterogeneous;
         return this;
      }

      @NotNull
      public TagStringIO.Builder emitHeterogeneousLists(final boolean heterogeneous) {
         this.emitHeterogeneousLists = heterogeneous;
         return this;
      }

      @NotNull
      public TagStringIO build() {
         return new TagStringIO(this);
      }
   }
}
