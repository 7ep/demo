package com.coveros.training.mathematics;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public interface TailRecursive {

    static <M, N, I, O> BiFunction<M, N, O> tailie(BiFunction<M, N, I> toIntermediary, UnaryOperator<I> unaryOperator, Predicate<I> predicate, Function<I, O> toOutput) {
        return (input1, input2) ->
                $.epsilon(
                        Stream.iterate(
                                toIntermediary.apply(input1, input2),
                                unaryOperator
                        ),
                        predicate,
                        toOutput
                )
                ;
    }

    enum $ {
        END;

        private static <I, O> O epsilon(Stream<I> stream, Predicate<I> predicate, Function<I, O> function) {
            return stream
                    .filter(predicate)
                    .map(function)
                    .findAny()
                    .orElseThrow(RuntimeException::new)
                    ;
        }
    }
}
