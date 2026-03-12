package com.nisovin.shopkeepers.commands.lib.util;

import java.util.stream.Stream;

public interface ObjectMatcher<T> {
   Stream<? extends T> match(String var1);
}
