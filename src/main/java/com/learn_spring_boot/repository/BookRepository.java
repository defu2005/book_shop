package com.learn_spring_boot.repository;

import com.learn_spring_boot.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends SoftDeleteRepository<Book, Long>  {
	List<Book> findAllByAuthor_Id(int authorId);
	List<Book> findAllByCategories_Id(int categoryId);
	@Query("SELECT b FROM Book b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%',:keyword,'%'))" +
			"OR LOWER(b.author.name) LIKE LOWER(CONCAT('%',:keyword,'%')) ")
	List<Book> search(@Param("keyword") String keyword);
}
