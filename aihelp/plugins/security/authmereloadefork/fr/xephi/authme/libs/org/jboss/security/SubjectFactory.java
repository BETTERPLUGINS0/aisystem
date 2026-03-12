package fr.xephi.authme.libs.org.jboss.security;

import javax.security.auth.Subject;

public interface SubjectFactory {
   Subject createSubject();

   Subject createSubject(String var1);
}
