package fr.xephi.authme.libs.org.slf4j.spi;

import fr.xephi.authme.libs.org.slf4j.ILoggerFactory;

public interface LoggerFactoryBinder {
   ILoggerFactory getLoggerFactory();

   String getLoggerFactoryClassStr();
}
