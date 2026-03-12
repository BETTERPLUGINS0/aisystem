package fr.xephi.authme.libs.org.apache.http.conn.routing;

import fr.xephi.authme.libs.org.apache.http.HttpHost;
import java.net.InetAddress;

public interface RouteInfo {
   HttpHost getTargetHost();

   InetAddress getLocalAddress();

   int getHopCount();

   HttpHost getHopTarget(int var1);

   HttpHost getProxyHost();

   RouteInfo.TunnelType getTunnelType();

   boolean isTunnelled();

   RouteInfo.LayerType getLayerType();

   boolean isLayered();

   boolean isSecure();

   public static enum LayerType {
      PLAIN,
      LAYERED;
   }

   public static enum TunnelType {
      PLAIN,
      TUNNELLED;
   }
}
