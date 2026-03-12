package com.nisovin.shopkeepers.util.data.property;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import java.util.List;
import java.util.function.Predicate;

public final class EmptyDataPredicates {
   public static final Predicate<Object> EMPTY_STRING = (data) -> {
      return data instanceof String && ((String)data).isEmpty();
   };
   public static final Predicate<Object> EMPTY_CONTAINER = (data) -> {
      DataContainer dataContainer = DataContainer.of(data);
      return dataContainer != null && dataContainer.isEmpty();
   };
   public static final Predicate<Object> EMPTY_LIST = (data) -> {
      return data instanceof List && ((List)data).isEmpty();
   };

   private EmptyDataPredicates() {
   }
}
