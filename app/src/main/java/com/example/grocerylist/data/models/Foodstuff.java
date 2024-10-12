package com.example.grocerylist.data.models;

public class Foodstuff {
    public String Name;
    public int Quantity;
    public double PricePerPiece;

    public Foodstuff(String name, int quantity, double pricePerPiece) {
        this.Name = name;
        this.Quantity = quantity;
        this.PricePerPiece = pricePerPiece;
    }
}
