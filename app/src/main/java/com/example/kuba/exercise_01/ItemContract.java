package com.example.kuba.exercise_01;

import android.provider.BaseColumns;

public final class ItemContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ItemContract() {
    }

    /* Inner class that defines the table contents */
    public static class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String NAME = "name";
        public static final String PRICE = "price";
        public static final String QUANTITY = "quantity";
        public static final String IS_BOUGHT = "isBought";
    }
}