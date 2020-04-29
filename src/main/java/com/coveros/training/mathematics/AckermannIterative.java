package com.coveros.training.mathematics;

import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * From https://rosettacode.org/wiki/Ackermann_function#Java
 */
public interface AckermannIterative {
    static BigInteger calculate(int m, int n) {
        BigInteger bigM = BigInteger.valueOf(m);
        BigInteger bigN = BigInteger.valueOf(n);
        return $.main(bigM, bigN);
    }
    BigInteger number1();
    BigInteger number2();

    Deque<BigInteger> stack();

    boolean flag();

    enum $ {
        END;

        private static final BigInteger ZERO = BigInteger.ZERO;
        private static final BigInteger ONE = BigInteger.ONE;
        private static final BigInteger TWO = BigInteger.valueOf(2);
        private static final BigInteger THREE = BigInteger.valueOf(3);

        private static AckermannIterative tail(BigInteger number1, BigInteger number2, Deque<BigInteger> stack, boolean flag) {
            return (FunctionalAckermann) field -> {
                switch (field) {
                    case NUMBER_1: return number1;
                    case NUMBER_2: return number2;
                    case STACK: return stack;
                    case FLAG: return flag;
                    default: throw new UnsupportedOperationException(
                            field instanceof Field
                                    ? "Field checker has not been updated properly."
                                    : "Field is not of the correct type."
                    );
                }
            };
        }

        private static final BinaryOperator<BigInteger> ACKERMANN =
                TailRecursive.tailie(
                        (BigInteger number1, BigInteger number2) ->
                                tail(
                                        number1,
                                        number2,
                                        Stream.of(number1).collect(
                                                Collectors.toCollection(ArrayDeque::new)
                                        ),
                                        false
                                )
                        ,
                        ackermann -> {
                            BigInteger number1 = ackermann.number1();
                            BigInteger number2 = ackermann.number2();
                            Deque<BigInteger> stack = ackermann.stack();
                            if (!stack.isEmpty() && !ackermann.flag()) {
                                number1 = stack.pop();
                            }
                            switch (number1.intValue()) {
                                case 0:
                                    return tail(
                                            number1,
                                            number2.add(ONE),
                                            stack,
                                            false
                                    );
                                case 1:
                                    return tail(
                                            number1,
                                            number2.add(TWO),
                                            stack,
                                            false
                                    );
                                case 2:
                                    return tail(
                                            number1,
                                            number2.multiply(TWO).add(THREE),
                                            stack,
                                            false
                                    );
                                default:
                                    if (ZERO.equals(number2)) {
                                        return tail(
                                                number1.subtract(ONE),
                                                ONE,
                                                stack,
                                                true
                                        );
                                    } else {
                                        stack.push(number1.subtract(ONE));
                                        return tail(
                                                number1,
                                                number2.subtract(ONE),
                                                stack,
                                                true
                                        );
                                    }
                            }
                        },
                        ackermann -> ackermann.stack().isEmpty(),
                        AckermannIterative::number2
                )::apply
                ;

        private static BigInteger main(BigInteger m, BigInteger n) {
            return ACKERMANN.apply(m, n);
        }

        private enum Field {
            NUMBER_1,
            NUMBER_2,
            STACK,
            FLAG
        }

        @FunctionalInterface
        private interface FunctionalAckermann extends FunctionalField<Field>, AckermannIterative {
            @Override
            default BigInteger number1() {
                return field(Field.NUMBER_1);
            }

            @Override
            default BigInteger number2() {
                return field(Field.NUMBER_2);
            }

            @Override
            default Deque<BigInteger> stack() {
                return field(Field.STACK);
            }

            @Override
            default boolean flag() {
                return field(Field.FLAG);
            }
        }
    }
}