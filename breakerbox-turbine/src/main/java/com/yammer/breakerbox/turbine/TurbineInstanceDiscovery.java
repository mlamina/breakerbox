package com.yammer.breakerbox.turbine;

import com.google.common.base.Joiner;
import com.netflix.config.ConfigurationManager;
import com.netflix.turbine.discovery.InstanceDiscovery;
import com.netflix.turbine.monitor.cluster.ClusterMonitorFactory;
import com.netflix.turbine.plugins.PluginsFactory;
import org.apache.commons.configuration.AbstractConfiguration;

import java.util.Collection;

public class TurbineInstanceDiscovery {
    public static final String BREAKERBOX_INSTANCE_ID = "Breakerbox-Instance-Id";

    private TurbineInstanceDiscovery() {}

    public static void registerClusters(Collection<String> clusterNames,
                                        String instanceUrlSuffix) {
        final AbstractConfiguration configurationManager = ConfigurationManager.getConfigInstance();
        configurationManager.setProperty(InstanceDiscovery.TURBINE_AGGREGATOR_CLUSTER_CONFIG,
                Joiner.on(',').join(clusterNames));
        configurationManager.setProperty("turbine.instanceUrlSuffix", instanceUrlSuffix);
        clusterNames.stream()
                .forEach((clusterName) -> configurationManager.setProperty("turbine.protocol." + clusterName, "https"));
        final ClusterMonitorFactory<?> clusterMonitorFactory = PluginsFactory.getClusterMonitorFactory();
        if (clusterMonitorFactory != null) {
            clusterMonitorFactory.initClusterMonitors();
        }
    }
}