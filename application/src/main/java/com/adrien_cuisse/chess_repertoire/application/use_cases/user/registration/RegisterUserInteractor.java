
package com.adrien_cuisse.chess_repertoire.application.use_cases.user.registration;

import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByMailAddressQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindCredentialsByNicknameQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.account.RegisterAccountCommand;
import com.adrien_cuisse.chess_repertoire.application.services.IPasswordHasher;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.mail_address.MailAddress;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.nickname.Nickname;
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

	private static final Pattern ALPHANUM_START_PATTERN = Pattern.compile("^[a-zA-Z0-9]");

	private static final Pattern VALID_NICKNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9]([a-zA-Z0-9 \\-_]*)$");

	private final static Pattern MAIL_ADDRESS_PATTERN = Pattern.compile("^(.+)@(\\S+)$");

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
		boolean errorOccured = nicknameIsInvalidOrTaken(request, response);
		errorOccured = mailAddressIsInvalidOrTaken(request, response) || errorOccured;
		errorOccured = passwordIsInvalid(request, response) || errorOccured;

		return errorOccured == false;
	}

	private boolean nicknameIsInvalidOrTaken(
		final UserRegistrationRequest request,
		final UserRegistrationResponse response
	) {
		if (request.nickname() == null)
			return response.nicknameIsMissing = true;

		final String nickname = request.nickname().replace("\\s+", "\\s").trim();

		if (nickname.equals(""))
			response.nicknameIsMissing = true;
		else if (nickname.length() < 2)
			response.nicknameIsTooShort = true;
		else if (nickname.length() > 16)
			response.nicknameIsTooLong = true;
		else if (nicknameContainsInvalidCharacters(nickname))
			response.nicknameIsInvalid = true;
		else if (startsWithAlphanum(nickname) == false)
			response.nicknameIsInvalid = true;
		else if (nicknameIsAlreadyTaken(request))
			response.nicknameIsAlreadyTaken = true;

		return response.nicknameIsMissing
			|| response.nicknameIsTooShort
			|| response.nicknameIsTooLong
			|| response.nicknameIsInvalid
			|| response.nicknameIsAlreadyTaken;
	}

	private boolean nicknameIsAlreadyTaken(final UserRegistrationRequest request)
	{
		final FindCredentialsByNicknameQuery query = new FindCredentialsByNicknameQuery(request.nickname());
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
			response.mailAddressIsMissing = true;
		else if (mailAddressHasInvalidFormat(mailAddress))
			response.mailAddressIsInvalid = true;
		else if (mailAddressIsAlreadyTaken(request))
			response.mailAddressIsAlreadyTaken = true;

		return response.mailAddressIsMissing
			|| response.mailAddressIsInvalid
			|| response.mailAddressIsAlreadyTaken;
	}

	private boolean mailAddressIsAlreadyTaken(final UserRegistrationRequest request) {
		final FindCredentialsByMailAddressQuery query = new FindCredentialsByMailAddressQuery(request.mailAddress());
		return this.findUserByMailAddressHandler.execute(query).isPresent();
	}

	private boolean passwordIsInvalid(final UserRegistrationRequest request, final UserRegistrationResponse response)
	{
		final String password = request.password();

		if (password == null)
			response.passwordIsMissing = true;
		else if (password.equals(""))
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
		return regexMatches(nickname, VALID_NICKNAME_PATTERN) == false;
	}

	private boolean startsWithAlphanum(final String text)
	{
		return regexMatches(text, ALPHANUM_START_PATTERN);
	}

	private boolean mailAddressHasInvalidFormat(final String mailAddress)
	{
		return regexMatches(mailAddress, MAIL_ADDRESS_PATTERN) == false;
	}

	private boolean regexMatches(final String text, final Pattern regex)
	{
		return regex.matcher(text).find();
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
