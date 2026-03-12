package fr.xephi.authme.libs.org.jboss.security.authorization.resources;

import fr.xephi.authme.libs.org.jboss.security.authorization.ResourceType;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class WebResource extends JavaEEResource {
   public static final String WEB_AUDIT_FLAG = "fr.xephi.authme.libs.org.jboss.security.web.audit";
   private ServletRequest servletRequest = null;
   private ServletResponse servletResponse = null;
   private String servletName = null;
   private String canonicalRequestURI = null;
   private static String auditFlag = " ";

   public WebResource() {
   }

   public WebResource(Map<String, Object> map) {
      this.map = map;
   }

   public ResourceType getLayer() {
      return ResourceType.WEB;
   }

   public String getCanonicalRequestURI() {
      return this.canonicalRequestURI;
   }

   public void setCanonicalRequestURI(String canonicalRequestURI) {
      this.canonicalRequestURI = canonicalRequestURI;
   }

   public ServletRequest getServletRequest() {
      return this.servletRequest;
   }

   public void setServletRequest(ServletRequest servletRequest) {
      this.servletRequest = servletRequest;
   }

   public ServletResponse getServletResponse() {
      return this.servletResponse;
   }

   public void setServletResponse(ServletResponse servletResponse) {
      this.servletResponse = servletResponse;
   }

   public String getServletName() {
      return this.servletName;
   }

   public void setServletName(String servletName) {
      this.servletName = servletName;
   }

   public String toString() {
      StringBuffer buf = new StringBuffer();
      buf.append("[").append(this.getClass().getName()).append(":contextMap=").append(this.map).append(",canonicalRequestURI=").append(this.canonicalRequestURI);
      if (!auditFlag.contains("off")) {
         buf.append(",request=").append(this.deriveUsefulInfo()).append(",CodeSource=").append(this.codeSource).append("]");
      }

      return buf.toString();
   }

   private String deriveUsefulInfo() {
      if (!(this.servletRequest instanceof HttpServletRequest)) {
         return " ";
      } else {
         HttpServletRequest httpRequest = (HttpServletRequest)this.servletRequest;
         StringBuilder sb = new StringBuilder();
         sb.append("[").append(httpRequest.getContextPath());
         if (auditFlag.contains("cookies")) {
            sb.append(":cookies=").append(Arrays.toString(httpRequest.getCookies()));
         }

         Enumeration enu;
         String paramName;
         if (auditFlag.contains("headers")) {
            sb.append(":headers=");
            enu = httpRequest.getHeaderNames();

            while(enu.hasMoreElements()) {
               paramName = (String)enu.nextElement();
               sb.append(paramName).append("=");
               if (!paramName.contains("authorization")) {
                  sb.append(httpRequest.getHeader(paramName)).append(",");
               }
            }

            sb.append("]");
         }

         if (auditFlag.contains("parameters")) {
            sb.append("[parameters=");

            for(enu = httpRequest.getParameterNames(); enu.hasMoreElements(); sb.append(",")) {
               paramName = (String)enu.nextElement();
               sb.append(paramName).append("=");
               if (paramName.equalsIgnoreCase("j_password")) {
                  sb.append("***");
               } else {
                  String[] paramValues = httpRequest.getParameterValues(paramName);
                  int len = paramValues != null ? paramValues.length : 0;

                  for(int i = 0; i < len; ++i) {
                     sb.append(paramValues[i]).append("::");
                  }
               }
            }
         }

         if (auditFlag.contains("attributes")) {
            sb.append("][attributes=");
            enu = httpRequest.getAttributeNames();

            while(enu.hasMoreElements()) {
               paramName = (String)enu.nextElement();
               sb.append(paramName).append("=");
               sb.append(httpRequest.getAttribute(paramName)).append(",");
            }
         }

         sb.append("]");
         return sb.toString();
      }
   }

   static {
      auditFlag = SecurityActions.getSystemProperty("fr.xephi.authme.libs.org.jboss.security.web.audit", " ").toLowerCase(Locale.ENGLISH);
   }
}
