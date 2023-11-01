package org.tiny.customtable.editor;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.tiny.datawrapper.Column;

/**
 *
 * @author dtmoyaji
 */
public class TableStructureEditor extends Panel {

    private Form defineControler;

    private Form classConstructionView;

    private Form tableStructureEditorForm;
    
    private AjaxButton editorVisibleButton;    
    private AjaxButton classConstractVisibleButton;
    
    public static final String PHISICAL_NAME = "phisicalName";
    public static final String LOGICAL_NAME = "logicalName";
    public static final String CLASS_CODE = "classCode";

    private String phisicalName = "NewTable";
    private String logicalName = "新規テーブル";
    
    private TableStructure tableStructure;
    private MultiLineLabel classStructure;
    
    private String classCode = "";

    public TableStructureEditor(String id) {
        super(id);

        this.tableStructureEditorForm = new Form("tableStructureEditorForm");
        this.add(this.tableStructureEditorForm);

        this.tableStructureEditorForm.add(
                new TextField(PHISICAL_NAME, new PropertyModel(this, PHISICAL_NAME))
        );

        this.tableStructureEditorForm.add(
                new TextField(LOGICAL_NAME, new PropertyModel(this, LOGICAL_NAME))
        );

        // 表示切り換えコントローラー
        this.editorVisibleButton = new AjaxButton("editorVisibleButton") {
            @Override
            public void onSubmit(AjaxRequestTarget target) {
                editorVisibleButton.add(new AttributeModifier("selected",Model.of("true")));
                target.add(editorVisibleButton);
                classConstractVisibleButton.add(new AttributeModifier("selected", Model.of("false")));
                target.add(classConstractVisibleButton);
                defineControler.add(new AttributeModifier("selected",Model.of("true")));
                target.add(defineControler);
                classConstructionView.add(new AttributeModifier("selected",Model.of("false")));
                target.add(classConstructionView);
            }
        };
        this.editorVisibleButton.setOutputMarkupId(true);
        this.tableStructureEditorForm.add(editorVisibleButton);

        this.classConstractVisibleButton = new AjaxButton("classConstractVisibleButton") {
            @Override
            public void onSubmit(AjaxRequestTarget target){
                editorVisibleButton.add(new AttributeModifier("selected", Model.of("false")));
                target.add(editorVisibleButton);
                classConstractVisibleButton.add(new AttributeModifier("selected",Model.of("true")));
                target.add(classConstractVisibleButton);
                defineControler.add(new AttributeModifier("selected",Model.of("false")));
                target.add(defineControler);
                classConstructionView.add(new AttributeModifier("selected",Model.of("true")));
                target.add(classConstructionView);
                classCode = generateCode();
                target.add(classStructure);
                System.out.println(tableStructure.getSqlCreateStatement());
            }
        };
        this.classConstractVisibleButton.setOutputMarkupId(true);
        this.tableStructureEditorForm.add(classConstractVisibleButton);

        // 編集用コントローラ
        this.defineControler = new Form("defineControler");
        this.tableStructureEditorForm.add(this.defineControler);

        ColumnStructureLineView cslv = new ColumnStructureLineView(
                "columnStructureLineView",
                new Model(new Column())
        );
        this.defineControler.add(cslv);
        this.defineControler.setOutputMarkupId(true);

        // ビュー
        this.classConstructionView = new Form("classConstructionView");
        this.classConstructionView.setOutputMarkupId(true);
        this.tableStructureEditorForm.add(this.classConstructionView);
        
        this.classStructure = new MultiLineLabel("classStructure", new PropertyModel(this, CLASS_CODE));
        this.classStructure.setOutputMarkupId(true);
        this.classConstructionView.add(this.classStructure);
        
    }
    
    public String generateCode(){
       this.tableStructure = new TableStructure(this.phisicalName);
       this.tableStructure.setLogicalName(this.logicalName);

       return this.tableStructure.getGroovyCode();
    }
    
    public TableStructure getTableStructure(){
        return this.tableStructure;
    }

}
