package org.tiny;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tiny.customtable.column.ColumnStructure;
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

    @BeforeEach
    public void setUp() {
        tester = new WicketTester(new WicketApplication());
    }

    @Test
    public void homepageRendersSuccessfully() {

        TestTable testTable = new TestTable();
        testTable.TestTableId.add(
                new RelationInfo(
                        TestTable.class,
                        testTable.TestTableString
                )
        );

        ColumnStructure cstructure = new ColumnStructure(testTable.TestTableString);
        String columnLogicalName = (String) cstructure.get(ColumnStructure.LOGICAL_NAME);
        String constr = cstructure.getConstructParameterTalken();
        String def = cstructure.getDefineTalken();

        //start and render the test page
        tester.startPage(HomePage.class);

        //assert rendered page class
        tester.assertRenderedPage(HomePage.class);
    }

    public class TestTable extends Table {

        @LogicalName("テストテーブルID")
        public IncrementalKey TestTableId;

        @LogicalName("テストテーブル文字列")
        public Column<String> TestTableString;

        @Override
        public void defineColumns() throws TinyDatabaseException {
            this.TestTableString.setAllowNull(true)
                    .setPrimaryKey(true)
                    .setLength(Column.SIZE_1024)
                    .setVisibleType(Column.VISIBLE_TYPE_LABEL);
        }

    }
}
