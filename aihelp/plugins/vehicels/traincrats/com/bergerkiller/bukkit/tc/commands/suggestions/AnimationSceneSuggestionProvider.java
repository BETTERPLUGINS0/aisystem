package com.bergerkiller.bukkit.tc.commands.suggestions;

import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.BlockingSuggestionProvider.Strings;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.attachments.animation.Animation;
import com.bergerkiller.bukkit.tc.controller.components.AnimationController;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class AnimationSceneSuggestionProvider implements Strings<CommandSender> {
   public static final AnimationSceneSuggestionProvider TRAIN_ANIMATION_SCENE = new AnimationSceneSuggestionProvider(true);
   public static final AnimationSceneSuggestionProvider CART_ANIMATION_SCENE = new AnimationSceneSuggestionProvider(false);
   private final boolean forTrain;

   private AnimationSceneSuggestionProvider(boolean forTrain) {
      this.forTrain = forTrain;
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull CommandContext<CommandSender> context, @NonNull CommandInput commandInput) {
      String input = commandInput.lastRemainingToken();
      String animationName = (String)context.getOrDefault("animation_name", "");
      if (animationName.isEmpty()) {
         return Collections.emptyList();
      } else {
         Object holder;
         try {
            if (this.forTrain) {
               TrainProperties properties = (TrainProperties)context.inject(TrainProperties.class).get();
               holder = properties.getHolder();
            } else {
               CartProperties properties = (CartProperties)context.inject(CartProperties.class).get();
               holder = properties.getHolder();
            }
         } catch (RuntimeException var8) {
            return Collections.emptyList();
         }

         if (holder == null) {
            return Collections.emptyList();
         } else {
            List<String> filtered = (List)((AnimationController)holder).getAnimationScenes(animationName).stream().filter((name) -> {
               return name.startsWith(input);
            }).collect(Collectors.toList());
            if (!filtered.isEmpty()) {
               return filtered;
            } else {
               Animation defaultAnim = (Animation)TCConfig.defaultAnimations.get(animationName);
               return (Iterable)(defaultAnim == null ? Collections.emptyList() : new ArrayList(defaultAnim.getSceneNames()));
            }
         }
      }
   }
}
