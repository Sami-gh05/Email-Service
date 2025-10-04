package final_project.framework;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SingletonSessionFactory {
    private static SessionFactory sessionFactory = null;
    private SingletonSessionFactory(){}
    public static SessionFactory get(){
        if(sessionFactory == null) {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();
        }
        return sessionFactory;
    }
    public static void close(){
        if(sessionFactory == null)
            return;
        sessionFactory.close();
    }

}
