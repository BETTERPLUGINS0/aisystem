package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.config.Lookup;
import fr.xephi.authme.libs.org.apache.http.config.RegistryBuilder;
import fr.xephi.authme.libs.org.apache.http.conn.util.PublicSuffixMatcher;
import fr.xephi.authme.libs.org.apache.http.conn.util.PublicSuffixMatcherLoader;
import fr.xephi.authme.libs.org.apache.http.cookie.CookieSpecProvider;
import fr.xephi.authme.libs.org.apache.http.impl.cookie.DefaultCookieSpecProvider;
import fr.xephi.authme.libs.org.apache.http.impl.cookie.IgnoreSpecProvider;
import fr.xephi.authme.libs.org.apache.http.impl.cookie.NetscapeDraftSpecProvider;
import fr.xephi.authme.libs.org.apache.http.impl.cookie.RFC6265CookieSpecProvider;

public final class CookieSpecRegistries {
   public static RegistryBuilder<CookieSpecProvider> createDefaultBuilder(PublicSuffixMatcher publicSuffixMatcher) {
      CookieSpecProvider defaultProvider = new DefaultCookieSpecProvider(publicSuffixMatcher);
      CookieSpecProvider laxStandardProvider = new RFC6265CookieSpecProvider(RFC6265CookieSpecProvider.CompatibilityLevel.RELAXED, publicSuffixMatcher);
      CookieSpecProvider strictStandardProvider = new RFC6265CookieSpecProvider(RFC6265CookieSpecProvider.CompatibilityLevel.STRICT, publicSuffixMatcher);
      return RegistryBuilder.create().register("default", defaultProvider).register("best-match", defaultProvider).register("compatibility", defaultProvider).register("standard", laxStandardProvider).register("standard-strict", strictStandardProvider).register("netscape", new NetscapeDraftSpecProvider()).register("ignoreCookies", new IgnoreSpecProvider());
   }

   public static RegistryBuilder<CookieSpecProvider> createDefaultBuilder() {
      return createDefaultBuilder(PublicSuffixMatcherLoader.getDefault());
   }

   public static Lookup<CookieSpecProvider> createDefault() {
      return createDefault(PublicSuffixMatcherLoader.getDefault());
   }

   public static Lookup<CookieSpecProvider> createDefault(PublicSuffixMatcher publicSuffixMatcher) {
      return createDefaultBuilder(publicSuffixMatcher).build();
   }

   private CookieSpecRegistries() {
   }
}
