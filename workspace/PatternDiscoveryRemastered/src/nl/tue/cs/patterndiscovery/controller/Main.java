package nl.tue.cs.patterndiscovery.controller;

import java.util.ArrayList;

import nl.tue.cs.patterndiscovery.config.Config;
import nl.tue.cs.patterndiscovery.view.PatternViewerFrame;

public class Main {

	public static void main(String[] args) {
		Config.initConfig();
		while(!Config.isInitialized()){
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		ArrayList<Integer> intArgs = new ArrayList<Integer>();

		for(int i = 0; i < args.length; i++){
			try{
				int arg = Integer.parseInt(args[i]);
				intArgs.add(arg);
			} catch(Exception e){
				
			}
		}
		
		int[] viewDistribution = new int[]{2, 3, 6};
		if(!intArgs.isEmpty()){
			viewDistribution = new int[intArgs.size()];
			for(int i = 0; i < viewDistribution.length; i++){
				viewDistribution[i] = intArgs.get(i);
			}
		}
        new PatternViewerFrame(1280, 720, 30, viewDistribution, true);
	}

}
