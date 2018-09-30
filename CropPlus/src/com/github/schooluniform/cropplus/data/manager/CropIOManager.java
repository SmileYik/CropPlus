package com.github.schooluniform.cropplus.data.manager;

public class CropIOManager implements Runnable{

	@Override
	public void run() {
		
		while(!CropManager.timerOut.isEmpty()){
			CropManager.timer.remove(CropManager.timerOut.pop());
		}
		
		while(!CropManager.timerIn.isEmpty()){
			CropManager.timer.put(CropManager.getID(CropManager.timerIn.peek().getLocation()), CropManager.timerIn.pop());
		}
		
	}

}
