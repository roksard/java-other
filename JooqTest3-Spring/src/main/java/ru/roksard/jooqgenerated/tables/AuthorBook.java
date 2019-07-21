/*
 * This file is generated by jOOQ.
 */
package ru.roksard.jooqgenerated.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import ru.roksard.jooqgenerated.Indexes;
import ru.roksard.jooqgenerated.Keys;
import ru.roksard.jooqgenerated.Public;
import ru.roksard.jooqgenerated.tables.records.AuthorBookRecord;


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
public class AuthorBook extends TableImpl<AuthorBookRecord> {

    private static final long serialVersionUID = 235762277;

    /**
     * The reference instance of <code>public.author_book</code>
     */
    public static final AuthorBook AUTHOR_BOOK = new AuthorBook();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AuthorBookRecord> getRecordType() {
        return AuthorBookRecord.class;
    }

    /**
     * The column <code>public.author_book.author_id</code>.
     */
    public final TableField<AuthorBookRecord, Integer> AUTHOR_ID = createField("author_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.author_book.book_id</code>.
     */
    public final TableField<AuthorBookRecord, Integer> BOOK_ID = createField("book_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>public.author_book</code> table reference
     */
    public AuthorBook() {
        this(DSL.name("author_book"), null);
    }

    /**
     * Create an aliased <code>public.author_book</code> table reference
     */
    public AuthorBook(String alias) {
        this(DSL.name(alias), AUTHOR_BOOK);
    }

    /**
     * Create an aliased <code>public.author_book</code> table reference
     */
    public AuthorBook(Name alias) {
        this(alias, AUTHOR_BOOK);
    }

    private AuthorBook(Name alias, Table<AuthorBookRecord> aliased) {
        this(alias, aliased, null);
    }

    private AuthorBook(Name alias, Table<AuthorBookRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> AuthorBook(Table<O> child, ForeignKey<O, AuthorBookRecord> key) {
        super(child, key, AUTHOR_BOOK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.AUTHOR_BOOK_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<AuthorBookRecord> getPrimaryKey() {
        return Keys.AUTHOR_BOOK_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<AuthorBookRecord>> getKeys() {
        return Arrays.<UniqueKey<AuthorBookRecord>>asList(Keys.AUTHOR_BOOK_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<AuthorBookRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<AuthorBookRecord, ?>>asList(Keys.AUTHOR_BOOK__FK_AB_AUTHOR, Keys.AUTHOR_BOOK__FK_AB_BOOK);
    }

    public Author author() {
        return new Author(this, Keys.AUTHOR_BOOK__FK_AB_AUTHOR);
    }

    public Book book() {
        return new Book(this, Keys.AUTHOR_BOOK__FK_AB_BOOK);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthorBook as(String alias) {
        return new AuthorBook(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthorBook as(Name alias) {
        return new AuthorBook(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public AuthorBook rename(String name) {
        return new AuthorBook(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public AuthorBook rename(Name name) {
        return new AuthorBook(name, null);
    }
}