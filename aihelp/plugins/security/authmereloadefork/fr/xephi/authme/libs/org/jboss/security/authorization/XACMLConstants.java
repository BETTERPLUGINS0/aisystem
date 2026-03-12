package fr.xephi.authme.libs.org.jboss.security.authorization;

public interface XACMLConstants {
   String ACTION_IDENTIFIER = "urn:oasis:names:tc:xacml:1.0:action:action-id";
   String CURRENT_TIME_IDENTIFIER = "urn:oasis:names:tc:xacml:1.0:environment:current-time";
   String RESOURCE_IDENTIFIER = "urn:oasis:names:tc:xacml:1.0:resource:resource-id";
   String SUBJECT_IDENTIFIER = "urn:oasis:names:tc:xacml:1.0:subject:subject-id";
   String SUBJECT_ROLE_IDENTIFIER = "urn:oasis:names:tc:xacml:2.0:subject:role";
   String SUBJECT_GROUP_IDENTIFIER = "urn:oasis:names:tc:xacml:2.0:subject:group";
   String JBOSS_DYNAMIC_POLICY_SET_IDENTIFIER = "urn:org:jboss:xacml:support:finder:dynamic-policy-set";
   String JBOSS_RESOURCE_PARAM_IDENTIFIER = "urn:oasis:names:tc:xacml:2.0:request-param:attribute:";
}
