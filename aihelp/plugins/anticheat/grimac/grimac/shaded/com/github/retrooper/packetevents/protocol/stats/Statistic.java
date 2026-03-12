package ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.stats;

import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public interface Statistic {
   String getId();

   Component display();
}
