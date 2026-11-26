package data;

import domain.Report;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// Writes a report to a CSV file: the headers first, then one line per row.
// Reports already build their rows as comma separated text, so this class
// only writes them to a file that other programs (like Excel) can open.
public class CsvExporter {

    // CSV files go in this folder, inside the folder the program runs from.
    private static final String FOLDER = "exports";

    // Writes the headers and every row of any report. It receives the base
    // type Report, so it works with the three reports (polymorphism).
    // The file is created, or replaced if it already exists.
    public static void export(Report report, String fileName){
        new File(FOLDER).mkdirs();                  // creates the folder if it does not exist
        File file = new File(FOLDER, fileName);     // path of the CSV inside the folder
        try(PrintWriter writer = new PrintWriter(new FileWriter(file))){
            writer.println(report.getHeaders());
            for(String row : report.getRows()){
                writer.println(row);
            }
            System.out.println("Reporte exportado: " + file.getPath());
        }catch(IOException e){
            System.out.println("Error al exportar: " + e.getMessage());
        }
    }
}
