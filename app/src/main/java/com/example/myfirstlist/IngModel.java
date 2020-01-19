package com.example.myfirstlist;

public class IngModel {
    private String Name;
    private String Unit;
    private String Amount;
    private String Category;


    public IngModel(String name, String amount, String unit) {
        Name = name;
        Unit = unit;
        Amount = amount;
        Category="None";
    }

    public IngModel(String name, String amount, String unit, String category) {
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

    public String getAmount() {
        return Amount;
    }

    public String getCategory() {
        return Category;
    }

    public void setUnit(String unit) {
        this.Unit = unit;
    }

    public void setAmount(String amount) {
        this.Amount = amount;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCategory(String category) {
        this.Category = category;
    }
}
