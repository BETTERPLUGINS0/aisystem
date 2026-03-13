package com.dfsek.terra.event;

import com.dfsek.terra.api.addon.BaseAddon;
import com.dfsek.terra.api.event.events.Event;
import com.dfsek.terra.api.event.events.FailThroughEvent;
import com.dfsek.terra.api.event.events.PackEvent;
import com.dfsek.terra.api.event.functional.EventContext;
import com.dfsek.terra.api.event.functional.FunctionalEventHandler;
import com.dfsek.terra.api.util.reflection.TypeKey;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctionalEventHandlerImpl implements FunctionalEventHandler {
   private static final Logger logger = LoggerFactory.getLogger(FunctionalEventHandlerImpl.class);
   private final Map<Type, List<EventContextImpl<?>>> contextMap = new HashMap();

   public void handle(Event event) {
      ((List)this.contextMap.getOrDefault(event.getClass(), Collections.emptyList())).forEach((context) -> {
         try {
            if (event instanceof PackEvent) {
               if (context.isGlobal() || ((PackEvent)event).getPack().addons().containsKey(context.getAddon())) {
                  context.handle(event);
               }
            } else {
               context.handle(event);
            }
         } catch (Exception var3) {
            if (context.isFailThrough() && event instanceof FailThroughEvent) {
               throw var3;
            }

            logger.warn("Exception occurred during event handling. Report this to the maintainers of {}@{}", new Object[]{context.getAddon().getID(), context.getAddon().getVersion().getFormatted(), var3});
         }

      });
   }

   public <T extends Event> EventContext<T> register(BaseAddon addon, Class<T> clazz) {
      EventContextImpl<T> eventContext = new EventContextImpl(addon, clazz, this);
      ((List)this.contextMap.computeIfAbsent(clazz, (c) -> {
         return new ArrayList();
      })).add(eventContext);
      return eventContext;
   }

   public <T extends Event> EventContext<T> register(BaseAddon addon, TypeKey<T> clazz) {
      EventContextImpl<T> eventContext = new EventContextImpl(addon, clazz.getType(), this);
      ((List)this.contextMap.computeIfAbsent(clazz.getType(), (c) -> {
         return new ArrayList();
      })).add(eventContext);
      return eventContext;
   }

   public void recomputePriorities(Type target) {
      ((List)this.contextMap.get(target)).sort(Comparator.naturalOrder());
   }
}
