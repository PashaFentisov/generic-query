package com.pashonokk.genericquery.api;

import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GenericSpecification<T> implements Specification<T> {

    private SearchCriteria searchCriteria;

    public GenericSpecification(final SearchCriteria searchCriteria) {
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        List<String> primitiveFields = getNamesOfPrimitiveFields(root);

        List<String> listFields = getNamesOfListFields(root);

        for (Criteria criteria : searchCriteria.getCriteriaList()) {
            if (primitiveFields.contains(criteria.getKey())) {
                filterByPrimitiveFields(root, builder, criteria, predicates);
            } else if (listFields.contains(criteria.getKey().split("\\.")[0])) {//2
                Subquery subquery = filterByListFields(root, query, criteria);
                return builder.exists(subquery);
            } else {
                //todo можем шукати по одному значенню і не можем шукати по декільком полям
                //todo варто все класти в загальні predicates а не повертати в кожній конструкції щось своє
                Path<String> path = getPath(root, criteria.getKey());
                if (path.getJavaType() == String.class) {
                    return builder.like(
                            path, "%" + criteria.getValues().get(0) + "%");
                } else {
                    return builder.equal(path, criteria.getValues().get(0));
                }
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private List<String> getNamesOfListFields(Root<T> root) {
        return Arrays.stream(root.getModel().getJavaType().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToMany.class))
                .map(Field::getName).toList();
    }

    private List<String> getNamesOfPrimitiveFields(Root<T> root) {
        return Arrays.stream(root.getModel().getJavaType().getDeclaredFields())
                .filter(field -> isPrimitive(field.getType()))
                .map(Field::getName).toList();
    }

    private Subquery filterByListFields(Root<T> root, CriteriaQuery<?> query, Criteria criteria) {
        List<Field> fields = Arrays.stream(root.getModel().getJavaType().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToMany.class))
                .toList();
        //todo тут має бути цикл бо поки робим тільки для одного філда
        //todo не можем юзати багато значеннь
        Type genericType = fields.get(0).getGenericType();
        Class<?> genericClass = null;
        ParameterizedType parameterizedType = (ParameterizedType) genericType;
        Type[] typeArguments = parameterizedType.getActualTypeArguments();

        if (typeArguments.length > 0) {
            genericClass = (Class<?>) typeArguments[0];
        }

        Subquery subquery = query.subquery(genericClass);
        Root subRoot = subquery.from(genericClass);

        subquery.select(subRoot);

        subquery.where(subRoot.get("client")
                        .in(root),
                subRoot.get(criteria.getKey().split("\\.")[1])
                        .in(criteria.getValues()));
        return subquery;
    }

    private void filterByPrimitiveFields(Root<T> root, CriteriaBuilder builder, Criteria criteria, List<Predicate> predicates) {
        if (root.get(criteria.getKey()).getJavaType() == String.class) {
            List<String> values = criteria.getValues();
            List<Predicate> orPredicates = new ArrayList<>();

            for (String value : values) {
                orPredicates.add(builder.like(root.get(criteria.getKey()), "%" + value + "%"));
            }

            predicates.add(builder.or(orPredicates.toArray(new Predicate[0])));
        } else {
            predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValues().get(0)));
        }
    }
    private Path<String> getPath(Root<T> root, String key) {
        String[] fields = key.split("\\.");
        Path<String> path = root.get(fields[0]);
        for (int i = 1; i < fields.length; i++) {
            path = path.get(fields[i]);
        }
        return path;
    }

    public static boolean isPrimitive(Class<?> type) {
        return type.isPrimitive() ||
                type == Integer.class ||
                type == Long.class ||
                type == Double.class ||
                type == String.class ||
                type == Boolean.class;
    }

}