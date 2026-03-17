package es.outlook.adriansrj.spigui.toolbar;

import java.util.Map;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum SGToolbarButtonType {
   PREV_BUTTON,
   CURRENT_BUTTON,
   NEXT_BUTTON,
   UNASSIGNED;

   private static final Map<Integer, SGToolbarButtonType> DEFAULT_MAPPINGS = (Map)Stream.of(new SimpleImmutableEntry(3, PREV_BUTTON), new SimpleImmutableEntry(4, CURRENT_BUTTON), new SimpleImmutableEntry(5, NEXT_BUTTON)).collect(Collectors.toMap(Entry::getKey, Entry::getValue));

   public static SGToolbarButtonType getDefaultForSlot(int var0) {
      return (SGToolbarButtonType)DEFAULT_MAPPINGS.getOrDefault(var0, UNASSIGNED);
   }
}
