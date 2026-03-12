package fr.xephi.authme.libs.com.google.common.html;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import fr.xephi.authme.libs.com.google.common.escape.Escaper;
import fr.xephi.authme.libs.com.google.common.escape.Escapers;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class HtmlEscapers {
   private static final Escaper HTML_ESCAPER = Escapers.builder().addEscape('"', "&quot;").addEscape('\'', "&#39;").addEscape('&', "&amp;").addEscape('<', "&lt;").addEscape('>', "&gt;").build();

   public static Escaper htmlEscaper() {
      return HTML_ESCAPER;
   }

   private HtmlEscapers() {
   }
}
