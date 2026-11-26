package ui;

import data.StoreFile;
import domain.ClothingStore;

// Entry point: loads the store (or starts a new one), creates the menus once
// and shows the main menu. Every menu receives the same store, console and
// selector. The store is saved with option 4 and again when leaving.
public class Main {

    public static void main(String[] args){
        // load returns null when there is no saved file yet.
        ClothingStore store = StoreFile.load();
        if(store == null){
            store = new ClothingStore();
        }
        Console console = new Console();
        Selector selector = new Selector(console);

        GarmentMenu garmentMenu = new GarmentMenu(store, console, selector);
        SaleMenu saleMenu = new SaleMenu(store, console, selector);
        ReportMenu reportMenu = new ReportMenu(store, console);

        int option;
        do{
            System.out.println("\n--- Tienda de ropa ---");
            System.out.println("1. Prendas e inventario");
            System.out.println("2. Ventas");
            System.out.println("3. Reportes y recomendaciones");
            System.out.println("4. Guardar");
            System.out.println("0. Salir");
            option = console.readInt("Opción: ", 0, 4);
            switch(option){
                case 1 -> garmentMenu.show();
                case 2 -> saleMenu.show();
                case 3 -> reportMenu.show();
                case 4 -> StoreFile.save(store);
                case 0 -> {
                    StoreFile.save(store);   // saves automatically when leaving
                    System.out.println("Hasta luego.");
                }
            }
        }while(option != 0);
    }
}
