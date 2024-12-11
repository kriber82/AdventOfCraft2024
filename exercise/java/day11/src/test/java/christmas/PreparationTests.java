package christmas;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import net.datafaker.Faker;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PreparationTests {

    private static final Faker faker = new Faker();

    @ParameterizedTest
    @CsvSource({
            "-1, No gifts to prepare.",
            "0, No gifts to prepare.",
            "1, Elves will prepare the gifts.",
            "49, Elves will prepare the gifts.",
            "50, Santa will prepare the gifts."
    })
    void prepareGifts(int numberOfGifts, String expected) {
        String result = Preparation.prepareGifts(numberOfGifts);
        assertThat(result).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Baby",
            "2, Baby",
            "3, Toddler",
            "5, Toddler",
            "6, Child",
            "12, Child",
            "13, Teen"
    })
    void categorizeGift(int age, String expectedCategory) {
        String result = Preparation.categorizeGift(age);
        assertThat(result).isEqualTo(expectedCategory);
    }

    @ParameterizedTest
    @CsvSource({
            "EDUCATIONAL, 25, 100, true",
            "FUN, 30, 100, true",
            "CREATIVE, 20, 100, true",
            "EDUCATIONAL, 249, 1000, false",
            "EDUCATIONAL, 20, 100, false",
            "FUN, 299, 1000, false",
            "FUN, 29, 100, false",
            "CREATIVE, 199, 1000, false",
            "CREATIVE, 15, 100, false"
    })
    void ensureToyBalance(ToyType toyType, int toysCount, int totalToys, boolean expected) {
        boolean result = Preparation.ensureToyBalance(toyType, toysCount, totalToys);
        assertThat(result).isEqualTo(expected);
    }

    // "Fuzz" tests using datafaker -> not that clever

    @ParameterizedTest
    @MethodSource
    void prepareGiftsShouldNotCrashWithRandomInput(int numberOfGifts) {
        Preparation.prepareGifts(numberOfGifts);
    }

    static Stream<Arguments> prepareGiftsShouldNotCrashWithRandomInput() {
        return Stream.generate(() -> Arguments.of(faker.random().nextInt()))
                .limit(10000);
    }

    @ParameterizedTest
    @MethodSource
    void categorizeGiftShouldNotCrashWithRandomInput(int age) {
        Preparation.categorizeGift(age);
    }

    static Stream<Arguments> categorizeGiftShouldNotCrashWithRandomInput() {
        return Stream.generate(() -> Arguments.of(faker.random().nextInt()))
                .limit(10000);
    }

    @ParameterizedTest
    @MethodSource
    void ensureToyBalanceShouldNotCrashWithRandomInput(ToyType toyType, int toysCount, int totalToys) {
        Preparation.ensureToyBalance(toyType, toysCount, totalToys);
    }

    static Stream<Arguments> ensureToyBalanceShouldNotCrashWithRandomInput() {
        return Stream.generate(() ->
                        Arguments.of(faker.options().option(ToyType.class), faker.random().nextInt(), faker.random().nextInt()))
                .limit(10000);
    }

    // hopefully better fuzz test using jazzer
    @FuzzTest
    void fuzzTest(FuzzedDataProvider data) {
        Preparation.prepareGifts(data.consumeInt());
        Preparation.categorizeGift(data.consumeInt());
        Preparation.ensureToyBalance(data.pickValue(ToyType.values()), data.consumeInt(), data.consumeInt());
    }
}