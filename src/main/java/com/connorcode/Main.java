package com.connorcode;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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
                String.format("Open Cart%s(%d Items)", " ".repeat(maxWidth - 22 - Misc.countDigits(items)),
                        items),
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
                System.out.println("[-] Unknown Option");
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

        input.nextLine();
        input.nextLine();
        homePage();
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
        if (toBuy < 0 || toBuy >= items.size()) homePage();

        Item item = items.get(toBuy);
        if (item.stock == 0) notifPage(String.format("We are all out of %s!", item.name), 0);
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
    }

    static void sneakPage() {
        Misc.clear();
        Misc.printHeader(" WEGGMENS PARKING LOT ", maxWidth);
    }

    static void notifPage(String notif, int backPage) {
        Misc.clear();
        Misc.printHeader(" WEGGMENS NOTIFICATION ", maxWidth);
        System.out.println(notif);
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
  ======== WEGGMENS CART ========
  YOUR ITEMS:
    - Duck Egg .. x23
    - Cool Bean . x263

  1) Exit Cart
  2) Empty Cart
*/

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