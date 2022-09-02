
package com.adrien_cuisse.chess_repertoire.application.use_cases.user.registration;

import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByMailAddressQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByNicknameQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.account.RegisterAccountCommand;
import com.adrien_cuisse.chess_repertoire.application.services.IPasswordHasher;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid.UuidV4;

import java.util.regex.Pattern;

public final class RegisterUserInteractor
{
	private static final Pattern LOWERCASE_PATTERN = Pattern.compile("[a-z]");

	private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");

	private static final Pattern DIGITS_PATTERN = Pattern.compile("[0-9]");

	// OWASP strong password recommendation
	private static final Pattern SYMBOLS_PATTERN = Pattern.compile("[!~<>,;:_=?*+#.\"&§%°()|\\[\\]\\-$^@/]");

	private static final Pattern ALPHANUM_START_PATTERN = Pattern.compile("^[a-zA-Z0-9]");

	private static final Pattern VALID_NICKNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]([a-zA-Z0-9 \\-_]*)$");

	private static final Pattern MAIL_ADDRESS_PATTERN = Pattern.compile("^(.+)@(\\S+)$");

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
		final var response = new UserRegistrationResponse();

		if (credentialsAreValidAndAvailable(request, response))
			hashPasswordAndRegisterUser(request);

		presenter.present(response);
	}

	private boolean credentialsAreValidAndAvailable(
		final UserRegistrationRequest request,
		final UserRegistrationResponse response
	) {
		boolean errorOccured = nicknameIsInvalidOrTaken(request, response);
		errorOccured |= mailAddressIsInvalidOrTaken(request, response);
		errorOccured |= passwordIsInvalid(request, response);

		return !errorOccured;
	}

	private boolean nicknameIsInvalidOrTaken(
		final UserRegistrationRequest request,
		final UserRegistrationResponse response
	) {
		if (request.nickname() == null)
			return response.nicknameIsMissing = true;

		final String nickname = request.nickname().replace("\\s+", "\\s").trim();

		if (nickname.equals(""))
			return response.nicknameIsMissing = true;
		if (nickname.length() < 2)
			return response.nicknameIsTooShort = true;
		if (nickname.length() > 16)
			return response.nicknameIsTooLong = true;
		if (nicknameContainsInvalidCharacters(nickname))
			return response.nicknameIsInvalid = true;
		if (!startsWithAlphanum(nickname))
			return response.nicknameIsInvalid = true;
		if (nicknameIsAlreadyTaken(request))
			return response.nicknameIsAlreadyTaken = true;

		return false;
	}

	private boolean nicknameIsAlreadyTaken(final UserRegistrationRequest request)
	{
		final var query = new FindCredentialsByNicknameQuery(request.nickname());
		return this.findUserByNicknameHandler.execute(query).isPresent();
	}

	private boolean mailAddressIsInvalidOrTaken(
		final UserRegistrationRequest request,
		final UserRegistrationResponse response
	) {
		if (request.mailAddress() == null)
			return response.mailAddressIsMissing = true;

		final String mailAddress = request.mailAddress().replace(" ", "");

		if (mailAddress.equals(""))
			return response.mailAddressIsMissing = true;
		if (mailAddressHasInvalidFormat(mailAddress))
			return response.mailAddressIsInvalid = true;
		if (mailAddressIsAlreadyTaken(request))
			return response.mailAddressIsAlreadyTaken = true;

		return false;
	}

	private boolean mailAddressIsAlreadyTaken(final UserRegistrationRequest request) {
		final var query = new FindCredentialsByMailAddressQuery(request.mailAddress());
		return this.findUserByMailAddressHandler.execute(query).isPresent();
	}

	private boolean passwordIsInvalid(final UserRegistrationRequest request, final UserRegistrationResponse response)
	{
		final String password = request.password();

		if (password == null)
			return response.passwordIsMissing = true;
		if (password.equals(""))
			return response.passwordIsMissing = true;
		if (password.length() < 6)
			return response.passwordIsTooShort = true;
		if (password.length() > 64)
			return response.passwordIsTooLong = true;
		if (!containsLowercase(password))
			return response.passwordIsTooWeak = true;
		if (!containsUpperCase(password))
			return response.passwordIsTooWeak = true;
		if (!containsDigits(password))
			return response.passwordIsTooWeak = true;
		if (!containsSymbols(password))
			return response.passwordIsTooWeak = true;

		return false;
	}

	private boolean containsLowercase(final String text)
	{
		return regexMatches(text, LOWERCASE_PATTERN);
	}

	private boolean containsUpperCase(final String text)
	{
		return regexMatches(text, UPPERCASE_PATTERN);
	}

	private boolean containsDigits(final String text)
	{
		return regexMatches(text, DIGITS_PATTERN);
	}

	private boolean containsSymbols(final String text)
	{
		return regexMatches(text, SYMBOLS_PATTERN);
	}

	private boolean nicknameContainsInvalidCharacters(final String nickname)
	{
		return !regexMatches(nickname, VALID_NICKNAME_PATTERN);
	}

	private boolean startsWithAlphanum(final String text)
	{
		return regexMatches(text, ALPHANUM_START_PATTERN);
	}

	private boolean mailAddressHasInvalidFormat(final String mailAddress)
	{
		return !regexMatches(mailAddress, MAIL_ADDRESS_PATTERN);
	}

	private boolean regexMatches(final String text, final Pattern regex)
	{
		return regex.matcher(text).find();
	}

	private void hashPasswordAndRegisterUser(final UserRegistrationRequest request)
	{
		final String hashedPassword = this.passwordHasher.hashPassword(request.password());

		final var command = new RegisterAccountCommand(
			new UuidV4().toString(),
			request.nickname().replaceAll("\\s+", " ").trim(),
			request.mailAddress().replaceAll("\\s", "").trim(),
			hashedPassword
		);
		this.registerUserHandler.execute(command);
	}
}
