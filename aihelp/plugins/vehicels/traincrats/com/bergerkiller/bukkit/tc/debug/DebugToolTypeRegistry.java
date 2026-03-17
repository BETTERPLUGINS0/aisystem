package com.bergerkiller.bukkit.tc.debug;

import com.bergerkiller.bukkit.tc.debug.types.DebugToolTypeListDestinations;
import com.bergerkiller.bukkit.tc.debug.types.DebugToolTypeRails;
import com.bergerkiller.bukkit.tc.debug.types.DebugToolTypeTrackDistance;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class DebugToolTypeRegistry {
   private static final List<DebugToolTypeRegistry.ToolTypeItem> registry = new ArrayList();

   public static Optional<DebugToolType> match(String debugToolName) {
      Iterator var1 = registry.iterator();

      DebugToolTypeRegistry.ToolTypeItem item;
      do {
         if (!var1.hasNext()) {
            return Optional.empty();
         }

         item = (DebugToolTypeRegistry.ToolTypeItem)var1.next();
      } while(!item.condition.test(debugToolName));

      return Optional.of((DebugToolType)item.factory.apply(debugToolName));
   }

   public static void register(Supplier<DebugToolType> constructor) {
      String debugToolName = ((DebugToolType)constructor.get()).getIdentifier();
      register((n) -> {
         return n.equalsIgnoreCase(debugToolName);
      }, (n) -> {
         return (DebugToolType)constructor.get();
      });
   }

   public static void register(Predicate<String> condition, Function<String, DebugToolType> factory) {
      registry.add(new DebugToolTypeRegistry.ToolTypeItem(condition, factory));
   }

   static {
      register(DebugToolTypeRails::new);
      register(DebugToolTypeListDestinations::new);
      register((n) -> {
         return n.startsWith("Destination ");
      }, (n) -> {
         return new DebugToolTypeListDestinations(n.substring(12));
      });
      register(DebugToolTypeTrackDistance::new);
   }

   private static class ToolTypeItem {
      public final Predicate<String> condition;
      public final Function<String, DebugToolType> factory;

      public ToolTypeItem(Predicate<String> condition, Function<String, DebugToolType> factory) {
         this.condition = condition;
         this.factory = factory;
      }
   }
}
