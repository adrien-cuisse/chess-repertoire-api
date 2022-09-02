
package com.adrien_cuisse.chess_repertoire.domain.value_objects.position.fen;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Fen implements IValueObject
{
	private final static Pattern EMPTY_SQUARES_DIGIT_PATTERN = Pattern.compile("([1-8])");

	private final static int RANKS_INDEX = 0;

	private final static int ACTIVE_PLAYER_INDEX = 1;

	private static final int CASTLING_POSSIBILITIES_INDEX = 2;

	private static final int EN_PASSANT_SQUARE_INDEX = 3;

	private static final int PLIES_COUNTER_INDEX = 4;

	private static final int TURNS_COUNTER_INDEX = 5;

	private static final int BLACK_PIECES_RANK_INDEX = 0;

	private static final int WHITE_PIECES_RANK_INDEX = 7;

	private static final char KING_FILE = 'e';

	private final String fen;

	/**
	 * @throws NullFenException - if fen is null
	 * @throws PartsCountException - if fen is not made of 6 space-delimited parts
	 * @throws MissingKingException - if part 1 doesn't contain king 'w' or 'b'
	 * @throws RanksCountException - if part 1 doesn't contain 8 slash-delimited ranks
	 * @throws RankException - if any rank in part 1 isn't made of characters [RNBQKrnbqk] and [1-8],
	 * 	or if it contains consecutive digits
	 * @throws RankLengthException - if any rank in part 1 has a different length than 8
	 * @throws ActivePlayerException - if part 2 contains something else than 'w' or 'b'
	 * @throws CastlingException - if part 3 contains characters other than [KQkq] in this order,
	 * 	or '-' if none
	 * @throws ConflictingCastlingException - if castling possibilities are conflicting with
	 * 	rooks or kings moves
	 * @throws EnPassantSquareException - if part 4 is not a valid lower square label, or '-' if none
	 * @throws PliesCounterException - if part 5 isn't an integer >= 0
	 * @throws TurnsCounterException - if part 6 isn't an integer > 0
	 */
	public Fen(final String fen)
	{
		if (fen == null)
			throw new NullFenException();

		this.fen = fen.replaceAll("\\s+", " ").trim();
		final String[] parts = this.fen.split(" ");

		throwIfPartsCount(parts);
		throwIfMissingKings(parts[RANKS_INDEX]);

		final String[] ranks = parts[RANKS_INDEX].split("/");

		throwIfInvalidRanksCount(ranks);
		throwInInvalidRanks(ranks);
		throwIfInvalidActivePlayer(parts[ACTIVE_PLAYER_INDEX]);
		throwIfInvalidCastlingPossibilities(parts[CASTLING_POSSIBILITIES_INDEX]);
		throwIfConflictingBlackCastling(parts[CASTLING_POSSIBILITIES_INDEX], ranks);
		throwIfConflictingWhiteCastling(parts[CASTLING_POSSIBILITIES_INDEX], ranks);
		throwIfInvalidEnPassantSquare(parts[EN_PASSANT_SQUARE_INDEX]);
		throwIfInvalidPliesCounter(parts[PLIES_COUNTER_INDEX]);
		throwIfInvalidTurnsCounter(parts[TURNS_COUNTER_INDEX]);
	}

	public boolean equals(IValueObject other)
	{
		if (other instanceof Fen otherInstance)
			return this.fen.equals(otherInstance.fen);
		return false;
	}

	public String toString()
	{
		return this.fen;
	}

	private static void throwIfPartsCount(final String[] parts)
	{
		if (parts.length != 6)
			throw new PartsCountException(parts.length);
	}

	private static void throwIfMissingKings(final String ranksPart)
	{
		if (!ranksPart.contains("K"))
			throw new MissingKingException("white", "K");
		if (!ranksPart.contains("k"))
			throw new MissingKingException("black", "k");
	}

	private static void throwIfInvalidRanksCount(final String[] ranks)
	{
		if (ranks.length != 8)
			throw new RanksCountException(ranks.length);
	}

	private static void throwInInvalidRanks(final String[] ranks)
	{
		for (final String rank : ranks)
			throwIfInvalidRank(rank);
	}

	private static void throwIfInvalidRank(final String rank)
	{
		if (rank.matches(".*[^1-8RNBQKrnbqkPp].*"))
			throw new RankException(rank);
		if (rank.matches("[1-8]{2,}"))
			throw new RankException(rank);

		final int length = rankLength(rank);
		if (length != 8)
			throw new RankLengthException(length);
	}

	private static int rankLength(final String rank)
	{
		int length = 0;

		final Matcher emptySquaresDigit = EMPTY_SQUARES_DIGIT_PATTERN.matcher(rank);
		while (emptySquaresDigit.find())
			length += Integer.parseInt(emptySquaresDigit.group());

		length += rank.replaceAll("[^RNBQKrnbqkPp]", "").length();

		return length;
	}

	private static void throwIfInvalidActivePlayer(final String activePlayer)
	{
		if (!activePlayer.equals("w") && !activePlayer.equals("b"))
			throw new ActivePlayerException(activePlayer);
	}

	private static void throwIfInvalidCastlingPossibilities(final String castlingPossibilities)
	{
		if (!castlingPossibilities.matches("^(-)|(K?Q?k?q?)$"))
			throw new CastlingException(castlingPossibilities);
	}

	private static void throwIfConflictingBlackCastling(final String possibilities, final String[] ranks)
	{
		final boolean queenSidePossibility = possibilities.contains("q");
		final boolean kingSidePossibility = possibilities.contains("k");

		final boolean hasPossibility = queenSidePossibility || kingSidePossibility;
		if (!hasPossibility)
			return;

		final boolean kingMoved = !kingSquareContent(ranks[BLACK_PIECES_RANK_INDEX]).equals("k");
		if (kingMoved)
			throw new ConflictingCastlingException("black", "player", "king", "" + KING_FILE + BLACK_PIECES_RANK_INDEX);

		final char[] pieces = ranks[BLACK_PIECES_RANK_INDEX].toCharArray();
		if (queenSidePossibility && pieces[0] != 'r')
			throw new ConflictingCastlingException("black", "queen side", "rook", "a8");
		if (kingSidePossibility && pieces[pieces.length - 1] != 'r')
			throw new ConflictingCastlingException("black", "king side", "rook", "h8");
	}

	private static void throwIfConflictingWhiteCastling(final String possibilities, final String[] ranks)
	{
		final boolean queenSidePossibility = possibilities.contains("Q");
		final boolean kingSidePossibility = possibilities.contains("K");

		final boolean hasPossibility = queenSidePossibility || kingSidePossibility;
		if (!hasPossibility)
			return;

		final boolean kingMoved = !kingSquareContent(ranks[WHITE_PIECES_RANK_INDEX]).equals("K");
		if (kingMoved)
			throw new ConflictingCastlingException("white", "player", "king", "" + KING_FILE + WHITE_PIECES_RANK_INDEX);

		final char[] pieces = ranks[WHITE_PIECES_RANK_INDEX].toCharArray();
		if (queenSidePossibility && pieces[0] != 'R')
			throw new ConflictingCastlingException("white", "queen side", "rook", "a1");
		if (kingSidePossibility && pieces[pieces.length - 1] != 'R')
			throw new ConflictingCastlingException("white", "king side", "rook", "h1");
	}

	private static String kingSquareContent(final String rank)
	{
		int file = 'a';

		String content = "";

		for (final String piece : rank.split(""))
		{
			final boolean isChessman = "RNBQKrnbqk".contains(piece);

			if (file >= 'e')
			{
				if (isChessman)
					content = piece;
				else
					content = " ";
				break;
			}

			if (isChessman)
				file++;
			else
				file += Integer.parseInt(piece);
		}

		return content;
	}

	private static void throwIfInvalidEnPassantSquare(final String enPassantSquare)
	{
		if (!enPassantSquare.matches("-|([a-h][36])"))
			throw new EnPassantSquareException(enPassantSquare);
	}

	private static void throwIfInvalidPliesCounter(final String pliesCounter)
	{
		if (!pliesCounter.matches("0|([1-9][0-9]*)"))
			throw new PliesCounterException(pliesCounter);
	}

	private static void throwIfInvalidTurnsCounter(final String turnsCounter)
	{
		if (!turnsCounter.matches("[1-9][0-9]*"))
			throw new TurnsCounterException(turnsCounter);
	}
}
