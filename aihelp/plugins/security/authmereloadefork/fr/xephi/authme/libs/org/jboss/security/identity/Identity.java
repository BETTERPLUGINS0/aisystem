package fr.xephi.authme.libs.org.jboss.security.identity;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;

public interface Identity extends Serializable {
   String getName();

   Role getRole();

   Group asGroup();

   Principal asPrincipal();
}
