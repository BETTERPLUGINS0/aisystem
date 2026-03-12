package fr.xephi.authme.libs.ch.jalu.configme.resource;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.ConfigurationData;
import fr.xephi.authme.libs.ch.jalu.configme.utils.CollectionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PropertyPathTraverser {
   private final ConfigurationData configurationData;
   private List<String> parentPathElements = new ArrayList(0);
   private boolean isFirstProperty = true;

   public PropertyPathTraverser(ConfigurationData configurationData) {
      this.configurationData = configurationData;
   }

   public List<PropertyPathTraverser.PathElement> getPathElements(List<String> pathElements) {
      List<String> commonPathParts = CollectionUtils.filterCommonStart(this.parentPathElements, pathElements.subList(0, pathElements.size() - 1));
      List<String> newPathParts = CollectionUtils.getRange(pathElements, commonPathParts.size());
      this.parentPathElements = pathElements.subList(0, pathElements.size() - 1);
      int indentationLevel = commonPathParts.size();
      String prefix = commonPathParts.isEmpty() ? "" : String.join(".", commonPathParts) + ".";
      return this.convertToPathElements(indentationLevel, prefix, newPathParts);
   }

   private List<PropertyPathTraverser.PathElement> convertToPathElements(int indentation, String prefix, List<String> elements) {
      List<PropertyPathTraverser.PathElement> pathElements = new ArrayList(elements.size());

      for(Iterator var5 = elements.iterator(); var5.hasNext(); ++indentation) {
         String element = (String)var5.next();
         List<String> comments = this.isFirstProperty ? this.getCommentsIncludingRoot(prefix + element) : this.configurationData.getCommentsForSection(prefix + element);
         pathElements.add(new PropertyPathTraverser.PathElement(indentation, element, comments, this.isFirstProperty));
         this.isFirstProperty = false;
         prefix = prefix + element + ".";
      }

      ((PropertyPathTraverser.PathElement)pathElements.get(0)).setFirstOfGroup(true);
      return pathElements;
   }

   private List<String> getCommentsIncludingRoot(String path) {
      List<String> rootComments = this.configurationData.getCommentsForSection("");
      if ("".equals(path)) {
         return rootComments;
      } else {
         List<String> sectionComments = this.configurationData.getCommentsForSection(path);
         if (sectionComments.isEmpty()) {
            return rootComments;
         } else {
            List<String> allComments = new ArrayList(rootComments);
            allComments.addAll(sectionComments);
            return allComments;
         }
      }
   }

   public static class PathElement {
      private final int indentationLevel;
      private final String name;
      private final List<String> comments;
      private final boolean isFirstElement;
      private boolean isFirstOfGroup;

      public PathElement(int indentationLevel, String name, List<String> comments, boolean isFirstElement) {
         this.indentationLevel = indentationLevel;
         this.name = name;
         this.comments = comments;
         this.isFirstElement = isFirstElement;
      }

      public int getIndentationLevel() {
         return this.indentationLevel;
      }

      public String getName() {
         return this.name;
      }

      public List<String> getComments() {
         return this.comments;
      }

      public boolean isFirstElement() {
         return this.isFirstElement;
      }

      public boolean isFirstOfGroup() {
         return this.isFirstOfGroup;
      }

      void setFirstOfGroup(boolean firstOfGroup) {
         this.isFirstOfGroup = firstOfGroup;
      }
   }
}
