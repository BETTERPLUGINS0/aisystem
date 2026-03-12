package fr.xephi.authme.libs.org.jboss.security.authorization;

import java.util.Set;

public interface EntitlementHolder<T> {
   Set<T> getEntitled();
}
