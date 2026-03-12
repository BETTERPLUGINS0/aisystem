package ch.jalu.configme.resource;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class PropertyPathTraverser {
   private String lastPath;
   private boolean isFirstElement = true;

   @NotNull
   public List<PropertyPathTraverser.PathElement> getPathElements(@NotNull String path) {
      String[] pathParts = path.split("\\.");
      int totalParts = pathParts.length;
      int levelOfFirstNewPart = this.returnLevelOfFirstNewPathElement(path);
      StringBuilder fullPathBuilder = new StringBuilder();
      List<PropertyPathTraverser.PathElement> pathElements = new ArrayList(totalParts);
      int level = 0;

      for(int i = 0; i < totalParts; ++i) {
         fullPathBuilder.append(pathParts[i]);
         PropertyPathTraverser.PathElement element = new PropertyPathTraverser.PathElement(level, pathParts[i], fullPathBuilder.toString(), this.isFirstElement);
         element.setEndOfPath(i == totalParts - 1);
         element.setFirstOfGroup(levelOfFirstNewPart == level);
         pathElements.add(element);
         ++level;
         fullPathBuilder.append(".");
         this.isFirstElement = false;
      }

      this.lastPath = path;
      return pathElements;
   }

   protected int returnLevelOfFirstNewPathElement(@NotNull String path) {
      if (this.lastPath == null) {
         return 0;
      } else {
         int minLength = Math.min(this.lastPath.length(), path.length());
         int i = 0;

         int level;
         for(level = 0; i < minLength && path.charAt(i) == this.lastPath.charAt(i); ++i) {
            if (path.charAt(i) == '.') {
               ++level;
            }
         }

         return level;
      }
   }

   public static class PathElement {
      private final int indentationLevel;
      private final String name;
      private final String fullPath;
      private final boolean isFirstElement;
      private boolean isFirstOfGroup;
      private boolean isEndOfPath;

      public PathElement(int indentationLevel, @NotNull String name, @NotNull String fullPath, boolean isFirstElement) {
         this.indentationLevel = indentationLevel;
         this.name = name;
         this.fullPath = fullPath;
         this.isFirstElement = isFirstElement;
      }

      public int getIndentationLevel() {
         return this.indentationLevel;
      }

      @NotNull
      public String getName() {
         return this.name;
      }

      @NotNull
      public String getFullPath() {
         return this.fullPath;
      }

      public boolean isFirstElement() {
         return this.isFirstElement;
      }

      public boolean isFirstOfGroup() {
         return this.isFirstOfGroup;
      }

      protected void setFirstOfGroup(boolean firstOfGroup) {
         this.isFirstOfGroup = firstOfGroup;
      }

      public boolean isEndOfPath() {
         return this.isEndOfPath;
      }

      protected void setEndOfPath(boolean isEndOfPath) {
         this.isEndOfPath = isEndOfPath;
      }
   }
}
