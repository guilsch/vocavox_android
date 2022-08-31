package com.guilsch.multivoc;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * @param args The arguments of the program.
     */
    public static void main(String[] args) {  
        Deck deck = new Deck();

        deck.init();
        deck.filterActive();
        deck.flip();

    }
}