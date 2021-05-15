package utils;

/**
 *
 * @author Vibeke
 */
public class InputSanitiser {
    /**
     * -- -
    '1' = '1
    1=1
    or ''='
    "="
    '='
    "" og ''
    ; {sql code}
    drop table
    %00
    ';' or %3B
     */
    public String sanitiser(String str) {
        /*
        Fjern sql kode fra content trin:
        Tjek om sql kode er der
        Find start og slut index
        Fjern den del fra string
        Send resten til database?
        */
        
        String semicolon = ";";
        String sqlDropTable = "; DROP TABLE";
        String sqlDeleteFrom = "; DELETE FROM";
        String sqlSelectFrom = "; SELECT \\*";
        String sqlSelectFrom2 = "; SELECT *";
        String sqlInsert = "; INSERT INTO";
        String sqlComment = "; --";
        
        if(str.toUpperCase().contains(sqlDropTable)) {
            int index = str.indexOf(semicolon);
            String sanitised = str.substring(0, index);
            return sanitised;
        } else if(str.toUpperCase().contains(sqlDeleteFrom)) {
            int index = str.indexOf(semicolon);
            String sanitised = str.substring(0, index);
            return sanitised;
        } else if(str.toUpperCase().contains(sqlSelectFrom)) {
            int index = str.indexOf(semicolon);
            String sanitised = str.substring(0, index);
            return sanitised;
        } else if(str.toUpperCase().contains(sqlSelectFrom2)) {
            int index = str.indexOf(semicolon);
            String sanitised = str.substring(0, index);
            return sanitised;
        } else if(str.toUpperCase().contains(sqlInsert)) {
            int index = str.indexOf(semicolon);
            String sanitised = str.substring(0, index);
            return sanitised;
        } else if(str.toUpperCase().contains(sqlComment)) {
            int index = str.indexOf(semicolon);
            String sanitised = str.substring(0, index);
            return sanitised;
        } else if(str.toUpperCase().contains("'='")) {
            int index = str.indexOf("'='");
            String sanitised = str.substring(0, index);
            return sanitised;
        } else if(str.toUpperCase().contains("\"=\"")) {
            int index = str.indexOf("\"=\"");
            String sanitised = str.substring(0, index);
            return sanitised;
        } else if(str.toUpperCase().contains("%00")) {
            int index = str.indexOf("%00");
            String sanitised = str.substring(0, index);
            return sanitised;
        } else if(str.toUpperCase().contains("%3B")) {
            int index = str.indexOf("%3B");
            String sanitised = str.substring(0, index);
            return sanitised;
        } else {
            return str;
        }
    }
}
