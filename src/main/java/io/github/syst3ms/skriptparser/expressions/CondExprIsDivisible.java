package io.github.syst3ms.skriptparser.expressions;

import io.github.syst3ms.skriptparser.Parser;
import io.github.syst3ms.skriptparser.lang.Expression;
import io.github.syst3ms.skriptparser.lang.TriggerContext;
import io.github.syst3ms.skriptparser.lang.properties.ConditionalType;
import io.github.syst3ms.skriptparser.lang.properties.PropertyConditional;
import io.github.syst3ms.skriptparser.parsing.ParseContext;
import io.github.syst3ms.skriptparser.util.math.BigDecimalMath;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Check if a given number is divisible by another number.
 * Note that when the number is a decimal number,
 * the check will automatically fail.
 *
 * @name Is Divisible
 * @type CONDITION
 * @pattern %numbers% (is|are)[ not|n't] divisible by %integer%
 * @since ALPHA
 * @author Syst3ms
 */
public class CondExprIsDivisible extends PropertyConditional<Number> {
    static {
        Parser.getMainRegistration().addSelfRegisteringElement(
                CondExprIsDivisible.class,
                "numbers",
                ConditionalType.BE,
                "divisible by %integer%"
        );
    }

    Expression<BigInteger> divider;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, ParseContext parseContext) {
        divider = (Expression<BigInteger>) expressions[1];
        return super.init(expressions, matchedPattern, parseContext);
    }

    @Override
    public boolean check(TriggerContext ctx, Number[] performers) {
        return isNegated() != Arrays.stream(performers)
                .allMatch(n -> divider.getSingle(ctx)
                        .filter(__ -> BigDecimalMath.isIntValue(BigDecimalMath.getBigDecimal(n)))
                        .filter(d -> BigDecimalMath.getBigInteger(n).mod(d).equals(BigInteger.ZERO))
                        .isPresent()
                );
    }
}
