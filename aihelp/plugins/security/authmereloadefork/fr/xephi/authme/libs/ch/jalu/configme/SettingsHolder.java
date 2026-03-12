package fr.xephi.authme.libs.ch.jalu.configme;

import fr.xephi.authme.libs.ch.jalu.configme.configurationdata.CommentsConfiguration;

public interface SettingsHolder {
   default void registerComments(CommentsConfiguration conf) {
   }
}
