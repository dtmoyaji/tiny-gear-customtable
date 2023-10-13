package org.tiny.customtable.column;

/**
 *
 * @author dtmOyaji
 */
public enum VisibleType {
    
    VISIBLE_TYPE_HIDDEN(1),
    VISIBLE_TYPE_LABEL(2),
    VISIBLE_TYPE_TEXT(4),
    VISIBLE_TYPE_CHECKBOX(8),
    VISIBLE_TYPE_PASSWORD(16),
    VISIBLE_TYPE_PASSWORD_NOREQ(32),
    VISIBLE_TYPE_TEXTAREA(64),
    VISIBLE_TYPE_RICHTEXTAREA(128),
    VISIBLE_TYPE_DATE(256),
    VISIBLE_TYPE_DATETIME(512);

    private int visibleTypeValue;

    private VisibleType(int type) {
        this.visibleTypeValue = type;
    }

    private int getVisibleTypeValue() {
        return this.visibleTypeValue;
    }
    
    public static VisibleType build(int type){
        VisibleType rvalue = VisibleType.VISIBLE_TYPE_LABEL;
        VisibleType[] types = VisibleType.values();
        for(VisibleType vt : types){
            if(vt.getVisibleTypeValue()==type){
                rvalue = vt;
            }
        }
        return rvalue;
    }
}
