/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.search;

import org.apache.lucene.util.Version;
import org.hibernate.annotations.common.reflection.ReflectionManager;
import org.hibernate.search.cfg.SearchMapping;
import org.hibernate.search.cfg.spi.SearchConfigurationBase;
import org.hibernate.search.engine.service.classloading.spi.ClassLoaderService;
import org.hibernate.search.engine.service.classloading.impl.DefaultClassLoaderService;
import org.hibernate.search.engine.service.spi.Service;
import org.hibernate.search.spi.SearchIntegrator;
import org.hibernate.search.spi.SearchIntegratorBuilder;
import org.hibernate.search.backend.spi.WorkType;
import org.hibernate.search.backend.spi.Work;
import org.hibernate.search.backend.TransactionContext;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzCloudlet;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzVm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * SearchProcessor handles the Hibernate Search indexing and querying logic.
 * It uses a RAM directory provider for standalone simulation search.
 */
public class SearchProcessor {
    private static SearchProcessor instance = null;
    private SearchIntegrator searchIntegrator;

    private SearchProcessor() {
        initSearchFactory();
    }

    public static synchronized SearchProcessor getInstance() {
        if (instance == null) {
            instance = new SearchProcessor();
        }
        return instance;
    }

    /**
     * Initializes the Hibernate Search Integrator with RAM directory provider.
     */
    private void initSearchFactory() {
        SearchConfigurationBase config = new SearchConfigurationBase() {
            @Override
            public Properties getProperties() {
                Properties props = new Properties();
                props.put("hibernate.search.default.directory_provider", "ram");
                props.setProperty("hibernate.search.lucene_version", Version.LATEST.toString());
                return props;
            }

            @Override
            public Iterator<Class<?>> getClassMappings() {
                Set<Class<?>> mappings = new HashSet<>();
                mappings.add(HzVm.class);
                mappings.add(HzCloudlet.class);
                return mappings.iterator();
            }

            @Override
            public ClassLoaderService getClassLoaderService() {
                return new DefaultClassLoaderService();
            }

            @Override
            public Map<Class<? extends Service>, Object> getProvidedServices() {
                return Collections.emptyMap();
            }

            @Override
            public SearchMapping getProgrammaticMapping() {
                return null;
            }

            @Override
            public ReflectionManager getReflectionManager() {
                return null;
            }

            @Override
            public String getProperty(String propertyName) {
                return getProperties().getProperty(propertyName);
            }

            @Override
            public Class<?> getClassMapping(String name) {
                try {
                    return Class.forName(name);
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }
        };

        SearchIntegratorBuilder builder = new SearchIntegratorBuilder();
        searchIntegrator = builder.configuration(config).buildSearchIntegrator();
    }

    /**
     * Indexes a simulation object.
     * @param obj the object to index (HzCloudlet or HzVm)
     */
    public void indexObject(Object obj) {
        TransactionContext txContext = new TransactionContext() {
            @Override
            public boolean isTransactionInProgress() { return false; }
            @Override
            public Object getTransactionIdentifier() { return null; }
            @Override
            public void registerSynchronization(javax.transaction.Synchronization synchronization) {}
        };

        if (obj instanceof HzVm) {
            HzVm vm = (HzVm) obj;
            searchIntegrator.getWorker().performWork(new Work(vm, vm.getId(), WorkType.ADD), txContext);
        } else if (obj instanceof HzCloudlet) {
            HzCloudlet cloudlet = (HzCloudlet) obj;
            searchIntegrator.getWorker().performWork(new Work(cloudlet, cloudlet.getCloudletId(), WorkType.ADD), txContext);
        }
    }

    /**
     * Performs a search across indexed entities.
     * @param entityType the type of entity to search for
     * @param queryString the Lucene query string
     * @return a list of matching object IDs or objects (simplified for now)
     */
    public java.util.List<Object> search(Class<?> entityType, String queryString) {
        System.out.println("[SearchProcessor] Searching for " + entityType.getSimpleName() + " with query: " + queryString);
        return Collections.emptyList();
    }
}
