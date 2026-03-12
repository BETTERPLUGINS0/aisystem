package fr.xephi.authme.libs.org.slf4j.impl;

import fr.xephi.authme.libs.org.slf4j.IMarkerFactory;
import fr.xephi.authme.libs.org.slf4j.helpers.BasicMarkerFactory;
import fr.xephi.authme.libs.org.slf4j.spi.MarkerFactoryBinder;

public class StaticMarkerBinder implements MarkerFactoryBinder {
   public static final StaticMarkerBinder SINGLETON = new StaticMarkerBinder();
   final IMarkerFactory markerFactory = new BasicMarkerFactory();

   private StaticMarkerBinder() {
   }

   public static StaticMarkerBinder getSingleton() {
      return SINGLETON;
   }

   public IMarkerFactory getMarkerFactory() {
      return this.markerFactory;
   }

   public String getMarkerFactoryClassStr() {
      return BasicMarkerFactory.class.getName();
   }
}
