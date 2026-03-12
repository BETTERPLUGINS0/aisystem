package ch.jalu.configme;

import ch.jalu.configme.configurationdata.CommentsConfiguration;
import org.jetbrains.annotations.NotNull;

public interface SettingsHolder {
   default void registerComments(@NotNull CommentsConfiguration conf) {
   }
}
