package com.bergerkiller.bukkit.tc.commands.suggestions;

import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.BlockingSuggestionProvider.Strings;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.controller.components.AnimationController;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class AnimationNameSuggestionProvider implements Strings<CommandSender> {
   public static final AnimationNameSuggestionProvider TRAIN_ANIMATION_NAME = new AnimationNameSuggestionProvider(true);
   public static final AnimationNameSuggestionProvider CART_ANIMATION_NAME = new AnimationNameSuggestionProvider(false);
   private final boolean forTrain;

   private AnimationNameSuggestionProvider(boolean forTrain) {
      this.forTrain = forTrain;
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull CommandContext<CommandSender> context, @NonNull CommandInput commandInput) {
      String input = commandInput.lastRemainingToken();

      Object holder;
      try {
         if (this.forTrain) {
            TrainProperties properties = (TrainProperties)context.inject(TrainProperties.class).get();
            holder = properties.getHolder();
         } else {
            CartProperties properties = (CartProperties)context.inject(CartProperties.class).get();
            holder = properties.getHolder();
         }
      } catch (RuntimeException var6) {
         return Collections.emptyList();
      }

      if (holder == null) {
         return Collections.emptyList();
      } else {
         List<String> filtered = (List)((AnimationController)holder).getAnimationNames().stream().filter((name) -> {
            return name.startsWith(input);
         }).collect(Collectors.toList());
         return (Iterable)(!filtered.isEmpty() ? filtered : new ArrayList(TCConfig.defaultAnimations.keySet()));
      }
   }
}
