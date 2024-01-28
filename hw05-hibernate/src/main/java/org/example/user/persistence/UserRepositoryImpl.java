package org.example.user.persistence;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.user.app.domain.model.User;
import org.example.user.app.port.out.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.hibernate.query.Query;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Repository
@AllArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Override
    public List<User> findAll() {
        log.trace("called");
        return inTransaction(s -> s.createQuery(
                        "SELECT u FROM User u", User.class)
                .getResultList());
    }

    @Override
    public User save(User user) {
        log.debug("called with args: user={}", user);
        return inTransaction(s -> s.merge(user));
    }

    @Override
    public boolean existsByLogin(String login) {
        log.trace("called with args: login={}", login);
        return inTransaction(s -> {
            Query<Long> query = s.createQuery(
                    "SELECT COUNT(1) FROM User u WHERE u.login=:login",
                    Long.class);
            query.setParameter(User.Fields.login, login);
            query.setMaxResults(1);
            return Optional.ofNullable(query.getSingleResult())
                    .filter(x -> x > 0)
                    .isPresent();
        });
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return inTransaction(s -> {
            Query<User> query = s.createQuery(
                    "SELECT u FROM User u WHERE u.login=:login",
                    User.class);
            query.setParameter(User.Fields.login, login);
            List<User> users = query.getResultList();
            if (users.size() > 1) {
                throw new IllegalStateException("more than 1 user found");
            }
            return users.isEmpty() ? Optional.empty() : Optional.of(users.getFirst());
        });
    }

    @Override
    public User update(User user) {
        return inTransaction(s -> s.merge(user));
    }

    private <R> R inTransaction(Function<Session, R> action) {
        if (inSession()) {
            return action.apply(sessionFactory.getCurrentSession());
        } else {
            Transaction transaction = null;
            try (Session session = sessionFactory.openSession()) {
                ManagedSessionContext.bind(session);
                transaction = session.beginTransaction();
                R result;
                try {
                    result = action.apply(session);
                } catch (RuntimeException ex) {
                    if (session.getTransaction().getStatus() == TransactionStatus.ACTIVE
                            || session.getTransaction().getStatus() == TransactionStatus.MARKED_ROLLBACK) {
                        session.getTransaction().rollback();
                    }
                    throw ex;
                }
                transaction.commit();

                return result;
            } catch (RuntimeException ex) {
                log.error("exception = {}", ex.getMessage(), ex);
                throw ex;
            } finally {
                ManagedSessionContext.unbind(sessionFactory);
            }
        }
    }

    private boolean inSession() {
        return ManagedSessionContext.hasBind(sessionFactory);
    }
}