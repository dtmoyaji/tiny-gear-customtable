package org.tiny.customtable.editor;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.tiny.datawrapper.Column;
import org.wicketstuff.select2.Select2Choice;

/**
 *
 * @author dtmoyaji
 */
public class ColumnStructureLineView extends Panel {

    private static String PHISICAL_NAME = "phisicalName";
    private static String LOGICAL_NAME = "logicalName";
    private static String DATA_TYPE = "dataType";
    private static String LENGTH = "length";
    private static String FLOAT_LENGTH = "floatlength";
    private static String PRIMARY_KEY = "primarykey";

    private String phisicalName;

    private String logicalName;

    private String dataType;

    private int length = -1;

    private int floatlength = -1;

    private boolean primarykey;

    public ColumnStructureLineView(String id, IModel<Column> model) {
        super(id, model);
        this.add(new TextField(PHISICAL_NAME, new PropertyModel(this, PHISICAL_NAME)));
        this.add(new TextField(LOGICAL_NAME, new PropertyModel(this, LOGICAL_NAME)));

        Select2Choice<DataType> dataTypeChoice = new Select2Choice<>(
                DATA_TYPE,
                new PropertyModel(this, DATA_TYPE),
                new DataTypeProvider()
        );
        this.add(dataTypeChoice);

        this.add(new TextField(LENGTH, new PropertyModel(this, LENGTH)));
        this.add(new TextField(FLOAT_LENGTH, new PropertyModel(this, FLOAT_LENGTH)));
        this.add(new CheckBox(PRIMARY_KEY, new PropertyModel(this, PRIMARY_KEY)));

        this.setOutputMarkupId(true);
    }

}
