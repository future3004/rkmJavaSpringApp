package com.example.demo;

import javax.persistence.Entity;

@Entity
public class AvailableComponents {
    private String componentName;
    private int componentPrice;
    private String componentDescription;

    public AvailableComponents(String componentName, int componentPrice,
                               String componentDescription) {
        this.componentName = componentName;
        this.componentPrice = componentPrice;
        this.componentDescription = componentDescription;

    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public int getComponentPrice() {
        return componentPrice;
    }

    public void setComponentPrice(int componentPrice) {
        this.componentPrice = componentPrice;
    }

    public String getComponentDescription() {
        return componentDescription;
    }

    public void setComponentDescription(String componentDescription) {
        this.componentDescription = componentDescription;
    }
}
