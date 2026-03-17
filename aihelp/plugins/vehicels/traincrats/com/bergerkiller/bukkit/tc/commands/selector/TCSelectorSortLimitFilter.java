package com.bergerkiller.bukkit.tc.commands.selector;

import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.utils.BoundingRange;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.command.CommandSender;

class TCSelectorSortLimitFilter {
   private BoundingRange.Axis senderBounds = null;
   private TCSelectorSortLimitFilter.SortMode sort;
   private int limit;

   TCSelectorSortLimitFilter() {
      this.sort = TCSelectorSortLimitFilter.SortMode.ARBITRARY;
      this.limit = -1;
   }

   public void read(CommandSender sender, List<SelectorCondition> conditions) throws SelectorException {
      Iterator iter = conditions.iterator();

      while(iter.hasNext()) {
         SelectorCondition condition = (SelectorCondition)iter.next();
         if (condition.getKey().equals("sort")) {
            try {
               this.sort = TCSelectorSortLimitFilter.SortMode.valueOf(condition.getValue().toUpperCase(Locale.ENGLISH));
            } catch (IllegalArgumentException var6) {
               throw new SelectorException("Unknown sort option: " + condition.getValue());
            }

            this.senderBounds = BoundingRange.Axis.forSender(sender);
            if (this.senderBounds.world == null) {
               throw new SelectorException("Sort by distance can only be used executing as a Player or CommandBlock");
            }

            iter.remove();
         } else if (condition.getKey().equals("limit")) {
            this.limit = (int)condition.getDouble();
            iter.remove();
         }
      }

   }

   public Stream<TrainProperties> apply(Stream<TrainProperties> stream) {
      stream = this.sort.modify(this.senderBounds, stream);
      if (this.limit >= 0) {
         stream = stream.limit((long)this.limit);
      }

      return stream;
   }

   private static double findDistanceSquaredTo(TrainProperties properties, BoundingRange.Axis senderBounds, double altValue) {
      TCSelectorSortLimitFilter.DoubleHolder result = new TCSelectorSortLimitFilter.DoubleHolder();
      result.value = Double.MAX_VALUE;
      TCSelectorLocationFilter.forAllCartPositions(properties, senderBounds.world, (position) -> {
         double distSq = senderBounds.distanceSquared(position);
         if (distSq < result.value) {
            result.value = distSq;
         }

         return false;
      });
      return result.value == Double.MAX_VALUE ? altValue : result.value;
   }

   private static enum SortMode {
      ARBITRARY,
      RANDOM {
         public Stream<TrainProperties> modify(BoundingRange.Axis senderBounds, Stream<TrainProperties> stream) {
            ArrayList<TrainProperties> allValues = (ArrayList)stream.collect(Collectors.toCollection(ArrayList::new));
            Collections.shuffle(allValues);
            return allValues.stream();
         }
      },
      NEAREST {
         public Stream<TrainProperties> modify(BoundingRange.Axis senderBounds, Stream<TrainProperties> stream) {
            return stream.sorted((t1, t2) -> {
               double t1d = TCSelectorSortLimitFilter.findDistanceSquaredTo(t1, senderBounds, Double.MAX_VALUE);
               double t2d = TCSelectorSortLimitFilter.findDistanceSquaredTo(t2, senderBounds, Double.MAX_VALUE);
               return Double.compare(t1d, t2d);
            });
         }
      },
      FURTHEST {
         public Stream<TrainProperties> modify(BoundingRange.Axis senderBounds, Stream<TrainProperties> stream) {
            return stream.sorted((t1, t2) -> {
               double t1d = TCSelectorSortLimitFilter.findDistanceSquaredTo(t1, senderBounds, -1.0D);
               double t2d = TCSelectorSortLimitFilter.findDistanceSquaredTo(t2, senderBounds, -1.0D);
               return Double.compare(t2d, t1d);
            });
         }
      };

      private SortMode() {
      }

      public Stream<TrainProperties> modify(BoundingRange.Axis senderBounds, Stream<TrainProperties> stream) {
         return stream;
      }

      // $FF: synthetic method
      private static TCSelectorSortLimitFilter.SortMode[] $values() {
         return new TCSelectorSortLimitFilter.SortMode[]{ARBITRARY, RANDOM, NEAREST, FURTHEST};
      }

      // $FF: synthetic method
      SortMode(Object x2) {
         this();
      }
   }

   private static class DoubleHolder {
      public double value;

      private DoubleHolder() {
      }

      // $FF: synthetic method
      DoubleHolder(Object x0) {
         this();
      }
   }
}
