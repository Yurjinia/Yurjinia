package com.yurjinia.platform.common.utils;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ContentGeneratorUtils {

    private static final Faker faker;

    static {
        faker = new Faker();
    }

    public static String generateFirstName() {
        return faker.name().firstName();
    }

    public static String generateLastName() {
        return faker.name().lastName();
    }

    public static String generateEmail() {
        return faker.internet().emailAddress();
    }

    public static String generateUsername() {
        return faker.name().username();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }

    public static String generateTitle() {
        return faker.book().title();
    }

    public static String generateComment() {
        return faker.harryPotter().spell();
    }

}
