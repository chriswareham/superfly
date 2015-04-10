/*
 * @(#) ComponentManager.java
 *
 * Copyright (C) 2015, Chris Wareham, All Rights Reserved
 */

package net.chriswareham.di;

import java.io.IOException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

/**
 * This class manages components that can be managed via JMX.
 *
 * @author Chris Wareham
 */
public class ComponentManager implements ComponentListener, LifecycleComponent {
    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ComponentManager.class);
    /**
     * The default registry port.
     */
    private static final int DEFAULT_REGISTRY_PORT = 9999;
    /**
     * The management URL prefix.
     */
    private static final String MANAGEMENT_URL_PREFIX = "service:jmx:rmi:///jndi/rmi://localhost:";
    /**
     * The management URL format.
     */
    private static final String MANAGEMENT_URL_SUFFIX = "/server";

    /**
     * The registry port.
     */
    private int registryPort = DEFAULT_REGISTRY_PORT;
    /**
     * Whether the registry was created.
     */
    private boolean registryCreated;
    /**
     * The registry.
     */
    private Registry registry;
    /**
     * The connector server.
     */
    private JMXConnectorServer connectorServer;
    /**
     * The managed bean server.
     */
    private MBeanServer managedBeanServer;
    /**
     * The managed bean names.
     */
    private final List<ObjectName> managedBeanNames = new ArrayList<>();

    /**
     * Set the registry port.
     *
     * @param rp the registry port
     */
    public void setRegistryPort(final int rp) {
        registryPort = rp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws ComponentException {
        startManagedBeanServer();
        startRegistry();
        startConnectorServer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        for (ObjectName managedBeanName : managedBeanNames) {
            try {
                managedBeanServer.unregisterMBean(managedBeanName);
            } catch (JMException exception) {
                LOGGER.warn("stop(): error unregistering managed bean '" + managedBeanName + "'", exception);
            }
        }

        stopConnectorServer();
        stopRegistry();
        stopManagedBeanServer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void componentCreated(final Object obj) throws ComponentException {
        if (obj instanceof ManagedComponent) {
            ManagedComponent component = (ManagedComponent) obj;

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("componentCreated(): registering managed bean:[" + component.getManagedName() + "]");
            }

            try {
                ObjectName managedBeanName = new ObjectName(component.getManagedName());
                managedBeanServer.registerMBean(component, managedBeanName);
                managedBeanNames.add(managedBeanName);
            } catch (JMException exception) {
                throw new ComponentException("Error registering managed bean '" + component.getManagedName() + "'", exception);
            }
        }
    }

    /**
     * Start the managed bean server.
     *
     * @throws ComponentException if an error occurs
     */
    private void startManagedBeanServer() throws ComponentException {
        try {
            managedBeanServer = MBeanServerFactory.createMBeanServer();
        } catch (RuntimeException exception) {
            throw new ComponentException("Failed to start managed bean server", exception);
        }
    }

    /**
     * Stop the managed bean server.
     */
    private void stopManagedBeanServer() {
        if (managedBeanServer != null) {
            try {
                MBeanServerFactory.releaseMBeanServer(managedBeanServer);
            } catch (RuntimeException exception) {
                LOGGER.warn("stopManagedBeanServer(): error releasing managed bean server", exception);
            }
        }
    }

    /**
     * Start the registry.
     *
     * @throws ComponentException if an error occurs
     */
    private void startRegistry() throws ComponentException {
        try {
            // Try to retrieve an existing registry on the required port. This
            // method throws an exception rather than returning null if the
            // registry doesn't already exist.
            registry = LocateRegistry.getRegistry(registryPort);

            // If we get this far, the registry exists, so we now make a call on
            // it to ensure it's still active.
            registry.list();
        } catch (RemoteException exception) {
            registry = null;
        }

        if (registry != null) {
            return;
        }

        try {
            // Try to create a registry on the required port.
            registry = LocateRegistry.createRegistry(registryPort);
            registryCreated = true;
        } catch (RemoteException exception) {
            throw new ComponentException("Failed to start registry", exception);
        }
    }

    /**
     * Stop the registry.
     */
    private void stopRegistry() {
        if (registryCreated) {
            try {
                UnicastRemoteObject.unexportObject(registry, true);
            } catch (NoSuchObjectException exception) {
                LOGGER.warn("stopRegistry(): error shutting down registry", exception);
            }
        }
        registry = null;
    }

    /**
     * Start the connector server.
     *
     * @throws ComponentException if an error occurs
     */
    private void startConnectorServer() throws ComponentException {
        try {
            JMXServiceURL url = new JMXServiceURL(MANAGEMENT_URL_PREFIX + registryPort + MANAGEMENT_URL_SUFFIX);
            connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, null, managedBeanServer);
            connectorServer.start();
        } catch (IOException exception) {
            throw new ComponentException("Failed to initialise connector server", exception);
        }
    }

    /**
     * Stop the connector server.
     */
    private void stopConnectorServer() {
        if (connectorServer != null) {
            try {
                connectorServer.stop();
            } catch (IOException exception) {
                LOGGER.warn("stopConnectorServer(): error stopping management connector server", exception);
            }
            connectorServer = null;
        }
    }
}
