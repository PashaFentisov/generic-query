package com.pashonokk.genericquery.api;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class GenericSpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    public GenericSpecification(final SearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (Criteria criteria : criteria.getCriteriaList()) {
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
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}