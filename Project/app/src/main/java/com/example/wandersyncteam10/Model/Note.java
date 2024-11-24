package com.example.wandersyncteam10.Model;

/**
 * Represents a single note object.
 */
public class Note {
    private String text;

    /**
     * Default constructor needed for Firestore deserialization.
     */
    public Note() {
    }

    /**
     * Constructs a Note object with the specified text.
     *
     * @param text The text of the note.
     */
    public Note(String text) {
        this.text = text;
    }

    /**
     * Gets the text of the note.
     *
     * @return The text of the note.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the note.
     *
     * @param text The text to set.
     */
    public void setText(String text) {
        this.text = text;
    }
}
