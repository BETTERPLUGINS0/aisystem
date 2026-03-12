package fr.xephi.authme.libs.org.slf4j.helpers;

import fr.xephi.authme.libs.org.slf4j.ILoggerFactory;
import fr.xephi.authme.libs.org.slf4j.Logger;

public class NOPLoggerFactory implements ILoggerFactory {
   public Logger getLogger(String name) {
      return NOPLogger.NOP_LOGGER;
   }
}
