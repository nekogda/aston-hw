package org.example.user.persistence;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user.app.exception.UserAppDataException;
import org.example.user.app.exception.UserAppException;
import org.example.user.app.port.out.TransactionManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@Slf4j
@AllArgsConstructor
public class TransactionManagerImpl implements TransactionManager {

    private final SessionFactory sessionFactory;

    @Override
    public <T> T inTransaction(Supplier<T> action) {
        Session session = sessionFactory.openSession();
        try (session) {
            ManagedSessionContext.bind(session);
            session.getTransaction().begin();
            T result;
            try {
                result = action.get();
            } catch (RuntimeException ex) {
                if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                        || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                    session.getTransaction().rollback();
                }
                throw ex;
            }
            session.getTransaction().commit();
            return result;
        } catch (RuntimeException ex) {
            log.error("exception = {}", ex.getMessage(), ex);
            if (ex instanceof UserAppException) {
                throw ex;
            }
            throw new UserAppDataException(ex.getMessage(), ex);
        } finally {
            ManagedSessionContext.unbind(sessionFactory);

        }
    }
}