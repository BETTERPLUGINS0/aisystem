package com.bergerkiller.bukkit.tc.properties.api.context;

import com.bergerkiller.bukkit.common.utils.ParseUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.api.PropertyInvalidInputException;
import com.bergerkiller.bukkit.tc.statements.Statement;
import java.util.regex.MatchResult;

public final class PropertyParseContext<T> extends PropertyContext {
   private final T current;
   private final String name;
   private final PropertyInputContext input;
   private final MatchResult matchResult;

   public PropertyParseContext(TrainCarts traincarts, IProperties properties, T current, String name, PropertyInputContext input, MatchResult matchResult) {
      super(traincarts, properties);
      this.current = current;
      this.name = name;
      this.input = input;
      this.matchResult = matchResult;
   }

   public String name() {
      return this.name;
   }

   public String nameGroup(int index) {
      return index >= 0 && index <= this.matchResult.groupCount() ? this.matchResult.group(index) : "";
   }

   public String input() {
      return this.input.input();
   }

   public PropertyInputContext inputContext() {
      return this.input;
   }

   public float inputFloat() {
      float result = ParseUtil.parseFloat(this.input(), Float.NaN);
      if (Float.isNaN(result)) {
         throw new PropertyInvalidInputException("Not a number");
      } else {
         return result;
      }
   }

   public float inputFloatOrNaN() {
      return this.input().equalsIgnoreCase("none") ? Float.NaN : ParseUtil.parseFloat(this.input(), Float.NaN);
   }

   public double inputDouble() {
      double result = ParseUtil.parseDouble(this.input(), Double.NaN);
      if (Double.isNaN(result)) {
         throw new PropertyInvalidInputException("Not a number");
      } else {
         return result;
      }
   }

   public int inputInteger() {
      int result = ParseUtil.parseInt(this.input(), Integer.MAX_VALUE);
      if (result == Integer.MAX_VALUE && ParseUtil.parseInt(this.input(), 0) == 0) {
         throw new PropertyInvalidInputException("Not a number");
      } else {
         return result;
      }
   }

   public boolean inputBoolean() {
      if (!ParseUtil.isBool(this.input())) {
         Statement.MatchResult match = Statement.Matcher.of(this.input()).withSignEvent(this.input.signEvent()).withGroup(this.isTrainProperties() ? this.trainProperties().getHolder() : null).withMember(this.isCartProperties() ? this.cartProperties().getHolder() : null).match();
         if (!match.isExactMatch()) {
            throw new PropertyInvalidInputException("Not a boolean (true/false) or Statement expression");
         } else {
            boolean result = match.has();
            this.input.setHasParsedStatements(true);
            return result;
         }
      } else {
         return ParseUtil.parseBool(this.input());
      }
   }

   public T current() {
      return this.current;
   }
}
