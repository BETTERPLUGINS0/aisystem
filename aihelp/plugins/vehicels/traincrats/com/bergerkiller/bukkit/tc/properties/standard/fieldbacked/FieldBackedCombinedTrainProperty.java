package com.bergerkiller.bukkit.tc.properties.standard.fieldbacked;

import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class FieldBackedCombinedTrainProperty<T> {
   private final List<Set<T>> previousSets = new ArrayList();
   private Set<T> previousResult = Collections.emptySet();

   public Set<T> update(TrainProperties properties, FieldBackedStandardCartProperty<Set<T>> property) {
      boolean different = false;
      int index = 0;

      for(Iterator var5 = properties.iterator(); var5.hasNext(); ++index) {
         CartProperties cartProperties = (CartProperties)var5.next();
         Set<T> cartSet = (Set)property.get(cartProperties);
         if (index >= this.previousSets.size()) {
            different = true;
            this.previousSets.add(cartSet);
         } else if (this.previousSets.get(index) != cartSet) {
            different = true;
            this.previousSets.set(index, cartSet);
         }
      }

      while(this.previousSets.size() > index) {
         this.previousSets.remove(this.previousSets.size() - 1);
         different = true;
      }

      if (different) {
         Set<T> combined = new HashSet(Math.max(8, this.previousResult.size()));
         List var10000 = this.previousSets;
         Objects.requireNonNull(combined);
         var10000.forEach(combined::addAll);
         this.previousResult = combined.isEmpty() ? Collections.emptySet() : Collections.unmodifiableSet(combined);
      }

      return this.previousResult;
   }
}
