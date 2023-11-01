package org.tiny.customtable.editor;

import java.util.ArrayList;
import java.util.Collection;
import org.wicketstuff.select2.ChoiceProvider;
import org.wicketstuff.select2.Response;

/**
 *
 * @author dtmoyaji
 */
public class DataTypeProvider extends ChoiceProvider<DataType> {

    @Override
    public String getDisplayValue(DataType t) {
        return t.getDataTypeValue();
    }

    @Override
    public String getIdValue(DataType t) {
        return t.name();
    }

    @Override
    public void query(String string, int i, Response<DataType> rspns) {
        if (string == null || string.length() < 0) {
            int num = 0;
            for (DataType dtype : DataType.values()) {
                rspns.add(dtype);
                num++;
                if (num > 4) {
                    break;
                }
            }
        } else {
            for (DataType dtype : DataType.values()) {
                if (dtype.getDataTypeValue().toUpperCase().contains(string.toUpperCase())) {
                    rspns.add(dtype);
                }
            }
        }
    }

    @Override
    public Collection<DataType> toChoices(Collection<String> clctn) {
        ArrayList<DataType> rvalue = new ArrayList<>();
        for (DataType dtype : DataType.values()) {
            for (String sz : clctn) {
                if (dtype.getDataTypeValue().equals(sz)) {
                    rvalue.add(dtype);
                }
            }
        }
        return rvalue;
    }

}
