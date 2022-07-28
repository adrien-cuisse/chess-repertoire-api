
package com.adrien_cuisse.chess_repertoire.application.use_cases.user.registration;

import com.adrien_cuisse.chess_repertoire.application.dto.account.RegisterAccountCommand;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindAccountByMailAddressQuery;
import com.adrien_cuisse.chess_repertoire.application.dto.account.FindAccountByNicknameQuery;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.uuid.UuidV4;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.mail_address.InvalidMailAddressException;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.mail_address.MailAddress;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.mail_address.NullMailAddressException;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.nickname.*;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.password.*;

public final class UserRegistrationInteractor
{
	private final FindAccountByNicknameQuery.IHandler findUserByNicknameHandler;

	private final FindAccountByMailAddressQuery.IHandler findUserByMailAddressHandler;

	private final RegisterAccountCommand.IHandler registerUserHandler;

	private final IPasswordHasher passwordHasher;

	public UserRegistrationInteractor(
		final FindAccountByNicknameQuery.IHandler findUserByNicknameHandler,
		final FindAccountByMailAddressQuery.IHandler findUserByMailAddressHandler,
		final RegisterAccountCommand.IHandler registerUserHandler,
		final IPasswordHasher passwordHasher
	) {
		this.findUserByNicknameHandler = findUserByNicknameHandler;
		this.findUserByMailAddressHandler = findUserByMailAddressHandler;
		this.registerUserHandler = registerUserHandler;
		this.passwordHasher = passwordHasher;
	}

	public void execute(final UserRegistrationRequest request, final IRegistrationPresenter presenter)
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
		boolean errorOccured = isUserNameInvalidOrTaken(request, response);
		errorOccured = isMailAddressInvalidOrTaken(request, response) || errorOccured;
		errorOccured = isPasswordInvalid(request, response) || errorOccured;

		return errorOccured == false;
	}

	private boolean isUserNameInvalidOrTaken(
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
		final FindAccountByNicknameQuery query = new FindAccountByNicknameQuery(request.nickname());
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
		final FindAccountByMailAddressQuery query = new FindAccountByMailAddressQuery(request.mailAddress());
		return this.findUserByMailAddressHandler.execute(query).isPresent();
	}

	private boolean isPasswordInvalid(final UserRegistrationRequest request, final UserRegistrationResponse response)
	{
		try { new PlainPassword(request.password()); }
		catch (NullPasswordException exception) { response.passwordIsMissing = true; }
		catch (PasswordTooShortException exception) { response.passwordIsTooShort = true; }
		catch (PasswordTooLongException exception) { response.passwordIsTooLong = true; }
		catch (PasswordWithoutLowercaseException exception) { response.passwordIsTooWeak = true; }
		catch (PasswordWithoutUppercaseException exception) { response.passwordIsTooWeak = true; }
		catch (PasswordWithoutDigitsException exception) { response.passwordIsTooWeak = true; }
		catch (PasswordWithoutSymbolsException exception) { response.passwordIsTooWeak = true; }

		return response.passwordIsMissing
			|| response.passwordIsTooWeak
			|| response.passwordIsTooShort
			|| response.passwordIsTooLong;
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
