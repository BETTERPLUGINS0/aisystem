package fr.xephi.authme.libs.org.apache.http.impl.conn;

import fr.xephi.authme.libs.org.apache.http.conn.DnsResolver;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SystemDefaultDnsResolver implements DnsResolver {
   public static final SystemDefaultDnsResolver INSTANCE = new SystemDefaultDnsResolver();

   public InetAddress[] resolve(String host) throws UnknownHostException {
      return InetAddress.getAllByName(host);
   }
}
