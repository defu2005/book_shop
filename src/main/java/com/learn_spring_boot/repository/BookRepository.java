package com.learn_spring_boot.repository;

import com.learn_spring_boot.entity.Book;
import com.learn_spring_boot.enums.BookSort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends SoftDeleteRepository<Book, Long>, JpaSpecificationExecutor<Book> {
	List<Book> findAllByAuthor_Id(int authorId);
	List<Book> findAllByCategories_Id(int categoryId);
}
