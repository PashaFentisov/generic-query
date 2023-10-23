package com.pashonokk.genericquery.api;

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
        List<String> primitiveFields = Arrays.stream(root.getModel().getJavaType().getDeclaredFields())
                .filter(field -> isPrimitive(field.getType()))
                .map(Field::getName).toList();

        List<String> listFields = Arrays.stream(root.getModel().getJavaType().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(OneToMany.class)) //todo implement ManyToMany
                .map(Field::getName).toList();

        for (Criteria criteria : searchCriteria.getCriteriaList()) {
            if (primitiveFields.contains(criteria.getKey())) {  //1
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
                return builder.and(predicates.toArray(new Predicate[0]));
            } else if (listFields.contains(criteria.getKey().split("\\.")[0])) {//2
                List<Field> fields = Arrays.stream(root.getModel().getJavaType().getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(OneToMany.class)).toList();
                Type genericType = fields.get(0).getGenericType();
                Class<?> genericClass = null;
                if (genericType instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                    Type[] typeArguments = parameterizedType.getActualTypeArguments();

                    if (typeArguments.length > 0) {
                        genericClass = (Class<?>) typeArguments[0];
                        System.out.println("Class of List Generic Type: " + genericClass.getName());
                    }
                }

                Subquery subquery = query.subquery(genericClass);
                Root subRoot = subquery.from(genericClass);

                subquery.select(subRoot);
                subquery.where(subRoot.get("client")
                                .in(root),
                        subRoot.get("description") //todo
                                .in(criteria.getValues()));

                return builder.exists(subquery);

            } else {
                Path<String> path = getPath(root, criteria.getKey());
                if (path.getJavaType() == String.class) {
                    return builder.like(
                            path, "%" + criteria.getValues().get(0) + "%");
                } else {
                    return builder.equal(path, criteria.getValues().get(0));
                }
            }
        }
        return null;
    }

    //todo now support only one field for filterng
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