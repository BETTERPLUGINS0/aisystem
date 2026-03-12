package ac.grim.grimac.shaded.slf4j.spi;

import ac.grim.grimac.shaded.slf4j.ILoggerFactory;
import ac.grim.grimac.shaded.slf4j.IMarkerFactory;

public interface SLF4JServiceProvider {
   ILoggerFactory getLoggerFactory();

   IMarkerFactory getMarkerFactory();

   MDCAdapter getMDCAdapter();

   String getRequestedApiVersion();

   void initialize();
}
