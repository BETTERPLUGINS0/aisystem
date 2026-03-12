package fr.xephi.authme.libs.org.jboss.security.authorization.modules.web;

import fr.xephi.authme.libs.org.jboss.security.PicketBoxLogger;
import fr.xephi.authme.libs.org.jboss.security.PicketBoxMessages;
import fr.xephi.authme.libs.org.jboss.security.authorization.util.JBossXACMLUtil;
import fr.xephi.authme.libs.org.jboss.security.identity.Role;
import fr.xephi.authme.libs.org.jboss.security.identity.RoleGroup;
import fr.xephi.authme.libs.org.jboss.security.xacml.core.model.context.ActionType;
import fr.xephi.authme.libs.org.jboss.security.xacml.core.model.context.AttributeType;
import fr.xephi.authme.libs.org.jboss.security.xacml.core.model.context.EnvironmentType;
import fr.xephi.authme.libs.org.jboss.security.xacml.core.model.context.RequestType;
import fr.xephi.authme.libs.org.jboss.security.xacml.core.model.context.ResourceType;
import fr.xephi.authme.libs.org.jboss.security.xacml.core.model.context.SubjectType;
import fr.xephi.authme.libs.org.jboss.security.xacml.factories.RequestAttributeFactory;
import fr.xephi.authme.libs.org.jboss.security.xacml.factories.RequestResponseContextFactory;
import fr.xephi.authme.libs.org.jboss.security.xacml.interfaces.RequestContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class WebXACMLUtil extends JBossXACMLUtil {
   public RequestContext createXACMLRequest(HttpServletRequest request, RoleGroup callerRoles) throws Exception {
      if (request == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("request");
      } else if (callerRoles == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("callerRoles");
      } else {
         String httpMethod = request.getMethod();
         String action = "GET".equals(httpMethod) ? "read" : "write";
         String actionURIBase = "urn:oasis:names:tc:xacml:2.0:request-param:attribute:";
         Principal principal = request.getUserPrincipal();
         RequestContext requestCtx = RequestResponseContextFactory.createRequestCtx();
         SubjectType subject = new SubjectType();
         subject.getAttribute().add(RequestAttributeFactory.createStringAttributeType("urn:oasis:names:tc:xacml:1.0:subject:subject-id", "jboss.org", principal.getName()));
         List<Role> rolesList = callerRoles.getRoles();
         if (rolesList != null) {
            Iterator i$ = rolesList.iterator();

            while(i$.hasNext()) {
               Role role = (Role)i$.next();
               String roleName = role.getRoleName();
               AttributeType attSubjectID = RequestAttributeFactory.createStringAttributeType("urn:oasis:names:tc:xacml:2.0:subject:role", "jboss.org", roleName);
               subject.getAttribute().add(attSubjectID);
            }
         }

         ResourceType resourceType = new ResourceType();
         resourceType.getAttribute().add(RequestAttributeFactory.createAnyURIAttributeType("urn:oasis:names:tc:xacml:1.0:resource:resource-id", (String)null, new URI(request.getRequestURI())));
         ActionType actionType = new ActionType();
         actionType.getAttribute().add(RequestAttributeFactory.createStringAttributeType("urn:oasis:names:tc:xacml:1.0:action:action-id", "jboss.org", action));
         Enumeration enumer = request.getParameterNames();

         while(enumer.hasMoreElements()) {
            String paramName = (String)enumer.nextElement();
            String paramValue = request.getParameter(paramName);
            URI actionUri = new URI(actionURIBase + paramName);
            actionType.getAttribute().add(RequestAttributeFactory.createStringAttributeType(actionUri.toASCIIString(), "jboss.org", paramValue));
         }

         EnvironmentType environmentType = new EnvironmentType();
         environmentType.getAttribute().add(RequestAttributeFactory.createDateTimeAttributeType("urn:oasis:names:tc:xacml:1.0:environment:current-time", (String)null));
         RequestType requestType = new RequestType();
         requestType.getSubject().add(subject);
         requestType.getResource().add(resourceType);
         requestType.setAction(actionType);
         requestType.setEnvironment(environmentType);
         requestCtx.setRequest(requestType);
         if (PicketBoxLogger.LOGGER.isDebugEnabled()) {
            ByteArrayOutputStream baos = null;

            try {
               baos = new ByteArrayOutputStream();
               requestCtx.marshall(baos);
               PicketBoxLogger.LOGGER.debug(new String(baos.toByteArray()));
            } catch (IOException var20) {
            } finally {
               this.safeClose(baos);
            }
         }

         return requestCtx;
      }
   }

   private void safeClose(OutputStream os) {
      try {
         if (os != null) {
            os.close();
         }
      } catch (Exception var3) {
      }

   }
}
