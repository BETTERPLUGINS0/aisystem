package fr.xephi.authme.libs.ch.jalu.configme.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CollectionUtils {
   private CollectionUtils() {
   }

   public static <T> List<T> getRange(List<T> list, int start) {
      return (List)(start >= list.size() ? new ArrayList() : list.subList(start, list.size()));
   }

   public static <T> List<T> filterCommonStart(List<T> list1, List<T> list2) {
      List<T> commonStart = new ArrayList();
      int minSize = Math.min(list1.size(), list2.size());

      for(int i = 0; i < minSize && Objects.equals(list1.get(i), list2.get(i)); ++i) {
         commonStart.add(list1.get(i));
      }

      return commonStart;
   }
}
