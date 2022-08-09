
package com.adrien_cuisse.chess_repertoire.domain.value_objects.credentials.password;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.IValueObject;

public interface IPassword extends IValueObject
{
    boolean isHashed();
}
