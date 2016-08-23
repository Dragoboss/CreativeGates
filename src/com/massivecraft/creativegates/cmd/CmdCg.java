package com.massivecraft.creativegates.cmd;

import java.util.List;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.creativegates.Perm;
import com.massivecraft.creativegates.entity.MConf;
import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;

public class CmdCg extends MassiveCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdCg i = new CmdCg();
	public static CmdCg get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdCgWorld cmdCgWorld = new CmdCgWorld();
	public MassiveCommandVersion cmdCgVersion = new MassiveCommandVersion(CreativeGates.get()).setAliases("v", "version").addRequirements(RequirementHasPerm.get(Perm.CG_VERSION));
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdCg()
	{
		// Children
		this.addChild(this.cmdCgWorld);
		this.addChild(this.cmdCgVersion);
		
		// Requirements
		this.addRequirements(RequirementHasPerm.get(Perm.CG));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesCg;
	}
	
}
