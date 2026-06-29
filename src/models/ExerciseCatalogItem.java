package com.example.gymprogressiontracker;

/**
 * Represents an item in the Exercise Catalog.
 */
public class ExerciseCatalogItem {
    private String name;
    private String description;
    private int imageResourceId; // References a drawable local vector/image file

    public ExerciseCatalogItem(String name, String description, int imageResourceId) {
        this.name = name;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getImageResourceId() { return imageResourceId; }
}
