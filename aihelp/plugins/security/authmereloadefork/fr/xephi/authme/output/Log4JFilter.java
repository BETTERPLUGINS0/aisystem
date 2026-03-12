package fr.xephi.authme.output;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter.Result;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class Log4JFilter extends AbstractFilter {
   private static final long serialVersionUID = -5594073755007974254L;

   private static Result validateMessage(Message message) {
      return message == null ? Result.NEUTRAL : validateMessage(message.getFormattedMessage());
   }

   private static Result validateMessage(String message) {
      return LogFilterHelper.isSensitiveAuthMeCommand(message) ? Result.DENY : Result.NEUTRAL;
   }

   public Result filter(LogEvent event) {
      Message candidate = null;
      if (event != null) {
         candidate = event.getMessage();
      }

      return validateMessage(candidate);
   }

   public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
      return validateMessage(msg);
   }

   public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
      return validateMessage(msg);
   }

   public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
      String candidate = null;
      if (msg != null) {
         candidate = msg.toString();
      }

      return validateMessage(candidate);
   }
}
