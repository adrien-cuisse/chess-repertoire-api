
package com.adrien_cuisse.chess_repertoire.application.use_cases.user.registration;

import com.adrien_cuisse.chess_repertoire.application.doubles.dto.account.FindCredentialsByTakenMailAddressFake;
import com.adrien_cuisse.chess_repertoire.application.doubles.dto.account.FindCredentialsByTakenNicknameFake;
import com.adrien_cuisse.chess_repertoire.application.doubles.dto.account.RegisterAccountMock;
import com.adrien_cuisse.chess_repertoire.application.doubles.services.PasswordHasherStub;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByMailAddressQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByNicknameQuery;
import com.adrien_cuisse.chess_repertoire.application.services.IPasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public final class RegisterUserInteractorTest
{
	private UserRegistrationPresenterMock presenter;

	private final FindCredentialsByNicknameQuery.IHandler findCredentialsByNickNameFake =
		new FindCredentialsByTakenNicknameFake();

	private final FindCredentialsByMailAddressQuery.IHandler findCredentialsByMailAddressFake =
		new FindCredentialsByTakenMailAddressFake();

	private RegisterAccountMock registerAccountMock;

	private RegisterUserInteractor interactor;

	private final IPasswordHasher passwordHasherStub = new PasswordHasherStub();

	@BeforeEach
	public void setup()
	{
		this.registerAccountMock = new RegisterAccountMock();
		this.presenter = new UserRegistrationPresenterMock();

		this.interactor = new RegisterUserInteractor(
			this.findCredentialsByNickNameFake,
			this.findCredentialsByMailAddressFake,
			this.registerAccountMock,
			this.passwordHasherStub
		);
	}

	@Test
	public void requiresNickname()
	{
		// given a registration request without nickname
		final var request = new UserRegistrationRequest(
			null,
			"foo@bar.org",
			"oG8\"aP6&fV3_gG9?oI2;hC2{jP4,vS8."
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing nickname" error
		assertTrue(
			this.presenter.receivedResponse().nicknameIsMissing,
			"Registration shouldn't be possible without nickname"
		);
	}

	@Test
	public void requiresNonBlankNickname()
	{
		// given a registration request with a blank nickname
		final var request = new UserRegistrationRequest(
			"         ",
			"foo@bar.org",
			"rO7{bB7{wL3\"jC4{aC5>bI9@rU2~rA9_"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing nickname" error
		assertTrue(
			this.presenter.receivedResponse().nicknameIsMissing,
			"Registration shouldn't be possible without nickname"
		);
	}

	@Test
	public void requiresLongEnoughNickname()
	{
		// given a registration request with a too short nickname
		final var request = new UserRegistrationRequest(
			"a",
			"foo@bar.org",
			"tU0^eC2&hP8^yB8<oI4&vG5}iI2~zZ2>"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "nickname too short" error
		assertTrue(
			this.presenter.receivedResponse().nicknameIsTooShort,
			"Registration shouldn't be possible with a too short nickname"
		);
	}

	@Test
	public void requiresShortEnoughNickname()
	{
		// given a registration request with a too long nickname
		final var request = new UserRegistrationRequest(
			"0123456789012345678",
			"foo@bar.org",
			"aJ7)bU9~rL5<gM9?jV8*eU5*tD5{jD5("
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "nickname too short" error
		assertTrue(
			this.presenter.receivedResponse().nicknameIsTooLong,
			"Registration shouldn't be possible with a too long nickname"
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
			{ '.' },
		};
	}

	@ParameterizedTest
	@MethodSource("invalidCharacters")
	public void requiresNicknameWithValidCharacters(final Character invalidCharacter)
	{
		// given a registration request with a nickname containing illegal characters
		final var request = new UserRegistrationRequest(
			"012345678901234" + invalidCharacter.toString(),
			"foo@bar.org",
			"iD9#tS3(fF5{dF2!hQ0+dI6,nV1>xJ4%"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be an "invalid nickname" error
 		assertTrue(
			this.presenter.receivedResponse().nicknameIsInvalid,
			"Registration shouldn't be possible with a nickname containing illegal characters"
		);
	}

	public static Object[][] validCharacters()
	{
		return new Object[][] {
			{ '-' }, { '_' },
		};
	}

	@ParameterizedTest
	@MethodSource("validCharacters")
	public void requiresNicknameStartingWithAlphanum(final Character validCharacter)
	{
		// given a registration request with a nickname containing illegal characters
		final var request = new UserRegistrationRequest(
			validCharacter.toString() + "nickname",
			"foo@bar.org",
			"iD9#tS3(fF5{dF2!hQ0+dI6,nV1>xJ4%"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be an "invalid nickname" error
		assertTrue(
			this.presenter.receivedResponse().nicknameIsInvalid,
			"Registration shouldn't be possible with a nickname which doesn't start with alphanum"
		);
	}

	@Test
	public void requiresMailAddress()
	{
		// given a registration request without mail address
		final var request = new UserRegistrationRequest(
			"nickname",
			null,
			"rM5#eW1?vA2>jU4)uK8!zJ0?cC8\"bX4^"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing mail address" error
		assertTrue(
			this.presenter.receivedResponse().mailAddressIsMissing,
			"Registration shouldn't be possible without mail address"
		);
	}

	@Test
	public void requiresNonBlankMailAddress()
	{
		// given a registration request with a blank mail address
		final var request = new UserRegistrationRequest(
			"nickname",
			"",
			"mI2%yU9(jV8:iR8.xR8>kW2{tQ3\"pQ6;"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing mail address" error
		assertTrue(
			this.presenter.receivedResponse().mailAddressIsMissing,
			"Registration shouldn't be possible without mail address"
		);
	}

	@Test
	public void requiresValidMailAddress()
	{
		// given a registration request without mail address
		final var request = new UserRegistrationRequest(
			"nickname",
			"not a mail address",
			"wF8.yT9>nS1*sP8*nM5(sE1%aW5*lF9)"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be an "invalid nickname" error
		assertTrue(
			this.presenter.receivedResponse().mailAddressIsInvalid,
			"Registration shouldn't be possible without a valid mail address"
		);
	}

	@Test
	public void requiresPassword()
	{
		// given a registration request without password
		final var request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			null
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing password" error
		assertTrue(
			this.presenter.receivedResponse().passwordIsMissing,
			"Registration shouldn't be possible without password"
		);
	}

	@Test
	public void requiresNonBlankPassword()
	{
		// given a registration request with a blank password
		final var request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			""
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing password" error
		assertTrue(
			this.presenter.receivedResponse().passwordIsMissing,
			"Registration shouldn't be possible without password"
		);
	}

	public static Object[][] weakPasswords()
	{
		return new Object[][] {
			{ "NO LOWERCASE 123456#@$~{<" },
			{ "no uppercase 123456#@$~{<" },
			{ "no digits ABCDEF#@$~{<" },
			{ "no symbols ABCDEF123456" },
		};
	}

	@ParameterizedTest
	@MethodSource("weakPasswords")
	public void requiresStrongPassword(final String weakPassword)
	{
		// given a registration request with a weak password
		final var request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			weakPassword
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "weak password" error
		assertTrue(
			this.presenter.receivedResponse().passwordIsTooWeak,
			"Registration shouldn't be possible with a weak password"
		);
	}

	@Test
	public void requiresLongEnoughPassword()
	{
		// given a registration request with a too short password
		final var request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			"aA1#"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "password too short" error
		assertTrue(
			this.presenter.receivedResponse().passwordIsTooShort,
			"Registration shouldn't be possible with a too short password"
		);
	}

	@Test
	public void requiresShortEnoughPassword()
	{
		// given a registration request with a too long password
		final var request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			"aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3 67"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "password too long" error
		assertTrue(
			this.presenter.receivedResponse().passwordIsTooLong,
			"Registration shouldn't be possible with a too long password"
		);
	}

	@Test
	public void requiresAvailableNickname()
	{
		// given a registration request with an already taken nickname
		final var request = new UserRegistrationRequest(
			"taken nickname",
			"foo@bar.org",
			"eT8!uZ3!yY0~fB4#eX1(xL0(eW8!zB7)"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "nickname already taken" error
		assertTrue(
			this.presenter.receivedResponse().nicknameIsAlreadyTaken,
			"Registration shouldn't be possible with an already taken nickname"
		);
	}

	@Test
	public void requiresAvailableMailAddress()
	{
		// given a registration request with an already taken mail address
		final var request = new UserRegistrationRequest(
			"nickname",
			"taken@email.org",
			"yC4{wW1~kV8:dR7^cF4_tO6*uG3+dE5+"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "mail address already taken" error
		assertTrue(
			this.presenter.receivedResponse().mailAddressIsAlreadyTaken,
			"Registration shouldn't be possible with an already taken mail address"
		);
	}

	@Test
	public void registersTheUser()
	{
		// given a registration request with an available nickname, available mail address and a strong password
		final var request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			"xO9$iS6&kZ4!wD9_jM4>qS2{fX5@iP4("
		);

		// when processing the registration
		this.interactor.execute(request, this.presenter);

		// then the account should have been registered
		assertTrue(
			this.registerAccountMock.wasExecuted(),
			"User should be registered if credentials are valid and available"
		);
	}

	@Test
	public void registersTheUserWithTrimmedCredentials()
	{
		// given a registration request with an available nickname, available mail address and a strong password
		final var request = new UserRegistrationRequest(
			"  nick   name ",
			" f o o    @ b a r . o r g ",
			"xO9$iS6&kZ4!wD9_jM4>qS2{fX5@iP4("
		);

		// when processing the registration
		this.interactor.execute(request, this.presenter);

		// then the account should be created with formatted credentials
		assertAll(
			"Formatting credentials",
			() -> assertEquals("nick name", this.registerAccountMock.receivedCommand().nickname()),
			() -> assertEquals("foo@bar.org", this.registerAccountMock.receivedCommand().mailAddress())
		);
	}

	@Test
	public void registersTheUserWithHashedPassword()
	{
		// given a registration request with an available nickname, available mail address and a strong password
		final var request = new UserRegistrationRequest(
			"  nick   name ",
			" f o o    @ b a r . o r g ",
			"oK2;mS8+cV9{xN9&yU3\"jJ8<eH9}gU9:"
		);

		// when processing the registration
		this.interactor.execute(request, this.presenter);

		// then the account should be created with its password hash
		assertEquals(
			"password hash",
			this.registerAccountMock.receivedCommand().hashedPassword(),
			"User should be registered with its password hash"
		);
	}
}
