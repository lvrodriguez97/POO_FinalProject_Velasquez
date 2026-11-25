package ui;

import domain.Category;
import domain.Color;
import domain.Garment;
import domain.Item;
import domain.Size;
import java.util.ArrayList;

// Shows numbered lists and returns the element the user picks.
// The user types the position (starting at 1); lists start at 0, so position - 1.
public class Selector {

    private final Console console;

    // Uses the same Console as the rest of the program.
    public Selector(Console console){
        this.console = console;
    }

    // Receives the list to choose from, so it works for all the garments
    // or for the garments of one category.
    // Throws an exception when the list is empty, so the menu shows the message.
    public Garment chooseGarment(ArrayList<Garment> garments){
        if(garments.isEmpty()){
            throw new IllegalStateException("No hay prendas registradas.");
        }
        for(int i = 0; i < garments.size(); i++){
            Garment garment = garments.get(i);
            System.out.println((i + 1) + ". " + garment.getName()
                    + " (" + garment.getCategory() + ") - $" + garment.getBasePrice());
        }
        int position = console.readInt("Elija una prenda: ", 1, garments.size());
        return garments.get(position - 1);
    }

    // Shows each item with its units and price (the price includes the
    // large size surcharge, through polymorphism).
    public Item chooseItem(Garment garment){
        ArrayList<Item> items = garment.getItems();
        if(items.isEmpty()){
            throw new IllegalStateException("La prenda " + garment.getName() + " no tiene artículos.");
        }
        for(int i = 0; i < items.size(); i++){
            Item item = items.get(i);
            System.out.println((i + 1) + ". " + item.getSize() + " " + item.getColor()
                    + " - " + item.getUnits() + " unidades - $" + item.getUnitPrice());
        }
        int position = console.readInt("Elija un artículo: ", 1, items.size());
        return items.get(position - 1);
    }

    // values() returns every constant of an enum, in the order they are declared.
    public Category chooseCategory(){
        Category[] categories = Category.values();
        for(int i = 0; i < categories.length; i++){
            System.out.println((i + 1) + ". " + categories[i]);
        }
        int position = console.readInt("Elija una categoría: ", 1, categories.length);
        return categories[position - 1];
    }

    public Size chooseSize(){
        Size[] sizes = Size.values();
        for(int i = 0; i < sizes.length; i++){
            System.out.println((i + 1) + ". " + sizes[i]);
        }
        int position = console.readInt("Elija una talla: ", 1, sizes.length);
        return sizes[position - 1];
    }

    public Color chooseColor(){
        Color[] colors = Color.values();
        for(int i = 0; i < colors.length; i++){
            System.out.println((i + 1) + ". " + colors[i]);
        }
        int position = console.readInt("Elija un color: ", 1, colors.length);
        return colors[position - 1];
    }
}
