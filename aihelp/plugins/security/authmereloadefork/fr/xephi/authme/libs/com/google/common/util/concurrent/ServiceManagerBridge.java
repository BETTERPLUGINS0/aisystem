package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtIncompatible;
import fr.xephi.authme.libs.com.google.common.collect.ImmutableMultimap;

@ElementTypesAreNonnullByDefault
@GwtIncompatible
interface ServiceManagerBridge {
   ImmutableMultimap<Service.State, Service> servicesByState();
}
