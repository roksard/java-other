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
import ru.roksard.jooqgenerated.tables.records.OrganisationEmployeeRecord;


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
public class OrganisationEmployee extends TableImpl<OrganisationEmployeeRecord> {

    private static final long serialVersionUID = 2028922231;

    /**
     * The reference instance of <code>public.organisation_employee</code>
     */
    public static final OrganisationEmployee ORGANISATION_EMPLOYEE = new OrganisationEmployee();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<OrganisationEmployeeRecord> getRecordType() {
        return OrganisationEmployeeRecord.class;
    }

    /**
     * The column <code>public.organisation_employee.organisation_id</code>.
     */
    public final TableField<OrganisationEmployeeRecord, Integer> ORGANISATION_ID = createField("organisation_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.organisation_employee.employee_id</code>.
     */
    public final TableField<OrganisationEmployeeRecord, Integer> EMPLOYEE_ID = createField("employee_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>public.organisation_employee</code> table reference
     */
    public OrganisationEmployee() {
        this(DSL.name("organisation_employee"), null);
    }

    /**
     * Create an aliased <code>public.organisation_employee</code> table reference
     */
    public OrganisationEmployee(String alias) {
        this(DSL.name(alias), ORGANISATION_EMPLOYEE);
    }

    /**
     * Create an aliased <code>public.organisation_employee</code> table reference
     */
    public OrganisationEmployee(Name alias) {
        this(alias, ORGANISATION_EMPLOYEE);
    }

    private OrganisationEmployee(Name alias, Table<OrganisationEmployeeRecord> aliased) {
        this(alias, aliased, null);
    }

    private OrganisationEmployee(Name alias, Table<OrganisationEmployeeRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> OrganisationEmployee(Table<O> child, ForeignKey<O, OrganisationEmployeeRecord> key) {
        super(child, key, ORGANISATION_EMPLOYEE);
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
        return Arrays.<Index>asList(Indexes.ORGANISATION_EMPLOYEE_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<OrganisationEmployeeRecord> getPrimaryKey() {
        return Keys.ORGANISATION_EMPLOYEE_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<OrganisationEmployeeRecord>> getKeys() {
        return Arrays.<UniqueKey<OrganisationEmployeeRecord>>asList(Keys.ORGANISATION_EMPLOYEE_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<OrganisationEmployeeRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<OrganisationEmployeeRecord, ?>>asList(Keys.ORGANISATION_EMPLOYEE__FK_ORGANISATION, Keys.ORGANISATION_EMPLOYEE__FK_EMPLOYEE);
    }

    public Organisations organisations() {
        return new Organisations(this, Keys.ORGANISATION_EMPLOYEE__FK_ORGANISATION);
    }

    public Employees employees() {
        return new Employees(this, Keys.ORGANISATION_EMPLOYEE__FK_EMPLOYEE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrganisationEmployee as(String alias) {
        return new OrganisationEmployee(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrganisationEmployee as(Name alias) {
        return new OrganisationEmployee(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public OrganisationEmployee rename(String name) {
        return new OrganisationEmployee(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public OrganisationEmployee rename(Name name) {
        return new OrganisationEmployee(name, null);
    }
}
