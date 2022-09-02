
package com.adrien_cuisse.chess_repertoire.application.use_cases.position.creation;

import com.adrien_cuisse.chess_repertoire.application.doubles.dto.position.FindPositionByUserAndFenFake;
import com.adrien_cuisse.chess_repertoire.application.doubles.dto.position.FindPositionByUserAndNameFake;
import com.adrien_cuisse.chess_repertoire.application.doubles.dto.position.RegisterPositionMock;
import com.adrien_cuisse.chess_repertoire.application.doubles.services.AuthenticatorMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public final class CreatePositionInteractorTest
{
	private CreatePositionInteractor interactor;

	private AuthenticatorMock authenticatorMock;

	private PositionCreationPresenterMock presenter;

	private RegisterPositionMock registerPositionMock;

	private FindPositionByUserAndNameFake findPositionByUserAndNameFake;

	private FindPositionByUserAndFenFake findPositionByUserAndFenFake;

	@BeforeEach
	public void setup()
	{
		this.authenticatorMock = new AuthenticatorMock();
		this.presenter = new PositionCreationPresenterMock();
		this.registerPositionMock = new RegisterPositionMock();
		this.findPositionByUserAndNameFake = new FindPositionByUserAndNameFake();
		this.findPositionByUserAndFenFake = new FindPositionByUserAndFenFake();

		this.interactor = new CreatePositionInteractor(
			this.authenticatorMock,
			this.registerPositionMock,
			this.findPositionByUserAndNameFake,
			this.findPositionByUserAndFenFake
		);
	}

	@Test
	public void authenticatesTheUser()
	{
		// given a position creation
		final var request = new PositionCreationRequest(
			this.authenticatorMock.bypassingToken(),
			"",
			""
		);

		// when trying to process the creation
		this.interactor.execute(request, this.presenter);

		// then the user should have been authenticated
		assertTrue(
			this.authenticatorMock.wasExecuted(),
			"User should have been authenticated"
		);
	}

	@Test
	public void deniesAccessIfNoAuthenticationTokenProvided()
	{
		// given a position creation request with no authentication token
		final var request = new PositionCreationRequest(
			null,
			"",
			""
		);

		// when trying to process the creation
		this.interactor.execute(request, this.presenter);

		// then access should be denied
		assertTrue(
			this.presenter.receivedResponse().authenticationTokenIsMissing,
			"Access should be denied if no authentication token is provided"
		);
	}

	@Test
	public void deniesAccessIfInvalidTokenProvided()
	{
		// given a position creation request with an invalid authentication token
		final var request = new PositionCreationRequest(
			"invalid token",
			"",
			""
		);
		// when trying to process the creation
		this.interactor.execute(request, this.presenter);

		// then access should be denied
		assertTrue(
			this.presenter.receivedResponse().authenticationTokenIsInvalid,
			"Access should be denied if authentication token is invalid"
		);
	}

	@Test
	public void requiresPositionName()
	{
		// given a position creation request without position name
		final var request = new PositionCreationRequest(
			this.authenticatorMock.bypassingToken(),
			null,
			""
		);

		// when executing it
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing name" error
		assertTrue(
			this.presenter.receivedResponse().nameIsMissing,
			"Creation shouldn't be possible without name"
		);
	}

	@Test
	public void requiresNonBlankPositionName()
	{
		// given a position creation request with a blank position name
		final var request = new PositionCreationRequest(
			this.authenticatorMock.bypassingToken(),
			"",
			""
		);
		// when executing it
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing name" error
		assertTrue(
			this.presenter.receivedResponse().nameIsMissing,
			"Creation shouldn't be possible without name"
		);
	}

	@Test
	public void requiresPositionNameAtMost32CharactersLong()
	{
		// given a position creation request with a too long position name
		final var request = new PositionCreationRequest(
			this.authenticatorMock.bypassingToken(),
			"a".repeat(33),
			""
		);

		// when executing it
		this.interactor.execute(request, this.presenter);

		// then there should be a "name too long" error
		assertTrue(
			this.presenter.receivedResponse().nameIsTooLong,
			"Creation shouldn't be possible with a too long name"
		);
	}

	@Test
	public void requiresFen()
	{
		// given a position creation request with no fen
		final var request = new PositionCreationRequest(
			this.authenticatorMock.bypassingToken(),
			"",
			null
		);

		// when executing it
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing FEN" error
		assertTrue(
			this.presenter.receivedResponse().fenIsMissing,
			"Creation shouldn't be possible without FEN"
		);
	}

	public static Object[][] invalidFenPartsCount()
	{
		return new Object[][] {
			{ "8/8/8/8/8/8/8/k6K w - - 0", "not enough parts" },
			{ "8/8/8/8/8/8/8/k6K w - - 0 1 errorPart", "too many parts" },
		};
	}
	public static Object[][] invalidFenRanksCount()
	{
		return new Object[][] {
			{ "k6K/8/8/8/8/8/8 w - - 0 1", "not enough ranks" },
			{ "k6K/8/8/8/8/8/8/8/8 w - - 0 1", "too many ranks" },
		};
	}
	public static Object[][] invalidFenRanks()
	{
		return new Object[][] {
			{ "08/8/8/8/8/8/8/6kK w - - 0 1", "invalid rank digit" },
			{ "8/a7/8/8/8/8/8/6kK w - - 0 1", "'a' is not a valid piece" },
			{ "8/8/11111111/8/8/8/8/6kK w - - 0 1", "empty squares count should be a single digit" },
			{ "k6K/8/8/8/8/8/8/7 w - - 0 1", "rank is too short" },
			{ "k6K/p3pPP/8/8/8/8/8/8 w - - 0 1", "rank is too short" },
			{ "rnbq1bn/8/8/8/8/8/8/k6K w - - 0 1", "rank is too short" },
			{ "8/RNBQBNR/8/8/8/8/8/k6K w - - 0 1", "rank is too short" },
			{ "8/8/1Q1n1pP/8/8/8/8/k6K w - - 0 1", "rank is too short" },
			{ "8/8/8/8p/8/8/8/k6K w - - 0 1", "rank is too long" },
			{ "8/8/8/8/8/8/8/8 w - - 0 1", "kings are missing" },
			{ "8/8/8/8/8/8/8/k7 w KQkq - 0 1", "white king is missing" },
			{ "8/8/8/8/8/8/8/7K w KQkq - 0 1", "black king is missing" },
		};
	}
	public static Object[][] invalidFenPlayers() {
		return new Object[][] {
			{ "8/8/8/8/8/8/8/k6K - - - 0 1", "no active player" },
			{ "8/8/8/8/8/8/8/k6K a - - 0 1", "'a' is not a valid active player" },
			{ "8/8/8/8/8/8/8/k6K 3 - - 0 1", "'3' is not a valid active player" },
			{ "8/8/8/8/8/8/8/k6K + - - 0 1", "'+' is not a valid active player" },
			{ "8/8/8/8/8/8/8/k6K W - - 0 1", "active player is uppercase" },
			{ "8/8/8/8/8/8/8/k6K B - - 0 1", "active player is uppercase" },
			{ "8/8/8/8/8/8/8/k6K e4 - - 0 1", "'e4' is not a valid ative player" },
		};
	}
	public static Object[][] invalidFenCastling()
	{
		return new Object[][]{
			{ "8/8/8/8/8/8/8/k6K w kqKQ - 0 1", "castling has incorrect color order" },
			{ "8/8/8/8/8/8/8/k6K w kqK - 0 1", "castling has incorrect color order" },
			{ "8/8/8/8/8/8/8/k6K w kqQ - 0 1", "castling has incorrect color order" },
			{ "8/8/8/8/8/8/8/k6K w kKQ - 0 1", "castling has incorrect color order" },
			{ "8/8/8/8/8/8/8/k6K w kK - 0 1", "castling has incorrect color order" },
			{ "8/8/8/8/8/8/8/k6K w kQ - 0 1", "castling has incorrect color order" },
			{ "8/8/8/8/8/8/8/k6K w qKQ - 0 1", "castling has incorrect color order" },
			{ "8/8/8/8/8/8/8/k6K w qK - 0 1", "castling has incorrect color order" },
			{ "8/8/8/8/8/8/8/k6K w qQ - 0 1", "castling has incorrect color order" },
			{ "8/8/8/8/8/8/8/k6K w QKqk - 0 1", "castling has incorrect king/queen order" },
			{ "8/8/8/8/8/8/8/k6K w Kqk - 0 1", "castling has incorrect king/queen order" },
			{ "8/8/8/8/8/8/8/k6K w Qqk - 0 1", "castling has incorrect king/queen order" },
			{ "8/8/8/8/8/8/8/k6K w QKk - 0 1", "castling has incorrect king/queen order" },
			{ "8/8/8/8/8/8/8/k6K w QKq - 0 1", "castling has incorrect king/queen order" },
			{ "8/8/8/8/8/8/8/k6K w qkQK - 0 1", "castling has incorrect king/queen and color order" },

			{ "r3k2r/8/8/8/8/8/8/R2K3R w KQkq - 0 1", "conflicting castling: white king moved, white should reject KQ" },
			{ "r2k3r/8/8/8/8/8/8/R3K2R w KQkq - 0 1", "conflicting castling: back king moved, black should reject kq" },

			{ "r3k2r/8/8/8/8/8/8/4K2R w KQkq - 0 1", "conflicting castling: missing a1 rook, white should reject Q" },
			{ "r3k2r/8/8/8/8/8/8/R3K3 w KQkq - 0 1", "conflicting castling: missing h1 rook, white should reject K" },
			{ "4k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1", "conflicting castling: missing a8 rook, black should reject q" },
			{ "r3k3/8/8/8/8/8/8/R3K2R w KQkq - 0 1", "conflicting castling: missing h8 rook, black should reject " },

			{ "r3k2r/8/8/8/8/8/8/1R2K2R w KQkq - 0 1", "conflicting castling: a1 rook moved, white should reject Q" },
			{ "r3k2r/8/8/8/8/8/8/R3K3 w KQkq - 0 1", "conflicting castling: h1 rook moved, white should reject K" },
			{ "4k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1", "conflicting castling: a8 rook moved, black should reject q" },
			{ "r3k3/8/8/8/8/8/8/R3K2R w KQkq - 0 1", "conflicting castling: h8 rook moved, black should reject k" },
		};
	}
	public static Object[][] invalidFenEnPassantSquare()
	{
		return new Object[][] {
			{ "8/8/8/8/8/8/8/k6K w - a 0 1", "invalid en-passant square, rank is missing" },
			{ "8/8/8/8/8/8/8/k6K w - 2 0 1", "invalid en-passant square, file is missing" },
			{ "8/8/8/8/8/8/8/k6K w - c9 0 1", "invalid en-passant square, '9' is not a valid rank" },
			{ "8/8/8/8/8/8/8/k6K w - k6 0 1", "invalid en-passant square, 'k' is not a valid file" },
			{ "8/8/8/8/8/8/8/k6K w - A6 0 1", "invalid en-passant square, 'A' file is uppercase" },
			{ "8/8/8/8/8/8/8/k6K w - + 0 1", "invalid en-passant square, '+' is not a valid square" },
		};
	}
	public static Object[][] invalidFenPlies()
	{
		return new Object[][] {
			{ "8/8/8/8/8/8/8/k6K w - - - 1", "invalid plies counter, '-' is not a digit" },
			{ "8/8/8/8/8/8/8/k6K w - - x 1", "invalid plies counter, 'x' is not a digit" },
			{ "8/8/8/8/8/8/8/k6K w - - -1 1", "invalid plies counter, must start at 0" },
			{ "8/8/8/8/8/8/8/k6K w - - +1 1", "invalid plies counter, contains useless '+' sign" },
			{ "8/8/8/8/8/8/8/k6K w - - 01 1", "invalid plies counter, contains leading '0'" },
		};
	}
	public static Object[][] invalidFenTurns()
	{
		return new Object[][] {
			{ "8/8/8/8/8/8/8/k6K w - - 0 -", "invalid turns counter, '-' is not a digit" },
			{ "8/8/8/8/8/8/8/k6K w - - 0 -1", "invalid turns counter, must start at 1" },
			{ "8/8/8/8/8/8/8/k6K w - - 0 0", "invalid turns counter, must start at 1" },
			{ "8/8/8/8/8/8/8/k6K w - - 0 +1", "invalid turns counter, contains a useless '+' sign" },
			{ "8/8/8/8/8/8/8/k6K w - - 0 x", "invalid turns counter, 'x' is not a digit" },
			{ "8/8/8/8/8/8/8/k6K w - - 0 01", "invalid turns counter, contains a leading '0'" },
		};
	}

	@ParameterizedTest
	@MethodSource({
		"invalidFenPartsCount",
		"invalidFenRanksCount",
		"invalidFenRanks",
		"invalidFenPlayers",
		"invalidFenCastling",
		"invalidFenEnPassantSquare",
		"invalidFenPlies",
		"invalidFenTurns"
	})
	public void requiresValidFen(final String invalidFen, final String reason)
	{
		// given a position creation request with an invalid fen
		final var request = new PositionCreationRequest(
			this.authenticatorMock.bypassingToken(),
			"",
			invalidFen
		);

		// when trying to process the creation
		this.interactor.execute(request, this.presenter);

		// then there should be an "invalid FEN" error
		assertTrue(
			this.presenter.receivedResponse().fenIsInvalid,
			"Creation shouldn't be possible with an invalid FEN: " + reason
		);
	}

	@Test
	public void registersThePosition()
	{
		// given a valid position creation request
		final var request = new PositionCreationRequest(
			this.authenticatorMock.bypassingToken(),
			"standard starting position",
			"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
		);

		// when processing the registration
		this.interactor.execute(request, this.presenter);

		// then the position should have been registered
		assertTrue(
			this.registerPositionMock.wasExecuted(),
			"Position should be registered if its name and fen are valid"
		);

	}

	@Test
	public void registerThePositionWithCorrectOwner()
	{
		// given a valid position creation request
		final var request = new PositionCreationRequest(
			this.authenticatorMock.bypassingToken(),
			"standard starting position",
			"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
		);

		// when processing the registration
		this.interactor.execute(request, this.presenter);

		// then the position should have been registered with correct owner
		assertEquals(
			this.authenticatorMock.authenticatedUser().identity().toString(),
			this.registerPositionMock.receivedCommand().ownerIdentity(),
			"Position should be registered with the authenticated as owner"
		);
	}

	@Test
	public void registersThePositionWithTrimmedName()
	{
		// given a valid position creation request, but with polluted name
		final var request = new PositionCreationRequest(
			this.authenticatorMock.bypassingToken(),
			"  position   name ",
			"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
		);

		// when processing the registration
		this.interactor.execute(request, this.presenter);

		// then the position should have been registered with a trimmed name
		assertEquals(
			"position name",
			this.registerPositionMock.receivedCommand().name(),
			"Position should be registered with a trimmed name"
		);
	}

	@Test
	public void requiresAvailableName()
	{
		// given a position creation request, but with a name already used by the user
		final var request = new PositionCreationRequest(
			this.authenticatorMock.bypassingToken(),
			this.findPositionByUserAndNameFake.matchingName(),
			"rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "name already used" error
		assertTrue(
			this.presenter.receivedResponse().nameIsAlreadyUsed,
			"Registration shouldn't be processed if the user has already a position with this name"
		);
	}

	@Test
	public void requiresAvailableFen()
	{
		// given a position creation request, but with a fen already used by the user
		final var request = new PositionCreationRequest(
			this.authenticatorMock.bypassingToken(),
			"name",
			this.findPositionByUserAndFenFake.matchingFen()
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "fen already used" error
		assertTrue(
			this.presenter.receivedResponse().fenIsAlreadyUsed,
			"Registration shouldn't be processed if the user has already a position with this fen"
		);
	}
}
