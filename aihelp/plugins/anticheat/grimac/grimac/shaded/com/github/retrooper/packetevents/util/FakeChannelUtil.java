package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util;

public class FakeChannelUtil {
   public static boolean isFakeChannel(Object channel) {
      return channel.getClass().getSimpleName().equals("FakeChannel") || channel.getClass().getSimpleName().equals("SpoofedChannel") || channel.getClass().getSimpleName().equals("EmbeddedChannel");
   }
}
