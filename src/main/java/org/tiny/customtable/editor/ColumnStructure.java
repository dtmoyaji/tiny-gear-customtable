package org.tiny.customtable.editor;

import java.util.HashMap;
import java.util.Iterator;
import org.tiny.datawrapper.Column;
import org.tiny.datawrapper.NameDescriptor;
import org.tiny.datawrapper.RelationInfo;

/**
 *
 * @author dtmoyaji
 */
public class ColumnStructure extends HashMap<String, Object> {

    public static final String PHISICAL_NAME = "PhisicalName";

    public static final String LOGICAL_NAME = "LogicalName";

    public static final String DESCRIPTION = "Description";

    public static final String DATA_TYPE = "DataType";

    public static final String LENGTH = "Length";

    public static final String FLOAT_LENGTH = "FloatLength";

    public static final String PRIMARY_KEY = "PrimaryKey";

    public static final String ALLOW_NULL = "AllowNull";

    public static final String AUTO_INCREMENT = "AutoIncrement";

    public static final String DEFAULT = "Default";

    public static final String RELATIONAL_TABLE = "RelationalTable";

    public static final String RELATIONAL_COLUMN = "RelationalColumn";

    public static final String VISIBLE_TYPE = "VisibleType";

    public static final String TEMPLATE_STRING = "\t.set%s(\"%s\")\n";

    public static final String TEMPLATE_NONSTRING = "\t.set%s(%s)\n";

    public static final String TEMPLATE_COLUMN_CONSTANT = "\t.set%s(Column.%s)\n";

    private Column srcColumn;

    public ColumnStructure(String columnPhisicalName) {
        this.put(PHISICAL_NAME, columnPhisicalName);
    }

    public ColumnStructure(Column src) {
        this.srcColumn = src;
        this.srcColumn.bindFieldWithArray();
        this.mapStructure(src);
    }

    public void mapStructure(Column src) {
        this.put(ColumnStructure.PHISICAL_NAME, src.getJavaName());
        this.put(ColumnStructure.LOGICAL_NAME, src.getTable().getColumnLogicalName(src));
        this.put(ColumnStructure.DATA_TYPE, src.getType());
        this.put(ColumnStructure.ALLOW_NULL, src.isNullable());
        this.put(ColumnStructure.LENGTH, src.getLength());
        this.put(ColumnStructure.FLOAT_LENGTH, src.getFloatLength());
        this.put(ColumnStructure.DEFAULT, src.getDefault());
        this.put(ColumnStructure.AUTO_INCREMENT, src.isAutoIncrement());
        this.put(ColumnStructure.VISIBLE_TYPE, src.getVisibleType());
        if (!src.isEmpty()) {
            for (Iterator it = src.iterator(); it.hasNext();) {
                RelationInfo relinfo = (RelationInfo) it.next();
                this.put(ColumnStructure.RELATIONAL_TABLE, relinfo.getTableClass().getCanonicalName());
                this.put(ColumnStructure.RELATIONAL_COLUMN, relinfo.getColumn().getJavaName());
            }
        }
    }

    public String getConstructParameterTalken() {
        String template = "@LogicalName(\"%s\")\n";
        template += "public Column<%s> %s;\n\n";
        template = String.format(
                template,
                this.get(ColumnStructure.LOGICAL_NAME),
                this.get(ColumnStructure.DATA_TYPE),
                this.get(ColumnStructure.PHISICAL_NAME)
        );
        return template;
    }

    public String getDefineTalken() {
        String template = "this.%s\n";
        template = String.format(template, this.get(ColumnStructure.PHISICAL_NAME));

        template += this.getIfContain(
                ColumnStructure.AUTO_INCREMENT,
                ColumnStructure.TEMPLATE_NONSTRING
        );
        template += this.getIfContain(
                ColumnStructure.LENGTH,
                ColumnStructure.TEMPLATE_NONSTRING
        );
        template += this.getIfContain(
                ColumnStructure.FLOAT_LENGTH,
                ColumnStructure.TEMPLATE_NONSTRING
        );
        template += this.getIfContain(
                ColumnStructure.ALLOW_NULL,
                ColumnStructure.TEMPLATE_NONSTRING
        );
        template += this.getIfContain(
                ColumnStructure.DEFAULT,
                ColumnStructure.TEMPLATE_STRING
        );
        template += this.deCodeVisibleType(
                ColumnStructure.VISIBLE_TYPE
        );

        template += "\t;\n";
        return template;
    }

    public String getIfContain(String name, String template) {
        String rvalue = "";
        if (this.containsKey(name)) {
            rvalue = String.format(template, name, this.get(name));
        }
        return rvalue;
    }

    public String deCodeVisibleType(String name) {
        if (!this.containsKey(ColumnStructure.VISIBLE_TYPE)) {
            throw new NullPointerException("Visible Type is not specified.");
        }
        int type = (int) this.get(ColumnStructure.VISIBLE_TYPE);
        String typeString = VisibleType.build(type).name();
        String rvalue = String.format(
                ColumnStructure.TEMPLATE_COLUMN_CONSTANT,
                name,
                typeString
        );
        return rvalue;
    }

    public String getRelationTalken() {
        String rvalue = "";
        if (this.containsKey(ColumnStructure.RELATIONAL_COLUMN)
                && this.containsKey(ColumnStructure.RELATIONAL_TABLE)) {
            String format = "_RelationInfo.add(\"%s,%s,%s\");\n";
            String relationFrom = (String) this.get(ColumnStructure.PHISICAL_NAME);
            relationFrom = NameDescriptor.toSqlName(relationFrom);
            String className = (String) this.get(ColumnStructure.RELATIONAL_TABLE);
            String relationTo = (String) this.get(ColumnStructure.RELATIONAL_COLUMN);
            relationTo = NameDescriptor.toSqlName(relationTo);
            rvalue = String.format(format, relationFrom, className, relationTo);
        }
        return rvalue;
    }

}
