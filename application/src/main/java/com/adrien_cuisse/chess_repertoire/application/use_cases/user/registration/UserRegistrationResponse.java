
package com.adrien_cuisse.chess_repertoire.application.use_cases.user.registration;

public final class UserRegistrationResponse
{
	public boolean nicknameIsMissing = false;
	public boolean nicknameIsTooShort = false;
	public boolean nicknameIsTooLong = false;
	public boolean nicknameIsInvalid = false;
	public boolean nicknameIsAlreadyTaken = false;

	public boolean mailAddressIsMissing = false;
	public boolean mailAddressIsInvalid = false;
	public boolean mailAddressIsAlreadyTaken = false;

	public boolean passwordIsMissing = false;
	public boolean passwordIsTooShort = false;
	public boolean passwordIsTooLong = false;
	public boolean passwordIsTooWeak = false;
}
