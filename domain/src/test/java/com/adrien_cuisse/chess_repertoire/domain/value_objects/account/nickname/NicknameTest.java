
package com.adrien_cuisse.chess_repertoire.domain.value_objects.account.nickname;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.account.nickname.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class NicknameTest
{
    public  static Object[][] trimming()
    {
        return new Object[][] {
            { " foo", "foo" },
            { "bar ", "bar" },
            { "b   az", "baz"},
            { "foo . bar - a _ baz", "foo.bar-a_baz" }
        };
    }

    @Test
    public void isNotNull()
    {
        // given no nickname
        final String invalidNickname = null;

        // when trying to make an instance of it
        Executable instanciation = () -> new Nickname(invalidNickname);

        // then there should be an error
        assertThrows(
            NullNicknameException.class,
            instanciation,
            "Nickname shouldn't be null"
        );
    }

    @ParameterizedTest
    @MethodSource("trimming")
    public void hasNoWhitespaces(final String pollutedNickname, final String trimmedNickname)
    {
        // given a nickname
        final Nickname instance = new Nickname(pollutedNickname);

        // when checking its actual format
        final String actualNickname = instance.toString();

        // then it should be the expected trimmed one
        assertEquals(
            trimmedNickname,
            actualNickname,
            "Nickname shouldn't contain whitespaces"
        );
    }

    @Test
    public void isAtLeast2CharactersLong()
    {
        // given nickname with invalid length (after trimming)
        final String invalidNickname = "i  ";

        // when trying to create an instance from it
        Executable instantiation = () -> new Nickname(invalidNickname);

        // then an exception should be thrown
        assertThrows(
            NicknameTooShortException.class,
            instantiation,
            "Nickname shouldn't be instantiated when its length is lesser than 2"
        );
    }

    @Test
    public void isAtMost16CharactersLong()
    {
        // given nickname with invalid length
        final String invalidNickname = "01234567890123456";

        // when trying to create an instance from it
        Executable instantiation = () -> new Nickname(invalidNickname);

        // then an exception should be thrown
        assertThrows(
            NicknameTooLongException.class,
            instantiation,
            "Nickname shouldn't be instantiated when its length is greater than 16"
        );
    }

    public static Object[][] invalidCharacters()
    {
        return new Object[][] {
            { '!' }, { '"' }, { '#' }, { '$' }, { '%' },
            { '&' }, { '\'' }, { '(' }, { ')' }, { '*' },
            { '+' }, { ',' }, { '/' }, { ':' }, { ';' },
            { '<' }, { '=' }, { '>' }, { '?' }, { '@' },
            { '[' }, { '\\' }, { ']' }, { '^' }, { '`' },
            { '{' }, { '|' }, { '}' },{ '~' }, { '°' },
        };
    }

    @ParameterizedTest
    @MethodSource("invalidCharacters")
    public void expectsValidSymbols(final Character invalidSymbol)
    {
        // given a nickname containing invalid symbols
        final String invalidNickname = "012345678901234" + invalidSymbol.toString();

        // when trying to create an instance from it
        Executable instantiation = () -> new Nickname(invalidNickname);

        // then an exception should be thrown
        assertThrows(
            InvalidNicknameException.class,
            instantiation,
            "Nickname shouldn't contain symbol"
        );
    }

    public static Object[][] validCharacters()
    {
        return new Object[][] {
            { '-' }, { '.' }, { '_' },
        };
    }

    @ParameterizedTest
    @MethodSource("validCharacters")
    public void mustStartWithAlphaNum(final Character validSymbol)
    {
        // given a nickname starting with a valid symbol
        final String invalidNickname = validSymbol.toString() + "012345678901234";

        // when trying to create an instance from it
        Executable instantiation = () -> new Nickname(invalidNickname);

        // then an exception should be thrown
        assertThrows(
            InvalidNicknameException.class,
            instantiation,
            "Nickname should start with an alpha-numeric character"
        );
    }

    public static Object[][] comparison()
    {
        final Nickname nickname = new Nickname("nickname");

        final IValueObject otherValueObject = new IValueObject() {
            public boolean equals(IValueObject other) { return false; }
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
