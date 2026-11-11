# POO Final Project

> Pendiente: título, integrante (nombre y código), descripción, instrucciones de ejecución, ejemplo de entrada/salida, entidades con CRUD y restricciones.

## Diagrama de clases

> En construcción: por ahora solo la estructura (clases, relaciones y multiplicidades). Los atributos y métodos se agregan en las siguientes fases.

```mermaid
classDiagram
    direction TB

    class ClothingStore {
        -garments : ArrayList~Garment~
        -sales : ArrayList~Sale~
        -nextSaleId : int
        +registerGarment(String name, Category category, long basePrice) Garment
        +getGarments() ArrayList~Garment~
        +findGarmentsByCategory(Category category) ArrayList~Garment~
        +updateBasePrice(Garment garment, long newPrice) void
        +addItemToGarment(Garment garment, Size size, Color color) Item
        +addUnits(Item item, int quantity) void
        +registerSale(ArrayList~SaleLine~ lines) Sale
        +getSales() ArrayList~Sale~
        +findSaleById(int id) Sale
    }
    class Garment {
        -name : String
        -category : Category
        -basePrice : long
        -items : ArrayList~Item~
        +getName() String
        +getCategory() Category
        +getBasePrice() long
        +getItems() ArrayList~Item~
        +setBasePrice(long newPrice) void
        +addItem(Size size, Color color) Item
    }
    class Item {
        -size : Size
        -color : Color
        -units : int
        -garment : Garment
        +getSize() Size
        +getColor() Color
        +getUnits() int
        +getGarment() Garment
        +addUnits(int quantity) void
        +hasEnoughUnits(int quantity) boolean
        +removeUnits(int quantity) void
        +getUnitPrice() long
    }
    class LargeSizeItem {
        -SURCHARGE$ : long = 5000
        +getUnitPrice() long
    }
    class Sale {
        -id : int
        -lines : ArrayList~SaleLine~
        +getId() int
        +getLines() ArrayList~SaleLine~
        +getTotal() long
    }
    class SaleLine {
        -item : Item
        -quantity : int
        -unitPrice : long
        +getItem() Item
        +getQuantity() int
        +getUnitPrice() long
        +getSubtotal() long
    }
    class StockClassifier {
        -store : ClothingStore
    }

    class Report {
        <<abstract>>
        -store : ClothingStore
        #getStore() ClothingStore
        +getTitle()* String
        +getHeaders()* String
        +getRows()* ArrayList~String~
    }
    class SalesSummaryReport {
        +getTitle() String
        +getHeaders() String
        +getRows() ArrayList~String~
    }
    class BestSellersReport {
        +getTitle() String
        +getHeaders() String
        +getRows() ArrayList~String~
    }

    class Category {
        <<enumeration>>
        T_SHIRT
        SHIRT
        POLO
        SWEATSHIRT
        JACKET
        JEANS
        PANTS
        SHORTS
        SKIRT
        DRESS
    }
    class Size {
        <<enumeration>>
        XS
        S
        M
        L
        XL
    }
    class Color {
        <<enumeration>>
        BLACK
        WHITE
        GRAY
        BEIGE
        BROWN
        RED
        BLUE
        NAVY
        GREEN
        YELLOW
        ORANGE
        PINK
        PURPLE
        OLIVE
        BURGUNDY
    }

    %% Composition: the whole creates and owns its parts
    ClothingStore "1" *-- "0..*" Garment : garments
    ClothingStore "1" *-- "0..*" Sale : sales
    Garment "1" *-- "0..*" Item : items
    Sale "1" *-- "1..*" SaleLine : lines

    %% Inheritance
    Item <|-- LargeSizeItem
    Report <|-- SalesSummaryReport
    Report <|-- BestSellersReport

    %% Association (navigable in one direction only)
    SaleLine "0..*" --> "1" Item : item
    Report "0..*" --> "1" ClothingStore : store
    StockClassifier "0..*" --> "1" ClothingStore : store

    %% Fixed lists
    Garment --> "1" Category : category
    Item --> "1" Size : size
    Item --> "1" Color : color
```
