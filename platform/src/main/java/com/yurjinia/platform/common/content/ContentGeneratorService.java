package com.yurjinia.platform.common.content;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

@Service
public class ContentGeneratorService {

    private final Faker faker = new Faker();

    public String generateFirstName() {
        return faker.name().firstName();
    }

    public String generateLastName() {
        return faker.name().lastName();
    }

    public String generateEmail() {
        return faker.internet().emailAddress();
    }

    public String generateUsername() {
        return faker.name().username();
    }

    public String generatePassword() {
        return faker.internet().password();
    }

    public String generateTitle() {
        return faker.book().title();
    }

    public String generateComment() {
        return faker.harryPotter().spell();
    }

}
