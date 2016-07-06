package com.marqeta.util;

import java.security.Provider;
import java.security.Security;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by amontecillo on 7/6/16.
 */
public class CryptoProviders {

    public static class ProviderInfo {
        public String version;
        public Set<String> algorithms;

        public ProviderInfo() {
            this.algorithms = new HashSet<>();
        }

        @Override
        public String toString() {
            return "ProviderInfo{" +
                    "version='" + version + '\'' +
                    ", algorithms=" + algorithms +
                    '}';
        }
    }

    public Set<ProviderInfo> getProviders() {
        Set<ProviderInfo> providerList = new HashSet<>();
        final Provider[] providers = Security.getProviders();
        for (final Provider p : providers) {
            ProviderInfo info = new ProviderInfo();
            info.version = String.format("%s %s", p.getName(), p.getVersion());
            for (final Object o : p.keySet()) {
                info.algorithms.add(String.format("%s : %s", o, p.getProperty((String)o)));
            }
        }
        return providerList;
    }

}
