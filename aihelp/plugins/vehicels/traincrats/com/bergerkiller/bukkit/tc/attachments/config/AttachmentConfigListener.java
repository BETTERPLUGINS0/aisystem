package com.bergerkiller.bukkit.tc.attachments.config;

import com.bergerkiller.bukkit.tc.attachments.api.Attachment;
import java.util.function.Consumer;

public interface AttachmentConfigListener {
   default void onChange(AttachmentConfig.Change change) {
      change.changeType().callback().accept(this, change.attachment());
   }

   default void onAttachmentAdded(AttachmentConfig attachmentConfig) {
   }

   default void onAttachmentRemoved(AttachmentConfig attachmentConfig) {
   }

   default void onAttachmentChanged(AttachmentConfig attachmentConfig) {
   }

   default void onSynchronized(AttachmentConfig rootAttachmentConfig) {
   }

   default void onAttachmentAction(AttachmentConfig attachmentConfig, Consumer<Attachment> action) {
   }
}
