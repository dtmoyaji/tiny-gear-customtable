
package org.tiny.customtable.editor;

/**
 *
 * @author dtmoyaji
 */
public enum DataType {
    INTEGER     ("Integer"),
    SHORT       ("Short"),
    STRING      ("String"),
    TIMESTAMP   ("Timestamp"),
    DATE        ("Date"),
    TIME        ("Time"),
    CHARACTERS  ("char[]"),
    DECIMAL     ("BigDecimal");
    
    String dataType;
    
    private DataType(String type){
        this.dataType = type;
    }
    
    public String getDataTypeValue(){
        return this.dataType;
    }

}
