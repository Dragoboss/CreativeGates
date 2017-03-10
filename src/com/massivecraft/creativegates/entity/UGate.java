package com.massivecraft.creativegates.entity;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.massivecraft.creativegates.CreativeGates;
import com.massivecraft.massivecore.mixin.MixinTeleport;
import com.massivecraft.massivecore.mixin.TeleporterException;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.teleport.Destination;
import com.massivecraft.massivecore.teleport.DestinationSimple;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.SmokeUtil;
import com.massivecraft.massivecore.util.Txt;
import org.bukkit.World.Environment;

public class UGate extends Entity<UGate> {
    public List<String> damageDisabled = new ArrayList<>();
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //
    public static UGate get(Object oid) {
        return UGateColls.get().get2(oid);
    }
    UConf uconf = new UConf();

    // -------------------------------------------- //
    // OVERRIDE: ENTITY
    // -------------------------------------------- //
    @Override
    public UGate load(UGate that) {
        this.setCreatorId(that.getCreatorId());
        this.setCreatedMillis(that.getCreatedMillis());
        this.setUsedMillis(that.getUsedMillis());
        this.setNetworkId(that.getNetworkId());
        this.setRestricted(that.isRestricted());
        this.setEnterEnabled(that.isEnterEnabled());
        this.setExitEnabled(that.isExitEnabled());
        this.setExit(that.getExit());
        this.setCoords(that.getCoords());
        this.setType(that.getType());

        return this;
    }

    @Override
    public void postAttach(String id) {
        if (this.getExit() == null) return;
        CreativeGates.get().getIndex().add(this);
    }

    @Override
    public void postDetach(String id) {
        if (this.getExit() == null) return;
        CreativeGates.get().getIndex().remove(this);
    }

    @Override
    public UGateColl getColl() {
        return (UGateColl) super.getColl();
    }
	
    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //
    private String creatorId = null;
    public String getCreatorId() { return this.creatorId; }
    public void setCreatorId(String creatorId) { this.creatorId = creatorId; this.changed(); }

    private long createdMillis = System.currentTimeMillis();
    public long getCreatedMillis() { return this.createdMillis; }
    public void setCreatedMillis(long createdMillis) { this.createdMillis = createdMillis; this.changed(); }

    private long usedMillis = 0;
    public long getUsedMillis() { return this.usedMillis; }
    public void setUsedMillis(long usedMillis) { this.usedMillis = usedMillis; this.changed(); }

    private String networkId = null;
    public String getNetworkId() { return this.networkId; }
    public void setNetworkId(String networkId) { this.networkId = networkId; this.changed(); }

    private boolean restricted = false;
    public boolean isRestricted() { return this.restricted; }
    public void setRestricted(boolean restricted) { this.restricted = restricted; this.changed(); }

    private boolean enterEnabled = true;
    public boolean isEnterEnabled() { return this.enterEnabled; }
    public void setEnterEnabled(boolean enterEnabled) { this.enterEnabled = enterEnabled; this.changed(); }

    private boolean exitEnabled = true;
    public boolean isExitEnabled() { return this.exitEnabled; }
    public void setExitEnabled(boolean exitEnabled) { this.exitEnabled = exitEnabled; this.changed(); }

    private PS exit = null;
    public PS getExit() { return this.exit; }
    public void setExit(PS exit) { this.exit = exit; this.changed(); }

    private Set<PS> coords = new TreeSet<PS>();
    public Set<PS> getCoords() { return Collections.unmodifiableSet(this.coords);}
    public void setCoords(Collection<PS> coords) {
        if (this.attached()) CreativeGates.get().getIndex().remove(this);
        this.coords = new TreeSet<PS>(coords);
        if (this.attached()) CreativeGates.get().getIndex().add(this);
        this.changed();
    }

    private String portalType = "normal";
    public String getType() { return this.portalType; }
    public void setType(String portalType) { this.portalType = portalType; this.changed(); }

    // -------------------------------------------- //
    // ASSORTED
    // -------------------------------------------- //
    public boolean isCreator(CommandSender sender) {
        String senderId = IdUtil.getId(sender);
        if (senderId == null) return false;
        return senderId.equalsIgnoreCase(this.creatorId);
    }

    public void destroy() {
        this.empty();
        this.detach();
        this.fxKitDestroy(null);
    }

    public void toggleMode() {
        boolean enter = this.isEnterEnabled();
        boolean exit = this.isExitEnabled();

        if (enter == false && exit == false) {
            this.setEnterEnabled(true);
            this.setExitEnabled(false);
        } else if (enter == true && exit == false) {
            this.setEnterEnabled(false);
            this.setExitEnabled(true);
        } else if (enter == false && exit == true) {
            this.setEnterEnabled(true);
            this.setExitEnabled(true);
        } else if (enter == true && exit == true) {
            this.setEnterEnabled(false);
            this.setExitEnabled(false);
        }
    }
	
    // -------------------------------------------- //
    // TRANSPORT
    // -------------------------------------------- //

    public void transport(Player player)
    {
            List<UGate> gateChain = this.getGateChain();

            String message = null;

            if (this.getType().equals("random")) {
                if (randomTransport(player, uconf.getRandomRadius())) {
                    damageDisabled.remove(player.getName());
                }                 
            } else {
                for (UGate ugate : gateChain) {
                    if (!(ugate.isExitEnabled())) continue;
                    PS destinationPs = ugate.getExit();
                    String destinationDesc = (MConf.get().teleportationMessageActive ? "the portal destination" : "");
                    Destination destination = new DestinationSimple(destinationPs, destinationDesc);

                    try
                    {
                        MixinTeleport.get().teleport(player, destination, 0);
                        this.setUsedMillis(System.currentTimeMillis());
                        this.fxKitUse(player);
                        return;
                    }
                    catch (TeleporterException e)
                    {
                            player.sendMessage(e.getMessage());
                    }
                }

                message = Txt.parse("<i>This gate does not seem to lead anywhere.");
                player.sendMessage(message);
            }
    }

    public boolean randomTransport(Player player, double radius){
        Block randBlock = getRandBlock(player, radius);

        Block highest = randBlock.getWorld().getHighestBlockAt(randBlock.getLocation());
        Location randLoc = highest.getLocation();
        ArrayList<Material> dangerList = Lists.newArrayList(Material.LAVA, Material.CACTUS, 
                Material.TNT, Material.WATER, Material.STATIONARY_LAVA, 
                Material.STATIONARY_WATER, Material.AIR, Material.CARPET);
        if(!dangerList.contains(highest.getRelative(BlockFace.DOWN).getType())) {
            damageDisabled.add(player.getName());
            player.teleport(randLoc.add(0.5, 0, 0.5));
            this.setUsedMillis(System.currentTimeMillis());
            this.fxKitUse(player);
            String serverName = uconf.getServerName();
            String message = Txt.parse("<i>Teleporting to random &d" + serverName + " &ecoordinates&e.");
            player.sendMessage(message);
            return true;
        } else {
            if (randomTransport(player, radius)) {
                damageDisabled.remove(player.getName());
            }
        }
        return false;
    }
    public Block getRandBlock(Player player, double radius) {
        double randomX = -radius +( Math.random() * (radius * 2));
        double randomZ = -radius + ( Math.random() * (radius * 2));
        Block randBlock = player.getWorld().getHighestBlockAt((int)randomX, (int)randomZ).getRelative(BlockFace.UP, 2);
        return randBlock;
    }
    public List<UGate> getGateChain() {
        List<UGate> ret = new ArrayList<UGate>();

        List<UGate> rawchain = this.getColl().getGateChain(this.getNetworkId());
        int myIndex = rawchain.indexOf(this);

        // Add what is after me
        ret.addAll(rawchain.subList(myIndex+1, rawchain.size()));

        // Add what is before me
        ret.addAll(rawchain.subList(0, myIndex));

        return ret;
    }

    // -------------------------------------------- //
    // CONTENT
    // -------------------------------------------- //
    
    // These blocks are sorted since the coords are sorted
    public List<Block> getBlocks() {
        List<Block> ret = new ArrayList<Block>();

        World world = null;
        try {
            world = this.getExit().asBukkitWorld(true);
        } catch (IllegalStateException e) {
            return null;
        }

        for (PS coord : this.getCoords()) {
            Block block = world.getBlockAt(coord.getBlockX(), coord.getBlockY(), coord.getBlockZ());
            ret.add(block);
        }

        return ret;
    }

    public Block getCenterBlock() {
        List<Block> blocks = this.getBlocks();
        if (blocks == null) return null;

        return blocks.get(blocks.size() / 2);
    }

    public boolean isIntact() {
        List<Block> blocks = this.getBlocks();
        if (blocks == null) return true;

        for (Block block : blocks) {
            if (CreativeGates.isVoid(block)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    public void setContent(Material material) {
        List<Block> blocks = this.getBlocks();
        if (blocks == null) return;
        byte data = 0;

        // Orientation check
        if (material == Material.PORTAL) {
            Block origin = blocks.get(0);
            Block blockSouth = origin.getRelative(BlockFace.SOUTH);
            Block blockNorth = origin.getRelative(BlockFace.NORTH);

            if (blocks.contains(blockNorth) || blocks.contains(blockSouth)) {
                data = 2;
            }
        }

        for (Block block : blocks) {
            Material blockMaterial = block.getType();
            if (blockMaterial != Material.PORTAL && blockMaterial != Material.STATIONARY_WATER && blockMaterial != Material.WATER && ! CreativeGates.isVoid(blockMaterial)) continue;
            block.setType(material);

            // Apply orientation
            if (material != Material.PORTAL) continue;
            block.setData(data);
        }
    }

    public void fill() {
        UConf uconf = UConf.get(this.getExit());
        if ("random".equals(this.getType())){
            CreativeGates.get().setFilling(true);
            this.setContent(uconf.getMaterialRandom());
            CreativeGates.get().setFilling(false);
        } else {
            CreativeGates.get().setFilling(true);
            Environment env = this.getCenterBlock().getWorld().getEnvironment();
            if (env == Environment.NETHER) {
                this.setContent(uconf.isUsingWater() ? Material.STATIONARY_LAVA : Material.PORTAL);
            } else { this.setContent(uconf.isUsingWater() ? Material.STATIONARY_WATER : Material.PORTAL); }
            CreativeGates.get().setFilling(false);
        }
    }

    public void empty() {
        this.setContent(Material.AIR);
    }

    // -------------------------------------------- //
    // FX KIT
    // -------------------------------------------- //
    public void fxKitCreate(Player player) {
        //this.fxSmoke();
        this.fxShootSound();
    }

    public void fxKitUse(Player player) {
        //this.fxEnder();
        if (!MConf.get().teleportationSoundActive) return;
        this.fxShootSound();
    }

    public void fxKitDestroy(Player player) {
        this.fxExplode();
    }

    // -------------------------------------------- //
    // FX SINGLE
    // -------------------------------------------- //
    public void fxSmoke() {
        List<Block> blocks = this.getBlocks();
        if (blocks == null) return;
        for (Block block : blocks) {
            SmokeUtil.spawnCloudSimple(block.getLocation());
        }
    }

    public void fxEnder() {
        List<Block> blocks = this.getBlocks();
        if (blocks == null) return;
        for (Block block : blocks) {
            Location location = block.getLocation();
            location.getWorld().playEffect(location, Effect.ENDER_SIGNAL, 0);
        }
    }

    public void fxExplode() {
        Block block = this.getCenterBlock();
        if (block == null) return;

        Location location = block.getLocation();

        SmokeUtil.fakeExplosion(location);
    }

    public void fxShootSound() {
        Block block = this.getCenterBlock();
        if (block == null) return;

        Location location = block.getLocation();

        location.getWorld().playEffect(location, Effect.GHAST_SHOOT, 0);
    }
}
