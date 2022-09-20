package com.kaspersky;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class MainSiteTest {

    @BeforeEach
    void configure() {
        open("https://www.kaspersky.ru/");
    }

    @ValueSource(strings = {"Для дома", "Для бизнеса", "О нас", "Партнерам"})
    @ParameterizedTest(name = "При выборе в меню вкладки \"{0}\" отображается более 1 подвыбора")
    void menuChoiceTest(String testData) {
        $("header").find(byText(testData)).click();
        $$("[class*='MenuDropdown_dropdownVisible'] a").shouldBe(sizeGreaterThanOrEqual(1));
    }

    @CsvSource(value = {
            "Посмотреть продукты|Позаботьтесь о безопасных занятиях, поиске и играх детей онлайн",
            "Продлить лицензию|Продлите лицензию, чтобы оставаться под защитой"
    }, delimiter = '|')
    @ParameterizedTest(name = "При выборе \"{0}\" на новой странице отображается заголовок \"{1}\"")
    void headerBaseProductsTest(String buttonData, String expectedHeaderText) {
        $("[class*='MastheadHome_content']").find(byText(buttonData)).click();
        $("[class*='MastHead_masthead'] h1").shouldHave(text(expectedHeaderText));
    }

    static Stream<Arguments> dataProviderForLanguageSiteMenuTest() {
        return Stream.of(
                Arguments.of("Brasil", "Segurança virtual que está sempre à frente"),
                Arguments.of("United States", "Cybersecurity that’s always a step ahead"),
                Arguments.of("Canada", "Cybersecurity that’s always a step ahead")
        );
    }

    @MethodSource("dataProviderForLanguageSiteMenuTest")
    @ParameterizedTest(name = "Пр выборе языка {0} на новой странице отображается заголовок {1}")
    void selenideSiteMenuTest(String language, String expectedHeader) {
        $("button[class*='PageHeader_globeWrapper']").click();
        $("#globe-9").find(byText(language)).click();
        $("h1[class*='MastHead_title']").shouldHave(text(expectedHeader));
    }

}
