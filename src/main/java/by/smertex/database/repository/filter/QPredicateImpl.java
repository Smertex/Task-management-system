package by.smertex.database.repository.filter;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class QPredicateImpl implements QPredicate{
    private final List<Predicate> predicates = new ArrayList<>();

    public static QPredicateImpl builder(){
        return new QPredicateImpl();
    }

    @Override
    public <T> QPredicateImpl add(T object, Function<T, Predicate> function){
        if(object != null)
            predicates.add(function.apply(object));
        return this;
    }

    @Override
    public Predicate buildAnd(){
        return Optional.ofNullable(ExpressionUtils.allOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }

    @Override
    public Predicate buildOr(){
        return Optional.ofNullable(ExpressionUtils.anyOf(predicates))
                .orElseGet(() -> Expressions.asBoolean(true).isTrue());
    }
}
