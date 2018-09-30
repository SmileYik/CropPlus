package com.github.schooluniform.cropplus.event;

import org.bukkit.Bukkit;
import org.bukkit.CropState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Crops;

import com.github.schooluniform.cropplus.api.event.natural.CPCropDeathEvent;
import com.github.schooluniform.cropplus.api.event.natural.CropDeathEnum;
import com.github.schooluniform.cropplus.api.event.player.CPPlayerHarvestingCropEvent;
import com.github.schooluniform.cropplus.api.event.player.CPPlayerPlantingCropEvent;
import com.github.schooluniform.cropplus.data.CropData;
import com.github.schooluniform.cropplus.data.Timer;
import com.github.schooluniform.cropplus.data.manager.CropManager;
import com.github.schooluniform.cropplus.data.manager.Manager;

public class CropEvents implements Listener{
	
	@EventHandler
	public void onEntityTrample(EntityInteractEvent e){
		if( e.getBlock().getType() == Material.SOIL){
			Block b = e.getBlock().getRelative(BlockFace.UP); 
			String id = CropManager.getID(b.getLocation());
			if(CropManager.containsTimer(id)){
				Timer timer = CropManager.getTimer(id);
				CropData data = CropManager.getCropData(timer.getName());
				
				CPCropDeathEvent event = new CPCropDeathEvent(data.getId(), b.getLocation(), CropDeathEnum.Trample,e.getEntity());
				Bukkit.getPluginManager().callEvent(event);
				if(!event.isCancelled()){
					CropManager.removeTimer(id);
					if(!(b.getState().getData() instanceof Crops))return;
					if(((Crops)b.getState().getData()).getState() == CropState.RIPE)
						for(ItemStack item:data.getDrops())
							b.getWorld().dropItem(b.getLocation(), item);
					else if(Math.random()*100<data.getBreakDropSeedChance())
						b.getWorld().dropItem(b.getLocation(), data.getSeed());
					b.setType(Material.AIR);						
				}
				
			}
			return;
		}
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerPlantCrop(PlayerInteractEvent e){
		if(e.isCancelled())return;
		
		if(e.getAction() == Action.PHYSICAL && e.getClickedBlock().getType() == Material.SOIL){
			Block b = e.getClickedBlock().getRelative(BlockFace.UP); 
			String id = CropManager.getID(b.getLocation());
			if(CropManager.containsTimer(id)){
				Timer timer = CropManager.getTimer(id);
				CropData data = CropManager.getCropData(timer.getName());
				
				CPCropDeathEvent event = new CPCropDeathEvent(data.getId(), b.getLocation(), CropDeathEnum.Trample,e.getPlayer());
				Bukkit.getPluginManager().callEvent(event);
				if(!event.isCancelled()){
					CropManager.removeTimer(id);
					if(!(b.getState().getData() instanceof Crops))return;
					if(((Crops)b.getState().getData()).getState() == CropState.RIPE)
						for(ItemStack item:data.getDrops())
							b.getWorld().dropItem(b.getLocation(), item);
					else if(Math.random()*100<data.getBreakDropSeedChance())
						b.getWorld().dropItem(b.getLocation(), data.getSeed());
					b.setType(Material.AIR);						
				}
				
			}
			return;
		}else if(e.hasItem() && e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock().getType() == Material.SOIL&&e.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.AIR){
			
			//Planting Crop
			
			if(CropManager.containsTimer(CropManager.getID(e.getClickedBlock().getRelative(BlockFace.UP).getLocation()))){
				if(e.getClickedBlock().getRelative(BlockFace.UP).getType() == Material.AIR){
					CropManager.removeTimer(CropManager.getID(e.getClickedBlock().getRelative(BlockFace.UP).getLocation()));
					e.setCancelled(true);
					return;
				}
			}
			
			String name = Manager.getSeed(e.getItem().clone());
			if(name==null)return;
			e.setCancelled(true);
			playerPlantingCrop(name, e);
			
		}else if(e.hasItem() && e.getAction() == Action.RIGHT_CLICK_BLOCK && CropManager.containsTimer(CropManager.getID(e.getClickedBlock().getLocation()))){
			if(e.getItem().getType() == Material.INK_SACK && e.getItem().getDurability() == 15){
				//Bone Meal
				e.setCancelled(true);
				e.getItem().setAmount(e.getItem().getAmount()-1);
				Timer timer = CropManager.getTimer(CropManager.getID(e.getClickedBlock().getLocation()));
				CropData data = CropManager.getCropData(timer.getName());
				timer.changeTime(data.getNeedTime()*data.getBoneMeal());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerBreakCrop(BlockBreakEvent e){
		
		if(e.isCancelled())return;
		
		String id = CropManager.getID(e.getBlock().getLocation());
		if(CropManager.containsTimer(id)){
			Timer timer = CropManager.getTimer(id);
			CropData data = CropManager.getCropData(timer.getName());
			
			if(!(e.getBlock().getState().getData() instanceof Crops)){
				CropManager.removeTimer(id);
				return;
			}
		
			if(((Crops)e.getBlock().getState().getData()).getState() == CropState.RIPE){
				
				CPPlayerHarvestingCropEvent cpphce = new CPPlayerHarvestingCropEvent(e.getPlayer(), data.getId(), data.getDrops(), e.getBlock().getLocation());
				Bukkit.getServer().getPluginManager().callEvent(cpphce);
				if(!cpphce.isCancelled()){
					e.setCancelled(true);
					if(timer.getMultiple()>0 || timer.getMultiple()==-1){
						BlockState bs = e.getBlock().getState();
						Crops c = ((Crops)bs.getData());
						c.setState(CropState.SEEDED);
						bs.setData(c);
						bs.update(true);
						timer.setTime(data.getNeedTime()*data.getReback());
					}else{
						e.getBlock().setType(Material.AIR);						
						CropManager.removeTimer(id);
					}
					for(ItemStack item:data.getDrops())
						e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), item);
				}
				
			}else{
				e.getBlock().setType(Material.AIR);
				if(Math.random()*100<data.getBreakDropSeedChance())
					e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), data.getSeed());
				CropManager.removeTimer(id);
			}
		}else if(e.getBlock().getType() == Material.SOIL){
			Block b = e.getBlock().getRelative(BlockFace.UP); 
			id = CropManager.getID(b.getLocation());
			if(CropManager.containsTimer(id)){
				Timer timer = CropManager.getTimer(id);
				CropData data = CropManager.getCropData(timer.getName());
				
				if(!(b.getState().getData() instanceof Crops))return;
				if(((Crops)b.getState().getData()).getState() == CropState.RIPE){
					
					CPPlayerHarvestingCropEvent cpphce = new CPPlayerHarvestingCropEvent(e.getPlayer(), data.getId(), data.getDrops(), b.getLocation());
					Bukkit.getServer().getPluginManager().callEvent(cpphce);
					if(!cpphce.isCancelled()){
						e.setCancelled(true);
						e.getBlock().setType(Material.AIR);						
						CropManager.removeTimer(id);
						for(ItemStack item:data.getDrops())
							b.getWorld().dropItem(b.getLocation(), item);
					}
					
					//playerHarvestingCrop(e.getPlayer(), data, b.getLocation(),id);
					/*for(ItemStack item:data.getDrops())
						b.getWorld().dropItem(b.getLocation(), item);*/
				}else{
					CPCropDeathEvent event = new CPCropDeathEvent(data.getId(), b.getLocation(), CropDeathEnum.Break, e.getPlayer());
					Bukkit.getPluginManager().callEvent(event);
					if(!event.isCancelled()){
						e.setCancelled(true);
						CropManager.removeTimer(id);
						if(Math.random()*100<data.getBreakDropSeedChance())
							b.getWorld().dropItem(b.getLocation(), data.getSeed());
						b.setType(Material.AIR);	
					}
				}	
			}
		}
	}
	
	@EventHandler
	public void onBlockPistonExtend(BlockPistonExtendEvent e){
		for(Block b : e.getBlocks()){
			String id = CropManager.getID(b.getLocation());
			if(CropManager.containsTimer(id)){
				Timer timer = CropManager.getTimer(id);
				CropData data = CropManager.getCropData(timer.getName());
				
				CPCropDeathEvent event = new CPCropDeathEvent(data.getId(), b.getLocation(), CropDeathEnum.PistonExtend);
				Bukkit.getPluginManager().callEvent(event);
				if(!event.isCancelled()){
					e.setCancelled(true);
					CropManager.removeTimer(id);
					if(((Crops)b.getState().getData()).getState() == CropState.RIPE)
						for(ItemStack item:data.getDrops())
							b.getWorld().dropItem(b.getLocation(), item);
					else if(Math.random()*100<data.getBreakDropSeedChance())
						b.getWorld().dropItem(b.getLocation(), data.getSeed());
					b.setType(Material.AIR);	
				}
				
					
			}
		}
	}
	
	@EventHandler
	public void corpGrowed(BlockGrowEvent e){
		String id = CropManager.getID(e.getBlock().getLocation());
		if(CropManager.containsTimer(id)){
			e.setCancelled(true);
		}
	}
	
	public void playerPlantingCrop(String name,PlayerInteractEvent e){
		Block b = e.getClickedBlock().getRelative(BlockFace.UP); 
		CPPlayerPlantingCropEvent cpppce = new CPPlayerPlantingCropEvent(e.getPlayer(), name, b.getLocation(), e.getClickedBlock().getLocation(), e.getItem());
		Bukkit.getServer().getPluginManager().callEvent(cpppce);
		if(!cpppce.isCancelled()){
			CropData data = CropManager.getCropData(cpppce.getCropId());
			b.setType(data.getType().getType());
			BlockState bs = b.getState();
			bs.setData(new Crops(data.getType().getType(), CropState.SEEDED));
			bs.update(true);
			CropManager.registerTimer(new Timer(cpppce.getCropId(), 0,data.getMultiple(), b.getLocation()));
			e.getItem().setAmount(e.getItem().getAmount()-1);			
		}
		
	}
	
/*	public void playerHarvestingCrop(Player p,CropData data,Location location,String id){
		CPPlayerHarvestingCropEvent cpphce = new CPPlayerHarvestingCropEvent(p, data.getId(), data.getDrops(), location);
		Bukkit.getServer().getPluginManager().callEvent(cpphce);
		if(!cpphce.isCancelled()){
			CropManager.removeTimer(id);
			for(ItemStack item : cpphce.getDrops())
				location.getWorld().dropItem(location, item);
			location.getBlock().setType(Material.AIR);	
		}
		
	}*/
	
}
