package org.tiny.customtable.editor;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.util.ArrayList;
import org.tiny.datawrapper.Column;
import org.tiny.datawrapper.Table;

/**
 * @author dtmoyaji
 */
public class TableStructure extends ArrayList<ColumnStructure> {

    public static final String CUSTOM_TABLE_PACKAGE = "org.tiny.gear.customtable";

    private Table srcTable;

    private String tableDefHeader = "";
    private String tablePhisicalName = "";
    private String tableLogicalName = "";

    public TableStructure(Table srcTable) {

        this.srcTable = srcTable;
        this.tablePhisicalName = srcTable.getJavaName();
        this.tableLogicalName = srcTable.getLogicalName();

        for (Column col : this.srcTable) {
            this.add(new ColumnStructure(col));
        }

        this.initTableClassHeader();
    }

    public TableStructure(String tableName) {
        this.tablePhisicalName = tableName;
        this.initTableClassHeader();
    }

    public void setLogicalName(String logicalName) {
        this.tableLogicalName = logicalName;
    }

    private void initTableClassHeader() {
        this.tableDefHeader += "package " + CUSTOM_TABLE_PACKAGE + ";\n\n";
        this.tableDefHeader += "import java.sql.Timestamp; \n";
        this.tableDefHeader += "import org.tiny.datawrapper.annotations.LogicalName; \n";
        this.tableDefHeader += "import org.tiny.datawrapper.annotations.Comment; \n";
        this.tableDefHeader += "import org.tiny.datawrapper.Table; \n";
        this.tableDefHeader += "import org.tiny.datawrapper.View; \n";
        this.tableDefHeader += "import org.tiny.datawrapper.Column; \n";
        this.tableDefHeader += "import org.tiny.datawrapper.IncrementalKey; \n";
        this.tableDefHeader += "import org.tiny.datawrapper.ShortFlagZero; \n";
        this.tableDefHeader += "import org.tiny.datawrapper.CurrentTimestamp; \n";
        this.tableDefHeader += "import org.tiny.datawrapper.TinyDatabaseException; \n";
    }

    public String getTableClassHeader() {
        return this.tableDefHeader;
    }

    public String getGroovyCode() {
        String rvalue = "";
        rvalue += "@LogicalName(\"%s\")\n";
        rvalue += "public class %s extends Table {\n\n";
        rvalue += "%s\n"; //カラムの変数宣言
        rvalue += "public void defineColumns() throws TinyDatabaseException {\n";
        rvalue += "%s\n"; //変数宣言
        rvalue += "}\n}\n";

        String columnConstructTalken = ""; //カラムの変数宣言を格納する
        String columnDefTalken = "";
        for (ColumnStructure cstr : this) {
            columnConstructTalken += cstr.getConstructParameterTalken();
            columnDefTalken += cstr.getDefineTalken();
        }

        rvalue = String.format(
                rvalue,
                this.tableLogicalName,
                this.tablePhisicalName,
                columnConstructTalken,
                columnDefTalken
        );
        return rvalue;
    }

    public ColumnStructure createColumnStructure(String phisicalName) {
        ColumnStructure rvalue = new ColumnStructure(phisicalName);
        this.add(rvalue);
        return rvalue;
    }

    public boolean contains(String phisicalName) {
        boolean rvalue = false;
        for (ColumnStructure cstr : this) {
            if (cstr.get(ColumnStructure.PHISICAL_NAME).equals(phisicalName)) {
                rvalue = true;
                break;
            }
        }
        return rvalue;
    }

    public String getRelationCode() {
        String rvalue = "";
        for (ColumnStructure cstr : this) {
            if (cstr.containsKey(ColumnStructure.RELATIONAL_TABLE)) {
                rvalue += cstr.getRelationTalken() + "\n";
            }
        }
        return rvalue;
    }

    /**
     * Goorvyでオブジェクト化した後、もう一度TableStructrueにマップすることで、クラスの表記を正規化する。
     *
     * @return
     */
    public String getNormalizedGroovyCode() {
        String cfi = String.format(
                "return new %s();", this.tablePhisicalName
        );
        String tableDef = this.tableDefHeader + "\n" + this.getGroovyCode() + "\n" + cfi;
        Binding binding = new Binding();
        GroovyShell grshell = new GroovyShell(binding);
        Table rvalue = (Table) grshell.evaluate(tableDef);

        TableStructure buf = new TableStructure(rvalue);
        return buf.getGroovyCode();
    }

    public String getSqlCreateStatement() {
        if (!this.isEmpty()) {
            String cfi = String.format(
                    "return new %s();", this.tablePhisicalName
            );
            String tableDef = this.tableDefHeader + "\n" + this.getGroovyCode() + "\n" + cfi;
            Binding binding = new Binding();
            GroovyShell grshell = new GroovyShell(binding);
            Table rvalue = (Table) grshell.evaluate(tableDef);
            return rvalue.getCreateSentence();
        } else {
            return "For sql creation, More than one column defintion is required.";
        }
    }

}
