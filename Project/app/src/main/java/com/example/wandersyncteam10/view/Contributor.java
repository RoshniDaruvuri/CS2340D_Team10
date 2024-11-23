package com.example.wandersyncteam10.view;

public class Contributor {
    private String name;
    private boolean canEdit;

    /**
     * Constructor for Contributor.
     *
     * @param name    The name of the contributor.
     * @param canEdit Whether the contributor has edit permissions.
     */
    public Contributor(String name, boolean canEdit) {
        this.name = name;
        this.canEdit = canEdit;
    }

    /**
     * Gets the name of the contributor.
     *
     * @return The name of the contributor.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the contributor has edit permissions.
     *
     * @return True if the contributor can edit, false otherwise.
     */
    public boolean canEdit() {
        return canEdit;
    }

    /**
     * Returns a string representation of the contributor.
     *
     * @return A string containing the contributor's name and edit permissions.
     */
    @Override
    public String toString() {
        return "Contributor{name='" + name + "', canEdit=" + canEdit + "}";
    }
}
