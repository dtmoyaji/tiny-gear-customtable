
package org.tiny.customtable.editor;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;


/**
 *
 * @author dtmoyaji
 */
public class TableStructureEditor extends Panel{
    
    private Form tableStructureEditorForm;
    
    public TableStructureEditor(String id){
        super(id);
        
        this.tableStructureEditorForm=new Form("tableStructureEditorForm");
        this.add(this.tableStructureEditorForm);
    }

}
