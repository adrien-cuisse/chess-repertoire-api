
package com.adrien_cuisse.chess_repertoire.application.use_cases.position.creation;

import com.adrien_cuisse.chess_repertoire.application.dto.position.FindPositionByUserAndFenQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.position.FindPositionByUserAndNameQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.position.RegisterPositionCommand;
import com.adrien_cuisse.chess_repertoire.application.services.IAuthenticator;
import com.adrien_cuisse.chess_repertoire.domain.entities.user.IUser;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid.UuidV4;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

public final class CreatePositionInteractor
{
	private final IAuthenticator authenticator;

	private final RegisterPositionCommand.IHandler registerPositionHandler;

	private final static Pattern RANK_PATTERN = Pattern.compile(
		"^[p1-8rnbqk]{1,8}$",
		Pattern.CASE_INSENSITIVE
	);

	private final FindPositionByUserAndNameQuery.IHandler findPositionByUserAndNameHandler;

	private final FindPositionByUserAndFenQuery.IHandler findPositionByUserAndFenHandler;

	/*private final static Pattern FEN_VALIDATION_REGEX = Pattern.compile(String.format(
		"%s %s %s %s %s %s",
		"(([1-8]|[pPrnbqkbnrRNBQKBNR]){1,8})/(([1-8]|[pPrnbqkbnrRNBQKBNR]){1,8}){7}", // ranks
		"w|b", // active player
		"(K?Q?k?q?)?|-", // castling possibilities
		"([a-h][36])|-", // en-passant square
		"[0-9]+", // plies counter
		"[1-9][0-9]*" // turns counter
	));*/

	public CreatePositionInteractor(
		final IAuthenticator authenticator,
		final RegisterPositionCommand.IHandler registerPositionHandler,
		final FindPositionByUserAndNameQuery.IHandler findPositionByUserAndNameHandler,
		final FindPositionByUserAndFenQuery.IHandler findPositionByUserAndFenHandler
	) {
		this.authenticator = authenticator;
		this.registerPositionHandler = registerPositionHandler;
		this.findPositionByUserAndNameHandler = findPositionByUserAndNameHandler;
		this.findPositionByUserAndFenHandler = findPositionByUserAndFenHandler;
	}

	public void execute(final PositionCreationRequest request, IPositionCreationPresenter presenter)
	{
		final var response = new PositionCreationResponse();

		final String authenticationToken = request.authenticationToken();

		if (authenticationToken == null)
			response.authenticationTokenIsMissing = true;
		else
		{
			final Optional<IUser> authenticatedUser = this.authenticator.authenticate(authenticationToken);
			if (authenticatedUser.isEmpty())
				response.authenticationTokenIsInvalid = true;
			else if (requestIsValid(request, response, authenticatedUser.get()))
				registerPosition(request, authenticatedUser.get());
		}

		presenter.present(response);
	}

	private boolean requestIsValid(
		final PositionCreationRequest request,
		final PositionCreationResponse response,
		final IUser authenticatedUser
	) {
		boolean errorOccured = positionNameIsMissingOrInvalidOrTaken(
			request,
			response,
			authenticatedUser
		);
		errorOccured |= fenIsMissingOrInvalidOrTaken(
			request,
			response,
			authenticatedUser
		);

		return !errorOccured;
	}

	private boolean positionNameIsMissingOrInvalidOrTaken(
		final PositionCreationRequest request,
		final PositionCreationResponse response,
		final IUser authenticatedUser
	) {
		if (request.name() == null)
			return response.nameIsMissing = true;

		final String name = request.name().replaceAll("\\s+", " ").trim();

		if (name.equals(""))
			return response.nameIsMissing = true;
		if (name.length() > 32)
			return response.nameIsTooLong = true;
		if (nameIsAlreadyUsedByTheUser(request, authenticatedUser))
			return response.nameIsAlreadyUsed = true;

		return false;
	}

	private boolean nameIsAlreadyUsedByTheUser(
		final PositionCreationRequest request,
		final IUser authenticatedUser
	) {
		final var query = new FindPositionByUserAndNameQuery(
			authenticatedUser.identity().toString(),
			request.name()
		);
		return this.findPositionByUserAndNameHandler.execute(query).isPresent();
	}

	private boolean fenIsMissingOrInvalidOrTaken(
		final PositionCreationRequest request,
		final PositionCreationResponse response,
		final IUser authenticatedUser
	) {
		if (request.fen() == null)
			return response.fenIsMissing = true;

		final String fen = request.fen().replaceAll("\\s+", " ").trim();

		final String[] fenParts = fen.split(" ");
		if (fenParts.length != 6)
			return response.fenIsInvalid = true;

		final String ranksPart = fenParts[0];
		final String[] ranks = ranksPart.split("/");
		if (fenRanksAreInvalid(ranks))
			return response.fenIsInvalid = true;

		final String activePlayer = fenParts[1];
		if (fenHasInvalidActivePlayer(activePlayer))
			return response.fenIsInvalid = true;

		final String castlingPossibilities = fenParts[2];
		final String blackKingRank = ranks[0];
		final String whiteKingRank = ranks[7];
		if (fenHasInvalidCastlingPossibilities(castlingPossibilities, whiteKingRank, blackKingRank))
			return response.fenIsInvalid = true;

		final String enPassantSquare = fenParts[3];
		if (fenHasInvalidEnPassantSquare(enPassantSquare))
			return response.fenIsInvalid = true;

		final String pliesCounter = fenParts[4];
		if (fenHasInvalidPliesCounter(pliesCounter))
			return response.fenIsInvalid = true;

		final String turnsCounter = fenParts[5];
		if (fenHasInvalidTurnsCounter(turnsCounter))
			return response.fenIsInvalid = true;

		if (fenIsAlreadyUsedByTheUser(request, authenticatedUser))
			return response.fenIsAlreadyUsed = true;

		return false;
	}

	private static boolean fenRanksAreInvalid(final String[] ranks)
	{
		if (ranks.length != 8)
			return true;

		if (fenRanksHaveMissingKing(ranks))
			return true;
		if (fenRanksHaveInvalidEmptySquares(ranks))
			return true;

		return Arrays.stream(ranks)
			.anyMatch(CreatePositionInteractor::fenRankIsInvalid);
	}

	private static boolean fenRanksHaveMissingKing(final String[] ranks)
	{
		boolean blackKingIsMissing = true;
		boolean whiteKingIsMissing = true;

		for (final String rank : ranks)
		{
			if (rank.contains("K")) whiteKingIsMissing = false;
			if (rank.contains("k")) blackKingIsMissing = false;
		}

		return whiteKingIsMissing || blackKingIsMissing;
	}

	private static boolean fenRanksHaveInvalidEmptySquares(final String[] ranks)
	{
		boolean hasInvalidEmptySquare = false;
		boolean hasConsecutiveEmptySquares = false;

		for (final String rank : ranks)
		{
			if (rank.matches(".*[09].*"))
				hasInvalidEmptySquare = true;
			if (rank.matches(".*[1-8]{2,}.*"))
				hasConsecutiveEmptySquares = true;
		}

		return hasInvalidEmptySquare || hasConsecutiveEmptySquares;
	}

	private static boolean fenRankIsInvalid(final String rank)
	{
		return fenRankContainsInvalidPiece(rank)
			|| fenRankHasInvalidLength(rank);
	}

	private static boolean fenRankContainsInvalidPiece(final String rank)
	{
		boolean isInvalid = !RANK_PATTERN.matcher(rank).find();
		return isInvalid;
	}

	private static boolean fenRankHasInvalidLength(final String rank)
	{
		int rankLength = 0;

		for (final char square : rank.toCharArray())
		{
			if (square >= '1' && square <= '8')
				rankLength += square - '0';
			else
				rankLength++;
		}

		return rankLength != 8;
	}

	private static boolean fenHasInvalidActivePlayer(final String activePlayer)
	{
		return !activePlayer.matches("[wb]");
	}

	private static boolean fenHasInvalidCastlingPossibilities(
		final String castlingPossibilities,
		final String whiteKingRank,
		final String blackKingRank
	) {
		if (fenHasMalformedCastlingPossibilities(castlingPossibilities))
			return true;

		return fenHasMalformedCastlingPossibilities(castlingPossibilities)
			|| fenHasConflictingCastlingPossibilities(
				castlingPossibilities,
				whiteKingRank,
				blackKingRank
		);
	}

	private static boolean fenHasMalformedCastlingPossibilities(final String castlingPossibilities)
	{
		return !castlingPossibilities.matches("-|(K?Q?k?q?)");
	}

	private static boolean fenHasConflictingCastlingPossibilities(
		final String castlingPossibilities,
	  	final String whiteKingRank,
		final String blackKingRank
	) {
		final boolean blackCanKingSide = castlingPossibilities.contains("k");
		final boolean blackCanQueenSide = castlingPossibilities.contains("q");

		final boolean blackKingMoved = !kingSquareContent(blackKingRank).equals("k");
		if (blackKingMoved && (blackCanKingSide || blackCanQueenSide))
			return true;

		final boolean a8RookMoved = blackKingRank.charAt(0) != 'r';
		if (a8RookMoved && blackCanQueenSide)
			return true;
		final boolean h8RookMoved = blackKingRank.charAt(blackKingRank.length() - 1) != 'r';
		if (h8RookMoved && blackCanKingSide)
			return true;

		final boolean whiteCanKingSide = castlingPossibilities.contains("K");
		final boolean whiteCanQueenSide = castlingPossibilities.contains("Q");

		final boolean whiteKingMoved = !kingSquareContent(whiteKingRank).equals("K");
		if (whiteKingMoved && (whiteCanKingSide || whiteCanQueenSide))
			return true;

		final boolean a1RookMoved = whiteKingRank.charAt(0) != 'R';
		if (a1RookMoved && whiteCanQueenSide)
			return true;
		final boolean h1RookMoved = whiteKingRank.charAt(whiteKingRank.length() - 1) != 'R';
		if (h1RookMoved && whiteCanKingSide)
			return true;

		return false;
	}

	private static String kingSquareContent(final String rank)
	{
		int file = 'a';

		String content = "";

		for (final String square : rank.split(""))
		{
			final boolean isChessman = "rnbqk".contains(square.toLowerCase());

			if (file >= 'e')
			{
				if (isChessman)
					content = square;
				else
					content = " ";
				break;
			}

			if (isChessman)
				file++;
			else
				file += Integer.parseInt(square);
		}

		return content;
	}

	private static boolean fenHasInvalidEnPassantSquare(final String enPassantSquare)
	{
		return !enPassantSquare.matches("-|([a-h][36])");
	}

	private static boolean fenHasInvalidPliesCounter(final String pliesCounter)
	{
		return !pliesCounter.matches("0|([1-9][0-9]*)");
	}

	private static boolean fenHasInvalidTurnsCounter(final String turnsCounter)
	{
		return !turnsCounter.matches("[1-9][0-9]*");
	}

	private boolean fenIsAlreadyUsedByTheUser(
		final PositionCreationRequest request,
		final IUser authenticatedUser
	) {
		final var query = new FindPositionByUserAndFenQuery(
			authenticatedUser.identity().toString(),
			request.fen()
		);
		return this.findPositionByUserAndFenHandler.execute(query).isPresent();
	}

	private void registerPosition(
		final PositionCreationRequest request,
		final IUser authenticatedUser
	) {
		final var command = new RegisterPositionCommand(
			new UuidV4().toString(),
			authenticatedUser.identity().toString(),
			request.name().replaceAll("\\s+", " ").trim(),
			request.fen()
		);
		this.registerPositionHandler.execute(command);
	}
}
