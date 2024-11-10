package com.example.wandersyncteam10.view;

public class Contributor {
    private String name;
    private boolean canEdit;

    // Constructor
    public Contributor(String name, boolean canEdit) {
        this.name = name;
        this.canEdit = canEdit;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for canEdit
    public boolean canEdit() {
        return canEdit;
    }

    // Optional: Override toString() to display the contributor information
    @Override
    public String toString() {
        return "Contributor{name='" + name + "', canEdit=" + canEdit + "}";
    }
}
