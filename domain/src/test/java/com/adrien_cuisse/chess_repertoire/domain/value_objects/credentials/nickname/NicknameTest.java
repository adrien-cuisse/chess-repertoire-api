
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.nickname;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NicknameTest
{
    @Test
    public void isNotNull()
    {
        // given no nickname
        final String invalidNickname = null;

        // when trying to make an instance of it
        final Executable instantiation = () -> new Nickname(invalidNickname);

        // then there should be an error
        assertThrows(
            NullNicknameException.class,
            instantiation,
            "Nickname shouldn't be null"
        );
    }

    @Test
    public void isNotEmpty()
    {
        // given an empty nickname
        final String emptyNickname = "";

        // when trying to make an instance of it
        final Executable instantiation = () -> new Nickname(emptyNickname);

        // then there should be an error
        assertThrows(
            EmptyNicknameException.class,
            instantiation,
            "Nickname shouldn't be empty"
        );
    }

    public static Object[][] trimming()
    {
        return new Object[][] {
            { "foo ", "foo" },
            { " bar", "bar" },
            { "foo    bar", "foo bar" },
        };
    }

    @ParameterizedTest
    @MethodSource("trimming")
    public void isTrimmed(final String pollutedNickname, final String trimmedNickname)
    {
        // given a nickname with useless whitespaces

        // when making an instance of it
        final var nickname = new Nickname(pollutedNickname);

        // then it should have been trimmed
        assertEquals(
            trimmedNickname,
            nickname.toString(),
            "Nickname shouldn't contain useless whitespaces"
        );
    }

    public static Object[][] comparison()
    {
        final var nickname = new Nickname("nickname");

        final var otherValueObject = new IValueObject() {
            @Override public boolean equals(IValueObject other) { return false; }
        };

        return new Object[][] {
            { nickname, nickname, true, "Nickname should equal itself" },
            { nickname, new Nickname("nickname"), true, "Nickname should equal if it represents the same value" },
            { nickname, new Nickname("different"), false, "Nickname shouldn't equal if the value is different" },
            { nickname, otherValueObject, false, "Nickname shouldn't equal when not given a nickname" }
        };
    }

    @ParameterizedTest
    @MethodSource("comparison")
    public void matchesSameNickname(
        final Nickname nickname,
        final IValueObject other,
        final boolean expectedEquality,
        final String errorMessage
    ) {
        // given a nickname, and another value object to compare with

        // when comparing them
        final boolean areTheSame = nickname.equals(other);

        // then it should be the expected equality
        assertEquals(expectedEquality, areTheSame, errorMessage);
    }
}
