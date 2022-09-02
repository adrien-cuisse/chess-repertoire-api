
package com.adrien_cuisse.chess_repertoire.domain.entities;

import com.adrien_cuisse.chess_repertoire.domain.value_objects.identity.IIdentity;

public interface IEntity
{
    IIdentity<?> identity();
}
