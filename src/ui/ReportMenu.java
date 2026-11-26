package ui;

import data.CsvExporter;
import domain.AnnulledSalesReport;
import domain.BestSellersReport;
import domain.ClothingStore;
import domain.Item;
import domain.Report;
import domain.SalesSummaryReport;
import domain.StockClassifier;
import domain.StockLevel;
import java.util.ArrayList;

// Menu for reports and stock recommendations. It shows the three reports,
// the decision support results and exports the reports to CSV.
public class ReportMenu {

    private final ClothingStore store;
    private final Console console;

    // It does not need the Selector: it never picks a garment or an item.
    public ReportMenu(ClothingStore store, Console console){
        this.store = store;
        this.console = console;
    }

    // Prints any report: title, headers and rows. It receives the base type
    // Report, so the same method works for the three reports (polymorphism).
    private void printReport(Report report){
        System.out.println("\n" + report.getTitle());
        System.out.println(report.getHeaders());
        for(String row : report.getRows()){
            System.out.println(row);
        }
    }

    // Prints a group of items under a title, or a message when it is empty.
    // Shows units and units sold so the employee sees why it is recommended.
    private void printItems(String title, ArrayList<Item> items){
        System.out.println("\n" + title + ":");
        if(items.isEmpty()){
            System.out.println("  Ninguno.");
            return;
        }
        for(Item item : items){
            System.out.println("  - " + item.getGarment().getName() + " " + item.getSize() + " " + item.getColor()
                    + " (" + item.getUnits() + " unidades, " + store.getUnitsSold(item) + " vendidas)");
        }
    }

    // Decision support: uses StockClassifier to list what is out of stock
    // and what should be restocked. Items with ENOUGH need no action.
    private void showRecommendations(){
        StockClassifier classifier = new StockClassifier(store);
        printItems("Agotados", classifier.getItemsByLevel(StockLevel.OUT_OF_STOCK));
        printItems("Reabastecer", classifier.getItemsByLevel(StockLevel.RESTOCK));
    }

    // Exports the three reports to CSV files in the exports folder.
    private void exportReports(){
        CsvExporter.export(new SalesSummaryReport(store), "resumen_ventas.csv");
        CsvExporter.export(new BestSellersReport(store), "mas_vendidos.csv");
        CsvExporter.export(new AnnulledSalesReport(store), "ventas_anuladas.csv");
    }

    // Main loop of the menu. No try/catch: these options only read data,
    // and file errors are already handled by CsvExporter.
    public void show(){
        int option;
        do{
            System.out.println("\n--- Reportes y recomendaciones ---");
            System.out.println("1. Resumen de ventas");
            System.out.println("2. Artículos más vendidos");
            System.out.println("3. Ventas anuladas");
            System.out.println("4. Recomendaciones de inventario");
            System.out.println("5. Exportar reportes a CSV");
            System.out.println("0. Volver");
            option = console.readInt("Opción: ", 0, 5);
            switch(option){
                case 1 -> printReport(new SalesSummaryReport(store));
                case 2 -> printReport(new BestSellersReport(store));
                case 3 -> printReport(new AnnulledSalesReport(store));
                case 4 -> showRecommendations();
                case 5 -> exportReports();
            }
        }while(option != 0);
    }
}
