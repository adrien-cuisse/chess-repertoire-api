
package com.adrien_cuisse.chess_repertoire.application.use_cases.user.registration;

import com.adrien_cuisse.chess_repertoire.application.dto.account.RegisterAccountCommand;
import com.adrien_cuisse.chess_repertoire.application.dto.account.CredentialsDTO;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByMailAddressQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByNicknameQuery;
import com.adrien_cuisse.chess_repertoire.application.services.IPasswordHasher;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid.UuidV4;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.account.mail_address.MailAddress;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.account.nickname.Nickname;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.account.password.HashedPassword;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.account.password.PlainPassword;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class RegisterUserInteractorTest
{
	private RawUserRegistrationPresenter presenter;

	@Mock
	private FindCredentialsByNicknameQuery.IHandler findAccountByNicknameHandlerMock;

	@Mock
	private FindCredentialsByMailAddressQuery.IHandler findAccountByMailAddressHandlerMock;

	@Mock
	private RegisterAccountCommand.IHandler registerAccountHandlerMock;

	private final CredentialsDTO existingAccount = new CredentialsDTO(
		new UuidV4("00000000-0000-4000-0000-000000000000"),
		new Nickname("nickname"),
		new MailAddress("foo@bar.org"),
		new HashedPassword("hashed")
	);

	private RegisterUserInteractor interactor;

	@Mock
	private IPasswordHasher passwordHasherMock;

	@BeforeEach
	public void setup()
	{
		reset(this.findAccountByNicknameHandlerMock);
		reset(this.findAccountByMailAddressHandlerMock);
		reset(this.registerAccountHandlerMock);
		reset(this.passwordHasherMock);

		this.presenter = new RawUserRegistrationPresenter();
		this.interactor = new RegisterUserInteractor(
			this.findAccountByNicknameHandlerMock,
			this.findAccountByMailAddressHandlerMock,
			this.registerAccountHandlerMock,
			this.passwordHasherMock
		);
	}

	@Test
	public void requiresUsername()
	{
		// given a registration request without nickname
		UserRegistrationRequest request = new UserRegistrationRequest(
			null,
			"foo@bar.org",
			"oG8\"aP6&fV3_gG9?oI2;hC2{jP4,vS8."
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing nickname" error
		assertTrue(
			this.presenter.response().nicknameIsMissing,
			"Registration shouldn't be possible without nickname"
		);
	}

	@Test
	public void requiresNonBlankUsername()
	{
		// given a registration request with a blank nickname
		UserRegistrationRequest request = new UserRegistrationRequest(
			"         ",
			"foo@bar.org",
			"rO7{bB7{wL3\"jC4{aC5>bI9@rU2~rA9_"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "nickname too short" error
		assertTrue(
			this.presenter.response().nicknameIsTooShort,
			"Registration shouldn't be possible without nickname"
		);
	}

	@Test
	public void requiresLongEnoughUsername()
	{
		// given a registration request with a too short nickname
		UserRegistrationRequest request = new UserRegistrationRequest(
			"a",
			"foo@bar.org",
			"tU0^eC2&hP8^yB8<oI4&vG5}iI2~zZ2>"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "nickname too short" error
		assertTrue(
			this.presenter.response().nicknameIsTooShort,
			"Registration shouldn't be possible with a too short nickname"
		);
	}

	@Test
	public void requiresShortEnoughUsername()
	{
		// given a registration request with a too long nickname
		UserRegistrationRequest request = new UserRegistrationRequest(
			"0123456789012345678",
			"foo@bar.org",
			"aJ7)bU9~rL5<gM9?jV8*eU5*tD5{jD5("
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "nickname too short" error
		assertTrue(
			this.presenter.response().nicknameIsTooLong,
			"Registration shouldn't be possible with a too long nickname"
		);
	}

	@Test
	public void requiresNicknameWithValidCharacters()
	{
		// given a registration request with a nickname containing illegal characters
		UserRegistrationRequest request = new UserRegistrationRequest(
			"@.",
			"foo@bar.org",
			"iD9#tS3(fF5{dF2!hQ0+dI6,nV1>xJ4%"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "invalid nickname" error
		assertTrue(
			this.presenter.response().nicknameIsInvalid,
			"Registration shouldn't be possible with a nickname containing illegal characters"
		);
	}

	@Test
	public void requiresMailAddress()
	{
		// given a registration request without mail address
		UserRegistrationRequest request = new UserRegistrationRequest(
			"nickname",
			null,
			"rM5#eW1?vA2>jU4)uK8!zJ0?cC8\"bX4^"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing mail address" error
		assertTrue(
			this.presenter.response().mailAddressIsMissing,
			"Registration shouldn't be possible without mail address"
		);
	}

	@Test
	public void requiresValidMailAddress()
	{
		// given a registration request without mail address
		UserRegistrationRequest request = new UserRegistrationRequest(
			"nickname",
			"not a mail address",
			"wF8.yT9>nS1*sP8*nM5(sE1%aW5*lF9)"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "invalid nickname" error
		assertTrue(
			this.presenter.response().mailAddressIsInvalid,
			"Registration shouldn't be possible without a valid mail address"
		);
	}

	@Test
	public void requiresPassword()
	{
		// given a registration request without password
		UserRegistrationRequest request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			null
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "missing password" error
		assertTrue(
			this.presenter.response().passwordIsMissing,
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
		UserRegistrationRequest request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			weakPassword
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "weak password" error
		assertTrue(
			this.presenter.response().passwordIsTooWeak,
			"Registration shouldn't be possible with a weak password"
		);
	}

	@Test
	public void requiresLongEnoughPassword()
	{
		// given a registration request with a too short password
		UserRegistrationRequest request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			"a"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "password too short" error
		assertTrue(
			this.presenter.response().passwordIsTooShort,
			"Registration shouldn't be possible with a too short password"
		);
	}

	@Test
	public void requiresShortEnoughPassword()
	{
		// given a registration request with a too long password
		UserRegistrationRequest request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			"aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3aB#3 67"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "password too long" error
		assertTrue(
			this.presenter.response().passwordIsTooLong,
			"Registration shouldn't be possible with a too long password"
		);
	}

	@Test
	public void requiresAvailableNickname()
	{
		// given a registration request with a nickname
		UserRegistrationRequest request = new UserRegistrationRequest(
			"TakenNickname",
			"foo@bar.org",
			"eT8!uZ3!yY0~fB4#eX1(xL0(eW8!zB7)"
		);
		// and that nickname being already taken
		when(this.findAccountByNicknameHandlerMock.execute(new FindCredentialsByNicknameQuery("TakenNickname")))
			.thenReturn(Optional.of(this.existingAccount));

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "nickname already taken" error
		assertTrue(
			this.presenter.response().nicknameIsAlreadyTaken,
			"Registration shouldn't be possible with an already taken nickname"
		);
	}

	@Test
	public void requiresAvailableMailAddress()
	{
		// given a registration request with a mail address
		UserRegistrationRequest request = new UserRegistrationRequest(
			"nickname",
			"taken@mail.org",
			"yC4{wW1~kV8:dR7^cF4_tO6*uG3+dE5+"
		);
		// and that mail address being already taken
		when(this.findAccountByMailAddressHandlerMock.execute(new FindCredentialsByMailAddressQuery("taken@mail.org")))
			.thenReturn(Optional.of(this.existingAccount));

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then there should be a "mail address already taken" error
		assertTrue(
			this.presenter.response().mailAddressIsAlreadyTaken,
			"Registration shouldn't be possible with an already taken mail address"
		);
	}

	@Test
	public void hashesPassword()
	{
		// given a registration request with an available nickname, available mail address and a strong password
		UserRegistrationRequest request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			"tM2?tZ4)cV7#mN2(mW9<eI7:jX1,gC7}"
		);

		// when trying to process the registration
		this.interactor.execute(request, this.presenter);

		// then the password should have been hashed
		verify(
			this.passwordHasherMock,
			times(1).
				description("Password should be hashed to be put to persistence")
		).hashPassword(any(PlainPassword.class));
	}

	@Test
	public void registersTheUser()
	{
		// given a registration request with an available nickname, available mail address and a strong password
		UserRegistrationRequest request = new UserRegistrationRequest(
			"nickname",
			"foo@bar.org",
			"xO9$iS6&kZ4!wD9_jM4>qS2{fX5@iP4("
		);

		// when processing the registration
		this.interactor.execute(request, this.presenter);

		// then the account should be created
		verify(
			this.registerAccountHandlerMock,
			times(1)
				.description("User should be registered if credentials are valid and available")
		).execute(any(RegisterAccountCommand.class));
	}
}
