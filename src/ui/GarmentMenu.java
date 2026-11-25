package ui;

import domain.Category;
import domain.ClothingStore;
import domain.Color;
import domain.Garment;
import domain.Item;
import domain.Size;
import java.util.ArrayList;

// Menu for garments and stock: register, list, search, update and remove
// garments and items. It only asks for data, calls the store and shows results.
public class GarmentMenu {

    private final ClothingStore store;
    private final Console console;
    private final Selector selector;

    // Receives the same store, console and selector that Main creates.
    public GarmentMenu(ClothingStore store, Console console, Selector selector){
        this.store = store;
        this.console = console;
        this.selector = selector;
    }

    // Shows a list of garments without asking to choose one.
    // Used by options 2 and 3.
    private void listGarments(ArrayList<Garment> garments){
        if(garments.isEmpty()){
            System.out.println("No hay prendas para mostrar.");
            return;
        }
        for(Garment garment : garments){
            System.out.println("- " + garment.getName() + " (" + garment.getCategory()
                    + ") - $" + garment.getBasePrice() + " - " + garment.getItems().size() + " artículos");
        }
    }

    private void registerGarment(){
        String name = console.readText("Nombre: ");
        Category category = selector.chooseCategory();
        long basePrice = console.readLong("Precio base: ");
        Garment garment = store.registerGarment(name, category, basePrice);
        System.out.println("Prenda registrada: " + garment.getName());
    }

    private void findByCategory(){
        Category category = selector.chooseCategory();
        listGarments(store.findGarmentsByCategory(category));
    }

    // Shows every item of one garment with its units and price
    // (L and XL include the surcharge, through polymorphism).
    private void showItems(){
        Garment garment = selector.chooseGarment(store.getGarments());
        ArrayList<Item> items = garment.getItems();
        if(items.isEmpty()){
            System.out.println(garment.getName() + " no tiene artículos.");
            return;
        }
        for(Item item : items){
            System.out.println("- " + item.getSize() + " " + item.getColor() + ": "
                    + item.getUnits() + " unidades - $" + item.getUnitPrice());
        }
    }

    private void updateBasePrice(){
        Garment garment = selector.chooseGarment(store.getGarments());
        long newPrice = console.readLong("Nuevo precio base: ");
        store.updateBasePrice(garment, newPrice);
        System.out.println("Precio actualizado.");
    }

    // Uses the returned item to add units right away, if the user wants.
    private void addItem(){
        Garment garment = selector.chooseGarment(store.getGarments());
        Size size = selector.chooseSize();
        Color color = selector.chooseColor();
        Item item = store.addItemToGarment(garment, size, color);
        System.out.println("Artículo agregado: " + item.getSize() + " " + item.getColor());
        if(console.readYesNo("¿Agregar unidades ahora?")){
            int quantity = console.readInt("Unidades: ");
            store.addUnits(item, quantity);
            System.out.println("Unidades agregadas.");
        }
    }

    private void addUnits(){
        Garment garment = selector.chooseGarment(store.getGarments());
        Item item = selector.chooseItem(garment);
        int quantity = console.readInt("Unidades a agregar: ");
        store.addUnits(item, quantity);
        System.out.println("Unidades actuales: " + item.getUnits());
    }

    // The store refuses if the item has sales; then the success message is not shown.
    private void removeItem(){
        Garment garment = selector.chooseGarment(store.getGarments());
        Item item = selector.chooseItem(garment);
        store.removeItem(garment, item);
        System.out.println("Artículo eliminado.");
    }

    // The store refuses if any item of the garment has sales.
    private void removeGarment(){
        Garment garment = selector.chooseGarment(store.getGarments());
        store.removeGarment(garment);
        System.out.println("Prenda eliminada.");
    }

    // Main loop of the menu. Domain and Selector exceptions are caught here
    // and shown as messages, so an invalid operation never stops the program.
    public void show(){
        int option;
        do{
            System.out.println("\n--- Prendas e inventario ---");
            System.out.println("1. Registrar prenda");
            System.out.println("2. Listar prendas");
            System.out.println("3. Buscar prendas por categoría");
            System.out.println("4. Ver artículos de una prenda");
            System.out.println("5. Cambiar precio base");
            System.out.println("6. Agregar artículo (talla y color)");
            System.out.println("7. Agregar unidades");
            System.out.println("8. Eliminar artículo");
            System.out.println("9. Eliminar prenda");
            System.out.println("0. Volver");
            option = console.readInt("Opción: ", 0, 9);
            try{
                switch(option){
                    case 1 -> registerGarment();
                    case 2 -> listGarments(store.getGarments());
                    case 3 -> findByCategory();
                    case 4 -> showItems();
                    case 5 -> updateBasePrice();
                    case 6 -> addItem();
                    case 7 -> addUnits();
                    case 8 -> removeItem();
                    case 9 -> removeGarment();
                }
            }catch(IllegalArgumentException | IllegalStateException e){
                System.out.println("Error: " + e.getMessage());
            }
        }while(option != 0);
    }
}
