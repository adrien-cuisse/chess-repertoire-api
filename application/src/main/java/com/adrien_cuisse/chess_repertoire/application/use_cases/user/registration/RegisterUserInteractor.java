
package com.adrien_cuisse.chess_repertoire.application.use_cases.user.registration;

import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByMailAddressQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByNicknameQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.account.RegisterAccountCommand;
import com.adrien_cuisse.chess_repertoire.application.services.IPasswordHasher;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.mail_address.InvalidMailAddressException;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.mail_address.MailAddress;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.mail_address.NullMailAddressException;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.nickname.*;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password.HashedPassword;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password.PlainPassword;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid.UuidV4;

import java.util.regex.Pattern;

public final class RegisterUserInteractor
{
	private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");

	private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");

	private static final Pattern DIGITS_PATTERN = Pattern.compile("[0-9]");

	// OWASP strong password recommendation
	private static final Pattern SYMBOLS_PATTERN = Pattern.compile("[!~<>,;:_=?*+#.\"&§%°()|\\[\\]\\-$^@/]");

	private final FindCredentialsByNicknameQuery.IHandler findUserByNicknameHandler;

	private final FindCredentialsByMailAddressQuery.IHandler findUserByMailAddressHandler;

	private final RegisterAccountCommand.IHandler registerUserHandler;

	private final IPasswordHasher passwordHasher;

	public RegisterUserInteractor(
		final FindCredentialsByNicknameQuery.IHandler findUserByNicknameHandler,
		final FindCredentialsByMailAddressQuery.IHandler findUserByMailAddressHandler,
		final RegisterAccountCommand.IHandler registerUserHandler,
		final IPasswordHasher passwordHasher
	) {
		this.findUserByNicknameHandler = findUserByNicknameHandler;
		this.findUserByMailAddressHandler = findUserByMailAddressHandler;
		this.registerUserHandler = registerUserHandler;
		this.passwordHasher = passwordHasher;
	}

	public void execute(final UserRegistrationRequest request, final IUserRegistrationPresenter presenter)
	{
		final UserRegistrationResponse response = new UserRegistrationResponse();

		if (credentialsAreValidAndAvailable(request, response))
			hashPasswordAndRegisterUser(request);

		presenter.present(response);
	}

	private boolean credentialsAreValidAndAvailable(
		final UserRegistrationRequest request,
		final UserRegistrationResponse response
	) {
		boolean errorOccured = isNicknameInvalidOrTaken(request, response);
		errorOccured = isMailAddressInvalidOrTaken(request, response) || errorOccured;
		errorOccured = isPasswordInvalid(request, response) || errorOccured;

		return errorOccured == false;
	}

	private boolean isNicknameInvalidOrTaken(
		final UserRegistrationRequest request,
		final UserRegistrationResponse response
	) {
		try {
			new Nickname(request.nickname());
			response.nicknameIsAlreadyTaken = isNicknameAlreadyTaken(request);
		}
		catch (NullNicknameException exception) { response.nicknameIsMissing = true; }
		catch (NicknameTooShortException exception) { response.nicknameIsTooShort = true; }
		catch (NicknameTooLongException exception) { response.nicknameIsTooLong = true; }
		catch (InvalidNicknameException exception) { response.nicknameIsInvalid = true; }

		return response.nicknameIsMissing
			|| response.nicknameIsTooShort
			|| response.nicknameIsTooLong
			|| response.nicknameIsInvalid
			|| response.nicknameIsAlreadyTaken;
	}

	private boolean isNicknameAlreadyTaken(final UserRegistrationRequest request)
	{
		final FindCredentialsByNicknameQuery query = new FindCredentialsByNicknameQuery(request.nickname());
		return this.findUserByNicknameHandler.execute(query).isPresent();
	}

	private boolean isMailAddressInvalidOrTaken(
		final UserRegistrationRequest request,
		final UserRegistrationResponse response
	) {
		try {
			new MailAddress(request.mailAddress());
			response.mailAddressIsAlreadyTaken = isMailAddressAlreadyTaken(request);
		}
		catch (NullMailAddressException exception) { response.mailAddressIsMissing = true; }
		catch (InvalidMailAddressException exception) { response.mailAddressIsInvalid = true; }

		return response.mailAddressIsMissing
			|| response.mailAddressIsInvalid
			|| response.mailAddressIsAlreadyTaken;
	}

	private boolean isMailAddressAlreadyTaken(final UserRegistrationRequest request) {
		final FindCredentialsByMailAddressQuery query = new FindCredentialsByMailAddressQuery(request.mailAddress());
		return this.findUserByMailAddressHandler.execute(query).isPresent();
	}

	private boolean isPasswordInvalid(final UserRegistrationRequest request, final UserRegistrationResponse response)
	{
		final String password = request.password();

		if (password == null)
			response.passwordIsMissing = true;
		else if (password.length() < 6)
			response.passwordIsTooShort = true;
		else if (password.length() > 64)
			response.passwordIsTooLong = true;
		else if (containsLowercase(password) == false)
			response.passwordIsTooWeak = true;
		else if (containsUpperCase(password) == false)
			response.passwordIsTooWeak = true;
		else if (containsDigits(password) == false)
			response.passwordIsTooWeak = true;
		else if (containsSymbols(password) == false)
			response.passwordIsTooWeak = true;

		return response.passwordIsMissing
			|| response.passwordIsTooWeak
			|| response.passwordIsTooShort
			|| response.passwordIsTooLong;
	}

	private boolean containsLowercase(final String password)
	{
		return containsAnyCharacter(password, LOWERCASE_PATTERN);
	}

	private boolean containsUpperCase(final String password)
	{
		return containsAnyCharacter(password, UPPERCASE_PATTERN);
	}

	private boolean containsDigits(final String password)
	{
		return containsAnyCharacter(password, DIGITS_PATTERN);
	}

	private boolean containsSymbols(final String password)
	{
		return containsAnyCharacter(password, SYMBOLS_PATTERN);
	}

	private boolean containsAnyCharacter(final String password, final Pattern regex)
	{
		return regex.matcher(password).find();
	}

	private void hashPasswordAndRegisterUser(final UserRegistrationRequest request)
	{
		final PlainPassword plainPassword = new PlainPassword(request.password());
		final HashedPassword hashedPassword = this.passwordHasher.hashPassword(plainPassword);

		final RegisterAccountCommand command = new RegisterAccountCommand(
			new UuidV4(),
			new Nickname(request.nickname()),
			new MailAddress(request.mailAddress()),
			hashedPassword
		);
		this.registerUserHandler.execute(command);
	}
}
