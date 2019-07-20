/*
 * This file is generated by jOOQ.
 */
package ru.roksard.jooqgenerated.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

import ru.roksard.jooqgenerated.tables.AuthorBook;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class AuthorBookRecord extends UpdatableRecordImpl<AuthorBookRecord> implements Record2<Integer, Integer> {

    private static final long serialVersionUID = 731334395;

    /**
     * Setter for <code>public.author_book.author_id</code>.
     */
    public void setAuthorId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.author_book.author_id</code>.
     */
    public Integer getAuthorId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.author_book.book_id</code>.
     */
    public void setBookId(Integer value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.author_book.book_id</code>.
     */
    public Integer getBookId() {
        return (Integer) get(1);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record2<Integer, Integer> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<Integer, Integer> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row2<Integer, Integer> valuesRow() {
        return (Row2) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return AuthorBook.AUTHOR_BOOK.AUTHOR_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field2() {
        return AuthorBook.AUTHOR_BOOK.BOOK_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getAuthorId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component2() {
        return getBookId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getAuthorId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value2() {
        return getBookId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthorBookRecord value1(Integer value) {
        setAuthorId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthorBookRecord value2(Integer value) {
        setBookId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthorBookRecord values(Integer value1, Integer value2) {
        value1(value1);
        value2(value2);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached AuthorBookRecord
     */
    public AuthorBookRecord() {
        super(AuthorBook.AUTHOR_BOOK);
    }

    /**
     * Create a detached, initialised AuthorBookRecord
     */
    public AuthorBookRecord(Integer authorId, Integer bookId) {
        super(AuthorBook.AUTHOR_BOOK);

        set(0, authorId);
        set(1, bookId);
    }
}
