package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpRequestInterceptor;
import java.util.List;

/** @deprecated */
@Deprecated
public interface HttpRequestInterceptorList {
   void addRequestInterceptor(HttpRequestInterceptor var1);

   void addRequestInterceptor(HttpRequestInterceptor var1, int var2);

   int getRequestInterceptorCount();

   HttpRequestInterceptor getRequestInterceptor(int var1);

   void clearRequestInterceptors();

   void removeRequestInterceptorByClass(Class<? extends HttpRequestInterceptor> var1);

   void setInterceptors(List<?> var1);
}
