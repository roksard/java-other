/*
 * This file is generated by jOOQ.
 */
package ru.roksard.jooqgenerated;


import javax.annotation.Generated;

import org.jooq.Sequence;
import org.jooq.impl.SequenceImpl;


/**
 * Convenience access to all sequences in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.11"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Sequences {

    /**
     * The sequence <code>public.employees_id_seq</code>
     */
    public static final Sequence<Integer> EMPLOYEES_ID_SEQ = new SequenceImpl<Integer>("employees_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.INTEGER.nullable(false));

    /**
     * The sequence <code>public.organisations_id_seq</code>
     */
    public static final Sequence<Integer> ORGANISATIONS_ID_SEQ = new SequenceImpl<Integer>("organisations_id_seq", Public.PUBLIC, org.jooq.impl.SQLDataType.INTEGER.nullable(false));
}
