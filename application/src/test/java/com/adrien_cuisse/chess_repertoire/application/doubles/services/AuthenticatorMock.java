
package com.adrien_cuisse.chess_repertoire.application.doubles.services;

import com.adrien_cuisse.chess_repertoire.application.services.IAuthenticator;
import com.adrien_cuisse.chess_repertoire.domain.entities.user.IUser;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.IIdentity;

import java.util.Optional;

public final class AuthenticatorMock implements IAuthenticator
{
	private final static String MATCHING_TOKEN = "valid token";

	private boolean wasExecuted = false;

	private IUser authenticatedUser = null;

	public Optional<IUser> authenticate(String token)
	{
		this.wasExecuted = true;

		if (token == null || !token.equals("valid token"))
			return Optional.empty();

		this.authenticatedUser = new IUser()
		{
			public IIdentity<String> identity()
			{
				return new IIdentity<String>() {
					@Override
					public String toString() { return "authenticated user identity"; }

					@Override
					public String toNative() { return "authenticated user identity"; }

					@Override
					public boolean equals(IValueObject other) { return false; }
				};
			}
		};

		return Optional.of(this.authenticatedUser);
	}

	public boolean wasExecuted()
	{
		return this.wasExecuted;
	}

	public IUser authenticatedUser()
	{
		return this.authenticatedUser;
	}

	public String bypassingToken()
	{
		return MATCHING_TOKEN;
	}
}
