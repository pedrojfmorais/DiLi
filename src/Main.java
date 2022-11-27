import model.data.DiLi;
import model.jdcb.ConnDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws SQLException {
        //System.out.println(new DiLi().authenticate("a1234567@isec.pt", "!Qq123456789"));

        /*
        public void insertBook(String title, String author, String synopsis, String language,
                           List<String> genres, boolean availability, double costPerDownload,
                           Map<String, String> downloadLink)
         */
        Map<String, String> downloadLink = new HashMap<String, String>();
        downloadLink.put("pdf", "linkPdf");
        downloadLink.put("epub", "linkEpub");
        downloadLink.put("mobi", "linkOutro");
        new ConnDB().insertBook("Titulo", "autor", "sinopse", "Korean",
                null, true, 10, downloadLink);
    }
}