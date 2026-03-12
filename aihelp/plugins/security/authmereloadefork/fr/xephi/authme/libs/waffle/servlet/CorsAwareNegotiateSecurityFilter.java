package fr.xephi.authme.libs.waffle.servlet;

import fr.xephi.authme.libs.org.slf4j.Logger;
import fr.xephi.authme.libs.org.slf4j.LoggerFactory;
import fr.xephi.authme.libs.waffle.util.AuthorizationHeader;
import fr.xephi.authme.libs.waffle.util.CorsPreFlightCheck;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class CorsAwareNegotiateSecurityFilter extends NegotiateSecurityFilter implements Filter {
   private static final Logger LOGGER = LoggerFactory.getLogger(CorsAwareNegotiateSecurityFilter.class);

   public CorsAwareNegotiateSecurityFilter() {
      LOGGER.info("[waffle.servlet.CorsAwareNegotiateSecurityFilter] loaded");
   }

   public void init(FilterConfig filterConfig) throws ServletException {
      LOGGER.info("[waffle.servlet.CorsAwareNegotiateSecurityFilter] Starting");
      super.init(filterConfig);
      LOGGER.info("[waffle.servlet.CorsAwareNegotiateSecurityFilter] Started");
   }

   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
      LOGGER.info("[waffle.servlet.CorsAwareNegotiateSecurityFilter] Filtering");
      HttpServletRequest httpServletRequest = (HttpServletRequest)request;
      AuthorizationHeader authorizationHeader = new AuthorizationHeader(httpServletRequest);
      if (CorsPreFlightCheck.isPreflight(httpServletRequest)) {
         LOGGER.info("[waffle.servlet.CorsAwareNegotiateSecurityFilter] Request is CORS preflight; continue filter chain");
         chain.doFilter(request, response);
      } else if (authorizationHeader.isBearerAuthorizationHeader()) {
         LOGGER.info("[waffle.servlet.CorsAwareNegotiateSecurityFilter] Request is Bearer, continue filter chain");
         chain.doFilter(request, response);
      } else {
         LOGGER.info("[waffle.servlet.CorsAwareNegotiateSecurityFilter] Request is Not CORS preflight");
         super.doFilter(request, response, chain);
         LOGGER.info("[waffle.servlet.CorsAwareNegotiateSecurityFilter] Authentication Completed");
      }

   }

   public void destroy() {
      super.destroy();
      LOGGER.info("[waffle.servlet.CorsAwareNegotiateSecurityFilter] unloaded");
   }
}
