package com.volmit.iris.core.link.data;

import com.volmit.iris.core.link.ExternalDataProvider;
import com.volmit.iris.core.link.Identifier;
import java.util.MissingResourceException;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public enum DataType implements BiPredicate<ExternalDataProvider, Identifier> {
   ITEM,
   BLOCK,
   ENTITY;

   public boolean test(ExternalDataProvider dataProvider, Identifier identifier) {
      if (!var1.isValidProvider(var2, this)) {
         return false;
      } else {
         try {
            switch(this.ordinal()) {
            case 0:
               var1.getItemStack(var2);
               break;
            case 1:
               var1.getBlockData(var2);
            case 2:
            }

            return true;
         } catch (MissingResourceException var4) {
            return false;
         }
      }
   }

   public Predicate<Identifier> asPredicate(ExternalDataProvider dataProvider) {
      return (var2) -> {
         return this.test(var1, var2);
      };
   }

   // $FF: synthetic method
   private static DataType[] $values() {
      return new DataType[]{ITEM, BLOCK, ENTITY};
   }
}
