package io.github.syst3ms.skriptparser.util.math;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utilities for various math operations using the generic {@link Number} type
 *
 * I do not claim ownership of this code, it is the intellectual property of <a href="github.com/eobermuhlner">@obermuhlner</a>.
 * @author @obermuhlner
 */
public class NumberMath {
    private static final BigDecimal RADIANS_TO_DEGREES = new BigDecimal(180).divide(BigDecimalMath.pi(BigDecimalMath.DEFAULT_CONTEXT), BigDecimalMath.DEFAULT_CONTEXT);
    private static final BigDecimal DEGREES_TO_RADIANS = BigDecimalMath.pi(BigDecimalMath.DEFAULT_CONTEXT).divide(new BigDecimal(180), BigDecimalMath.DEFAULT_CONTEXT);

    public static Number abs(Number n) {
        if (n instanceof Long) {
            return Math.abs(n.longValue());
        } else if (n instanceof Double) {
            return Math.abs(n.doubleValue());
        } else if (n instanceof BigInteger) {
            return ((BigInteger) n).abs();
        } else if (n instanceof BigDecimal) {
            return ((BigDecimal) n).abs();
        }
        throw new IllegalArgumentException();
    }

    public static Number negate(Number n) {
        if (n instanceof Long) {
            return -n.longValue();
        } else if (n instanceof Double) {
            return -n.doubleValue();
        } else if (n instanceof BigInteger) {
            return ((BigInteger) n).negate();
        } else if (n instanceof BigDecimal) {
            return ((BigDecimal) n).negate();
        }
        throw new IllegalArgumentException();
    }

    public static Number sqrt(Number n) {
        if (n instanceof Long || n instanceof Double) {
            return Math.sqrt(n.doubleValue());
        } else {
            return BigDecimalMath.sqrt(bigToBigDecimal(n), BigDecimalMath.DEFAULT_CONTEXT);
        }
    }

    public static Number ln(Number n) {
        if (n instanceof Long || n instanceof Double) {
            return Math.log(n.doubleValue());
        } else {
            return BigDecimalMath.log(bigToBigDecimal(n), BigDecimalMath.DEFAULT_CONTEXT);
        }
    }

    public static Number log(Number base, Number n) {
        if ((n instanceof Long || n instanceof Double) && (base instanceof Long || base instanceof Double)) {
            return Math.log(n.doubleValue()) / Math.log(base.doubleValue());
        } else {
            var bd = bigToBigDecimal(n);
            var bdBase = bigToBigDecimal(base);
            if (bdBase.compareTo(BigDecimal.valueOf(2)) == 0) {
                return BigDecimalMath.log2(bd, BigDecimalMath.DEFAULT_CONTEXT);
            } else if (bdBase.compareTo(BigDecimal.TEN) == 0) {
                return BigDecimalMath.log10(bd, BigDecimalMath.DEFAULT_CONTEXT);
            } else {
                return BigDecimalMath.log(bd, BigDecimalMath.DEFAULT_CONTEXT)
                                     .divide(BigDecimalMath.log(bdBase, BigDecimalMath.DEFAULT_CONTEXT), BigDecimalMath.DEFAULT_ROUNDING_MODE);
            }
        }
    }

    public static Number factorial(Number n) {
        if (n instanceof Long && n.longValue() < 13)
            return BigDecimalMath.factorial(n.intValue()).longValue();
        var fac = BigDecimalMath.factorial(new BigDecimal(n.toString()), BigDecimalMath.DEFAULT_CONTEXT);
        if (n instanceof Long || n instanceof BigInteger) {
            return fac.toBigInteger();
        } else {
            return fac;
        }
    }

    public static Number floor(Number n) {
        if (n instanceof Long || n instanceof BigInteger) {
            return n;
        } else if (n instanceof Double) {
            return Math.floor(n.doubleValue());
        } else {
            assert n instanceof BigDecimal;
            return ((BigDecimal) n).setScale(0, RoundingMode.FLOOR);
        }
    }

    public static Number ceil(Number n) {
        if (n instanceof Long || n instanceof BigInteger) {
            return n;
        } else if (n instanceof Double) {
            return Math.ceil(n.doubleValue());
        } else {
            assert n instanceof BigDecimal;
            return ((BigDecimal) n).setScale(0, RoundingMode.CEILING);
        }
    }

    public static Number round(Number n) {
        if (n instanceof Long || n instanceof BigInteger) {
            return n;
        } else if (n instanceof Double) {
            return Math.round(n.doubleValue());
        } else {
            assert n instanceof BigDecimal;
            return ((BigDecimal) n).setScale(0, RoundingMode.HALF_EVEN);
        }
    }

    public static Number sin(Number n) {
        if (n instanceof Long || n instanceof Double) {
            return Math.sin(Math.toDegrees(n.doubleValue()));
        } else {
            return BigDecimalMath.sin(bigToBigDecimal(n).multiply(DEGREES_TO_RADIANS), BigDecimalMath.DEFAULT_CONTEXT);
        }
    }

    public static Number cos(Number n) {
        if (n instanceof Long || n instanceof Double) {
            return Math.cos(Math.toDegrees(n.doubleValue()));
        } else {
            return BigDecimalMath.cos(bigToBigDecimal(n).multiply(DEGREES_TO_RADIANS), BigDecimalMath.DEFAULT_CONTEXT);
        }
    }

    public static Number tan(Number n) {
        if (n instanceof Long || n instanceof Double) {
            return Math.tan(Math.toDegrees(n.doubleValue()));
        } else {
            return BigDecimalMath.tan(bigToBigDecimal(n).multiply(DEGREES_TO_RADIANS), BigDecimalMath.DEFAULT_CONTEXT);
        }
    }

    public static Number asin(Number n) {
        if (n instanceof Long || n instanceof Double) {
            return Math.asin(n.doubleValue());
        } else {
            return BigDecimalMath.asin(bigToBigDecimal(n), BigDecimalMath.DEFAULT_CONTEXT).multiply(RADIANS_TO_DEGREES);
        }
    }

    public static Number acos(Number n) {
        if (n instanceof Long || n instanceof Double) {
            return Math.acos(n.doubleValue());
        } else {
            return BigDecimalMath.acos(bigToBigDecimal(n), BigDecimalMath.DEFAULT_CONTEXT).multiply(RADIANS_TO_DEGREES);
        }
    }

    public static Number atan(Number n) {
        if (n instanceof Long || n instanceof Double) {
            return Math.atan(n.doubleValue());
        } else {
            return BigDecimalMath.atan(bigToBigDecimal(n), BigDecimalMath.DEFAULT_CONTEXT).multiply(RADIANS_TO_DEGREES);
        }
    }

    public static Number sinh(Number n) {
        if (n instanceof Long || n instanceof Double) {
            return Math.sinh(n.doubleValue());
        } else {
            return BigDecimalMath.exp(bigToBigDecimal(n), BigDecimalMath.DEFAULT_CONTEXT)
                    .subtract(BigDecimalMath.exp(bigToBigDecimal(n).negate(), BigDecimalMath.DEFAULT_CONTEXT))
                    .divide(BigDecimal.valueOf(2), BigDecimalMath.DEFAULT_CONTEXT);
        }
    }

    public static Number cosh(Number n) {
        if (n instanceof Long || n instanceof Double) {
            return Math.cosh(n.doubleValue());
        } else {
            return BigDecimalMath.exp(bigToBigDecimal(n), BigDecimalMath.DEFAULT_CONTEXT)
                    .add(BigDecimalMath.exp(bigToBigDecimal(n).negate(), BigDecimalMath.DEFAULT_CONTEXT))
                    .divide(BigDecimal.valueOf(2), BigDecimalMath.DEFAULT_CONTEXT);
        }
    }

    public static Number tanh(Number n) {
        if (n instanceof Long || n instanceof Double) {
            return Math.tanh(n.doubleValue());
        } else {
            return BigDecimalMath.exp(bigToBigDecimal(n), BigDecimalMath.DEFAULT_CONTEXT)
                    .subtract(BigDecimalMath.exp(bigToBigDecimal(n).negate(), BigDecimalMath.DEFAULT_CONTEXT))
                    .divide(
                            BigDecimalMath.exp(bigToBigDecimal(n), BigDecimalMath.DEFAULT_CONTEXT)
                                    .add(BigDecimalMath.exp(bigToBigDecimal(n).negate(), BigDecimalMath.DEFAULT_CONTEXT)),
                            BigDecimalMath.DEFAULT_CONTEXT
                    );
        }
    }

    /**
     * Computes a random number between the two given bounds
     *
     * The return type is related to the parameter types in the following way :
     * <ul>
     *     <li>Two bounds of the same type will return a result of the same type</li>
     *     <li>If one of the bounds is a {@link BigDecimal}, so will be the result</li>
     *     <li>If one of the bounds is a {@link BigInteger} and the other is a {@link Double}, the result will be a {@link BigDecimal}</li>
     *     <li>If one of the bounds is a {@link BigInteger} and the other is a {@link Long}, the result will be a {@link BigInteger}</li>
     *     <li>Otherwise (i.e one of the bounds is a {@code long} and the other a {@code double}), the result will be a {@code double}</li>
     * </ul>
     * @param lower the lower bound
     * @param upper the upper bound
     * @param inclusive whether the bounds are exclusive or not (skript-parser defaults to true in its implementation)
     * @param random the {@link ThreadLocalRandom} instance used
     * @return a random {@link Number} between the two given bounds
     */
    public static Number random(Number lower, Number upper, boolean inclusive, ThreadLocalRandom random) {
        if (lower.equals(upper))
            return lower;
        if (inclusive) {
            upper = changeBoundExclusion(upper, lower);
        } else {
            lower = changeBoundExclusion(lower, upper);
        }
        if (lower instanceof Long && upper instanceof Long) {
            return random.nextLong((long) lower, (long) upper);
        } else if (lower instanceof BigDecimal || upper instanceof BigDecimal) {
            if (lower instanceof BigDecimal && upper instanceof BigDecimal) {
                return randomBigDecimal((BigDecimal) lower, (BigDecimal) upper, random);
            } else  {
                return randomBigDecimal(BigDecimalMath.getBigDecimal(lower), BigDecimalMath.getBigDecimal(upper), random);
            }
        } else if (lower instanceof BigInteger || upper instanceof BigInteger) {
            if (lower instanceof BigInteger && upper instanceof BigInteger) {
                return randomBigInteger((BigInteger) lower, (BigInteger) upper, random);
            } else if (lower instanceof Double || upper instanceof Double) {
                return randomBigDecimal(BigDecimalMath.getBigDecimal(lower), BigDecimalMath.getBigDecimal(upper), random);
            } else {
                return randomBigInteger(BigDecimalMath.getBigInteger(lower), BigDecimalMath.getBigInteger(upper), random);
            }
        } else {
            return random.nextDouble(lower.doubleValue(), upper.doubleValue());
        }
    }

    /**
     * @param lower the lower bound
     * @param upper the upper bound
     * @return a random {@link BigInteger} between {@code lower} (inclusive) and {@code upper} (exclusive)
     */
    public static BigInteger randomBigInteger(BigInteger lower, BigInteger upper, ThreadLocalRandom random) {
        upper = upper.subtract(BigInteger.ONE); // Make the upper bound exclusive for consistency
        BigInteger span = upper.subtract(lower);
        int len = upper.bitLength();
        BigInteger res = new BigInteger(len, random); // this creates a random BigInteger between 0 and the power of 2 above the upper bound
        if (res.compareTo(lower) < 0 || res.compareTo(upper) > 0) {
            /*
             * If the result is not in the range we fit it into the span using modulo, and then add the lower bound.
             */
            res = res.mod(span).add(lower);
        }
        return res;
    }

    /**
     * @param lower the lower bound
     * @param upper the upper bound
     * @return a random {@link BigDecimal} between {@code lower} (inclusive) and {@code upper} (exclusive)
     */
    public static BigDecimal randomBigDecimal(BigDecimal lower, BigDecimal upper, ThreadLocalRandom random) {
        BigDecimal rand = BigDecimal.valueOf(random.nextDouble());
        BigDecimal scaled = lower.add(rand.multiply(upper.subtract(lower)));
        /*
         * When we scale the random value, we may lose some digits of precision, especially if "upper - lower" is large.
         * To remedy that, we generate another random number and use it to fill in the ending digits by shifting its
         * significant digits until after the point where "scaled" loses some precision
         */
        BigDecimal padding = BigDecimal.valueOf(random.nextDouble())
                                       .scaleByPowerOfTen(-scaled.scale());
        // Finally, we add the padding and make sure the number of decimals is that same as before
        return scaled.add(padding).setScale(rand.scale(), BigDecimalMath.DEFAULT_ROUNDING_MODE); // Finally, we add the padding
    }

    private static Number changeBoundExclusion(Number n, Number other) {
        /*
         * The methods used for each number type have an inclusive lower bound but an exclusive upper bound.
         * This method makes a lower bound exclusive and an upper bound inclusive.
         */
        if (n instanceof Long && (other instanceof Long || other instanceof BigInteger)) {
            return (long) n + 1L;
        } else if (n instanceof BigInteger && (other instanceof Long || other instanceof BigInteger)) {
            return ((BigInteger) n).add(BigInteger.ONE);
        } else if (n instanceof Double || n instanceof Long) {
            double d = n.doubleValue();
            return d + Math.ulp(d);
        } else {
            assert n instanceof BigDecimal || n instanceof BigInteger;
            BigDecimal bd = bigToBigDecimal(n);
            BigDecimal nudge = BigDecimal.ONE.scaleByPowerOfTen(-BigDecimalMath.DEFAULT_CONTEXT.getPrecision());
            return bd.add(nudge);
        }
    }

    private static BigDecimal bigToBigDecimal(Number n) {
        return n instanceof BigDecimal ? (BigDecimal) n : new BigDecimal((BigInteger) n);
    }

}
