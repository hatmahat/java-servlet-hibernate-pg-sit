package com.example.constants;

public class AppConstants {
    // Prevent instantiation
    private AppConstants() {}

    // Example constant values
    public static final String VIP_MEMBERSHIP = "VIP";
    public static final String REGULAR_MEMBERSHIP = "REGULAR";
    public static final String NO_MEMBERSHIP = "NONE";

    public static final String[] MEMBERSHIP_TYPES = new String[]{
        VIP_MEMBERSHIP,
        REGULAR_MEMBERSHIP,
        NO_MEMBERSHIP,
    };

    // Other constants
    public static final double VIP_DISCOUNT_OVER_100 = 0.20;
    public static final double VIP_DISCOUNT_UNDER_100 = 0.10;
    public static final double REGULAR_DISCOUNT_OVER_100 = 0.10;
    public static final double NO_DISCOUNT = 0.00;
}
