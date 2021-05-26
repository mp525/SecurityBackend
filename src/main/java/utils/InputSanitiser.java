package utils;

/**
 *
 * @author Vibeke
 */
public class InputSanitiser {
    /**
     * Sanitiser til strings. Tjekker for sql kode der potentielt kan skade databasen ved eksekvering.
     * @param str Teksten fra opslaget i String format.
     * @return saniteret string.
     */
    public String sanitiser(String str) {
        /*
        Bad stuff:
        -- -
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
        } else if(str.contains("<scri")) {
            int index = str.indexOf("<scri");
            String sanitised = str.substring(0, index);
            return sanitised;
        } else if(str.contains("<xml")) {
            int index = str.indexOf("<xml");
            String sanitised = str.substring(0, index);
            return sanitised;
        } else if(str.contains("<?=")) {
            int index = str.indexOf("<?=");
            String sanitised = str.substring(0, index);
            return sanitised;
        } else {
            return str;
        }
    }
}
