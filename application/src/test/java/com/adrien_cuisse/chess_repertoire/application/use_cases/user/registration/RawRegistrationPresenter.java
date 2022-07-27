
package com.adrien_cuisse.chess_repertoire.application.use_cases.user.registration;

public final class RawRegistrationPresenter implements IRegistrationPresenter
{
	private UserRegistrationResponse response;

	public void present(final UserRegistrationResponse response)
	{
		this.response = response;
	}

	public UserRegistrationResponse response()
	{
		return this.response;
	}
}
