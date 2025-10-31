package com.learn_spring_boot.repository;

import com.learn_spring_boot.entity.Book;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends SoftDeleteRepository<Book, Long> {
	List<Book> findAllByAuthor_Id(int authorId);
	List<Book> findAllByCategories_Id(int categoryId);
}
