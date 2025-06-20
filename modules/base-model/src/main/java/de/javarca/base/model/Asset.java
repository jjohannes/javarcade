package de.javarca.base.model;

public record Asset(char symbol, byte[] image) {
    static Asset EMPTY = new Asset(' ', null);
}
