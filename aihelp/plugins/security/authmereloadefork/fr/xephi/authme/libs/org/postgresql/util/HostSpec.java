package fr.xephi.authme.libs.org.postgresql.util;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.qual.Nullable;

public class HostSpec {
   public static final String DEFAULT_NON_PROXY_HOSTS = "localhost|127.*|[::1]|0.0.0.0|[::0]";
   @Nullable
   protected final String localSocketAddress;
   protected final String host;
   protected final int port;

   public HostSpec(String host, int port) {
      this(host, port, (String)null);
   }

   public HostSpec(String host, int port, @Nullable String localSocketAddress) {
      this.host = host;
      this.port = port;
      this.localSocketAddress = localSocketAddress;
   }

   public String getHost() {
      return this.host;
   }

   public int getPort() {
      return this.port;
   }

   public String toString() {
      return this.host + ":" + this.port;
   }

   public boolean equals(@Nullable Object obj) {
      return obj instanceof HostSpec && this.port == ((HostSpec)obj).port && this.host.equals(((HostSpec)obj).host) && Objects.equals(this.localSocketAddress, ((HostSpec)obj).localSocketAddress);
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.localSocketAddress, this.host, this.port});
   }

   @Nullable
   public String getLocalSocketAddress() {
      return this.localSocketAddress;
   }

   public Boolean shouldResolve() {
      String socksProxy = System.getProperty("socksProxyHost");
      return socksProxy != null && !socksProxy.trim().isEmpty() ? this.matchesNonProxyHosts() : true;
   }

   private Boolean matchesNonProxyHosts() {
      String nonProxyHosts = System.getProperty("socksNonProxyHosts", "localhost|127.*|[::1]|0.0.0.0|[::0]");
      if (nonProxyHosts != null && !this.host.isEmpty()) {
         Pattern pattern = this.toPattern(nonProxyHosts);
         Matcher matcher = pattern == null ? null : pattern.matcher(this.host);
         return matcher != null && matcher.matches();
      } else {
         return false;
      }
   }

   @Nullable
   private Pattern toPattern(String mask) {
      StringBuilder joiner = new StringBuilder();
      String separator = "";
      String[] var4 = mask.split("\\|");
      int var5 = var4.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String disjunct = var4[var6];
         if (!disjunct.isEmpty()) {
            String regex = this.disjunctToRegex(disjunct.toLowerCase(Locale.ROOT));
            joiner.append(separator).append(regex);
            separator = "|";
         }
      }

      return joiner.length() == 0 ? null : Pattern.compile(joiner.toString());
   }

   private String disjunctToRegex(String disjunct) {
      String regex;
      if (disjunct.startsWith("*")) {
         regex = ".*" + Pattern.quote(disjunct.substring(1));
      } else if (disjunct.endsWith("*")) {
         regex = Pattern.quote(disjunct.substring(0, disjunct.length() - 1)) + ".*";
      } else {
         regex = Pattern.quote(disjunct);
      }

      return regex;
   }
}
