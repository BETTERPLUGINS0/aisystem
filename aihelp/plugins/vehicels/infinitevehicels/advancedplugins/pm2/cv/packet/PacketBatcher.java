package advancedplugins.pm2.cv.packet;

import com.google.common.collect.Sets;
import io.netty.channel.ChannelPipeline;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class PacketBatcher {
   protected final Set<ChannelPipeline> pipelines = Sets.newConcurrentHashSet();
   protected volatile boolean active;

   public synchronized void begin() {
      this.active = true;
   }

   public synchronized void track(@NotNull Collection<ChannelPipeline> pipelines) {
      if (this.active) {
         this.pipelines.addAll(var1);
      }

   }

   public synchronized void complete() {
      if (this.active) {
         this.active = false;
         Iterator var1 = this.pipelines.iterator();

         while(var1.hasNext()) {
            ChannelPipeline var2 = (ChannelPipeline)var1.next();
            var2.flush();
         }

         this.pipelines.clear();
      }

   }
}
