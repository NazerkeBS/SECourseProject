package model;

import javax.persistence.EntityManager;

public class Context {

    private EntityManager entityManager;

    private Context() {
    }

    private static class SingletonHelper {
        private static final Context INSTANCE = new Context();
    }

    public static Context getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}