
package com.adrien_cuisse.chess_repertoire.application.use_cases.user.registration;

public final class UserRegistrationPresenterMock implements IUserRegistrationPresenter
{
	private UserRegistrationResponse response = null;

	public void present(final UserRegistrationResponse response)
	{
		this.response = response;
	}

	public UserRegistrationResponse receivedResponse()
	{
		return this.response;
	}
}
