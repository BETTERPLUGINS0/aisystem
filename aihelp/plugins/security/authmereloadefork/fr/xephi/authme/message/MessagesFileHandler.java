package fr.xephi.authme.message;

import fr.xephi.authme.ConsoleLogger;
import fr.xephi.authme.libs.javax.inject.Inject;
import fr.xephi.authme.message.updater.MessageUpdater;
import fr.xephi.authme.output.ConsoleLoggerFactory;

public class MessagesFileHandler extends AbstractMessageFileHandler {
   private final ConsoleLogger logger = ConsoleLoggerFactory.get(MessagesFileHandler.class);
   @Inject
   private MessageUpdater messageUpdater;

   MessagesFileHandler() {
   }

   public void reload() {
      this.reloadInternal(false);
   }

   private void reloadInternal(boolean isFromReload) {
      super.reload();
      String language = this.getLanguage();
      boolean hasChange = this.messageUpdater.migrateAndSave(this.getUserLanguageFile(), this.createFilePath(language), this.createFilePath("en"));
      if (hasChange) {
         if (isFromReload) {
            this.logger.warning("Migration after reload attempt");
         } else {
            this.reloadInternal(true);
         }
      }

   }

   protected String createFilePath(String language) {
      return MessagePathHelper.createMessageFilePath(language);
   }
}
