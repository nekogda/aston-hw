package org.example.user.app.port.out;

import java.util.function.Supplier;

public interface TransactionManager {

    <T> T inTransaction(Supplier<T> action);
}