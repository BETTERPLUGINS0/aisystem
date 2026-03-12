package ch.jalu.configme.resource;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.ToIntFunction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlFileResourceOptions {
   @NotNull
   private final Charset charset;
   @Nullable
   private final ToIntFunction<PropertyPathTraverser.PathElement> numberOfLinesBeforeFunction;
   private final int indentationSize;
   private final boolean splitDotPaths;

   protected YamlFileResourceOptions(@Nullable Charset charset, @Nullable ToIntFunction<PropertyPathTraverser.PathElement> numberOfLinesBeforeFunction, int indentationSize, boolean splitDotPaths) {
      this.charset = charset == null ? StandardCharsets.UTF_8 : charset;
      this.numberOfLinesBeforeFunction = numberOfLinesBeforeFunction;
      this.indentationSize = indentationSize;
      this.splitDotPaths = splitDotPaths;
   }

   @NotNull
   public static YamlFileResourceOptions.Builder builder() {
      return new YamlFileResourceOptions.Builder();
   }

   @NotNull
   public Charset getCharset() {
      return this.charset;
   }

   public int getNumberOfEmptyLinesBefore(@NotNull PropertyPathTraverser.PathElement pathElement) {
      return this.numberOfLinesBeforeFunction == null ? 0 : this.numberOfLinesBeforeFunction.applyAsInt(pathElement);
   }

   public int getIndentationSize() {
      return this.indentationSize;
   }

   public boolean splitDotPaths() {
      return this.splitDotPaths;
   }

   @Nullable
   protected final ToIntFunction<PropertyPathTraverser.PathElement> getIndentFunction() {
      return this.numberOfLinesBeforeFunction;
   }

   public static class Builder {
      private Charset charset;
      private ToIntFunction<PropertyPathTraverser.PathElement> numberOfLinesBeforeFunction;
      private int indentationSize = 4;
      private boolean splitDotPaths = true;

      @NotNull
      public YamlFileResourceOptions.Builder charset(Charset charset) {
         this.charset = charset;
         return this;
      }

      @NotNull
      public YamlFileResourceOptions.Builder numberOfLinesBeforeFunction(@NotNull ToIntFunction<PropertyPathTraverser.PathElement> numberOfLinesBeforeFunction) {
         this.numberOfLinesBeforeFunction = numberOfLinesBeforeFunction;
         return this;
      }

      @NotNull
      public YamlFileResourceOptions.Builder indentationSize(int indentationSize) {
         this.indentationSize = indentationSize;
         return this;
      }

      public YamlFileResourceOptions.Builder splitDotPaths(boolean splitDotPaths) {
         this.splitDotPaths = splitDotPaths;
         return this;
      }

      @NotNull
      public YamlFileResourceOptions build() {
         return new YamlFileResourceOptions(this.charset, this.numberOfLinesBeforeFunction, this.indentationSize, this.splitDotPaths);
      }
   }
}
