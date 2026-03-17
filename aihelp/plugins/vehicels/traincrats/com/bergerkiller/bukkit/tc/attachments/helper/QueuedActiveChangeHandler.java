package com.bergerkiller.bukkit.tc.attachments.helper;

import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueuedActiveChangeHandler implements ActiveChangeHandler {
   private final Queue<QueuedActiveChangeHandler.PendingChange> queue = new ConcurrentLinkedQueue();

   public void scheduleActiveChange(Attachment attachment, boolean active) {
      this.queue.offer(new QueuedActiveChangeHandler.PendingChange(attachment, active));
   }

   public void sync() {
      try {
         Iterator var1 = this.queue.iterator();

         while(var1.hasNext()) {
            QueuedActiveChangeHandler.PendingChange pending = (QueuedActiveChangeHandler.PendingChange)var1.next();
            pending.attachment.setActive(pending.active);
         }
      } finally {
         this.queue.clear();
      }

   }

   private static final class PendingChange {
      public final Attachment attachment;
      public final boolean active;

      public PendingChange(Attachment attachment, boolean active) {
         this.attachment = attachment;
         this.active = active;
      }
   }
}
