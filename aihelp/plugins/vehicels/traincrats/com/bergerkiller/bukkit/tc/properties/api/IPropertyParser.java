package com.bergerkiller.bukkit.tc.properties.api;

import com.bergerkiller.bukkit.common.utils.LogicUtil;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.IProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyInputContext;
import java.util.Iterator;
import java.util.function.Consumer;

public interface IPropertyParser<T> {
   IProperty<T> getProperty();

   String getName();

   boolean isInputPreProcessed();

   boolean isProcessedPerCart();

   default PropertyParseResult<T> parse(IProperties properties, String input) {
      return this.parse(properties, PropertyInputContext.of(input));
   }

   PropertyParseResult<T> parse(IProperties var1, PropertyInputContext var2);

   default PropertyParseResult<T> parseAndSet(IProperties properties, String input) {
      return this.parseAndSet(properties, input, LogicUtil.noopConsumer());
   }

   default PropertyParseResult<T> parseAndSet(IProperties properties, String input, Consumer<PropertyParseResult<?>> beforeSet) {
      return this.parseAndSet(properties, PropertyInputContext.of(input).beforeSet(beforeSet));
   }

   default PropertyParseResult<T> parseAndSet(IProperties properties, PropertyInputContext inputContext) {
      if (this.isProcessedPerCart() && properties instanceof TrainProperties) {
         TrainProperties trainProperties = (TrainProperties)properties;
         if (trainProperties.isEmpty()) {
            return this.parse(properties, inputContext);
         } else {
            boolean successful = false;
            String name = inputContext.input();
            Iterator<CartProperties> cartIter = trainProperties.iterator();
            CartProperties cartProp = (CartProperties)cartIter.next();
            PropertyParseResult<T> result = inputContext.handleBeforeSet(this.parse(cartProp, (PropertyInputContext)inputContext));
            if (result.isSuccessful()) {
               cartProp.set(this.getProperty(), result.getValue());
               name = result.getName();
               successful = true;
            }

            while(cartIter.hasNext()) {
               cartProp = (CartProperties)cartIter.next();
               PropertyParseResult<T> cartResult = inputContext.handleBeforeSet(this.parse(cartProp, (PropertyInputContext)inputContext));
               if (cartResult.isSuccessful()) {
                  cartProp.set(this.getProperty(), cartResult.getValue());
                  name = result.getName();
                  successful = true;
               }
            }

            if (successful) {
               result = PropertyParseResult.success(inputContext, this.getProperty(), name, properties.get(this.getProperty()));
            }

            return result;
         }
      } else {
         PropertyParseResult<T> result = inputContext.handleBeforeSet(this.parse(properties, inputContext));
         if (result.isSuccessful()) {
            properties.set(this.getProperty(), result.getValue());
         }

         return result;
      }
   }
}
