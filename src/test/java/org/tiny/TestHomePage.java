package org.tiny;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tiny.customtable.editor.ColumnStructure;
import org.tiny.customtable.editor.DataType;
import org.tiny.customtable.editor.TableStructure;
import org.tiny.datawrapper.Column;
import org.tiny.datawrapper.IncrementalKey;
import org.tiny.datawrapper.RelationInfo;
import org.tiny.datawrapper.Table;
import org.tiny.datawrapper.TinyDatabaseException;
import org.tiny.datawrapper.annotations.LogicalName;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage {

    private WicketTester tester;
    private TestTable testTable;

    @BeforeEach
    public void setUp() {
        tester = new WicketTester(new WicketApplication());
        testTable = new TestTable();
    }

    @Test
    public void homepageRendersSuccessfully() throws Exception {

        testTable.TestTableString.add(
                new RelationInfo(
                        TestTable.class,
                        testTable.TestTableId
                )
        );

        ColumnStructure cstructure = new ColumnStructure(testTable.TestTableString);
        String columnLogicalName = (String) cstructure.get(ColumnStructure.LOGICAL_NAME);
        String constr = cstructure.getConstructParameterTalken();
        String def = cstructure.getDefineTalken();
        String ref = cstructure.getRelationTalken();

        TableStructure tstr = new TableStructure(testTable);
        String classHead = tstr.getTableClassHeader();
        String ts = tstr.getGroovyCode();
        System.out.println(ts);
        
        TableStructure handMade = new TableStructure("TestTable");
        handMade.setLogicalName("テストテーブル");
        
        ColumnStructure testTableId = handMade.createColumnStructure("TestTableId");
        testTableId.put(ColumnStructure.LOGICAL_NAME, "テストテーブルID");
        testTableId.put(ColumnStructure.AUTO_INCREMENT, "true");
        testTableId.put(ColumnStructure.DATA_TYPE, DataType.INTEGER.getDataTypeValue());
        testTableId.put(ColumnStructure.VISIBLE_TYPE, Column.VISIBLE_TYPE_LABEL);
        
        ColumnStructure testTableString = handMade.createColumnStructure("TestTableString");
        testTableString.put(ColumnStructure.LOGICAL_NAME, "テストテーブル文字列");
        testTableString.put(ColumnStructure.DATA_TYPE, DataType.STRING.getDataTypeValue());
        testTableString.put(ColumnStructure.LENGTH, Column.SIZE_1024);
        testTableString.put(ColumnStructure.VISIBLE_TYPE, Column.VISIBLE_TYPE_TEXT);
        testTableString.put(ColumnStructure.RELATIONAL_TABLE, TableStructure.CUSTOM_TABLE_PACKAGE + ".TestTable");
        testTableString.put(ColumnStructure.RELATIONAL_COLUMN, "TestTableId");
        
        DataType[] types = DataType.values();
        
        String hs = handMade.getNormalizedGroovyCode();
        System.out.println(hs);
        
        if(!ts.equals(hs)){
            throw new Exception("ソースコード比較の結果、差異があります。");
        }
        
        System.out.println(handMade.getRelationCode());

        //start and render the test page
        tester.startPage(HomePage.class);

        //assert rendered page class
        tester.assertRenderedPage(HomePage.class);
    }

    @LogicalName("テストテーブル")
    public class TestTable extends Table {

        @LogicalName("テストテーブルID")
        public IncrementalKey TestTableId;

        @LogicalName("テストテーブル文字列")
        public Column<String> TestTableString;

        @Override
        public void defineColumns() throws TinyDatabaseException {
            this.TestTableId.setAllowNull(true)
                    .setPrimaryKey(true)
                    .setVisibleType(Column.VISIBLE_TYPE_LABEL);
            
            this.TestTableString
                    .setLength(Column.SIZE_1024);
        }

    }

}
