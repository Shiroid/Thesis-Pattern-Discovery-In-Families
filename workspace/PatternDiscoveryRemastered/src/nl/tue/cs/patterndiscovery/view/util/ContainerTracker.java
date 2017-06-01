package nl.tue.cs.patterndiscovery.view.util;

import nl.tue.cs.patterndiscovery.view.PanelContainer;

public class ContainerTracker {

	private static PanelContainer lastClicked;
	private static ClickMode clickMode = ClickMode.FILL;
	
	
	public static PanelContainer getLastClicked() {
		return lastClicked;
	}


	public static void setLastClicked(PanelContainer lastClicked) {
		ContainerTracker.lastClicked = lastClicked;
	}


	public static ClickMode getClickMode() {
		return clickMode;
	}


	public static void setClickMode(ClickMode clickMode) {
		ContainerTracker.clickMode = clickMode;
	}


	public enum ClickMode {
		FILL, SWAP, COPYTO, COPYFROM;
	}
}
