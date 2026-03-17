package com.bergerkiller.bukkit.tc.controller.spawnable;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.properties.SavedTrainProperties;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;

public abstract class TrainSpawnPattern {
   public static final int MAX_SPAWNABLE_TRAIN_LENGTH = 1024;
   private final TrainSpawnPattern.QuantityPrefix quantity;

   protected TrainSpawnPattern(TrainSpawnPattern.QuantityPrefix quantity) {
      this.quantity = quantity;
   }

   public TrainSpawnPattern.QuantityPrefix quantity() {
      return this.quantity;
   }

   public int amount() {
      return this.quantity().amount;
   }

   protected abstract TrainSpawnPattern.Applier newGroupApplier();

   protected TrainSpawnPattern.Applier repeatWithAmount(TrainSpawnPattern.Applier callback) {
      int amount = this.quantity().amount;
      if (amount <= 0) {
         return (group, random, savedTrainMatcher) -> {
         };
      } else {
         return amount == 1 ? callback : (group, random, savedTrainMatcher) -> {
            for(int n = 0; n < amount; ++n) {
               callback.apply(group, random, savedTrainMatcher);
            }

         };
      }
   }

   public static TrainSpawnPattern.ParsedSpawnPattern parse(String spawnPattern, Function<String, String> savedTrainMatcher) {
      TrainSpawnPattern.Parser parser = new TrainSpawnPattern.Parser(spawnPattern, 0, savedTrainMatcher);
      parser.parse();
      return parser.createSpawnPattern();
   }

   public static String findNameInSortedList(List<String> sortedNames, String input) {
      int index = Collections.binarySearch(sortedNames, input);
      if (index >= 0) {
         return (String)sortedNames.get(index);
      } else {
         String longestPrefix = null;
         ListIterator iter = sortedNames.listIterator(-(index + 1));

         while(iter.hasPrevious()) {
            String name = (String)iter.previous();
            if (input.startsWith(name) && (longestPrefix == null || name.length() > longestPrefix.length())) {
               longestPrefix = name;
            } else if (longestPrefix != null) {
               break;
            }
         }

         return longestPrefix;
      }
   }

   private static TrainSpawnPattern.Applier twoStage(Function<SpawnableGroup, List<SpawnableMember>> initializer) {
      return new TrainSpawnPattern.TwoStageApplier(initializer);
   }

   public interface Applier {
      void apply(SpawnableGroup var1, Random var2, Function<String, String> var3);

      default TrainSpawnPattern.Applier reverse() {
         return (group, random, savedTrainMatcher) -> {
            int countBefore = group.getMembers().size();
            boolean var11 = false;

            try {
               var11 = true;
               this.apply(group, random, savedTrainMatcher);
               var11 = false;
            } finally {
               if (var11) {
                  List added = group.getMembers().subList(countBefore, group.getMembers().size());
                  Collections.reverse(added);
                  ListIterator iterx = added.listIterator();

                  while(iterx.hasNext()) {
                     iterx.set(((SpawnableMember)iterx.next()).cloneReversed());
                  }

               }
            }

            List<SpawnableMember> addedx = group.getMembers().subList(countBefore, group.getMembers().size());
            Collections.reverse(addedx);
            ListIterator iter = addedx.listIterator();

            while(iter.hasNext()) {
               iter.set(((SpawnableMember)iter.next()).cloneReversed());
            }

         };
      }
   }

   public static class QuantityPrefix {
      public static final TrainSpawnPattern.QuantityPrefix ZERO = new TrainSpawnPattern.QuantityPrefix(0);
      public static final TrainSpawnPattern.QuantityPrefix ONE = new TrainSpawnPattern.QuantityPrefix(1);
      public final int amount;
      public final double chanceWeight;

      public QuantityPrefix(int amount) {
         this(amount, Double.NaN);
      }

      public QuantityPrefix(int amount, double chanceWeight) {
         this.amount = amount;
         this.chanceWeight = chanceWeight;
      }

      public boolean isOne() {
         return this.amount == 1 && Double.isNaN(this.chanceWeight);
      }

      public boolean hasChanceWeight() {
         return !Double.isNaN(this.chanceWeight);
      }

      public String toString() {
         if (this.hasChanceWeight()) {
            StringBuilder str = new StringBuilder();
            if (this.chanceWeight == Math.floor(this.chanceWeight)) {
               str.append((int)this.chanceWeight);
            } else {
               str.append(this.chanceWeight);
            }

            str.append('%');
            if (this.amount != 1) {
               str.append(this.amount);
            }

            return str.toString();
         } else {
            return this.isOne() ? "" : Integer.toString(this.amount);
         }
      }
   }

   private static class Parser {
      private final StringBuilder quantityBuilder = new StringBuilder();
      private final String spawnPattern;
      private final int startIndex;
      private final Function<String, String> savedTrainMatcher;
      public final List<TrainSpawnPattern> patterns = new ArrayList();
      public SpawnableGroup.CenterMode centerMode;
      private boolean foundSequenceEnd;

      public Parser(String spawnPattern, int startIndex, Function<String, String> savedTrainMatcher) {
         this.centerMode = SpawnableGroup.CenterMode.NONE;
         this.foundSequenceEnd = false;
         this.spawnPattern = spawnPattern;
         this.startIndex = startIndex;
         this.savedTrainMatcher = savedTrainMatcher;
      }

      public boolean hasPatterns() {
         return !this.patterns.isEmpty();
      }

      public boolean hasParsedContent() {
         return !this.patterns.isEmpty() || !this.quantityBuilder.toString().trim().isEmpty();
      }

      public TrainSpawnPattern.SequenceSpawnPattern toSequence(TrainSpawnPattern.QuantityPrefix quantity) {
         return new TrainSpawnPattern.SequenceSpawnPattern(quantity, this.patterns);
      }

      public void addPattern(TrainSpawnPattern pattern) {
         this.patterns.add(pattern);
      }

      public TrainSpawnPattern.QuantityPrefix consumeQuantity() {
         TrainSpawnPattern.QuantityPrefix var5;
         try {
            int chanceIndex = this.quantityBuilder.indexOf("%");
            if (chanceIndex == -1) {
               int amount = ParseUtil.parseInt(this.quantityBuilder.toString(), 1);
               TrainSpawnPattern.QuantityPrefix var3 = new TrainSpawnPattern.QuantityPrefix(amount);
               return var3;
            }

            double chanceWeight = ParseUtil.parseDouble(this.quantityBuilder.substring(0, chanceIndex), 0.0D);
            if (chanceWeight <= 0.0D) {
               TrainSpawnPattern.QuantityPrefix var10 = TrainSpawnPattern.QuantityPrefix.ZERO;
               return var10;
            }

            int amount = ParseUtil.parseInt(this.quantityBuilder.substring(chanceIndex + 1), 1);
            var5 = new TrainSpawnPattern.QuantityPrefix(amount, chanceWeight);
         } finally {
            this.quantityBuilder.setLength(0);
         }

         return var5;
      }

      public TrainSpawnPattern.ParsedSpawnPattern createSpawnPattern() {
         return this.toSequence(TrainSpawnPattern.QuantityPrefix.ONE).simplify().asSpawnPattern(this.centerMode);
      }

      public int parse() {
         int index = this.startIndex;
         String spawnPattern = this.spawnPattern;

         while(true) {
            while(index < spawnPattern.length()) {
               char c = spawnPattern.charAt(index);
               if (LogicUtil.containsChar(c, "[<({")) {
                  TrainSpawnPattern.Parser subParser = new TrainSpawnPattern.Parser(spawnPattern, index + 1, this.savedTrainMatcher);
                  int subEndIndex = subParser.parse();
                  if (subEndIndex == spawnPattern.length() && this.startIndex == 0 && !this.hasParsedContent()) {
                     this.centerMode = subParser.foundSequenceEnd ? SpawnableGroup.CenterMode.MIDDLE : SpawnableGroup.CenterMode.RIGHT;
                     Iterator var6 = subParser.patterns.iterator();

                     while(var6.hasNext()) {
                        TrainSpawnPattern pattern = (TrainSpawnPattern)var6.next();
                        this.addPattern(pattern);
                     }

                     return subEndIndex;
                  }

                  this.addPattern(subParser.toSequence(this.consumeQuantity()));
                  index = subEndIndex;
               } else if (LogicUtil.containsChar(c, "]>)}")) {
                  if (this.startIndex > 0) {
                     this.foundSequenceEnd = true;
                     return index + 1;
                  }

                  this.centerMode = SpawnableGroup.CenterMode.LEFT;
                  ++index;
               } else {
                  String name = (String)this.savedTrainMatcher.apply(spawnPattern.substring(index));
                  if (name == null || name.length() <= 1 && SpawnableGroup.VanillaCartType.parse(c).isPresent()) {
                     Optional<SpawnableGroup.VanillaCartType> type = SpawnableGroup.VanillaCartType.parse(c);
                     if (type.isPresent()) {
                        ++index;
                        this.addPattern(new TrainSpawnPattern.VanillaCartSpawnPattern(this.consumeQuantity(), (SpawnableGroup.VanillaCartType)type.get()));
                     } else {
                        ++index;
                        if (Character.isDigit(c) || c == '.' || c == '%') {
                           this.quantityBuilder.append(c);
                        }
                     }
                  } else {
                     index += name.length();
                     this.addPattern(new TrainSpawnPattern.SavedTrainSpawnPattern(this.consumeQuantity(), name));
                  }
               }
            }

            return index;
         }
      }
   }

   public static class ParsedSpawnPattern extends TrainSpawnPattern.SequenceSpawnPattern {
      private final SpawnableGroup.CenterMode centerMode;

      protected ParsedSpawnPattern(TrainSpawnPattern.SequenceSpawnPattern sequence, SpawnableGroup.CenterMode centerMode) {
         super(sequence);
         this.centerMode = centerMode;
      }

      public SpawnableGroup.CenterMode centerMode() {
         return this.centerMode;
      }

      public String toString() {
         String str = super.toString();
         return str.substring(1, str.length() - 1);
      }
   }

   private static class TwoStageApplier implements TrainSpawnPattern.Applier {
      private final Function<SpawnableGroup, List<SpawnableMember>> initializer;
      private List<SpawnableMember> initializedMembers = null;

      public TwoStageApplier(Function<SpawnableGroup, List<SpawnableMember>> initializer) {
         this.initializer = initializer;
      }

      public void apply(SpawnableGroup spawnableGroup, Random random, Function<String, String> savedTrainMatcher) {
         List<SpawnableMember> initializedMembers = this.initializedMembers;
         if (initializedMembers != null) {
            if (spawnableGroup.getMembers().size() + initializedMembers.size() > 1024) {
               throw new TrainSpawnPattern.TrainTooLongException();
            }

            Objects.requireNonNull(spawnableGroup);
            initializedMembers.forEach(spawnableGroup::addMember);
         } else {
            this.initializedMembers = (List)this.initializer.apply(spawnableGroup);
            if (spawnableGroup.getMembers().size() > 1024) {
               for(int n = 0; n < initializedMembers.size() && !spawnableGroup.getMembers().isEmpty(); ++n) {
                  spawnableGroup.getMembers().remove(spawnableGroup.getMembers().size() - 1);
               }

               throw new TrainSpawnPattern.TrainTooLongException();
            }
         }

      }
   }

   public static class TrainTooLongException extends RuntimeException {
   }

   public static class SequenceSpawnPattern extends TrainSpawnPattern {
      private final List<TrainSpawnPattern> patterns;

      public SequenceSpawnPattern(TrainSpawnPattern.SequenceSpawnPattern copy) {
         this(copy.quantity(), copy.patterns());
      }

      public SequenceSpawnPattern(TrainSpawnPattern.QuantityPrefix quantity, List<TrainSpawnPattern> patterns) {
         super(quantity);
         this.patterns = patterns;
      }

      public List<TrainSpawnPattern> patterns() {
         return this.patterns;
      }

      public TrainSpawnPattern.SequenceSpawnPattern simplify() {
         if (this.patterns.size() == 1 && this.quantity().isOne()) {
            TrainSpawnPattern p = (TrainSpawnPattern)this.patterns.get(0);
            if (p instanceof TrainSpawnPattern.SequenceSpawnPattern) {
               return (TrainSpawnPattern.SequenceSpawnPattern)p;
            }
         }

         return this;
      }

      public TrainSpawnPattern.ParsedSpawnPattern asSpawnPattern(SpawnableGroup.CenterMode centerMode) {
         return new TrainSpawnPattern.ParsedSpawnPattern(this, centerMode);
      }

      public String toString() {
         StringBuilder str = new StringBuilder();
         str.append(this.quantity().toString());
         str.append("[");
         boolean first = true;

         TrainSpawnPattern p;
         for(Iterator var3 = this.patterns.iterator(); var3.hasNext(); str.append(p.toString())) {
            p = (TrainSpawnPattern)var3.next();
            if (first) {
               first = false;
            } else {
               str.append(" ");
            }
         }

         str.append("]");
         return str.toString();
      }

      protected TrainSpawnPattern.Applier newGroupApplier() {
         boolean hasChanceWeight = false;
         Iterator var2 = this.patterns.iterator();

         while(var2.hasNext()) {
            TrainSpawnPattern pattern = (TrainSpawnPattern)var2.next();
            if (pattern.quantity().hasChanceWeight()) {
               hasChanceWeight = true;
               break;
            }
         }

         ArrayList appliers;
         if (hasChanceWeight) {
            appliers = new ArrayList(this.patterns.size());
            double chanceWeightPosition = 0.0D;
            Iterator var7 = this.patterns.iterator();

            while(var7.hasNext()) {
               TrainSpawnPattern pattern = (TrainSpawnPattern)var7.next();
               if (pattern.quantity().hasChanceWeight()) {
                  double nextChanceWeightPosition = chanceWeightPosition + pattern.quantity().chanceWeight;
                  appliers.add(new TrainSpawnPattern.SequenceSpawnPattern.WeightedApplier(pattern.newGroupApplier(), chanceWeightPosition, nextChanceWeightPosition));
                  chanceWeightPosition = nextChanceWeightPosition;
               } else {
                  appliers.add(new TrainSpawnPattern.SequenceSpawnPattern.WeightedApplier(pattern.newGroupApplier()));
               }
            }

            return this.repeatWithAmount((group, random, savedTrainMatcher) -> {
               double chanceWeightPositionx = random.nextDouble(chanceWeightPosition);
               appliers.forEach((applier) -> {
                  applier.apply(group, random, savedTrainMatcher, chanceWeightPositionx);
               });
            });
         } else {
            appliers = new ArrayList(this.patterns.size());
            Iterator var12 = this.patterns.iterator();

            while(var12.hasNext()) {
               TrainSpawnPattern pattern = (TrainSpawnPattern)var12.next();
               appliers.add(pattern.newGroupApplier());
            }

            return this.repeatWithAmount((group, random, savedTrainMatcher) -> {
               appliers.forEach((applier) -> {
                  applier.apply(group, random, savedTrainMatcher);
               });
            });
         }
      }

      private static class WeightedApplier {
         private final TrainSpawnPattern.Applier applier;
         private final boolean always;
         private final double chanceWeightRangeStart;
         private final double chanceWeightRangeEnd;

         public WeightedApplier(TrainSpawnPattern.Applier applier) {
            this.applier = applier;
            this.always = true;
            this.chanceWeightRangeStart = Double.NaN;
            this.chanceWeightRangeEnd = Double.NaN;
         }

         public WeightedApplier(TrainSpawnPattern.Applier applier, double chanceWeightRangeStart, double chanceWeightRangeEnd) {
            this.applier = applier;
            this.always = false;
            this.chanceWeightRangeStart = chanceWeightRangeStart;
            this.chanceWeightRangeEnd = chanceWeightRangeEnd;
         }

         public void apply(SpawnableGroup group, Random random, Function<String, String> savedTrainMatcher, double chanceWeightPosition) {
            if (this.always || chanceWeightPosition >= this.chanceWeightRangeStart && chanceWeightPosition < this.chanceWeightRangeEnd) {
               this.applier.apply(group, random, savedTrainMatcher);
            }

         }
      }
   }

   public static class SavedTrainSpawnPattern extends TrainSpawnPattern {
      private final String name;

      public SavedTrainSpawnPattern(TrainSpawnPattern.QuantityPrefix quantity, String name) {
         super(quantity);
         this.name = name;
      }

      public String name() {
         return this.name;
      }

      public String toString() {
         return this.quantity().toString() + this.name;
      }

      protected TrainSpawnPattern.Applier newGroupApplier() {
         return new TrainSpawnPattern.Applier() {
            TrainSpawnPattern.Applier applier = null;

            public void apply(SpawnableGroup group, Random random, Function<String, String> savedTrainMatcher) {
               TrainSpawnPattern.Applier applier = this.applier;
               if (applier == null) {
                  this.applier = applier = SavedTrainSpawnPattern.this.repeatWithAmount(this.createApplier(group, random, savedTrainMatcher));
               }

               applier.apply(group, random, savedTrainMatcher);
            }

            private TrainSpawnPattern.Applier createApplier(SpawnableGroup group, Random random, Function<String, String> savedTrainMatcher) {
               SavedTrainProperties properties = group.getTrainCarts().getSavedTrains().getProperties(SavedTrainSpawnPattern.this.name);
               if (!properties.hasSpawnPattern()) {
                  return TrainSpawnPattern.twoStage((g) -> {
                     return g.addTrainWithConfig(properties);
                  });
               } else {
                  Function<String, String> filteredSavedTrainMatcher = (name) -> {
                     String foundName = (String)savedTrainMatcher.apply(name);
                     if (foundName != null && foundName.equals(SavedTrainSpawnPattern.this.name)) {
                        foundName = null;
                     }

                     return foundName;
                  };
                  TrainSpawnPattern.Applier patternApplier = TrainSpawnPattern.parse(properties.getSpawnPattern(), filteredSavedTrainMatcher).newGroupApplier();
                  if ((Boolean)properties.getConfig().getOrDefault("flipped", false)) {
                     patternApplier = patternApplier.reverse();
                  }

                  return patternApplier;
               }
            }
         };
      }
   }

   public static class VanillaCartSpawnPattern extends TrainSpawnPattern {
      private final SpawnableGroup.VanillaCartType type;

      public VanillaCartSpawnPattern(TrainSpawnPattern.QuantityPrefix quantity, SpawnableGroup.VanillaCartType type) {
         super(quantity);
         this.type = type;
      }

      public SpawnableGroup.VanillaCartType type() {
         return this.type;
      }

      public String toString() {
         return this.quantity().toString() + this.type.getCode();
      }

      protected TrainSpawnPattern.Applier newGroupApplier() {
         return this.repeatWithAmount(TrainSpawnPattern.twoStage((group) -> {
            ConfigurationNode standardCartConfig = TrainPropertiesStore.getDefaultsByName("spawner").getConfig().clone();
            standardCartConfig.remove("carts");
            group.addTrainWithConfig(standardCartConfig);
            standardCartConfig.set("entityType", this.type.getType());
            return Collections.singletonList(group.addMember(standardCartConfig));
         }));
      }
   }
}
