package com.shopro.shop1905.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public enum ErrorCode {
    PRODUCT_NOT_FOUND_IN_CART(1023, "Product does not exist, please update cart", HttpStatus.NOT_FOUND),
    CART_PRODUCT_SIZE_NOT_EXISTED(1024, "Cart product size not existed", HttpStatus.NOT_FOUND),
    TOKEN_EXPIRED(1009, "Token expired", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    PRODUCT_SIZE_NOT_EXISTED(1025, "Product size not existed", HttpStatus.NOT_FOUND),
    VOUCHER_NOT_FOUND(1026, "Voucher not found", HttpStatus.NOT_FOUND),
    PLEASE_UPDATE_CART(1027, "Please update cart", HttpStatus.BAD_REQUEST),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1009, "Error Token", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESHTOKEN(1009, "Error Token, Please relogin", HttpStatus.BAD_REQUEST),
    ERROR_SUBCATEGORY(1011, "add Subcategory is fail", HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_EXISTED(1013, "Product not existed", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_EXISTED(1012, "Category not existed", HttpStatus.NOT_FOUND),
    SIZE_NOT_EXISTED(1014, "Size not existed", HttpStatus.NOT_FOUND),
    CART_NOT_EXISTED(1015, "Cart not existed", HttpStatus.NOT_FOUND),
    QUANTITY_INVALID(1016, "Quantity invalid", HttpStatus.BAD_REQUEST),
    QUANTITY_NOT_ENOUGH(1017, "Quantity not enough", HttpStatus.BAD_REQUEST),
    CART_NOT_BELONG_TO_USER(1018, "Cart not belong to user", HttpStatus.BAD_REQUEST),
    SIZE_NOT_BELONG_TO_CATEGORY(1019, "Size not belong to category", HttpStatus.BAD_REQUEST),
    ERROR_SYSTEM(1020, "System error", HttpStatus.INTERNAL_SERVER_ERROR),
    SUBCATEGORY_NOT_EXISTED(1021, "Subcategory not existed", HttpStatus.NOT_FOUND),
    INVALIDTE_FIELD(1022, "Field wrong syntax", HttpStatus.BAD_REQUEST),
    VOUCHER_DONT_BELONG_TO_USER(1026, "Voucher don't belong to user", HttpStatus.BAD_REQUEST),
    VOUCHER_IS_USED_ALREADY(1026, "Voucher is used already", HttpStatus.BAD_REQUEST),
    PLEASE_RELOAD_PAGE(1030, "please reload page", HttpStatus.BAD_REQUEST),
    BAD_REQUEST(1031, "BAD_REQUEST", HttpStatus.BAD_REQUEST),
    VOUCHER_NOT_ACTIVE(1026, "Voucher is not active", HttpStatus.BAD_REQUEST),
    RECHECKOUT_FAILED(1033, "Recheckout failed", HttpStatus.BAD_REQUEST),
    ERROR_PAYMENT(1034, "Error payment", HttpStatus.BAD_REQUEST),
    DONT_HAVE_PERMISSION(1035, "Don't have permission", HttpStatus.BAD_REQUEST),
    COLOR_NOT_EXISTED(1036, "Color not existed", HttpStatus.NOT_FOUND),
    PRODUCT_SIZE_COLOR_NOT_EXISTED(1037, "Product size color not existed", HttpStatus.NOT_FOUND),
    INVALID_QUANTITY(1038, "Invalid quantity", HttpStatus.BAD_REQUEST),
    PRODUCT_SIZE_COLOR_EXISTED(1039, "Product size color existed", HttpStatus.BAD_REQUEST),
    CHAT_BOX_NOT_FOUND(1040, "Chat box not found", HttpStatus.NOT_FOUND),
    BRAND_NOT_EXISTED(1041, "Brand not existed", HttpStatus.NOT_FOUND),
    INVALID_REQUEST(1042, "Invalid request", HttpStatus.BAD_REQUEST),
    ;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}