package fr.xephi.authme.util.lazytags;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class TagReplacer<A> {
   private final List<Tag<A>> tags;
   private final Collection<String> messages;

   private TagReplacer(List<Tag<A>> tags, Collection<String> messages) {
      this.tags = tags;
      this.messages = messages;
   }

   public static <A> TagReplacer<A> newReplacer(Collection<Tag<A>> allTags, Collection<String> messages) {
      List<Tag<A>> usedTags = determineUsedTags(allTags, messages);
      return new TagReplacer(usedTags, messages);
   }

   public List<String> getAdaptedMessages(A argument) {
      List<TagReplacer.TagValue> tagValues = new LinkedList();
      Iterator var3 = this.tags.iterator();

      while(var3.hasNext()) {
         Tag<A> tag = (Tag)var3.next();
         tagValues.add(new TagReplacer.TagValue(tag.getName(), tag.getValue(argument)));
      }

      List<String> adaptedMessages = new LinkedList();
      Iterator var10 = this.messages.iterator();

      while(var10.hasNext()) {
         String line = (String)var10.next();
         String adaptedLine = line;

         TagReplacer.TagValue tagValue;
         for(Iterator var7 = tagValues.iterator(); var7.hasNext(); adaptedLine = adaptedLine.replace(tagValue.tag, tagValue.value)) {
            tagValue = (TagReplacer.TagValue)var7.next();
         }

         adaptedMessages.add(adaptedLine);
      }

      return adaptedMessages;
   }

   private static <A> List<Tag<A>> determineUsedTags(Collection<Tag<A>> allTags, Collection<String> messages) {
      return (List)allTags.stream().filter((tag) -> {
         return messages.stream().anyMatch((msg) -> {
            return msg.contains(tag.getName());
         });
      }).collect(Collectors.toList());
   }

   private static final class TagValue {
      private final String tag;
      private final String value;

      TagValue(String tag, String value) {
         this.tag = tag;
         this.value = value;
      }

      public String toString() {
         return "TagValue[tag='" + this.tag + "', value='" + this.value + "']";
      }
   }
}
