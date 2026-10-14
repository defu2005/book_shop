package com.learn_spring_boot.specification;

import com.learn_spring_boot.entity.Book;
import com.learn_spring_boot.entity.Category;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {
    public static Specification<Book> hasNameOrAuthorNameContaining(String name){
        return(root, query, criteriaBuilder) -> {
            if(name==null||name.isEmpty()) return null;
            return criteriaBuilder.or(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),"%"+name.toLowerCase()+"%"),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("author").get("name")),"%"+name.toLowerCase()+"%"));
        };
    }
    public static Specification<Book> hasPriceBetween(Integer minPrice,Integer maxPrice){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates=new ArrayList<>();
            if(minPrice!=null){
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"),minPrice));
            }
            if(maxPrice!=null){
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"),maxPrice));
            }
            return predicates.isEmpty()?null:criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
    public static Specification<Book> hasCategory(Integer id){
        if(id==null) return null;
        return (root, query, criteriaBuilder) ->
        {
            query.distinct(true);
            Join<Book, Category> categoryJoin=
                    root.join("categories");
            return criteriaBuilder.equal(categoryJoin.get("id"),id);



        };

    }
    public static Specification<Book> hasAuthor(Integer id){
        return (root, query, criteriaBuilder) ->
                id==null?null:criteriaBuilder.equal(root.get("author").get("id"),id);
    }

}
