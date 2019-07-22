package ru.roksard.jooqtest;

import org.jooq.DSLContext;
import org.jooq.Record3;
import org.jooq.Result;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.roksard.jooqgenerated.tables.Author;
import ru.roksard.jooqgenerated.tables.AuthorBook;
import ru.roksard.jooqgenerated.tables.Book;

@Component
public class DBInteract {
	@Autowired
	private DSLContext dsl;
	 
	Author author = Author.AUTHOR;
	Book book = Book.BOOK;
	AuthorBook authorBook = AuthorBook.AUTHOR_BOOK;
	
	public void insertData() {
		dsl.insertInto(author)
		  .set(author.ID, 4)
		  .set(author.FIRST_NAME, "Herbert")
		  .set(author.LAST_NAME, "Schildt")
		  .execute();
		dsl.insertInto(book)
		  .set(book.ID, 4)
		  .set(book.TITLE, "A Beginner's Guide")
		  .execute();
		dsl.insertInto(authorBook)
		  .set(authorBook.AUTHOR_ID, 4)
		  .set(authorBook.BOOK_ID, 4)
		  .execute();
	}
	
	public void extractDataAfterInsert() {
		Result<Record3<Integer, String, String>> result = dsl
				  .select(author.ID, author.LAST_NAME, author.FIRST_NAME)
				  .from(author)
//				  .join(authorBook)
//				  .on(author.ID.equal(authorBook.AUTHOR_ID))
//				  .join(book)
//				  .on(authorBook.BOOK_ID.equal(book.ID))
				  //.groupBy(author.LAST_NAME)
				  .fetch();
		System.out.println(result);
		/* should produce:
+----+---------+-----+
|  ID|LAST_NAME|count|
+----+---------+-----+
|   1|Sierra   |    2|
|   2|Bates    |    1|
|   4|Schildt  |    1|
+----+---------+-----+
		 */
	}
	
	public void updateData() {
		dsl.update(author)
		  .set(author.LAST_NAME, "Baeldung")
		  .where(author.ID.equal(3))
		  .execute();
		dsl.update(book)
		  .set(book.TITLE, "Building your REST API with Spring")
		  .where(book.ID.equal(3))
		  .execute();
		dsl.insertInto(authorBook)
		  .set(authorBook.AUTHOR_ID, 3)
		  .set(authorBook.BOOK_ID, 3)
		  .execute(); 
	}
	
	public void extractDataAfterUpdate() {
		Result<Record3<Integer, String, String>> result = dsl
				  .select(author.ID, author.LAST_NAME, book.TITLE)
				  .from(author)
				  .join(authorBook)
				  .on(author.ID.equal(authorBook.AUTHOR_ID))
				  .join(book)
				  .on(authorBook.BOOK_ID.equal(book.ID))
				  .where(author.ID.equal(3))
				  .fetch();
		/*
		 * should be:
+----+---------+----------------------------------+
|  ID|LAST_NAME|TITLE                             |
+----+---------+----------------------------------+
|   3|Baeldung |Building your REST API with Spring|
+----+---------+----------------------------------+
		 */
	}
	
	public void deleteData() {
		dsl.delete(author)
		  .where(author.ID.lt(3))
		  .execute();
	}
	
	public void extractAfterDelete() {
		Result<Record3<Integer, String, String>> result = dsl
				  .select(author.ID, author.FIRST_NAME, author.LAST_NAME)
				  .from(author)
				  .fetch();
		
		/*
		 * The query output:

+----+----------+---------+
|  ID|FIRST_NAME|LAST_NAME|
+----+----------+---------+
|   3|Bryan     |Basham   |
+----+----------+---------+
		 */
	}
	
	
	
}
