
package com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.IIdentity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class UuidTest
{
    @Test
    public void isNotNull1()
    {
        // given no bytes
        final byte[] bytes = null;

        // when trying to make an instance of it
        final Executable instantiation = () -> new Uuid(bytes);

        // then there should be an error
        assertThrows(
            NullUuidException.class,
            instantiation,
            "Uuid shouldn't be null"
        );
    }

    @Test
    public void isNotNull2()
    {
        // given no bytes
        final byte[] bytes = null;

        // when trying to make an instance of it
        final Executable instantiation = () -> new Uuid(bytes, 1);

        // then there should be an error
        assertThrows(
            NullUuidException.class,
            instantiation,
            "Uuid shouldn't be null"
        );
    }

    @Test
    public void isNotNull3()
    {
        // given no bytes
        final byte[] bytes = null;

        // when trying to make an instance of it
        final Executable instantiation = () -> new Uuid(bytes, 1, IUuid.Variant.RFC_VARIANT);

        // then there should be an error
        assertThrows(
            NullUuidException.class,
            instantiation,
            "Uuid shouldn't be null"
        );
    }

    @Test
    public void isNotNull4()
    {
        // given no uuid
        final String uuid = null;

        // when trying to make an instance of it
        final Executable instantiation = () -> new Uuid(uuid, 1);

        // then there should be an error
        assertThrows(
            NullUuidException.class,
            instantiation,
            "Uuid shouldn't be null"
        );
    }

    public static byte[] nullBytes()
    {
        final var bytes = new byte[16];
        Arrays.fill(bytes, (byte) 0);
        return bytes;
    }

    public static byte[] nullBytesAndVersion(final int version)
    {
        final byte[] bytes = nullBytes();
        bytes[6] = (byte) (((version & 0b0000_1111) << 4) | (bytes[6] & 0b0000_1111));
        return bytes;
    }

    @Test
    public void requires16Bytes1()
    {
        // given an invalid bytes count
        final var bytes = new byte[] { 0x42 };

        // when trying to create a UUID from it
        final Executable instantiation = () -> new Uuid(bytes);

        // then an exception should be thrown
        assertThrows(
            InvalidUuidBytesCountException.class,
            instantiation,
            "Uuid shouldn't be created when its not 16 bytes long"
        );
    }

    @Test
    public void requires16Bytes2()
    {
        // given an invalid bytes count
        final var bytes = new byte[] { 0x42 };

        // when trying to create a UUID from it
        final Executable instantiation = () -> new Uuid(bytes, 1);

        // then an exception should be thrown
        assertThrows(
            InvalidUuidBytesCountException.class,
            instantiation,
            "Uuid shouldn't be created when its not 16 bytes long"
        );
    }

    @Test
    public void requires16Bytes3()
    {
        // given an invalid bytes count
        final var bytes = new byte[] { 0x42 };

        // when trying to create a UUID from it
        final Executable instantiation = () -> new Uuid(bytes, 1, IUuid.Variant.RFC_VARIANT);

        // then an exception should be thrown
        assertThrows(
            InvalidUuidBytesCountException.class,
            instantiation,
            "Uuid shouldn't be created when its not 16 bytes long"
        );
    }

    @Test
    public void requires32HexDigits()
    {
        // given an invalid string representation
        final String format = "42";

        // when trying to create a UUID from it
        final Executable instantiation = () -> new Uuid(format, 0);

        // then an exception should be thrown
        assertThrows(
            InvalidUuidFormatException.class,
            instantiation,
            "Uuid shouldn't be created if it doesn't comply with RFC format"
        );
    }


    @Test
    public void isTrimmed()
    {
        // given a UUID made from a string representation with spaces
        final var uuid = new Uuid("   00 000000-000 0-00 00-00 00-000 000 00000 0   ", 0);

        // when checking its format
        final String format = uuid.toString();

        // then it shouldn't contain whitespaces
        assertEquals(
            format,
            "00000000-0000-0000-0000-000000000000",
            "String representation shouldn't contain spaces"
        );
    }

    @Test
    public void requiresStringMatchingVersion()
    {
        // given a string representation that mismatches the expected version
        final String format = "00000000-0000-f000-0000-000000000000";
        final int wrongVersion = 0xe;

        // when trying to build a UUID from it
        final Executable instantiation = () -> new Uuid(format, wrongVersion);

        // then an exception should be thrown
        assertThrows(
            InvalidUuidVersionException.class,
            instantiation,
            "Uuid shouldn't be created if version mismatches the string"
        );
    }

    @Test
    public void isRfcCompliant()
    {
        // given a valid UUID
        final var uuid = new Uuid(nullBytes());

        // when checking its string format
        final String format = uuid.toNative();

        // then it should match RFC representation
        assertTrue(
            format.matches("[a-z0-9]{8}-([a-z0-9]{4}-){3}[a-z0-9]{12}"),
            // format.matches("[a-z0-9]{8}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{4}-[a-z0-9]{12}")
            "Uuid should comply with RFC format xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"
        );
    }

    public static Object[][] version()
    {
        return new Object[][] {
            { 4 },
            { 2 },
        };
    }

    @ParameterizedTest
    @MethodSource("version")
    public void hasSpecifiedVersion(final int expectedVersion)
    {
        // given a UUID from specified bytes, and a version
        final var uuid = new Uuid(nullBytesAndVersion(expectedVersion), expectedVersion);

        // when checking the actual version of the UUID
        final int actualVersion = uuid.version();

        // then it should be the expected one
        assertEquals(
            expectedVersion,
            actualVersion,
            "Version wasn't correctly set"
        );
    }

    @ParameterizedTest
    @MethodSource("version")
    public void versionIsThe13thDigit(final int expectedVersion)
    {
        // given a UUID from specified bytes, and a version
        final var uuid = new Uuid(nullBytesAndVersion(expectedVersion), expectedVersion);

        // when checking the string representation of the UUID
        final String format = uuid.toString();

        // then the 13th digit (skipping dashes) should be the version
        assertEquals(
            Character.forDigit(expectedVersion, 16),
            format.charAt(14),
            "Version should be set as the 13th digit"
        );
    }

    @Test
    public void requiresVariant()
    {
        // given no variant
        final IUuid.Variant variant = null;

        // when trying to make an instance of it
        final Executable instantiation = () -> new Uuid(nullBytes(), 1, variant);

        // then an exception should be thrown
        assertThrows(
            NullVariantException.class,
            instantiation,
            "Variant shouldn't be null"
        );
    }

    public static Object[][] variant()
    {
        return new Object[][] {
            { IUuid.Variant.APOLLO_NCS_VARIANT, List.of('0', '1', '2', '3', '4', '5', '6', '7') },
            { IUuid.Variant.RFC_VARIANT, List.of('8', '9', 'a', 'b') },
            { IUuid.Variant.MICROSOFT_VARIANT, List.of('c', 'd') },
            { IUuid.Variant.FUTURE_VARIANT, List.of('e', 'f') },
        };
    }

    @ParameterizedTest
    @MethodSource("variant")
    public void hasSpecifiedVariant(final IUuid.Variant expectedVariant, final List<Character> __)
    {
        // given a UUID given specified bytes, and a variant
        final var uuid = new Uuid(nullBytes(), 0, expectedVariant);

        // when checking the actual variant of the UUID
        final IUuid.Variant actualVariant = uuid.variant();

        // then it should be the expected one
        assertEquals(
            expectedVariant,
            actualVariant,
            "Expected to get variant " + expectedVariant.name() + ", got " + actualVariant.name()
        );
    }

    @ParameterizedTest
    @MethodSource("variant")
    public void variantIsThe17thDigit(final IUuid.Variant expectedVariant, final List<Character> possibleVariantDigits)
    {
        // given a UUID given specified bytes, and a variant
        final var uuid = new Uuid(nullBytes(), 0, expectedVariant);

        // when checking the variant digit in the string representation of the UUID
        final Character actualVariantDigit = uuid.toString().charAt(19);

        // then it should be a matching one
        assertTrue(
            possibleVariantDigits.contains(actualVariantDigit),
            "Digit '" + actualVariantDigit + "' doesn't match variant " + expectedVariant.name()
        );
    }

    public static Object[][] string()
    {
        return new Object[][] {
            { "c940267f-9bab-62f9-1510-850b693705cd", 6, IUuid.Variant.APOLLO_NCS_VARIANT },
            { "ee6fbac0-0ba7-7b42-9356-bb9aa340b7f2", 7, IUuid.Variant.RFC_VARIANT },
            { "792c4ae2-0f55-834d-d4b8-68c013256fc3", 8, IUuid.Variant.MICROSOFT_VARIANT },
            { "606e60f0-5fcf-9c25-f52a-456394790232", 9, IUuid.Variant.FUTURE_VARIANT },
        };
    }

    @ParameterizedTest
    @MethodSource("string")
    public void buildsFromString(final String expectedFormat, final int expectedVersion, final IUuid.Variant __)
    {
        // given an UUID made from a string
        final var uuid = new Uuid(expectedFormat, expectedVersion);

        // when checking its string representation
        final String actualFormat = uuid.toString();

        // then it should be the one used to build it
        assertEquals(
            expectedFormat,
            actualFormat,
            "Expected to get UUID '" + expectedFormat + "', got '" + actualFormat + "'"
        );
    }

    @ParameterizedTest
    @MethodSource("string")
    public void parsesVersionFromString(final String expectedFormat, final int expectedVersion, final IUuid.Variant __)
    {
        // given a UUID made from a string
        final var uuid = new Uuid(expectedFormat, expectedVersion);

        // when checking its version
        final int actualVersion = uuid.version();

        // then it should be the expected one
        assertEquals(
            expectedVersion,
            actualVersion,
            "Expected to parse version " + expectedVersion + " from UUID '" + expectedFormat + "', got version " + actualVersion
        );
    }

    @ParameterizedTest
    @MethodSource("string")
    public void parsesVariantFromString(final String expectedFormat, final int expectedVersion, final IUuid.Variant expectedVariant)
    {
        // given an UUID made from a string
        final var uuid = new Uuid(expectedFormat, expectedVersion);

        // when checking its variant
        final IUuid.Variant actualVariant = uuid.variant();

        // then it should be the expected one
        assertEquals(
            expectedVariant,
            actualVariant,
            "Expected to get variant " + expectedVariant.name() + ", got " + actualVariant.name() + " from UUID " + expectedFormat
        );
    }

    @Test
    public void requiresHexDigits()
    {
        // given an invalid format
        final String invalidFormat = "00000000-0z00-0000-0000-000000000000";

        // when trying to create a UUID from it
        final Executable instantiation = () -> new Uuid(invalidFormat, 0);

        // then an exception should be thrown
        assertThrows(
            InvalidUuidFormatException.class,
            instantiation,
            "Uuid should contain only hex digits"
        );
    }

    public static Object[][] comparison()
    {
        final var uuid = new Uuid(nullBytesAndVersion(1));

        final var otherIdentity = new IIdentity<String>() {
            @Override public String toNative() { return "42"; }
            @Override public boolean equals(final IValueObject other) { return false; }
        };

        final var otherValueObject = new IValueObject() {
            @Override public boolean equals(IValueObject other) { return false; }
        };

        return new Object[][] {
            { uuid, uuid, true, "UUID should equal itself" },
            { uuid, new Uuid(nullBytesAndVersion(1)), true, "UUID should equal when bytes are the same" },
            { uuid, new Uuid(nullBytesAndVersion(3)), false, "UUID shouldn't equal when bytes aren't the same" },
            { uuid, otherIdentity, false, "UUID shouldn't equal when not given an UUID" },
            { uuid, otherValueObject, false, "UUID shouldn't equal when not given an identity" }
        };
    }

    @ParameterizedTest
    @MethodSource("comparison")
    public void matchesSameUuid(
        final IValueObject uuid,
        final IValueObject other,
        final boolean expectedEquality,
        final String errorMessage
    ) {
        // given an UUID, and another value object to compare with

        // when comparing them
        final boolean areTheSame = uuid.equals(other);

        // then it should match the expected equality
        assertEquals(expectedEquality, areTheSame, errorMessage);
    }
}
