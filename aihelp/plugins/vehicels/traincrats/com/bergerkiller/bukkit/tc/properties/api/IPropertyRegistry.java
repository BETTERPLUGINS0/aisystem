package com.bergerkiller.bukkit.tc.properties.api;

import com.bergerkiller.bukkit.common.utils.CommonUtil;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyInputContext;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface IPropertyRegistry {
   <T> Optional<IPropertyParser<T>> findParser(String var1);

   default <T> Optional<IProperty<T>> find(String name) {
      return this.findParser(name).map(IPropertyParser::getProperty);
   }

   default <T> PropertyParseResult<T> parse(IProperties properties, String name, String input) {
      return this.parse(properties, name, PropertyInputContext.of(input));
   }

   default <T> PropertyParseResult<T> parse(IProperties properties, String name, PropertyInputContext inputContext) {
      Optional<IPropertyParser<T>> optParser = this.findParser(name);
      return optParser.isPresent() ? ((IPropertyParser)optParser.get()).parse(properties, inputContext) : PropertyParseResult.failPropertyNotFound(inputContext, name);
   }

   default <T> PropertyParseResult<T> parseAndSet(IProperties properties, String name, String input) {
      return this.parseAndSet(properties, name, PropertyInputContext.of(input));
   }

   default <T> PropertyParseResult<T> parseAndSet(IProperties properties, String name, String input, Consumer<PropertyParseResult<?>> beforeSet) {
      return this.parseAndSet(properties, name, PropertyInputContext.of(input).beforeSet(beforeSet));
   }

   default <T> PropertyParseResult<T> parseAndSet(IProperties properties, String name, PropertyInputContext inputContext) {
      Optional<IPropertyParser<T>> optParser = this.findParser(name);
      return optParser.isPresent() ? ((IPropertyParser)optParser.get()).parseAndSet(properties, inputContext) : PropertyParseResult.failPropertyNotFound(inputContext, name);
   }

   Collection<IProperty<Object>> all();

   Map<String, IProperty<Object>> byListedName();

   void register(IProperty<?> var1);

   void unregister(IProperty<?> var1);

   default void registerAll(Class<?> propertiesClass) {
      IProperty[] var2 = (IProperty[])CommonUtil.getClassConstants(propertiesClass, IProperty.class);
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         IProperty<?> property = var2[var4];
         this.register(property);
      }

   }

   default void unregisterAll(Class<?> propertiesClass) {
      IProperty[] var2 = (IProperty[])CommonUtil.getClassConstants(propertiesClass, IProperty.class);
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         IProperty<?> property = var2[var4];
         this.unregister(property);
      }

   }

   static IPropertyRegistry instance() {
      return TrainCarts.plugin.getPropertyRegistry();
   }
}
