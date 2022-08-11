
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public final class FenTest
{
	@Test
	public void isNotNull()
	{
		// given a null fen
		final String nullFen = null;

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(nullFen);

		// then there should be an error
		assertThrows(
			NullFenException.class,
			instantiation,
			"Fen shouldn't be null"
		);
	}

	public static Object[][] invalidPartsCount()
	{
		return new Object[][] {
			{ "8/8/8/8/8/8/8/k6K" }, // 1 part
			{ "8/8/8/8/8/8/8/k6K w" }, // 2 parts
			{ "8/8/8/8/8/8/8/k6K w -" }, // 3 parts
			{ "8/8/8/8/8/8/8/k6K w - -" }, // 4 parts
			{ "8/8/8/8/8/8/8/k6K w - - 0" }, // 5 parts
			{ "8/8/8/8/8/8/8/k6K w - - 0 1 errorPart" }, // 7 parts
		};
	}

	@ParameterizedTest
	@MethodSource("invalidPartsCount")
	public void requires6SpaceDelimitedParts(final String invalidPartsCountFen)
	{
		// given a fen which is not made of 6 parts

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(invalidPartsCountFen);

		// then there should be an error
		assertThrows(
			PartsCountException.class,
			instantiation,
			"Fen should contain 6 space-delimited parts"
		);
	}

	public static Object[][] invalidRanksCounts()
	{
		final String padding = " w - - 0 1";

		return new Object[][] {
			{ "k6K" + padding }, // 1 rank
			{ "k6K/8" + padding }, // 2 ranks
			{ "k6K/8/8" + padding }, // 3 ranks
			{ "k6K/8/8/8" + padding }, // 4 ranks
			{ "k6K/8/8/8/8" + padding }, // 5 ranks
			{ "k6K/8/8/8/8/8" + padding }, // 6 ranks
			{ "k6K/8/8/8/8/8/8" + padding }, // 7 ranks
			{ "k6K/8/8/8/8/8/8/8/8" + padding }, // 9 ranks
		};
	}

	@ParameterizedTest
	@MethodSource("invalidRanksCounts")
	public void requires8RanksInFirstPart(final String missingRankFen)
	{
		// given an invalid fen with missing ranks

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(missingRankFen);

		// then there should be an error
		assertThrows(
			RanksCountException.class,
			instantiation,
			"Fen should contain 8 slash-delimited ranks in part 1"
		);
	}

	public static Object[][] invalidRankFens()
	{
		final String padding = " w - - 0 1";

		return new Object[][] {
			{ "08/8/8/8/8/8/8/6kK" + padding, "rank digits must be from 1 to 8" },
			{ "8/a7/8/8/8/8/8/6kK" + padding, "'a' is not a valid piece" },
			{ "8/8/11111111/8/8/8/8/6kK" + padding, "empty squares count should be a single digit" },
		};
	}

	@ParameterizedTest
	@MethodSource("invalidRankFens")
	public void ranksMustContainPieceOrNumber(final String invalidRankFen, final String reason)
	{
		// given a rank which contain an invalid character

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(invalidRankFen);

		// then there should be an error
		assertThrows(
			RankException.class,
			instantiation,
			"Fen shouldn't be created: " + reason + " in '" + invalidRankFen + "'"
		);
	}

	public static Object[][] invalidRankLengths()
	{
		final String padding = " w - - 0 1";

		return new Object[][] {
			{ "8/1/8/8/8/8/8/k6K" + padding }, // length 1
			{ "8/8/2/8/8/8/8/k6K" + padding }, // length 2
			{ "8/8/8/3/8/8/8/k6K" + padding }, // length 3
			{ "8/8/8/8/4/8/8/k6K" + padding }, // length 4
			{ "8/8/8/8/8/5/8/k6K" + padding }, // length 5
			{ "8/8/8/8/8/8/6/k6K" + padding }, // length 6
			{ "k6K/8/8/8/8/8/8/7" + padding }, // length 7
			{ "k6K/p3pPP/8/8/8/8/8/8" + padding }, // length 7, combination of digits and pawns
			{ "rnbq1bn/8/8/8/8/8/8/k6K" + padding }, // length 7, combination of digits and black pieces
			{ "8/RNBQBNR/8/8/8/8/8/k6K" + padding }, // length 7, combination of digits and white pieces
			{ "8/8/1Q1n1pP/8/8/8/8/k6K" + padding }, // length 7, combination of digits and black/white pawn/pieces
			{ "8/8/8/8p/8/8/8/k6K" + padding }, // length 9
		};
	}

	@ParameterizedTest
	@MethodSource("invalidRankLengths")
	public void requiresRanksOfLength8(final String incompleteRankFen)
	{
		// given a fen with an incomplete rank

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(incompleteRankFen);

		// then there should be an error
		assertThrows(
			RankLengthException.class,
			instantiation,
			"Fen shouldn't be created if at least one of its rank is not of length 8"
		);
	}

	public static Object[][] fensWithoutKings()
	{
		final String padding = " w - - 0 1";

		return new Object[][] {
			{
				"rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR" + padding,
				"no king is present"
			},
			{
				"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR" + padding,
				"white king is missing"
			},
			{
				"rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR" + padding,
				"black king is missing"
			},
			{
				"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQ1BNR" + padding,
				"white king is missing from rows, but present in castling possibilities"
			},
			{
				"rnbq1bnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR" + padding,
				"black king is missing from rows, but present in castling possibilities"
			},
		};
	}

	@ParameterizedTest
	@MethodSource("fensWithoutKings")
	public void requiresKingsInRanksPart(final String missingKingFen, final String errorMessage)
	{
		// given a fen where at least one king is missing

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(missingKingFen);

		// then there should be an error
		assertThrows(
			MissingKingException.class,
			instantiation,
			"Fen should contain kings, " + errorMessage + " in '" + missingKingFen + "'"
		);
	}

	public static Object[][] invalidActivePlayers()
	{
		final String padding1 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR ";
		final String padding2 = " KQkq - 0 1";

		return new Object[][] {
			{ padding1 + "-" + padding2 }, // no player
			{ padding1 + "a" + padding2 }, // not a player (file)
			{ padding1 + "3" + padding2 }, // not a player (file)
			{ padding1 + "+" + padding2 }, // not a player (symbol)
			{ padding1 + "W" + padding2 }, // uppercase white
			{ padding1 + "B" + padding2 }, // uppercase black
			{ padding1 + "e4" + padding2 }, // not a player (square)
		};
	}

	@ParameterizedTest
	@MethodSource("invalidActivePlayers")
	public void requiresValidActivePlayer(final String invalidActivePlayerFen)
	{
		// given a fen with an invalid active player

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(invalidActivePlayerFen);

		// then there should be an error
		assertThrows(
			ActivePlayerException.class,
			instantiation,
			"Fen should contain either lowercase 'w' or 'b' as active player in part 2"
		);
	}

	public static Object[][] invalidCastlingPossibilities()
	{
		final String padding1 = "8/8/8/8/8/8/8/k6K w ";
		final String padding2 = " - 0 1";

		return new Object[][] {
			// correct king/queen order, incorrect color order
			{ padding1 + "kqKQ" + padding2 },
			{ padding1 + "kqK" + padding2 },
			{ padding1 + "kqQ" + padding2 },
			{ padding1 + "kKQ" + padding2 },
			{ padding1 + "kK" + padding2 },
			{ padding1 + "kQ" + padding2 },
			{ padding1 + "qKQ" + padding2 },
			{ padding1 + "qK" + padding2 },
			{ padding1 + "qQ" + padding2 },
			// incorrect king/queen order, correct color order
			{ padding1 + "QKqk" + padding2 },
			{ padding1 + "Kqk" + padding2 },
			{ padding1 + "Qqk" + padding2 },
			{ padding1 + "QKk" + padding2 },
			{ padding1 + "QKq" + padding2 },
			// incorrect king/queen order, incorrect color order
			{ padding1 + "qkQK" + padding2 },
		};
	}

	@ParameterizedTest
	@MethodSource("invalidCastlingPossibilities")
	public void requiresCastlingPossibilitiesWithWhiteFirstKingFirst(final String invalidCastlingFen)
	{
		// given a fen with castling possibilities, black first or queen first

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(invalidCastlingFen);

		// then there should be an error
		assertThrows(
			CastlingException.class,
			instantiation,
			"Fen should contain castling possibilities with white first, and king first in part 3"
		);
	}

	public static Object[][] conflictingCastlingPossibilities()
	{
		final String padding1 = " w ";
		final String padding2 = " - 0 1";

		return new Object[][] {
			{ "r3k2r/8/8/8/8/8/8/R2K3R" + padding1 + "KQkq" + padding2 }, // white king moved, should reject KQ
			{ "r2k3r/8/8/8/8/8/8/R3K2R" + padding1 + "KQkq" + padding2 }, // black king moved, should reject kq

			{ "r3k2r/8/8/8/8/8/8/4K2R" + padding1 + "KQkq" + padding2 }, // missing a1 rook, should reject Q
			{ "r3k2r/8/8/8/8/8/8/R3K3" + padding1 + "KQkq" + padding2 }, // missing h1 rook, should reject K
			{ "4k2r/8/8/8/8/8/8/R3K2R" + padding1 + "KQkq" + padding2 }, // missing a8 rook, should reject q
			{ "r3k3/8/8/8/8/8/8/R3K2R" + padding1 + "KQkq" + padding2 }, // missing h8 rook, should reject k

			{ "r3k2r/8/8/8/8/8/8/1R2K2R" + padding1 + "KQkq" + padding2, }, // a1 rook moved, should reject Q
			{ "r3k2r/8/8/8/8/8/8/R3K3" + padding1 + "KQkq" + padding2 }, // h1 rook moved, should reject K
			{ "4k2r/8/8/8/8/8/8/R3K2R" + padding1 + "KQkq" + padding2 }, // a8 rook moved, should reject q
			{ "r3k3/8/8/8/8/8/8/R3K2R" + padding1 + "KQkq" + padding2 }, // h8 rook moved, should reject k
		};
	}

	@ParameterizedTest
	@MethodSource("conflictingCastlingPossibilities")
	public void requiresNotConflictingCastlingPossibilities(final String conflictingCastlingPossibilities)
	{
		// given a fen with castling possibilities contradicted by king/rook moves or missing rook

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(conflictingCastlingPossibilities);

		assertThrows(
			ConflictingCastlingException.class,
			instantiation,
			"checkpoint"
		);
	}


	public static Object[][] invalidEnPassantSquares()
	{
		final String padding1 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq ";
		final String padding2 = " 0 1";

		return new Object[][] {
			{ padding1 + "a" + padding2, "no rank"},
			{ padding1 + "2" + padding2, "no file" },
			{ padding1 + "c9" + padding2, "invalid rank"},
			{ padding1 + "k6" + padding2, "invalid file"},
			{ padding1 + "A6" + padding2, "uppercase file"},
			{ padding1 + "+" + padding2, "invalid square notation"},
		};
	}

	@ParameterizedTest
	@MethodSource("invalidEnPassantSquares")
	public void requiresEnPassantSquare(final String invalidEnPassantSquareFen, final String reason)
	{
		// given a fen with an invalid en-passant square

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(invalidEnPassantSquareFen);

		// then there should be an error
		assertThrows(
			EnPassantSquareException.class,
			instantiation,
			String.format(
				"Fen should contain valid en-passant square in part 4 (got %s in '%s')",
				reason, invalidEnPassantSquareFen
			)
		);
	}

	public static Object[][] invalidPliesCounters()
	{
		final String padding1 = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - ";
		final String padding2 = " 1";

		return new Object[][] {
			{ padding1 + "-" + padding2 }, // not a digit
			{ padding1 + "x" + padding2 }, // not a digit
			{ padding1 + "-1" + padding2 }, // negative count
			{ padding1 + "+1" + padding2 }, // useless '+' sign
		};
	}

	@ParameterizedTest
	@MethodSource("invalidPliesCounters")
	public void requiresPositiveIntegerPliesCount(final String invalidPliesCountFen)
	{
		// given a fen with an invalid plies count

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(invalidPliesCountFen);

		// then there should be an error
		assertThrows(
			PliesCounterException.class,
			instantiation,
			"Fen should contain a number >= 0 as plies counter in part 5"
		);
	}

	public static Object[][] invalidTurnsCounters()
	{
		final String padding = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 ";

		return new Object[][] {
			{ padding + "-" }, // not a digit
			{ padding + "-1" }, // negative count
			{ padding + "+1" }, // useless '+' sign
			{ padding + "x" }, // not a digit
			{ padding + "0" }, // zero count
		};
	}

	@ParameterizedTest
	@MethodSource("invalidTurnsCounters")
	public void requiresPositiveIntegerTurnsCount(final String invalidTurnsCountFen)
	{
		// given a fen with an invalid turns count

		// when trying to make an instance of it
		final Executable instantiation = () -> new Fen(invalidTurnsCountFen);

		// then there should be an error
		assertThrows(
			TurnsCounterException.class,
			instantiation,
			"Fen should contain a number > 0 as turns counter in part 6"
		);
	}

	@Test
	public void isTrimmed()
	{
		// given a fen with useless whitespaces
		final String trimmableFenString = " rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR  w   KQkq  -  0    1 ";

		// when making an instance of it
		final Fen fen = new Fen(trimmableFenString);

		// then it should be trimmed
		assertEquals(
			"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
			fen.toString(),
			"Fen shouldn't contain extra whitespaces"
		);
	}

	public static Object[][] comparison()
	{
		final String fenString = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

		final Fen fen = new Fen(fenString);

		final Fen differentStringFen = new Fen("r2q1rk1/p1p2ppp/1bp3n1/3pP3/3P1PQ1/2N1B2P/PP6/2KR3R b - f3 0 18");

		final IValueObject otherValueObject = new IValueObject() {
			public boolean equals(IValueObject other) { return false; }
		};

		return new Object[][] {
			{ fen, fen, true, "Fen should equal itself" },
			{ fen, new Fen(fenString), true, "Fen should equal if it represents the same value" },
			{ fen, differentStringFen, false, "Fen shouldn't equal if the value is different" },
			{ fen, otherValueObject, false, "Fen shouldn't equal when not given a fen" }
		};
	}

	@ParameterizedTest
	@MethodSource("comparison")
	public void equalsSameFen(
		final Fen fen,
		final IValueObject other,
		final boolean expectedEquality,
		final String errorMessage
	) {
		// given a fen, and another value object to compare with

		// when comparing them
		final boolean areTheSame = fen.equals(other);

		// then it should be the expected equality
		assertEquals(expectedEquality, areTheSame, errorMessage);
	}
}
