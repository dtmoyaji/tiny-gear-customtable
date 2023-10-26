package org.tiny;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.tiny.customtable.editor.TableStructureEditor;

public class HomePage extends WebPage {

    private static final long serialVersionUID = 1L;

    private TableStructureEditor tableStructureEditor;

    public HomePage(final PageParameters parameters) {
        super(parameters);

        this.tableStructureEditor = new TableStructureEditor("tableStructureEditor");
        this.add(this.tableStructureEditor);

    }
}
