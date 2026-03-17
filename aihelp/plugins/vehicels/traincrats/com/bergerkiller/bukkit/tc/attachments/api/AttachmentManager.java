package com.bergerkiller.bukkit.tc.attachments.api;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.attachments.config.AttachmentConfig;
import java.util.Collection;
import java.util.Iterator;
import org.bukkit.World;
import org.bukkit.entity.Player;

public interface AttachmentManager {
   World getWorld();

   AttachmentWorldFeatures getWorldFeatures();

   Collection<Player> getViewers();

   Collection<AttachmentViewer> getAttachmentViewers();

   AttachmentViewer asAttachmentViewer(Player var1);

   default AttachmentTypeRegistry getTypeRegistry() {
      return AttachmentTypeRegistry.instance();
   }

   default Attachment createAttachment(AttachmentConfig attachmentConfig) {
      AttachmentType attachmentType = this.getTypeRegistry().findOrEmpty(attachmentConfig.typeId());
      ConfigurationNode config = attachmentConfig.config();
      Attachment attachment = attachmentType.createController(config);
      AttachmentInternalState state = attachment.getInternalState();
      state.manager = this;
      state.rootParent = attachment;
      state.onLoad(this.getClass(), attachmentType, config);
      Iterator var6 = attachmentConfig.children().iterator();

      while(var6.hasNext()) {
         AttachmentConfig childAttachmentConfig = (AttachmentConfig)var6.next();
         attachment.addChild(this.createAttachment(childAttachmentConfig));
      }

      return attachment;
   }

   AttachmentNameLookup getNameLookup(Attachment var1);
}
