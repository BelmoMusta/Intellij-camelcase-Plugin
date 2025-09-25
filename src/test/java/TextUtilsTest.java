import musta.belmo.plugins.camelcase.ast.CamelCaseTransformer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextUtilsTest {
    @ParameterizedTest
    @CsvSource({"a b c,aBC", "this is a simple non-camelcase sentence,thisIsASimpleNonCamelcaseSentence"})
    void testCamelcase(String input, String expected) {
        String result = CamelCaseTransformer.camelCase(input);
        assertEquals(expected, result);

    }
}