/*
 * This file is generated by jOOQ.
 */
package ru.roksard.jooqgenerated;


import javax.annotation.Generated;

import ru.roksard.jooqgenerated.tables.Author;
import ru.roksard.jooqgenerated.tables.AuthorBook;
import ru.roksard.jooqgenerated.tables.Book;


/**
 * Convenience access to all tables in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.author</code>.
     */
    public static final Author AUTHOR = ru.roksard.jooqgenerated.tables.Author.AUTHOR;

    /**
     * The table <code>public.author_book</code>.
     */
    public static final AuthorBook AUTHOR_BOOK = ru.roksard.jooqgenerated.tables.AuthorBook.AUTHOR_BOOK;

    /**
     * The table <code>public.book</code>.
     */
    public static final Book BOOK = ru.roksard.jooqgenerated.tables.Book.BOOK;
}
