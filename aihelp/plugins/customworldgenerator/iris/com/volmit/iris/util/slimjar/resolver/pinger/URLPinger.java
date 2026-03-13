package com.volmit.iris.util.slimjar.resolver.pinger;

import java.net.URL;
import org.jetbrains.annotations.NotNull;

public interface URLPinger {
   boolean ping(@NotNull URL var1);

   boolean isSupported(@NotNull URL var1);
}
