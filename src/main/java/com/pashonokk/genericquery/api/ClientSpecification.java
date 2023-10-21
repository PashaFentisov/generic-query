package com.pashonokk.genericquery.api;

import com.pashonokk.genericquery.entity.Client;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ClientSpecification implements Specification<Client> {

    private SearchCriteria criteria;

    // constructor
    public ClientSpecification(final SearchCriteria criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Client> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        for (Criteria criteria : criteria.getCriteriaList()) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                predicates.add(builder.like(
                        root.get(criteria.getKey()), "%" + criteria.getValue() + "%"));
            } else {
                predicates.add(builder.equal(root.get(criteria.getKey()), criteria.getValue()));
            }
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}