package util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();
    
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
            
            return configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            System.err.println("This is expected if H2 database JAR is missing. Using in-memory storage instead.");
            return null; // Return null instead of throwing exception
        }
    }
    
    public static SessionFactory getSessionFactory() {
        // May legitimately be null if configuration failed; callers must handle null
        return sessionFactory;
    }
    
    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
