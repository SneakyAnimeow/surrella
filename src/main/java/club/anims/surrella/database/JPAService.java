package club.anims.surrella.database;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.Getter;

public class JPAService {
    private static JPAService instance;

    @Getter
    private final EntityManagerFactory emf;

    private JPAService() {
        emf = Persistence.createEntityManagerFactory("database");
    }

    public void shutdown() {
        if(emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    public static synchronized JPAService getInstance() {
        return instance == null ? instance = new JPAService() : instance;
    }
}
