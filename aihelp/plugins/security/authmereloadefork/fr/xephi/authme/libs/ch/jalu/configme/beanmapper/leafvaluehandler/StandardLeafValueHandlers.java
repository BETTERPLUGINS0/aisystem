package fr.xephi.authme.libs.ch.jalu.configme.beanmapper.leafvaluehandler;

public final class StandardLeafValueHandlers {
   private static LeafValueHandler defaultHandler;

   private StandardLeafValueHandlers() {
   }

   public static LeafValueHandler getDefaultLeafValueHandler() {
      if (defaultHandler == null) {
         defaultHandler = new CombiningLeafValueHandler(new LeafValueHandler[]{new StringLeafValueHandler(), new EnumLeafValueHandler(), new BooleanLeafValueHandler(), new NumberLeafValueHandler(), new BigNumberLeafValueHandler(), new ObjectLeafValueHandler()});
      }

      return defaultHandler;
   }
}
