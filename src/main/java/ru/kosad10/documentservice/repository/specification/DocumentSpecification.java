package ru.kosad10.documentservice.repository.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.kosad10.documentservice.entity.Document;
import ru.kosad10.documentservice.enums.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DocumentSpecification {

    public static Specification<Document> hasAuthor(String author) {
        return (root, query, criteriaBuilder) -> {
            if (author == null || author.isBlank()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("author")),
                    author.toLowerCase() + "%");
        };
    }

    public static Specification<Document> createdBetween(LocalDate from, LocalDate to) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (from != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), from));
            }
            if (to != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), to));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Document> hasStatus(Status status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Document> withFilters(Status status, String author,
                                                      LocalDate dateFrom, LocalDate dateTo) {
        return Specification.<Document>unrestricted()
                .and(hasStatus(status))
                .and(hasAuthor(author))
                .and(createdBetween(dateFrom, dateTo));
    }
}
