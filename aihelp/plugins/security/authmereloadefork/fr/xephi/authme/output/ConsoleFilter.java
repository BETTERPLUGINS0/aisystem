package fr.xephi.authme.output;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ConsoleFilter implements Filter {
   public boolean isLoggable(LogRecord record) {
      if (record != null && record.getMessage() != null) {
         if (LogFilterHelper.isSensitiveAuthMeCommand(record.getMessage())) {
            String playerName = record.getMessage().split(" ")[0];
            record.setMessage(playerName + " issued an AuthMe command");
         }

         return true;
      } else {
         return true;
      }
   }
}
