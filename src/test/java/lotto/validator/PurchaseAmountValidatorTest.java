package lotto.validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PurchaseAmountValidatorTest {
    private static final String NUMERIC_ERROR_MESSAGE = "[ERROR] 로또 구입 금액은 숫자만 입력해야 합니다.";
    private static final String BLANK_ERROR_MESSAGE = "[ERROR] 로또 구입 금액이 없거나 공백만 입력하였습니다.";
    private static final String STARTS_ZERO_ERROR_MESSAGE = "[ERROR] 첫 문자가 0이 아니어야 합니다.";
    private static final String DIVISIBLE_ERROR_MESSAGE = "[ERROR] 로또 구입 금액은 1000원 단위로 입력해야 합니다.";
    private static final String MAX_RANGE_ERROR_MESSAGE =
            "[ERROR] 로또 구입 금액은 복권 및 복권 기금법 제5조 2항, 시행령 제3조에 따라 최대 100000원 까지만 구매 가능합니다.";

    private void assertExceptionTest(PurchaseAmountValidator validator, String input, String message) {
        assertThatThrownBy(() -> validator.validate(input))
                // then
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(message);
    }

    @Nested
    @DisplayName("로또 구입 금액 입력에 대한 검증")
    class create {

        @DisplayName("모든 검증 성공시 입력한 문자열 반환")
        @Test
        void success() {
            // given
            PurchaseAmountValidator validator = new PurchaseAmountValidator();
            String input = "50000";
            // when
            String validated = validator.validate(input);
            // then
            assertThat(validated).isEqualTo(input);
        }

        @DisplayName("숫자 이외의 문자를 포함해서 입력시 예외를 발생시킨다.")
        @Test
        void fail_Numeric() {
            // given
            PurchaseAmountValidator validator = new PurchaseAmountValidator();
            String input = "12a3456b7a89+0";
            // when, then
            assertExceptionTest(validator, input, NUMERIC_ERROR_MESSAGE);
        }

        @DisplayName("공백을 포함해서 입력시 예외를 발생시킨다.")
        @Test
        void fail_NoSpace() {
            // given
            PurchaseAmountValidator validator = new PurchaseAmountValidator();
            String input = "ab c";
            //when, then
            assertExceptionTest(validator, input, NUMERIC_ERROR_MESSAGE);

        }

        @DisplayName("아무 것도 입력하지 않거나 공백만 입력시 예외를 발생시킨다.")
        @ParameterizedTest
        @ValueSource(strings = {"", "  "})
        void fail_NotBlank() {
            // given
            PurchaseAmountValidator validator = new PurchaseAmountValidator();
            String input = "";
            // when, then
            assertExceptionTest(validator, input, BLANK_ERROR_MESSAGE);
        }

        @DisplayName("음수를 입력할 시 예외를 발생시킨다.")
        @Test
        void fail_NotNegative() {
            // given
            PurchaseAmountValidator validator = new PurchaseAmountValidator();
            String input = "-1000";
            // when, then
            assertExceptionTest(validator, input, NUMERIC_ERROR_MESSAGE);
        }

        @DisplayName("소수점를 입력할 시 예외를 발생시킨다.")
        @Test
        void fail_NotDecimal() {
            // given
            PurchaseAmountValidator validator = new PurchaseAmountValidator();
            String input = "100.0";
            // when, then
            assertExceptionTest(validator, input, NUMERIC_ERROR_MESSAGE);
        }

        @DisplayName("계산식을 입력할 시 예외를 발생시킨다.")
        @Test
        void fail_NotFormula() {
            // given
            PurchaseAmountValidator validator = new PurchaseAmountValidator();
            String input = "1000+1000";
            // when, then
            assertExceptionTest(validator, input, NUMERIC_ERROR_MESSAGE);
        }

        @DisplayName("맨 앞자리에 0을 입력할 시 예외를 발생시킨다.")
        @Test
        void fail_NotStartsWithZero() {
            // given
            PurchaseAmountValidator validator = new PurchaseAmountValidator();
            String input = "01000";
            // when, then
            assertExceptionTest(validator, input, STARTS_ZERO_ERROR_MESSAGE);
        }

        @DisplayName("1000원 단위가 아닌 숫자를 입력시 예외를 발생시킨다.")
        @Test
        void fail_Divisible() {
            // given
            PurchaseAmountValidator validator = new PurchaseAmountValidator();
            String input = "10001";
            // when, then
            assertExceptionTest(validator, input, DIVISIBLE_ERROR_MESSAGE);
        }

        @DisplayName("최대 구입 가능 금액을 넘어선 값을 입력할 시 예외를 발생시킨다.")
        @Test
        void fail_MaxRange() {
            // given
            PurchaseAmountValidator validator = new PurchaseAmountValidator();
            String input = "101000";
            // when, then
            assertExceptionTest(validator, input, MAX_RANGE_ERROR_MESSAGE);
        }
    }
}