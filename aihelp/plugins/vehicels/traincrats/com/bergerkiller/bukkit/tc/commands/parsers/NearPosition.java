package com.bergerkiller.bukkit.tc.commands.parsers;

import com.bergerkiller.bukkit.common.dep.cloud.bukkit.parser.location.LocationParser;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandContext;
import com.bergerkiller.bukkit.common.dep.cloud.context.CommandInput;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ArgumentParseResult;
import com.bergerkiller.bukkit.common.dep.cloud.parser.ParserDescriptor;
import com.bergerkiller.bukkit.common.dep.cloud.parser.aggregate.AggregateParser;
import com.bergerkiller.bukkit.common.dep.cloud.parser.standard.DoubleParser;
import com.bergerkiller.bukkit.common.dep.typetoken.TypeToken;
import java.util.LinkedList;
import java.util.Queue;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

public class NearPosition {
   public final Location at;
   public final double radius;

   public NearPosition(Location at, double radius) {
      this.at = at;
      this.radius = radius;
   }

   public String toString() {
      return "near{world=" + this.at.getWorld().getName() + ", x=" + this.at.getX() + ", y=" + this.at.getY() + ", z=" + this.at.getZ() + ", radius=" + this.radius + "}";
   }

   public static ArgumentParseResult<NearPosition> parseNearest(CommandContext<CommandSender> commandContext) {
      Queue<String> atSenderQueue = new LinkedList();
      atSenderQueue.add("~");
      atSenderQueue.add("~");
      atSenderQueue.add("~");
      ArgumentParseResult<Location> locationResult = (new LocationParser()).parse(commandContext, CommandInput.of(atSenderQueue));
      return locationResult.failure().isPresent() ? ArgumentParseResult.failure((Throwable)locationResult.failure().get()) : ArgumentParseResult.success(new NearPosition((Location)locationResult.parsedValue().get(), 128.0D));
   }

   public static ParserDescriptor<CommandSender, NearPosition> nearParser() {
      return AggregateParser.pairBuilder("location", LocationParser.locationParser(), "radius", DoubleParser.doubleParser(0.0D)).withMapper(TypeToken.get(NearPosition.class), (context, location, radius) -> {
         return ArgumentParseResult.successFuture(new NearPosition(location, radius));
      }).build();
   }
}
