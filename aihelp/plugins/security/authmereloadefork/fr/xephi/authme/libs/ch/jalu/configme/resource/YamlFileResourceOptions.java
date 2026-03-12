package fr.xephi.authme.libs.ch.jalu.configme.resource;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.ToIntFunction;
import javax.annotation.Nullable;

public class YamlFileResourceOptions {
   private final Charset charset;
   private final ToIntFunction<PropertyPathTraverser.PathElement> numberOfLinesBeforeFunction;
   private final int indentationSize;
   private final boolean splitDotPaths;

   protected YamlFileResourceOptions(@Nullable Charset charset, @Nullable ToIntFunction<PropertyPathTraverser.PathElement> numberOfLinesBeforeFunction, int indentationSize, boolean splitDotPaths) {
      this.charset = charset == null ? StandardCharsets.UTF_8 : charset;
      this.numberOfLinesBeforeFunction = numberOfLinesBeforeFunction;
      this.indentationSize = indentationSize;
      this.splitDotPaths = splitDotPaths;
   }

   public static YamlFileResourceOptions.Builder builder() {
      return new YamlFileResourceOptions.Builder();
   }

   public Charset getCharset() {
      return this.charset;
   }

   public int getNumberOfEmptyLinesBefore(PropertyPathTraverser.PathElement pathElement) {
      return this.numberOfLinesBeforeFunction == null ? 0 : this.numberOfLinesBeforeFunction.applyAsInt(pathElement);
   }

   public int getIndentationSize() {
      return this.indentationSize;
   }

   public boolean splitDotPaths() {
      return this.splitDotPaths;
   }

   public String getIndentation() {
      if (this.indentationSize == 4) {
         return "    ";
      } else {
         StringBuilder sb = new StringBuilder(this.indentationSize);

         for(int i = 0; i < this.indentationSize; ++i) {
            sb.append(" ");
         }

         return sb.toString();
      }
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

      public YamlFileResourceOptions.Builder charset(Charset charset) {
         this.charset = charset;
         return this;
      }

      public YamlFileResourceOptions.Builder numberOfLinesBeforeFunction(ToIntFunction<PropertyPathTraverser.PathElement> numberOfLinesBeforeFunction) {
         this.numberOfLinesBeforeFunction = numberOfLinesBeforeFunction;
         return this;
      }

      public YamlFileResourceOptions.Builder indentationSize(int indentationSize) {
         this.indentationSize = indentationSize;
         return this;
      }

      public YamlFileResourceOptions.Builder splitDotPaths(boolean splitDotPaths) {
         this.splitDotPaths = splitDotPaths;
         return this;
      }

      public YamlFileResourceOptions build() {
         return new YamlFileResourceOptions(this.charset, this.numberOfLinesBeforeFunction, this.indentationSize, this.splitDotPaths);
      }
   }
}
