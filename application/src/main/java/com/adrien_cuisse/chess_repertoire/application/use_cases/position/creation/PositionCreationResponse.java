
package com.adrien_cuisse.chess_repertoire.application.use_cases.position.creation;

public final class PositionCreationResponse
{
	public boolean authenticationTokenIsMissing = false;
	public boolean authenticationTokenIsInvalid = false;

	public boolean nameIsMissing = false;
	public boolean nameIsTooLong = false;
	public boolean nameIsAlreadyUsed = false;

	public boolean fenIsMissing = false;
	public boolean fenIsInvalid = false;
	public boolean fenIsAlreadyUsed = false;
}
