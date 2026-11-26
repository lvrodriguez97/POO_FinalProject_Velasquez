package ui;

import domain.ClothingStore;
import domain.Garment;
import domain.Item;
import domain.Sale;
import domain.SaleLine;
import java.util.ArrayList;

// Menu for sales: register, list, show the detail and annul.
// It only asks for data, calls the store and shows results.
public class SaleMenu {

    private final ClothingStore store;
    private final Console console;
    private final Selector selector;

    // Receives the same store, console and selector that Main creates.
    public SaleMenu(ClothingStore store, Console console, Selector selector){
        this.store = store;
        this.console = console;
        this.selector = selector;
    }

    // Prints every line of a sale and its total, using the price copied
    // in each line (not the item's current price).
    // Used after registering a sale and to show a sale's detail.
    private void printSaleLines(Sale sale){
        for(SaleLine line : sale.getLines()){
            Item item = line.getItem();
            System.out.println("- " + item.getGarment().getName() + " " + item.getSize() + " " + item.getColor()
                    + ": " + line.getQuantity() + " x $" + line.getUnitPrice() + " = $" + line.getSubtotal());
        }
        System.out.println("Total: $" + sale.getTotal());
    }

    // Builds the sale line by line and sends it complete to the store.
    // The store checks repeated items and stock before changing anything:
    // if one line fails, the whole sale is rejected.
    private void registerSale(){
        ArrayList<SaleLine> lines = new ArrayList<>();
        boolean addAnother = true;
        while(addAnother){
            Garment garment = selector.chooseGarment(store.getGarments());
            Item item = selector.chooseItem(garment);
            int quantity = console.readInt("Cantidad: ");
            lines.add(new SaleLine(item, quantity));
            addAnother = console.readYesNo("¿Agregar otro artículo?");
        }
        Sale sale = store.registerSale(lines);
        System.out.println("Venta registrada: #" + sale.getId());
        printSaleLines(sale);
    }

    // One summary row per sale, annulled ones included and marked.
    private void listSales(){
        ArrayList<Sale> sales = store.getSales();
        if(sales.isEmpty()){
            System.out.println("No hay ventas registradas.");
            return;
        }
        for(Sale sale : sales){
            String state = sale.isAnnulled() ? " (ANULADA)" : "";
            System.out.println("#" + sale.getId() + " - " + sale.getLines().size()
                    + " líneas - $" + sale.getTotal() + state);
        }
    }

    // Asks for a sale id and shows its lines; annulled sales also show the reason.
    private void showSaleDetail(){
        int id = console.readInt("Número de venta: ");
        Sale sale = store.findSaleById(id);
        System.out.println("Venta #" + sale.getId());
        printSaleLines(sale);
        if(sale.isAnnulled()){
            System.out.println("ANULADA - Motivo: " + sale.getAnnulmentReason());
        }
    }

    // The store annuls the sale and returns its units to the stock.
    private void annulSale(){
        int id = console.readInt("Número de venta a anular: ");
        String reason = console.readText("Motivo (sin comas): ");
        store.annulSale(id, reason);
        System.out.println("Venta #" + id + " anulada. Las unidades volvieron al inventario.");
    }

    // Main loop of the menu. Domain and Selector exceptions are caught here
    // and shown as messages; then the menu is shown again.
    public void show(){
        int option;
        do{
            System.out.println("\n--- Ventas ---");
            System.out.println("1. Registrar venta");
            System.out.println("2. Listar ventas");
            System.out.println("3. Ver detalle de una venta");
            System.out.println("4. Anular venta");
            System.out.println("0. Volver");
            option = console.readInt("Opción: ", 0, 4);
            try{
                switch(option){
                    case 1 -> registerSale();
                    case 2 -> listSales();
                    case 3 -> showSaleDetail();
                    case 4 -> annulSale();
                }
            }catch(IllegalArgumentException | IllegalStateException e){
                System.out.println("Error: " + e.getMessage());
            }
        }while(option != 0);
    }
}
