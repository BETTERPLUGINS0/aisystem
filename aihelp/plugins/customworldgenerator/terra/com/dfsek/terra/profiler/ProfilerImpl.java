package com.dfsek.terra.profiler;

import com.dfsek.terra.api.profiler.Profiler;
import com.dfsek.terra.api.profiler.Timings;
import com.dfsek.terra.api.util.mutable.MutableInteger;
import com.dfsek.terra.profiler.exception.MalformedStackException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfilerImpl implements Profiler {
   private static final Logger logger = LoggerFactory.getLogger(ProfilerImpl.class);
   private static final ThreadLocal<Stack<Frame>> THREAD_STACK = ThreadLocal.withInitial(Stack::new);
   private static final ThreadLocal<Map<String, List<Long>>> TIMINGS = ThreadLocal.withInitial(HashMap::new);
   private static final ThreadLocal<Boolean> SAFE = ThreadLocal.withInitial(() -> {
      return false;
   });
   private static final ThreadLocal<MutableInteger> STACK_SIZE = ThreadLocal.withInitial(() -> {
      return new MutableInteger(0);
   });
   private static boolean instantiated = false;
   private final List<Map<String, List<Long>>> accessibleThreadMaps = new ArrayList();
   private volatile boolean running = false;

   public ProfilerImpl() {
      if (instantiated) {
         throw new IllegalStateException("Only one instance of Profiler may exist!");
      } else {
         instantiated = true;
      }
   }

   public void push(String frame) {
      if (this.running) {
         ((MutableInteger)STACK_SIZE.get()).increment();
         if ((Boolean)SAFE.get()) {
            Stack<Frame> stack = (Stack)THREAD_STACK.get();
            stack.push(new Frame(stack.isEmpty() ? frame : ((Frame)stack.peek()).getId() + "." + frame));
         } else {
            SAFE.set(false);
         }
      }

   }

   public void pop(String frame) {
      if (this.running) {
         MutableInteger size = (MutableInteger)STACK_SIZE.get();
         size.decrement();
         if ((Boolean)SAFE.get()) {
            long time = System.nanoTime();
            Stack<Frame> stack = (Stack)THREAD_STACK.get();
            Map<String, List<Long>> timingsMap = (Map)TIMINGS.get();
            if (timingsMap.isEmpty()) {
               synchronized(this.accessibleThreadMaps) {
                  this.accessibleThreadMaps.add(timingsMap);
               }
            }

            Frame top = (Frame)stack.pop();
            if (!stack.isEmpty()) {
               if (!top.getId().endsWith("." + frame)) {
                  throw new MalformedStackException("Expected " + frame + ", found " + String.valueOf(top));
               }
            } else if (!top.getId().equals(frame)) {
               throw new MalformedStackException("Expected " + frame + ", found " + String.valueOf(top));
            }

            List<Long> timings = (List)timingsMap.computeIfAbsent(top.getId(), (id) -> {
               return new ArrayList();
            });
            timings.add(time - top.getStart());
         }

         if ((Integer)size.get() == 0) {
            SAFE.set(true);
         }
      }

   }

   public void start() {
      logger.info("Starting Terra profiler");
      this.running = true;
   }

   public void stop() {
      logger.info("Stopping Terra profiler");
      this.running = false;
      SAFE.set(false);
   }

   public void reset() {
      logger.info("Resetting Terra profiler");
      this.accessibleThreadMaps.forEach(Map::clear);
   }

   public Map<String, Timings> getTimings() {
      Map<String, Timings> map = new HashMap();
      synchronized(this.accessibleThreadMaps) {
         this.accessibleThreadMaps.forEach((smap) -> {
            smap.forEach((key, list) -> {
               String[] keys = key.split("\\.");
               Timings timings = (Timings)map.computeIfAbsent(keys[0], (id) -> {
                  return new Timings();
               });

               for(int i = 1; i < keys.length; ++i) {
                  timings = timings.getSubItem(keys[i]);
               }

               ArrayList var10000 = new ArrayList(list);
               Objects.requireNonNull(timings);
               var10000.forEach(timings::addTime);
            });
         });
         return map;
      }
   }
}
