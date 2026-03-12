package ac.grim.grimac.shaded.slf4j.helpers;

import ac.grim.grimac.shaded.slf4j.ILoggerFactory;
import ac.grim.grimac.shaded.slf4j.Logger;

public class NOPLoggerFactory implements ILoggerFactory {
   public Logger getLogger(String name) {
      return NOPLogger.NOP_LOGGER;
   }
}
