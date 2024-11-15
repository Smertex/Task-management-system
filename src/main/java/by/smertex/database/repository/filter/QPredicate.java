package by.smertex.database.repository.filter;

import com.querydsl.core.types.Predicate;

import java.util.function.Function;

public interface QPredicate{
    <T> QPredicateImpl add(T object, Function<T, Predicate> function);

    Predicate buildAnd();

    Predicate buildOr();
}
