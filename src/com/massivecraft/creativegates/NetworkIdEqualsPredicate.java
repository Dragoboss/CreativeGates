package com.massivecraft.creativegates;

import com.massivecraft.creativegates.entity.UGate;
import com.massivecraft.massivecore.predicate.Predicate;

import java.util.function.Predicate;

									

public class NetworkIdEqualsPredicate implements Predicate<UGate>
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String networkId;
	public String getNetworkId() { return this.networkId; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public NetworkIdEqualsPredicate(String networkId)
	{
		this.networkId = networkId;
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean test(UGate ugate)
	{
		return ugate.getNetworkId().equalsIgnoreCase(this.networkId);
	}
	
}