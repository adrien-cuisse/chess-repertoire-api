
package com.adrien_cuisse.chess_repertoire.application.use_cases.user.registration;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.password.HashedPassword;
import com.adrien_cuisse.chess_repertoire.domain.value_objects.password.PlainPassword;

public interface IPasswordHasher
{
	public HashedPassword hashPassword(final PlainPassword password);
}
