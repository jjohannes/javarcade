package de.javarca.model;

public record Asset(char symbol, byte[] image) {
    static Asset EMPTY = new Asset(' ', null);
}
