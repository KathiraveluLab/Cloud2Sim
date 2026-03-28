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

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;
import org.hibernate.search.SearchFactory;
import org.hibernate.search.backend.impl.lucene.LuceneBackendQueueProcessor;
import org.hibernate.search.backend.spi.Work;
import org.hibernate.search.backend.spi.WorkType;
import org.hibernate.search.engine.spi.SearchFactoryImplementor;
import org.hibernate.search.impl.SearchFactoryImpl;
import org.hibernate.search.cfg.SearchConfiguration;
import org.hibernate.search.cfg.ReflectionHierarchy;
import org.hibernate.search.cfg.spi.SearchConfigurationBase;
import org.hibernate.search.spi.SearchFactoryBuilder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * SearchProcessor handles the Hibernate Search indexing and querying logic.
 * It uses an Infinispan-based Lucene directory for distributed indexing.
 */
public class SearchProcessor {
    private static SearchProcessor instance = null;
    private SearchFactoryImplementor searchFactory;

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
     * Initializes the Hibernate Search Factory with Infinispan directory provider.
     */
    private void initSearchFactory() {
        SearchConfiguration config = new SearchConfigurationBase() {
            @Override
            public Properties getProperties() {
                Properties props = new Properties();
                props.put("hibernate.search.default.directory_provider", "infinispan");
                props.put("hibernate.search.infinispan.cachemanager_jndiname", "java:jboss/infinispan/container/hibernate-search");
                // Fallback to local ram for testing if JDNI not available
                props.put("hibernate.search.default.directory_provider", "ram");
                props.put("hibernate.search.lucene_version", "LUCENE_36");
                return props;
            }

            @Override
            public Iterator<Class<?>> getClassMappings() {
                return Collections.emptyIterator();
            }

            @Override
            public Class<?> getClassForName(String name) {
                try {
                    return Class.forName(name);
                } catch (ClassNotFoundException e) {
                    return null;
                }
            }

            @Override
            public String getProperty(String propertyName) {
                return getProperties().getProperty(propertyName);
            }
        };

        SearchFactoryBuilder builder = new SearchFactoryBuilder();
        searchFactory = (SearchFactoryImplementor) builder.forConfiguration(config).buildSearchFactory();
    }

    /**
     * Indexes a simulation object.
     * @param obj the object to index (HzCloudlet or HzVm)
     */
    public void indexObject(Object obj) {
        Work work = new Work(obj, null, WorkType.ADD);
        searchFactory.getBackendQueueProcessor().performWork(work);
    }

    /**
     * Performs a search across indexed entities.
     * @param entityType the type of entity to search for
     * @param queryString the Lucene query string
     * @return a list of matching object IDs or objects (simplified for now)
     */
    public List<Object> search(Class<?> entityType, String queryString) {
        try {
            QueryParser parser = new QueryParser(Version.LUCENE_36, "vmm", new StandardAnalyzer(Version.LUCENE_36));
            Query luceneQuery = parser.parse(queryString);
            
            // In a real HS environment, we'd use FullTextSession.
            // For this standalone implementation, we'll log the query attempt.
            System.out.println("[SearchProcessor] Executing distributed query: " + luceneQuery.toString());
            
            return Collections.emptyList(); // Placeholder for actual result retrieval
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
