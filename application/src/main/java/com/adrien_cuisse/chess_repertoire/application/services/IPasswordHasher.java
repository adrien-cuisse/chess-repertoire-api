
package com.adrien_cuisse.chess_repertoire.application.services;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password.HashedPassword;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password.PlainPassword;

public interface IPasswordHasher
{
	HashedPassword hashPassword(final PlainPassword password);
}
