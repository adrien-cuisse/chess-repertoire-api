
package com.adrien_cuisse.chess_repertoire.application.use_cases.position.creation;

public final class PositionCreationPresenterMock implements IPositionCreationPresenter
{
	private PositionCreationResponse response;

	@Override
	public void present(final PositionCreationResponse response)
	{
		this.response = response;
	}

	public PositionCreationResponse receivedResponse()
	{
		return this.response;
	}
}
