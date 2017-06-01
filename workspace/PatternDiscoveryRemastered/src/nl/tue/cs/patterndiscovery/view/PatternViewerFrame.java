package nl.tue.cs.patterndiscovery.view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import nl.tue.cs.patterndiscovery.config.ConfigSetter;
import nl.tue.cs.patterndiscovery.view.util.SharedChangeListener;

public class PatternViewerFrame extends JFrame implements SharedChangeListener, ActionListener{
	private JMenuBar menuBar;
	private JMenu optionsMenu;
	private JMenuItem setConfig;
	
	public PatternViewerFrame(int width, int height, double maxScale, int[] numPanelsPerRow, boolean transpose){
		super("Pattern Viewer");
		//Set look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Make menu for config
		this.menuBar = new JMenuBar();
		this.optionsMenu = new JMenu("Options");
		this.menuBar.add(this.optionsMenu);
		this.setConfig = new JMenuItem("Configure");
		this.optionsMenu.add(setConfig);
		this.setJMenuBar(this.menuBar);
		this.setConfig.addActionListener(this);
		
		//Fill this frame with panel containers
		this.getContentPane().setLayout(new GridBagLayout());
        JPanel currentPanel;
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		if(transpose)
			constraints.weighty = 1;
		else 
			constraints.weightx = 1;
        
		for(int i = 0; i < numPanelsPerRow.length; i++){
			if(transpose){
				constraints.gridy = 1;
				constraints.gridx = i;
				constraints.weightx = 1.0/((double) numPanelsPerRow[i]);
			} else{
				constraints.gridx = 1;
				constraints.gridy = i;
				constraints.weighty = 1.0/((double) numPanelsPerRow[i]);
			}
	        
	        currentPanel = new JPanel();
	        currentPanel.setLayout(new GridBagLayout());
	        this.getContentPane().add(currentPanel, constraints);
	        
			for(int j = 0; j < numPanelsPerRow[i]; j++){
				PanelContainer pc = new PanelContainer(maxScale/((double) numPanelsPerRow[i]));
				if(transpose)
					constraints.gridy = j;
				else 
					constraints.gridx = j;
				currentPanel.add(new JScrollPane(pc), constraints);
			}
		}
		
        this.setBounds( 0, 0, width, height );
        this.setDefaultCloseOperation( this.EXIT_ON_CLOSE );
        this.setVisible(true);
	}

	@Override
	public void sharedChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scrollChange() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == this.setConfig){
			new ConfigSetter();
		}
	}

}
