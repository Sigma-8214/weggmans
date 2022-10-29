package com.connorcode;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    // Consts //
    static final int maxWidth = 30;

    // Global Vars //
    static List<Item> items = new ArrayList<>();
    static List<Item.CartItem> cart = new ArrayList<>();
    static boolean weggmensMembership = false;
    static int eggsInNeed = 0;
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        String rawStock = Misc.loadResourceString("stock.json");
        for (JsonElement i : JsonParser.parseString(rawStock)
                .getAsJsonArray())
            items.add(new Item(i));
        homePage();
    }

    static void homePage() {
        int items = 0;
        for (Item.CartItem i : cart) items += i.count;
        String[] OPTIONS = new String[]{
                String.format("Open Cart%s(%d Item%s)",
                        " ".repeat(maxWidth - 22 - Misc.countDigits(items) - (items == 1 ? 0 : 1)),
                        items, items == 1 ? "" : "s"),
                "Go Shopping",
                "Checkout",
                "*sneak out*"
        };

        Misc.clear();
        Misc.printHeader(" WEGGMENS ", maxWidth);
        for (int i = 0; i < OPTIONS.length; i++)
            System.out.printf("  %d) %s\n", i + 1, OPTIONS[i]);

        switch (input.nextInt()) {
            case 1:
                cartPage();
                break;
            case 2:
                shoppingPage();
                break;
            case 3:
                checkoutPage();
                break;
            case 4:
                sneakPage();
                break;
            default:
                notifyPage("Unknown Option", 0);
        }
    }

    static void cartPage() {
        Misc.clear();
        Misc.printHeader(" WEGGMENS CART ", maxWidth);
        System.out.println("YOUR ITEMS:");

        int maxNameLen = 0;
        for (Item.CartItem i : cart)
            maxNameLen = Math.max(maxNameLen, items.get(i.stockIndex).name.length());

        for (Item.CartItem cartItem : cart) {
            Item item = items.get(cartItem.stockIndex);
            System.out.printf("  - %s .%s x%d\n", item.name, ".".repeat(maxNameLen - item.name.length()),
                    cartItem.count);
        }

        System.out.println();
        System.out.println("  1) Exit Cart");
        System.out.println("  2) Clear Cart");
        switch (input.nextInt()) {
            case 1:
                homePage();
                break;
            case 2:
                cart.clear();
                notifyPage("Cart Cleared", 0);
                break;
            default:
                notifyPage("Unknown Option", 0);
        }
    }

    static void shoppingPage() {
        Misc.clear();
        Misc.printHeader(" WEGGMENS SHELVES ", maxWidth);
        int maxNameLen = 0;
        int maxPriceLen = 0;
        for (Item i : items) {
            maxNameLen = Math.max(maxNameLen, i.name.length());
            maxPriceLen = Math.max(maxPriceLen, Misc.countDigits((int) i.price));
        }

        System.out.println("  0) Back");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            System.out.printf("  %d) %s%s | $%.2f%s | x%d\n", i + 1, item.name,
                    " ".repeat(maxNameLen - item.name.length()), item.price,
                    " ".repeat(maxPriceLen - Misc.countDigits((int) item.price)), item.stock);
        }

        int toBuy = input.nextInt() - 1;
        if (toBuy == -1) homePage();
        if (toBuy < 0 || toBuy >= items.size())
            notifyPage(String.format("Value %d is out of range (0 < x < %d)", toBuy + 1, items.size()), 0);

        Item item = items.get(toBuy);
        if (item.stock == 0) notifyPage(String.format("We are all out of %s!", item.name), 0);
        item.stock--;

        Optional<Item.CartItem> prevItem = cart.stream()
                .filter(i -> i.stockIndex == toBuy)
                .findFirst();
        if (prevItem.isPresent()) {
            prevItem.get().count++;
            homePage();
        }

        cart.add(new Item.CartItem(toBuy, 1));
        homePage();
    }

    static void checkoutPage() {
        Misc.clear();
        Misc.printHeader(" WEGGMENS CHECKOUT ", maxWidth);

        double totalPrice = 0d;
        for (Item.CartItem i : cart)
            totalPrice += items.get(i.stockIndex).price * i.count;
        if (weggmensMembership) totalPrice += 15;
        totalPrice += 10 * eggsInNeed;

        System.out.println();
        System.out.println("                          |¯¯¯¯¯|");
        System.out.println("     |¯|  ()   |¯¯|  \\_/  | EGG |");
        System.out.println("   (¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|_____|¯¯¯¯\\___");
        System.out.println();
        System.out.println("    *BEEP*");

        System.out.println();
        System.out.printf("    1) Pay $%.2f\n", totalPrice);
        System.out.println("    2) Buy the weggmens membership card");
        System.out.println("    3) Donate to eggs in need");

        switch (input.nextInt()) {
            case 1:
                System.out.println("\n*swipe*");
                break;
            case 2:
                if (weggmensMembership) notifyPage("You are already getting the membership card!", 2);
                weggmensMembership = true;
                notifyPage("That will be an additional 15$ on your purchase", 2);
                break;
            case 3:
                eggsInNeed++;
                notifyPage("That will be an additional 10$ on your purchase", 2);
                break;
            default:
                notifyPage("Unknown Option", 2);
        }
    }

    static void sneakPage() {
        Misc.clear();
        Misc.printHeader(" WEGGMENS PARKING LOT ", maxWidth);

        double totalPrice = 0d;
        for (Item.CartItem i : cart)
            totalPrice += items.get(i.stockIndex).price * i.count;

        System.out.println();
        System.out.println("    |¯¯¯¯¯¯¯¯¯¯¯¯|");
        System.out.println("    |  WEGGMENS  |__________");
        System.out.println("    |                      |");
        System.out.println("    |    |¯¯|              |");
        System.out.println("    ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
        System.out.println("          O");
        System.out.println("  \\____| /|/");
        System.out.println("   o  o  / \\");
        System.out.println();
        System.out.printf("you quietly sneak out of the store with your %.2f$ of stolen goods\n", totalPrice);
    }

    static void notifyPage(String notify, int backPage) {
        Misc.clear();
        Misc.printHeader(" WEGGMENS NOTIFICATION ", maxWidth);
        System.out.println(notify);
        input.nextLine();
        input.nextLine();

        switch (backPage) {
            case 0:
                homePage();
                break;
            case 1:
                shoppingPage();
                break;
            case 2:
                checkoutPage();
                break;
            case 3:
                sneakPage();
                break;
            default:
                throw new RuntimeException("Unknown Page Index");
        }
    }
}

// go go mango
/*
  ======== WEGGMENS CHECKOUT ========

                          |¯¯¯¯¯|
     |¯|  ()   |¯¯|  \_/  | EGG |
   (¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|_____|¯¯¯¯\___

    *BEEP*

    1) Pay $XX.XX
    2) Buy the weggmens membership card
    3) Donate to eggs in need
*/

/*
==== WEGGMENS PARKING LOT ====

    |¯¯¯¯¯¯¯¯¯¯¯¯|
    |  WEGGMENS  |__________
    |                      |
    |    |¯¯|              |
    ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
          O
  \____| /|/
   o  o  / \

you quietly sneak out of the store with your xx$ of stolen goods
*/