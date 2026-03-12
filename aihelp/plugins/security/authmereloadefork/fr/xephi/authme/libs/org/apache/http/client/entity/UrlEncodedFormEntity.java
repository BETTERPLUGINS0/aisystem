package fr.xephi.authme.libs.org.apache.http.client.entity;

import fr.xephi.authme.libs.org.apache.http.NameValuePair;
import fr.xephi.authme.libs.org.apache.http.client.utils.URLEncodedUtils;
import fr.xephi.authme.libs.org.apache.http.entity.ContentType;
import fr.xephi.authme.libs.org.apache.http.entity.StringEntity;
import fr.xephi.authme.libs.org.apache.http.protocol.HTTP;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.List;

public class UrlEncodedFormEntity extends StringEntity {
   public UrlEncodedFormEntity(List<? extends NameValuePair> parameters, String charset) throws UnsupportedEncodingException {
      super(URLEncodedUtils.format(parameters, charset != null ? charset : HTTP.DEF_CONTENT_CHARSET.name()), ContentType.create("application/x-www-form-urlencoded", charset));
   }

   public UrlEncodedFormEntity(Iterable<? extends NameValuePair> parameters, Charset charset) {
      super(URLEncodedUtils.format(parameters, charset != null ? charset : HTTP.DEF_CONTENT_CHARSET), ContentType.create("application/x-www-form-urlencoded", charset));
   }

   public UrlEncodedFormEntity(List<? extends NameValuePair> parameters) throws UnsupportedEncodingException {
      this((Iterable)parameters, (Charset)((Charset)null));
   }

   public UrlEncodedFormEntity(Iterable<? extends NameValuePair> parameters) {
      this((Iterable)parameters, (Charset)null);
   }
}
