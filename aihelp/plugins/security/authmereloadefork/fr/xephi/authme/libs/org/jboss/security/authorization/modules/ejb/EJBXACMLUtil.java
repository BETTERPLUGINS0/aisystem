package fr.xephi.authme.libs.org.jboss.security.authorization.modules.ejb;

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
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Iterator;
import java.util.List;

public class EJBXACMLUtil extends JBossXACMLUtil {
   public RequestContext createXACMLRequest(String ejbName, Method ejbMethod, Principal principal, RoleGroup callerRoles) throws Exception {
      String action = ejbMethod.getName();
      Class<?>[] paramTypes = ejbMethod.getParameterTypes();
      if (paramTypes.length == 0) {
         return this.createXACMLRequest(ejbName, action, principal, callerRoles);
      } else {
         StringBuilder builder = new StringBuilder("(");
         int i = 0;
         Class[] arr$ = paramTypes;
         int len$ = paramTypes.length;

         for(int i$ = 0; i$ < len$; ++i$) {
            Class<?> paramClass = arr$[i$];
            if (i > 0) {
               builder.append(",");
            }

            builder.append(paramClass.getSimpleName());
            ++i;
         }

         builder.append(")");
         ActionType actionType = this.getActionType(action + builder.toString());
         RequestContext requestCtx = this.getRequestContext(ejbName, actionType, principal, callerRoles);
         if (PicketBoxLogger.LOGGER.isDebugEnabled()) {
            ByteArrayOutputStream baos = null;

            try {
               baos = new ByteArrayOutputStream();
               requestCtx.marshall(baos);
               PicketBoxLogger.LOGGER.debug(new String(baos.toByteArray()));
            } catch (IOException var16) {
            } finally {
               this.safeClose(baos);
            }
         }

         return requestCtx;
      }
   }

   public RequestContext createXACMLRequest(String ejbName, String methodName, Principal principal, RoleGroup callerRoles) throws Exception {
      ActionType actionType = this.getActionType(methodName);
      RequestContext requestCtx = this.getRequestContext(ejbName, actionType, principal, callerRoles);
      if (PicketBoxLogger.LOGGER.isDebugEnabled()) {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         requestCtx.marshall(baos);
         PicketBoxLogger.LOGGER.debug(new String(baos.toByteArray()));
      }

      return requestCtx;
   }

   private RequestContext getRequestContext(String ejbName, ActionType actionType, Principal principal, RoleGroup callerRoles) throws IOException {
      if (principal == null) {
         throw PicketBoxMessages.MESSAGES.invalidNullArgument("principal");
      } else {
         RequestContext requestCtx = RequestResponseContextFactory.createRequestCtx();
         SubjectType subject = this.getSubjectType(principal, callerRoles);
         ResourceType resourceType = this.getResourceType(ejbName);
         EnvironmentType environmentType = this.getEnvironmentType();
         RequestType requestType = this.getRequestType(subject, resourceType, actionType, environmentType);
         requestCtx.setRequest(requestType);
         return requestCtx;
      }
   }

   private RequestType getRequestType(SubjectType subject, ResourceType resourceType, ActionType actionType, EnvironmentType environmentType) {
      RequestType requestType = new RequestType();
      requestType.getSubject().add(subject);
      requestType.getResource().add(resourceType);
      requestType.setAction(actionType);
      requestType.setEnvironment(environmentType);
      return requestType;
   }

   private EnvironmentType getEnvironmentType() {
      EnvironmentType environmentType = new EnvironmentType();
      environmentType.getAttribute().add(RequestAttributeFactory.createDateTimeAttributeType("urn:oasis:names:tc:xacml:1.0:environment:current-time", (String)null));
      return environmentType;
   }

   private ActionType getActionType(String action) {
      String actionID_NS = "urn:oasis:names:tc:xacml:1.0:action:action-id";
      AttributeType actionAttribute = RequestAttributeFactory.createStringAttributeType(actionID_NS, "jboss.org", action);
      ActionType actionType = new ActionType();
      actionType.getAttribute().add(actionAttribute);
      return actionType;
   }

   private ResourceType getResourceType(String ejbName) {
      String resourceID_NS = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
      ResourceType resourceType = new ResourceType();
      AttributeType resourceAttribute = RequestAttributeFactory.createStringAttributeType(resourceID_NS, (String)null, ejbName);
      resourceType.getAttribute().add(resourceAttribute);
      return resourceType;
   }

   private SubjectType getSubjectType(Principal principal, RoleGroup callerRoles) {
      String subjectID_NS = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
      String roleID_NS = "urn:oasis:names:tc:xacml:2.0:subject:role";
      String principalName = principal.getName();
      SubjectType subject = new SubjectType();
      AttributeType attribute = RequestAttributeFactory.createStringAttributeType(subjectID_NS, "jboss.org", principalName);
      subject.getAttribute().add(attribute);
      List<Role> rolesList = callerRoles.getRoles();
      if (rolesList != null) {
         Iterator i$ = rolesList.iterator();

         while(i$.hasNext()) {
            Role role = (Role)i$.next();
            String roleName = role.getRoleName();
            AttributeType attSubjectID = RequestAttributeFactory.createStringAttributeType(roleID_NS, "jboss.org", roleName);
            subject.getAttribute().add(attSubjectID);
         }
      }

      return subject;
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
