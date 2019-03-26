package com.example.demo;


public class SelectedComponents {
    private String compName;
    private String compPrice;
    private int compQty;

    public SelectedComponents(String compName, String compPrice, int compQty) {
        this.compName = compName;
        this.compPrice = compPrice;
        this.compQty = compQty;

    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getCompPrice() {
        return compPrice;
    }

    public void setCompPrice(String compPrice) {
        this.compPrice = compPrice;
    }

    public int getCompQty() {
        return compQty;
    }

    public void setCompQty(int compQty) {
        this.compQty = compQty;
    }
}
