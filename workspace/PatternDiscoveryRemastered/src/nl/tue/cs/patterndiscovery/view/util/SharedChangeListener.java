package nl.tue.cs.patterndiscovery.view.util;

public interface SharedChangeListener {

	/*
	 * A shared change takes place, a set manager listening to this should make sure the entire set is notified.
	 */
	public void sharedChange();

	/*
	 * A shared change in only a scroll pane takes place, a set manager listening to this should make sure the entire set is notified.
	 */
	public void scrollChange();
}
