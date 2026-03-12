package ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufAllocationOperator;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.buffer.ByteBufOperator;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.netty.channel.ChannelOperator;

public interface NettyManager {
   ChannelOperator getChannelOperator();

   ByteBufOperator getByteBufOperator();

   ByteBufAllocationOperator getByteBufAllocationOperator();
}
