package com.bergerkiller.bukkit.tc.controller.components;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.IPropertiesHolder;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import com.bergerkiller.bukkit.tc.properties.standard.type.SignSkipOptions;
import com.bergerkiller.bukkit.tc.rails.RailLookup;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

public class SignSkipTracker {
   private final IPropertiesHolder owner;
   private boolean isLoaded = false;
   private final Map<RailLookup.TrackedSign, Boolean> history = new HashMap();

   public SignSkipTracker(IPropertiesHolder owner) {
      this.owner = owner;
   }

   public boolean isSkipped(RailLookup.TrackedSign sign) {
      return Boolean.TRUE.equals(this.history.get(sign));
   }

   public List<RailLookup.TrackedSign> getSkippedSigns() {
      return this.history.isEmpty() ? Collections.emptyList() : (List)this.history.entrySet().stream().filter(Entry::getValue).map(Entry::getKey).collect(Collectors.toList());
   }

   public void setSkipped(SignTracker.ActiveSign sign) {
      this.setSkipped(sign, true);
   }

   public void setSkipped(SignTracker.ActiveSign sign, boolean skipped) {
      this.history.put(sign.getSign(), skipped);
   }

   public void loadSigns(List<SignTracker.ActiveSign> signs) {
      this.isLoaded = true;
      this.history.clear();
      if (!signs.isEmpty()) {
         SignSkipOptions options = (SignSkipOptions)this.owner.getProperties().get(StandardProperties.SIGN_SKIP);
         Iterator var3;
         if (options.hasSkippedSigns()) {
            var3 = options.skippedSigns().iterator();

            label43:
            while(true) {
               while(true) {
                  if (!var3.hasNext()) {
                     break label43;
                  }

                  BlockLocation signPos = (BlockLocation)var3.next();
                  Iterator var5 = signs.iterator();

                  while(var5.hasNext()) {
                     SignTracker.ActiveSign sign = (SignTracker.ActiveSign)var5.next();
                     Block signBlock = sign.getSign().signBlock;
                     if (signPos.x == signBlock.getX() && signPos.y == signBlock.getY() && signPos.z == signBlock.getZ() && signPos.world.equals(signBlock.getWorld().getName())) {
                        this.history.put(sign.getSign(), Boolean.TRUE);
                        break;
                     }
                  }
               }
            }
         }

         var3 = signs.iterator();

         while(var3.hasNext()) {
            SignTracker.ActiveSign sign = (SignTracker.ActiveSign)var3.next();
            this.history.putIfAbsent(sign.getSign(), Boolean.FALSE);
         }

      }
   }

   public void unloadSigns() {
      if (this.isLoaded) {
         this.isLoaded = false;
         this.history.clear();
      }

   }

   public void onSignVisitStart(List<SignTracker.ActiveSign> signs) {
      if (!this.isLoaded) {
         this.loadSigns(signs);
      }

      Iterator iter = this.history.entrySet().iterator();

      while(iter.hasNext()) {
         Entry<RailLookup.TrackedSign, Boolean> e = (Entry)iter.next();
         boolean found = false;
         Iterator var5 = signs.iterator();

         while(var5.hasNext()) {
            SignTracker.ActiveSign sign = (SignTracker.ActiveSign)var5.next();
            RailLookup.TrackedSign trackedSign = (RailLookup.TrackedSign)e.getKey();
            if (sign.getSign().equals(trackedSign)) {
               found = true;
               break;
            }
         }

         if (!found) {
            iter.remove();
         }
      }

   }

   public boolean onSignVisit(SignTracker.ActiveSign sign) {
      Boolean isSignSkipped = (Boolean)this.history.computeIfAbsent(sign.getSign(), (trackedSign) -> {
         IProperties properties = this.owner.getProperties();
         SignSkipOptions options = (SignSkipOptions)properties.get(StandardProperties.SIGN_SKIP);
         if (!options.isActive()) {
            return Boolean.FALSE;
         } else {
            boolean passFilter = true;
            if (options.hasFilter()) {
               if (trackedSign.sign == null) {
                  passFilter = false;
               } else {
                  passFilter = Util.getCleanLine((Sign)trackedSign.sign, 1).toLowerCase(Locale.ENGLISH).startsWith(options.filter());
               }
            }

            Boolean isNewSignSkipped;
            if (passFilter) {
               SignSkipTracker.SkipOptionChanges changes = new SignSkipTracker.SkipOptionChanges(options);
               isNewSignSkipped = changes.handleSkip();
               if (options.hasSkippedSigns() || changes.countersChanged) {
                  properties.set(StandardProperties.SIGN_SKIP, SignSkipOptions.create(changes.ignoreCounter, changes.skipCounter, options.filter(), Collections.emptySet()));
               }
            } else {
               isNewSignSkipped = Boolean.FALSE;
            }

            return isNewSignSkipped;
         }
      });
      return !isSignSkipped;
   }

   private static final class SkipOptionChanges {
      public int ignoreCounter;
      public int skipCounter;
      public boolean countersChanged;

      public SkipOptionChanges(SignSkipOptions options) {
         this.ignoreCounter = options.ignoreCounter();
         this.skipCounter = options.skipCounter();
         this.countersChanged = false;
      }

      public Boolean handleSkip() {
         if (this.ignoreCounter > 0) {
            --this.ignoreCounter;
            this.countersChanged = true;
            return Boolean.FALSE;
         } else if (this.skipCounter > 0) {
            --this.skipCounter;
            this.countersChanged = true;
            return Boolean.TRUE;
         } else {
            return Boolean.FALSE;
         }
      }
   }
}
