package fr.xephi.authme.libs.org.apache.http.impl.conn;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.conn.DnsResolver;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDnsResolver implements DnsResolver {
   private final Log log = LogFactory.getLog(InMemoryDnsResolver.class);
   private final Map<String, InetAddress[]> dnsMap = new ConcurrentHashMap();

   public void add(String host, InetAddress... ips) {
      Args.notNull(host, "Host name");
      Args.notNull(ips, "Array of IP addresses");
      this.dnsMap.put(host, ips);
   }

   public InetAddress[] resolve(String host) throws UnknownHostException {
      InetAddress[] resolvedAddresses = (InetAddress[])this.dnsMap.get(host);
      if (this.log.isInfoEnabled()) {
         this.log.info("Resolving " + host + " to " + Arrays.deepToString(resolvedAddresses));
      }

      if (resolvedAddresses == null) {
         throw new UnknownHostException(host + " cannot be resolved");
      } else {
         return resolvedAddresses;
      }
   }
}
