package com.example.myfirstlist;

public class IngModel {
    private String Name;
    private String Unit;
    private float Amount;
    private String Category;

    public IngModel(String name, String unit, float amount) {
        Name = name;
        Unit = unit;
        Amount = amount;
        Category="None";
    }

    public IngModel(String name, String unit, float amount, String category) {
        Name = name;
        Unit = unit;
        Amount = amount;
        Category = category;
    }

    public String getName() {
        return Name;
    }

    public String getUnit() {
        return Unit;
    }

    public float getAmount() {
        return Amount;
    }

    public String getCategory() {
        return Category;
    }

    public void setUnit(String unit) {
        this.Unit = unit;
    }

    public void setAmount(float amount) {
        this.Amount = amount;
    }

    public void setCategory(String category) {
        this.Category = category;
    }
}
