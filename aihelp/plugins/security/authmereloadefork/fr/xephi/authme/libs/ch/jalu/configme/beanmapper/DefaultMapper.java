package fr.xephi.authme.libs.ch.jalu.configme.beanmapper;

public final class DefaultMapper extends MapperImpl {
   private static DefaultMapper instance;

   private DefaultMapper() {
   }

   public static Mapper getInstance() {
      if (instance == null) {
         instance = new DefaultMapper();
      }

      return instance;
   }
}
