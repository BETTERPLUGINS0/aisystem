package fr.xephi.authme.libs.org.slf4j.spi;

import fr.xephi.authme.libs.org.slf4j.IMarkerFactory;

public interface MarkerFactoryBinder {
   IMarkerFactory getMarkerFactory();

   String getMarkerFactoryClassStr();
}
