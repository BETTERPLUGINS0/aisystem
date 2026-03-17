package com.bergerkiller.bukkit.tc.commands.parsers;

import com.bergerkiller.bukkit.common.BlockLocation;
import com.bergerkiller.bukkit.common.cloud.CloudLocalizedException;
import com.bergerkiller.bukkit.common.cloud.parsers.QuotedArgumentParser;
import com.bergerkiller.bukkit.common.dep.cloud.Command.Builder;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.BuilderModifier;
import com.bergerkiller.bukkit.common.dep.cloud.bukkit.parser.WorldParser;
import com.bergerkiller.bukkit.common.dep.cloud.component.CommandComponent;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParseResult;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParser;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ParserDescriptor;
import com.bergerkiller.bukkit.common.dep.cloud.parser.flag.CommandFlag;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.SuggestionProvider;
import com.bergerkiller.bukkit.common.dep.cloud.suggestion.BlockingSuggestionProvider.Strings;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecart;
import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.common.utils.WorldUtil;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.commands.suggestions.TrainNameSuggestionProvider;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.controller.MinecartMemberStore;
import com.bergerkiller.bukkit.tc.controller.global.TrainCartsPlayer;
import com.bergerkiller.bukkit.tc.exception.command.NoTrainNearbyException;
import com.bergerkiller.bukkit.tc.exception.command.NoTrainSelectedException;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.CartPropertiesStore;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

public class TrainTargetingFlags implements BuilderModifier<CommandTargetTrain, CommandSender> {
   public static final TrainTargetingFlags INSTANCE = new TrainTargetingFlags();
   private final CommandFlag<TrainProperties> flagTrain = CommandFlag.builder("train").withComponent(CommandComponent.builder("train_name", trainFlagParser())).build();
   private final CommandFlag<TrainTargetingFlags.CartSelectorResult> flagCart = CommandFlag.builder("cart").withComponent(CommandComponent.builder("cart_uuid", cartFlagParser())).build();
   private final CommandFlag<World> flagWorld = CommandFlag.builder("world").withComponent(CommandComponent.builder("world_name", WorldParser.worldParser())).build();
   private final CommandFlag<NearPosition> flagNear = CommandFlag.builder("near").withComponent(CommandComponent.builder("where", NearPosition.nearParser())).build();
   private final CommandFlag<Void> flagNearest = CommandFlag.builder("nearest").build();

   private TrainTargetingFlags() {
   }

   public boolean isTrainTargetingFlag(CommandFlag<?> flag) {
      return flag == this.flagTrain || flag == this.flagCart || flag == this.flagWorld || flag == this.flagNear || flag == this.flagNearest;
   }

   public Builder<? extends CommandSender> modifyBuilder(@NonNull CommandTargetTrain annotation, Builder<CommandSender> builder) {
      builder = builder.flag(this.flagTrain).flag(this.flagCart);
      builder = builder.flag(this.flagWorld).flag(this.flagNear).flag(this.flagNearest);
      return builder;
   }

   public CartProperties findCartProperties(CommandContext<CommandSender> context) {
      TrainProperties trainProperties = null;
      CartProperties cartProperties = null;
      if (context.flags().hasFlag(this.flagTrain.name())) {
         trainProperties = (TrainProperties)context.flags().get(this.flagTrain.name());
         if (!trainProperties.isEmpty()) {
            cartProperties = trainProperties.get(0);
         }
      }

      World atWorld = (World)context.flags().getValue(this.flagWorld.name(), (Object)null);
      if (context.flags().hasFlag(this.flagNear.name()) || context.flags().hasFlag(this.flagNearest.name())) {
         Permission.COMMAND_TARGET_NEAR.handle((CommandSender)context.sender());
         NearPosition near;
         if (context.flags().hasFlag(this.flagNear.name())) {
            near = (NearPosition)context.flags().get(this.flagNear.name());
         } else {
            ArgumentParseResult<NearPosition> parseResult = NearPosition.parseNearest(context);
            if (parseResult.failure().isPresent()) {
               Throwable t = (Throwable)parseResult.failure().get();
               if (t instanceof RuntimeException) {
                  throw (RuntimeException)t;
               }

               return null;
            }

            near = (NearPosition)parseResult.parsedValue().get();
         }

         if (atWorld != null) {
            near.at.setWorld(atWorld);
         }

         List<Entity> nearby = WorldUtil.getNearbyEntities(near.at, near.radius, near.radius, near.radius);
         double distanceSquaredMax = near.radius * near.radius;
         Stream<TrainTargetingFlags.MemberResult> nearbyMembers = nearby.stream().map(MinecartMemberStore::getFromEntity).filter(Objects::nonNull).map((member) -> {
            return new TrainTargetingFlags.MemberResult(member, near.at);
         }).filter((r) -> {
            return r.distanceSquared <= distanceSquaredMax;
         });
         if (trainProperties != null) {
            nearbyMembers = nearbyMembers.filter((r) -> {
               return r.member.getProperties().getTrainProperties() == trainProperties;
            });
         }

         Optional<TrainTargetingFlags.MemberResult> result = nearbyMembers.sorted().findFirst();
         if (!result.isPresent()) {
            throw new NoTrainNearbyException();
         }

         cartProperties = ((TrainTargetingFlags.MemberResult)result.get()).member.getProperties();
         trainProperties = cartProperties.getTrainProperties();
      }

      if (cartProperties == null && trainProperties == null && context.sender() instanceof Player) {
         cartProperties = ((TrainCartsPlayer)context.inject(TrainCartsPlayer.class).get()).getEditedCart();
         if (cartProperties != null) {
            trainProperties = cartProperties.getTrainProperties();
         }
      }

      if (context.flags().hasFlag(this.flagCart.name())) {
         TrainTargetingFlags.CartSelectorResult cartSelector = (TrainTargetingFlags.CartSelectorResult)context.flags().get(this.flagCart.name());
         if (cartSelector.cart_result != null) {
            if (trainProperties != null && trainProperties != cartSelector.cart_result.getTrainProperties()) {
               throw new CloudLocalizedException(context, Localization.COMMAND_CART_NOT_FOUND_IN_TRAIN, new String[]{"uuid=" + cartSelector.cart_result.getUUID().toString()});
            }

            cartProperties = cartSelector.cart_result;
            trainProperties = cartProperties.getTrainProperties();
         } else {
            if (trainProperties == null) {
               throw new NoTrainSelectedException();
            }

            int indexInCart = cartSelector.index_in_train < 0 ? trainProperties.size() + cartSelector.index_in_train : cartSelector.index_in_train;
            if (indexInCart < 0 || indexInCart >= trainProperties.size()) {
               throw new CloudLocalizedException(context, Localization.COMMAND_CART_NOT_FOUND_IN_TRAIN, new String[]{"index=" + cartSelector.index_in_train});
            }

            MinecartGroup group = trainProperties.getHolder();
            if (group == null) {
               cartProperties = trainProperties.get(indexInCart);
            } else {
               cartProperties = ((MinecartMember)group.get(indexInCart)).getProperties();
            }
         }
      }

      if (cartProperties != null && atWorld != null) {
         BlockLocation loc = cartProperties.getLocation();
         if (loc == null || loc.getWorld() != atWorld) {
            throw new NoTrainSelectedException();
         }
      }

      if (cartProperties == null) {
         throw new NoTrainSelectedException();
      } else {
         return cartProperties;
      }
   }

   @NotNull
   private static ParserDescriptor<CommandSender, TrainTargetingFlags.CartSelectorResult> cartFlagParser() {
      return ParserDescriptor.of(new TrainTargetingFlags.CartFlagParser(), TrainTargetingFlags.CartSelectorResult.class);
   }

   @NotNull
   private static ParserDescriptor<CommandSender, TrainProperties> trainFlagParser() {
      return (new TrainTargetingFlags.TrainFlagParser()).createDescriptor(TrainProperties.class);
   }

   private static class MemberResult implements Comparable<TrainTargetingFlags.MemberResult> {
      public final MinecartMember<?> member;
      public final double distanceSquared;

      public MemberResult(MinecartMember<?> member, Location at) {
         this.member = member;
         this.distanceSquared = ((CommonMinecart)member.getEntity()).loc.distanceSquared(at);
      }

      public int compareTo(TrainTargetingFlags.MemberResult o) {
         return Double.compare(this.distanceSquared, o.distanceSquared);
      }
   }

   private static class CartSelectorResult {
      public final CartProperties cart_result;
      public final int index_in_train;

      public CartSelectorResult(int index) {
         this.cart_result = null;
         this.index_in_train = index;
      }

      public CartSelectorResult(CartProperties result) {
         this.cart_result = result;
         this.index_in_train = Integer.MAX_VALUE;
      }
   }

   private static class CartFlagParser implements ArgumentParser<CommandSender, TrainTargetingFlags.CartSelectorResult>, Strings<CommandSender> {
      private CartFlagParser() {
      }

      @NonNull
      public ArgumentParseResult<TrainTargetingFlags.CartSelectorResult> parse(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput commandInput) {
         String uuidName = commandInput.readString();
         if (uuidName.equalsIgnoreCase("head")) {
            return ArgumentParseResult.success(new TrainTargetingFlags.CartSelectorResult(0));
         } else if (uuidName.equalsIgnoreCase("tail")) {
            return ArgumentParseResult.success(new TrainTargetingFlags.CartSelectorResult(-1));
         } else {
            int numCart = ParseUtil.parseInt(uuidName, Integer.MIN_VALUE);
            if (numCart != Integer.MIN_VALUE) {
               return ArgumentParseResult.success(new TrainTargetingFlags.CartSelectorResult(numCart));
            } else {
               try {
                  UUID uuid = UUID.fromString(uuidName);
                  CartProperties prop = CartPropertiesStore.getByUUID(uuid);
                  return prop == null ? ArgumentParseResult.failure(new CloudLocalizedException(commandContext, Localization.COMMAND_CART_NOT_FOUND_BY_UUID, new String[]{uuid.toString()})) : ArgumentParseResult.success(new TrainTargetingFlags.CartSelectorResult(prop));
               } catch (IllegalArgumentException var7) {
                  return ArgumentParseResult.failure(new CloudLocalizedException(commandContext, Localization.COMMAND_CART_NOT_FOUND_BY_UUID, new String[]{uuidName}));
               }
            }
         }
      }

      @NonNull
      public Iterable<String> stringSuggestions(@NonNull CommandContext<CommandSender> commandContext, @NonNull CommandInput input) {
         return input.isEmpty() ? (Iterable)Stream.concat(Stream.of("<uuid>", "head", "tail"), IntStream.range(0, 10).mapToObj(Integer::toString)).collect(Collectors.toList()) : (Iterable)IntStream.range(0, 10).mapToObj(Integer::toString).map((o) -> {
            return input + o;
         }).collect(Collectors.toList());
      }

      // $FF: synthetic method
      CartFlagParser(Object x0) {
         this();
      }
   }

   private static class TrainFlagParser implements QuotedArgumentParser<CommandSender, TrainProperties> {
      private final TrainNameSuggestionProvider suggestionProvider;

      private TrainFlagParser() {
         this.suggestionProvider = new TrainNameSuggestionProvider();
      }

      @NonNull
      public ArgumentParseResult<TrainProperties> parseQuotedString(@NonNull CommandContext<CommandSender> commandContext, String inputString) {
         TrainProperties properties = TrainPropertiesStore.get(inputString);
         if (properties == null) {
            properties = TrainPropertiesStore.getRelaxed(inputString);
         }

         if (properties == null) {
            return ArgumentParseResult.failure(new CloudLocalizedException(commandContext, Localization.COMMAND_TRAIN_NOT_FOUND, new String[]{inputString}));
         } else {
            commandContext.set("trainProperties", properties);
            return ArgumentParseResult.success(properties);
         }
      }

      @NonNull
      public SuggestionProvider<CommandSender> suggestionProvider() {
         return this.suggestionProvider;
      }

      // $FF: synthetic method
      TrainFlagParser(Object x0) {
         this();
      }
   }
}
