package com.bergerkiller.bukkit.tc.attachments.api;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public interface AttachmentSelection<T> extends Iterable<T> {
   AttachmentSelection<Attachment> NONE = none(Attachment.class);

   static <T> AttachmentSelection<T> none(Class<T> typeFilter) {
      final AttachmentSelector<T> selector = AttachmentSelector.none(typeFilter);
      return new AttachmentSelection<T>() {
         public AttachmentSelector<T> selector() {
            return selector;
         }

         public List<String> names() {
            return Collections.emptyList();
         }

         public List<T> values() {
            return Collections.emptyList();
         }

         public boolean sync() {
            return false;
         }
      };
   }

   AttachmentSelector<T> selector();

   List<String> names();

   List<T> values();

   boolean sync();

   default Iterator<T> iterator() {
      return this.values().iterator();
   }

   default void forEach(Consumer<? super T> action) {
      this.values().forEach(action);
   }
}
