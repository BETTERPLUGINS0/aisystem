package com.bergerkiller.bukkit.tc;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.stream.Stream;

public enum InteractType {
   CHEST,
   FURNACE,
   DISPENSER,
   GROUNDITEM,
   DROPPER;

   private static final InteractType.TargetInOut[] COLLECT_TARGETS = new InteractType.TargetInOut[]{new InteractType.TargetInOut("chest out", CHEST), new InteractType.TargetInOut("dispenser out", DISPENSER), new InteractType.TargetInOut("dropper out", DROPPER), new InteractType.TargetInOut("furnace out", FURNACE), new InteractType.TargetInOut("pickup", GROUNDITEM), new InteractType.TargetInOut("pick up", GROUNDITEM)};
   private static final InteractType.TargetInOut[] DEPOSIT_TARGETS = new InteractType.TargetInOut[]{new InteractType.TargetInOut("chest in", CHEST), new InteractType.TargetInOut("dispenser in", DISPENSER), new InteractType.TargetInOut("furnace in", FURNACE), new InteractType.TargetInOut("smelt", FURNACE), new InteractType.TargetInOut("drop items", GROUNDITEM), new InteractType.TargetInOut("dropitems", GROUNDITEM)};

   public static String[] getAllUniqueTypeIdentifiers() {
      return (String[])Stream.concat(Stream.concat(Stream.of(COLLECT_TARGETS), Stream.of(DEPOSIT_TARGETS)).map((target) -> {
         return target.prefix;
      }), Stream.of("collect", "deposit")).toArray((x$0) -> {
         return new String[x$0];
      });
   }

   public static Collection<InteractType> parse(String root, String name) {
      name = name.toLowerCase(Locale.ENGLISH);
      LinkedHashSet<InteractType> typesToCheck = new LinkedHashSet();
      InteractType.TargetInOut[] var3;
      int var4;
      int var5;
      InteractType.TargetInOut target;
      if (root.equals("collect")) {
         var3 = COLLECT_TARGETS;
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            target = var3[var5];
            if (name.startsWith(target.prefix)) {
               typesToCheck.add(target.type);
               break;
            }
         }
      } else if (root.equals("deposit")) {
         var3 = DEPOSIT_TARGETS;
         var4 = var3.length;

         for(var5 = 0; var5 < var4; ++var5) {
            target = var3[var5];
            if (name.startsWith(target.prefix)) {
               typesToCheck.add(target.type);
               break;
            }
         }
      }

      if (name.startsWith(root + ' ')) {
         String types = name.substring(root.length() + 1).toLowerCase();
         if (types.startsWith("chest")) {
            typesToCheck.add(CHEST);
         } else if (types.startsWith("furn")) {
            typesToCheck.add(FURNACE);
         } else if (types.startsWith("disp")) {
            typesToCheck.add(DISPENSER);
         } else if (types.startsWith("drop")) {
            typesToCheck.add(DROPPER);
         } else if (types.startsWith("ground")) {
            typesToCheck.add(GROUNDITEM);
         } else {
            char[] var9 = types.toCharArray();
            var5 = var9.length;

            for(int var10 = 0; var10 < var5; ++var10) {
               char c = var9[var10];
               if (c == 'c') {
                  typesToCheck.add(CHEST);
               } else if (c == 'f') {
                  typesToCheck.add(FURNACE);
               } else if (c == 'd') {
                  typesToCheck.add(DISPENSER);
               } else if (c == 'g') {
                  typesToCheck.add(GROUNDITEM);
               }
            }
         }
      }

      if (name.startsWith(root) && typesToCheck.isEmpty()) {
         typesToCheck.add(CHEST);
         typesToCheck.add(FURNACE);
         typesToCheck.add(DISPENSER);
         typesToCheck.add(DROPPER);
      }

      return typesToCheck;
   }

   // $FF: synthetic method
   private static InteractType[] $values() {
      return new InteractType[]{CHEST, FURNACE, DISPENSER, GROUNDITEM, DROPPER};
   }

   private static class TargetInOut {
      public final String prefix;
      public final InteractType type;

      public TargetInOut(String prefix, InteractType type) {
         this.prefix = prefix;
         this.type = type;
      }
   }
}
