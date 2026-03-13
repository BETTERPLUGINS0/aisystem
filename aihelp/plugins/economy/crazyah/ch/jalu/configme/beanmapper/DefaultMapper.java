package ch.jalu.configme.beanmapper;

import org.jetbrains.annotations.NotNull;

public final class DefaultMapper extends MapperImpl {
   private static DefaultMapper instance;

   private DefaultMapper() {
   }

   @NotNull
   public static Mapper getInstance() {
      if (instance == null) {
         instance = new DefaultMapper();
      }

      return instance;
   }
}
