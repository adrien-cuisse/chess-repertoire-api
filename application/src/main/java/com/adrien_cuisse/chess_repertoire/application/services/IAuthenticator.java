
package com.adrien_cuisse.chess_repertoire.application.services;

import com.adrien_cuisse.chess_repertoire.domain.entities.user.IUser;

import java.util.Optional;

public interface IAuthenticator
{
	Optional<IUser> authenticate(final String token);
}
