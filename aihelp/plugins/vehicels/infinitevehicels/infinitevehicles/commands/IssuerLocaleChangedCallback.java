package me.PM2.infinitevehicles.commands;

import java.util.Locale;

public interface IssuerLocaleChangedCallback<I extends CommandIssuer> {
   void onIssuerLocaleChange(I issuer, Locale oldLocale, Locale newLocale);
}
