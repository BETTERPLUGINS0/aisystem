package ac.grim.grimac.shaded.incendo.cloud.bukkit.parser.location;

import ac.grim.grimac.shaded.incendo.cloud.bukkit.BukkitCommandContextKeys;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.BlockingSuggestionProvider;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class Location2DParser<C> implements ArgumentParser<C, Location2D>, BlockingSuggestionProvider.Strings<C> {
   private final LocationCoordinateParser<C> locationCoordinateParser = new LocationCoordinateParser();

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> ParserDescriptor<C, Location2D> location2DParser() {
      return ParserDescriptor.of(new Location2DParser(), (Class)Location2D.class);
   }

   @API(
      status = Status.STABLE,
      since = "2.0.0"
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, Location2D> location2DComponent() {
      return CommandComponent.builder().parser(location2DParser());
   }

   public ArgumentParseResult<Location2D> parse(final CommandContext<C> commandContext, final CommandInput commandInput) {
      if (commandInput.remainingTokens() < 2) {
         return ArgumentParseResult.failure(new LocationParser.LocationParseException(commandContext, LocationParser.LocationParseException.FailureReason.WRONG_FORMAT, commandInput.remainingInput()));
      } else {
         LocationCoordinate[] coordinates = new LocationCoordinate[2];

         for(int i = 0; i < 2; ++i) {
            if (commandInput.peekString().isEmpty()) {
               return ArgumentParseResult.failure(new LocationParser.LocationParseException(commandContext, LocationParser.LocationParseException.FailureReason.WRONG_FORMAT, commandInput.remainingInput()));
            }

            ArgumentParseResult<LocationCoordinate> coordinate = this.locationCoordinateParser.parse(commandContext, commandInput);
            if (coordinate.failure().isPresent()) {
               return ArgumentParseResult.failure((Throwable)coordinate.failure().get());
            }

            coordinates[i] = (LocationCoordinate)coordinate.parsedValue().orElseThrow(NullPointerException::new);
         }

         CommandSender bukkitSender = (CommandSender)commandContext.get(BukkitCommandContextKeys.BUKKIT_COMMAND_SENDER);
         Location originalLocation;
         if (bukkitSender instanceof BlockCommandSender) {
            originalLocation = ((BlockCommandSender)bukkitSender).getBlock().getLocation();
         } else if (bukkitSender instanceof Entity) {
            originalLocation = ((Entity)bukkitSender).getLocation();
         } else {
            originalLocation = new Location((World)Bukkit.getWorlds().get(0), 0.0D, 0.0D, 0.0D);
         }

         if (coordinates[0].type() == LocationCoordinateType.LOCAL && coordinates[1].type() != LocationCoordinateType.LOCAL) {
            return ArgumentParseResult.failure(new LocationParser.LocationParseException(commandContext, LocationParser.LocationParseException.FailureReason.MIXED_LOCAL_ABSOLUTE, ""));
         } else {
            if (coordinates[0].type() == LocationCoordinateType.ABSOLUTE) {
               originalLocation.setX(coordinates[0].coordinate());
            } else if (coordinates[0].type() == LocationCoordinateType.RELATIVE) {
               originalLocation.add(coordinates[0].coordinate(), 0.0D, 0.0D);
            }

            if (coordinates[1].type() == LocationCoordinateType.ABSOLUTE) {
               originalLocation.setZ(coordinates[1].coordinate());
            } else {
               if (coordinates[1].type() != LocationCoordinateType.RELATIVE) {
                  Vector declaredPos = new Vector(coordinates[0].coordinate(), 0.0D, coordinates[1].coordinate());
                  Location local = LocationParser.toLocalSpace(originalLocation, declaredPos);
                  return ArgumentParseResult.success(Location2D.from(originalLocation.getWorld(), local.getX(), local.getZ()));
               }

               originalLocation.add(0.0D, 0.0D, coordinates[1].coordinate());
            }

            return ArgumentParseResult.success(Location2D.from(originalLocation.getWorld(), originalLocation.getX(), originalLocation.getZ()));
         }
      }
   }

   @NonNull
   public Iterable<String> stringSuggestions(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput input) {
      return LocationParser.getSuggestions(2, commandContext, input);
   }
}
