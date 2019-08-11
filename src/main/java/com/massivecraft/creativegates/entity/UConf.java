package com.massivecraft.creativegates.entity;

import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class UConf extends Entity<UConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	public static UConf get(Object oid)
	{
		return UConfColls.get().get2(oid);
	}
	
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	@Override
	public UConf load(UConf that)
	{
		this.setEnabled(that.isEnabled());
		this.setUsingWater(that.isUsingWater());
		this.setPigmanPortalSpawnAllowed(that.isPigmanPortalSpawnAllowed());
		this.setMaxarea(that.getMaxarea());
		this.setBlocksrequired(that.getBlocksrequired());
		this.setRemovingCreateToolName(that.isRemovingCreateToolName());
		this.setRemovingCreateToolItem(that.isRemovingCreateToolItem());
		this.setMaterialCreate(that.getMaterialCreate());
		this.setMaterialInspect(that.getMaterialInspect());
		this.setMaterialSecret(that.getMaterialSecret());
		this.setMaterialMode(that.getMaterialMode());
		this.setLimitranks(that.getLimitranks());
		this.setLimitedranks(that.getLimitedranks());
		this.setItemRandom(that.getItemRandom());
		this.setMaterialRandom(that.getMaterialRandom());
		this.setRandomRadius(that.getRandomRadius());
		this.setServerName(that.getServerName());
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private boolean enabled = true;
	public boolean isEnabled() { return this.enabled; }
	public void setEnabled(boolean enabled)
	{
		this.changed(this.enabled, enabled);
		this.enabled = enabled;
	}
	
	private boolean usingWater = false;
	public boolean isUsingWater() { return this.usingWater; }
	public void setUsingWater(boolean usingWater)
	{
		this.changed(this.usingWater, usingWater);
		this.usingWater = usingWater;
	}
	
	private boolean pigmanPortalSpawnAllowed = true;
	public boolean isPigmanPortalSpawnAllowed() { return this.pigmanPortalSpawnAllowed; }
	public void setPigmanPortalSpawnAllowed(boolean pigmanPortalSpawnAllowed)
	{
		this.changed(this.pigmanPortalSpawnAllowed, pigmanPortalSpawnAllowed);
		this.pigmanPortalSpawnAllowed = pigmanPortalSpawnAllowed;
	}

	private int maxarea = 200;
	public int getMaxarea() { return this.maxarea; }
	public void setMaxarea(int maxarea)
	{
		this.changed(this.maxarea, maxarea);
		this.maxarea = maxarea;
	}
	
	private Map<Material, Integer> blocksrequired = MUtil.map(
		Material.EMERALD_BLOCK, 2
	);
	public Map<Material, Integer> getBlocksrequired() { return new HashMap<>(this.blocksrequired); }
	public void setBlocksrequired(Map<Material, Integer> blocksrequired)
	{
		this.changed(this.blocksrequired, blocksrequired);
		this.blocksrequired = new HashMap<>(blocksrequired);
	}
	
	private boolean removingCreateToolName = true;
	public boolean isRemovingCreateToolName() { return this.removingCreateToolName; }
	public void setRemovingCreateToolName(boolean removingCreateToolName)
	{
		this.changed(this.removingCreateToolName, removingCreateToolName);
		this.removingCreateToolName = removingCreateToolName;
	}
	
	private boolean removingCreateToolItem = false;
	public boolean isRemovingCreateToolItem() { return this.removingCreateToolItem; }
	public void setRemovingCreateToolItem(boolean removingCreateToolItem)
	{
		this.changed(this.removingCreateToolItem, removingCreateToolItem);
		this.removingCreateToolItem = removingCreateToolItem;
	}
	private Material materialCreate = Material.CLOCK;
	public Material getMaterialCreate() { return this.materialCreate; }
	public void setMaterialCreate(Material materialCreate)
	{
		this.changed(this.materialCreate, materialCreate);
		this.materialCreate = materialCreate;
	}
	
	private Material materialInspect = Material.BLAZE_POWDER;
	public Material getMaterialInspect() { return this.materialInspect; }
	public void setMaterialInspect(Material materialInspect)
	{
		this.changed(this.materialInspect, materialInspect);
		this.materialInspect = materialInspect;
	}
	
	private Material materialSecret = Material.MAGMA_CREAM;
	public Material getMaterialSecret() { return this.materialSecret; }
	public void setMaterialSecret(Material materialSecret)
	{
		this.changed(this.materialSecret, materialSecret);
		this.materialSecret = materialSecret;
	}
	
	private Material materialMode = Material.BLAZE_ROD;
	public Material getMaterialMode() { return this.materialMode; }
	public void setMaterialMode(Material materialMode)
	{
		this.changed(this.materialMode, materialMode);
		this.materialMode = materialMode;
	}
	
	private boolean limitranks = true;
	public boolean getLimitranks() { return this.limitranks; }
	public void setLimitranks(boolean limitranks) { this.limitranks = limitranks; this.changed(); }

	private Map<String, Integer> limitedranks = MUtil.map(
		"rank0", 2
	);
	public Map<String, Integer> getLimitedranks() { return new HashMap<>(this.limitedranks); }
	public void setLimitedranks(Map<String, Integer> limitedranks) { this.limitedranks = new HashMap<>(limitedranks); this.changed(); }

	private Material itemRandom = Material.FIRE_CHARGE;
	public Material getItemRandom() { return this.itemRandom; }
	public void setItemRandom(Material itemRandom) { this.itemRandom = itemRandom; this.changed(); }

	private Material randomPortalMaterial = Material.END_PORTAL;
	public Material getMaterialRandom() { return this.randomPortalMaterial; }
	public void setMaterialRandom(Material randomPortalMaterial) { this.randomPortalMaterial = randomPortalMaterial; this.changed(); }

	private int randomPortalRadius = 10000;
	public int getRandomRadius() { return this.randomPortalRadius; }
	public void setRandomRadius(int randomPortalRadius) { this.randomPortalRadius = randomPortalRadius; this.changed(); }

	private String serverName = "";
	public String getServerName() { return this.serverName; }
	public void setServerName(String serverName) { this.serverName = serverName; this.changed(); }
}
