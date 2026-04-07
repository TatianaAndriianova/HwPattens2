package ru.netology;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.data.DataGenerator.Registration.getUser;
import static ru.netology.data.DataGenerator.getRandomLogin;
import static ru.netology.data.DataGenerator.getRandomPassword;

public class AuthTest {

    @BeforeEach
    public void setup() {
        Configuration.baseUrl = "http://localhost:9999/";
        open("");
    }

    @Test
    public void shouldSuccessfulLoginIfRegisteredActiveUser() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[id=root]").shouldHave(text("Личный кабинет")); // предполагаемый текст успешного входа
    }

    @Test
    public void shouldGetErrorIfNotRegisteredUser() {
        DataGenerator.RegistrationDto notRegisteredUser = getUser("active");

        $("[data-test-id=login] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id=password] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldGetErrorIfBlockedUser() {
        DataGenerator.RegistrationDto blockedUser = getRegisteredUser("blocked");

        $("[data-test-id=login] input").setValue(blockedUser.getLogin());
        $("[data-test-id=password] input").setValue(blockedUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    public void shouldGetErrorIfWrongLogin() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        String wrongLogin = getRandomLogin();

        $("[data-test-id=login] input").setValue(wrongLogin);
        $("[data-test-id=password] input").setValue(registeredUser.getPassword());
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    public void shouldGetErrorIfWrongPassword() {
        DataGenerator.RegistrationDto registeredUser = getRegisteredUser("active");
        String wrongPassword = getRandomPassword();

        $("[data-test-id=login] input").setValue(registeredUser.getLogin());
        $("[data-test-id=password] input").setValue(wrongPassword);
        $("[data-test-id=action-login]").click();

        $("[data-test-id=error-notification]").shouldHave(text("Ошибка! Неверно указан логин или пароль"));
    }
}