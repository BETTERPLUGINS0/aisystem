package fr.xephi.authme.libs.waffle.servlet;

import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import fr.xephi.authme.libs.waffle.servlet.spi.SecurityFilterProvider;
import fr.xephi.authme.libs.waffle.servlet.spi.SecurityFilterProviderCollection;
import fr.xephi.authme.libs.waffle.util.AuthorizationHeader;
import fr.xephi.authme.libs.waffle.util.CorsPreFlightCheck;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsAuthProvider;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsIdentity;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsImpersonationContext;
import fr.xephi.authme.libs.waffle.windows.auth.PrincipalFormat;
import fr.xephi.authme.libs.waffle.windows.auth.impl.WindowsAuthProviderImpl;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import javax.security.auth.Subject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class NegotiateSecurityFilter implements Filter {
   private static final Logger LOGGER = LoggerFactory.getLogger(NegotiateSecurityFilter.class);
   private static final String PRINCIPALSESSIONKEY = NegotiateSecurityFilter.class.getName() + ".PRINCIPAL";
   private static Boolean windows;
   private PrincipalFormat principalFormat;
   private PrincipalFormat roleFormat;
   private SecurityFilterProviderCollection providers;
   private IWindowsAuthProvider auth;
   private String[] excludePatterns;
   private boolean allowGuestLogin;
   private boolean impersonate;
   private boolean excludeBearerAuthorization;
   private boolean excludeCorsPreflight;
   private boolean disableSSO;

   public NegotiateSecurityFilter() {
      this.principalFormat = PrincipalFormat.FQN;
      this.roleFormat = PrincipalFormat.FQN;
      this.allowGuestLogin = true;
      LOGGER.debug("[waffle.servlet.NegotiateSecurityFilter] loaded");
   }

   public void destroy() {
      LOGGER.info("[waffle.servlet.NegotiateSecurityFilter] stopped");
   }

   public void doFilter(ServletRequest sreq, ServletResponse sres, FilterChain chain) throws IOException, ServletException {
      HttpServletRequest request = (HttpServletRequest)sreq;
      HttpServletResponse response = (HttpServletResponse)sres;
      LOGGER.debug("{} {}, contentlength: {}", request.getMethod(), request.getRequestURI(), request.getContentLength());
      if (!isWindows()) {
         LOGGER.debug("Running in a non windows environment, SSO skipped");
         chain.doFilter(request, response);
      } else if (this.disableSSO) {
         LOGGER.debug("SSO is disabled, resuming filter chain");
         chain.doFilter(request, response);
      } else {
         if (request.getRequestURL() != null && this.excludePatterns != null) {
            String url = request.getRequestURL().toString();
            String[] var7 = this.excludePatterns;
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
               String pattern = var7[var9];
               if (url.matches(pattern)) {
                  LOGGER.info((String)"Pattern :{} excluded URL:{}", (Object)url, (Object)pattern);
                  chain.doFilter(sreq, sres);
                  return;
               }
            }
         }

         if (this.excludeCorsPreflight && CorsPreFlightCheck.isPreflight(request)) {
            LOGGER.debug("[waffle.servlet.NegotiateSecurityFilter] CORS preflight");
            chain.doFilter(sreq, sres);
         } else {
            AuthorizationHeader authorizationHeader = new AuthorizationHeader(request);
            if (this.excludeBearerAuthorization && authorizationHeader.isBearerAuthorizationHeader()) {
               LOGGER.debug("[waffle.servlet.NegotiateSecurityFilter] Authorization: Bearer");
               chain.doFilter(sreq, sres);
            } else if (!this.doFilterPrincipal(request, response, chain)) {
               if (authorizationHeader.isNull()) {
                  LOGGER.debug("authorization required");
                  this.sendUnauthorized(response, false);
               } else {
                  IWindowsIdentity windowsIdentity;
                  try {
                     windowsIdentity = this.providers.doFilter(request, response);
                     if (windowsIdentity == null) {
                        return;
                     }
                  } catch (IOException var17) {
                     LOGGER.warn((String)"error logging in user: {}", (Object)var17.getMessage());
                     LOGGER.trace((String)"", (Throwable)var17);
                     this.sendUnauthorized(response, true);
                     return;
                  }

                  IWindowsImpersonationContext ctx = null;

                  try {
                     if (!this.allowGuestLogin && windowsIdentity.isGuest()) {
                        LOGGER.warn((String)"guest login disabled: {}", (Object)windowsIdentity.getFqn());
                        this.sendUnauthorized(response, true);
                        return;
                     }

                     LOGGER.debug((String)"logged in user: {} ({})", (Object)windowsIdentity.getFqn(), (Object)windowsIdentity.getSidString());
                     HttpSession session = request.getSession(true);
                     if (session == null) {
                        throw new ServletException("Expected HttpSession");
                     }

                     Subject subject = (Subject)session.getAttribute("javax.security.auth.subject");
                     if (subject == null) {
                        subject = new Subject();
                     }

                     Object windowsPrincipal;
                     if (this.impersonate) {
                        windowsPrincipal = new AutoDisposableWindowsPrincipal(windowsIdentity, this.principalFormat, this.roleFormat);
                     } else {
                        windowsPrincipal = new WindowsPrincipal(windowsIdentity, this.principalFormat, this.roleFormat);
                     }

                     LOGGER.debug((String)"roles: {}", (Object)((WindowsPrincipal)windowsPrincipal).getRolesString());
                     subject.getPrincipals().add(windowsPrincipal);
                     request.getSession(false).setAttribute("javax.security.auth.subject", subject);
                     LOGGER.info((String)"successfully logged in user: {}", (Object)windowsIdentity.getFqn());
                     request.getSession(false).setAttribute(PRINCIPALSESSIONKEY, windowsPrincipal);
                     NegotiateRequestWrapper requestWrapper = new NegotiateRequestWrapper(request, (WindowsPrincipal)windowsPrincipal);
                     if (this.impersonate) {
                        LOGGER.debug("impersonating user");
                        ctx = windowsIdentity.impersonate();
                     }

                     chain.doFilter(requestWrapper, response);
                  } finally {
                     if (this.impersonate && ctx != null) {
                        LOGGER.debug("terminating impersonation");
                        ctx.revertToSelf();
                     } else {
                        windowsIdentity.dispose();
                     }

                  }

               }
            }
         }
      }
   }

   private boolean doFilterPrincipal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
      Principal principal = request.getUserPrincipal();
      if (principal == null) {
         HttpSession session = request.getSession(false);
         if (session != null) {
            principal = (Principal)session.getAttribute(PRINCIPALSESSIONKEY);
         }
      }

      if (principal == null) {
         return false;
      } else if (this.providers.isPrincipalException(request)) {
         return false;
      } else {
         if (principal instanceof WindowsPrincipal) {
            LOGGER.debug((String)"previously authenticated Windows user: {}", (Object)principal.getName());
            WindowsPrincipal windowsPrincipal = (WindowsPrincipal)principal;
            if (this.impersonate && windowsPrincipal.getIdentity() == null) {
               return false;
            }

            NegotiateRequestWrapper requestWrapper = new NegotiateRequestWrapper(request, windowsPrincipal);
            IWindowsImpersonationContext ctx = null;
            if (this.impersonate) {
               LOGGER.debug("re-impersonating user");
               ctx = windowsPrincipal.getIdentity().impersonate();
            }

            try {
               chain.doFilter(requestWrapper, response);
            } finally {
               if (this.impersonate && ctx != null) {
                  LOGGER.debug("terminating impersonation");
                  ctx.revertToSelf();
               }

            }
         } else {
            LOGGER.debug((String)"previously authenticated user: {}", (Object)principal.getName());
            chain.doFilter(request, response);
         }

         return true;
      }
   }

   public void init(FilterConfig filterConfig) throws ServletException {
      Map<String, String> implParameters = new HashMap();
      LOGGER.debug("[waffle.servlet.NegotiateSecurityFilter] starting");
      String authProvider = null;
      String[] providerNames = null;
      if (filterConfig != null) {
         List<String> parameterNames = Collections.list(filterConfig.getInitParameterNames());
         LOGGER.debug("[waffle.servlet.NegotiateSecurityFilter] processing filterConfig");
         Iterator var6 = parameterNames.iterator();

         while(var6.hasNext()) {
            String parameterName = (String)var6.next();
            String parameterValue = filterConfig.getInitParameter(parameterName);
            LOGGER.debug((String)"Init Param: '{}={}'", (Object)parameterName, (Object)parameterValue);
            byte var10 = -1;
            switch(parameterName.hashCode()) {
            case -1903267686:
               if (parameterName.equals("allowGuestLogin")) {
                  var10 = 2;
               }
               break;
            case -1618913785:
               if (parameterName.equals("disableSSO")) {
                  var10 = 9;
               }
               break;
            case -1394073671:
               if (parameterName.equals("authProvider")) {
                  var10 = 5;
               }
               break;
            case -1287500630:
               if (parameterName.equals("securityFilterProviders")) {
                  var10 = 4;
               }
               break;
            case -1194612130:
               if (parameterName.equals("excludeBearerAuthorization")) {
                  var10 = 8;
               }
               break;
            case -695338555:
               if (parameterName.equals("principalFormat")) {
                  var10 = 0;
               }
               break;
            case 360271581:
               if (parameterName.equals("excludePatterns")) {
                  var10 = 6;
               }
               break;
            case 478304236:
               if (parameterName.equals("excludeCorsPreflight")) {
                  var10 = 7;
               }
               break;
            case 1106883597:
               if (parameterName.equals("roleFormat")) {
                  var10 = 1;
               }
               break;
            case 1206722169:
               if (parameterName.equals("impersonate")) {
                  var10 = 3;
               }
            }

            switch(var10) {
            case 0:
               this.principalFormat = PrincipalFormat.valueOf(parameterValue.toUpperCase(Locale.ENGLISH));
               break;
            case 1:
               this.roleFormat = PrincipalFormat.valueOf(parameterValue.toUpperCase(Locale.ENGLISH));
               break;
            case 2:
               this.allowGuestLogin = Boolean.parseBoolean(parameterValue);
               break;
            case 3:
               this.impersonate = Boolean.parseBoolean(parameterValue);
               break;
            case 4:
               providerNames = parameterValue.split("\\s+", -1);
               break;
            case 5:
               authProvider = parameterValue;
               break;
            case 6:
               this.excludePatterns = parameterValue.split("\\s+", -1);
               break;
            case 7:
               this.excludeCorsPreflight = Boolean.parseBoolean(parameterValue);
               break;
            case 8:
               this.excludeBearerAuthorization = Boolean.parseBoolean(parameterValue);
               break;
            case 9:
               this.disableSSO = Boolean.parseBoolean(parameterValue);
               break;
            default:
               implParameters.put(parameterName, parameterValue);
            }
         }
      }

      LOGGER.debug("[waffle.servlet.NegotiateSecurityFilter] authProvider");
      if (authProvider != null) {
         try {
            this.auth = (IWindowsAuthProvider)Class.forName(authProvider).getConstructor().newInstance();
         } catch (IllegalArgumentException | SecurityException | InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException var13) {
            throw new ServletException(var13);
         }
      }

      if (this.auth == null) {
         this.auth = new WindowsAuthProviderImpl();
      }

      if (providerNames != null) {
         this.providers = new SecurityFilterProviderCollection(providerNames, this.auth);
      }

      if (this.providers == null) {
         LOGGER.debug("initializing default security filter providers");
         this.providers = new SecurityFilterProviderCollection(this.auth);
      }

      LOGGER.debug("[waffle.servlet.NegotiateSecurityFilter] load provider parameters");
      Iterator var14 = implParameters.entrySet().iterator();

      while(var14.hasNext()) {
         Entry<String, String> implParameter = (Entry)var14.next();
         String[] classAndParameter = ((String)implParameter.getKey()).split("/", 2);
         if (classAndParameter.length != 2) {
            LOGGER.error("Invalid parameter: {}", implParameter.getKey());
            throw new ServletException("Invalid parameter: " + (String)implParameter.getKey());
         }

         try {
            LOGGER.debug("setting {}, {}={}", classAndParameter[0], classAndParameter[1], implParameter.getValue());
            SecurityFilterProvider provider = this.providers.getByClassName(classAndParameter[0]);
            provider.initParameter(classAndParameter[1], (String)implParameter.getValue());
         } catch (ClassNotFoundException var11) {
            LOGGER.error((String)"invalid class: {} in {}", (Object)classAndParameter[0], (Object)implParameter.getKey());
            throw new ServletException(var11);
         } catch (Exception var12) {
            LOGGER.error((String)"Error setting {} in {}", (Object)classAndParameter[0], (Object)classAndParameter[1]);
            throw new ServletException(var12);
         }
      }

      LOGGER.info("[waffle.servlet.NegotiateSecurityFilter] started");
   }

   public void setPrincipalFormat(String format) {
      this.principalFormat = PrincipalFormat.valueOf(format.toUpperCase(Locale.ENGLISH));
      LOGGER.info((String)"principal format: {}", (Object)this.principalFormat);
   }

   public PrincipalFormat getPrincipalFormat() {
      return this.principalFormat;
   }

   public void setRoleFormat(String format) {
      this.roleFormat = PrincipalFormat.valueOf(format.toUpperCase(Locale.ENGLISH));
      LOGGER.info((String)"role format: {}", (Object)this.roleFormat);
   }

   public PrincipalFormat getRoleFormat() {
      return this.roleFormat;
   }

   private void sendUnauthorized(HttpServletResponse response, boolean close) {
      try {
         this.providers.sendUnauthorized(response);
         if (close) {
            response.setHeader("Connection", "close");
         } else {
            response.setHeader("Connection", "keep-alive");
         }

         response.sendError(401);
         response.flushBuffer();
      } catch (IOException var4) {
         throw new RuntimeException(var4);
      }
   }

   public IWindowsAuthProvider getAuth() {
      return this.auth;
   }

   public void setAuth(IWindowsAuthProvider provider) {
      this.auth = provider;
   }

   public boolean isAllowGuestLogin() {
      return this.allowGuestLogin;
   }

   public void setImpersonate(boolean value) {
      this.impersonate = value;
   }

   public boolean isImpersonate() {
      return this.impersonate;
   }

   public SecurityFilterProviderCollection getProviders() {
      return this.providers;
   }

   private static boolean isWindows() {
      if (windows == null) {
         windows = System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("win");
      }

      return windows;
   }
}
