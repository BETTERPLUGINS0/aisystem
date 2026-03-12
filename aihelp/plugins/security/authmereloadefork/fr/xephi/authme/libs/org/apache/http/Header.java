package fr.xephi.authme.libs.org.apache.http;

public interface Header extends NameValuePair {
   HeaderElement[] getElements() throws ParseException;
}
