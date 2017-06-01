package nl.tue.cs.patterndiscovery.config;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * Frame used to set set config variables.
 */
public class ConfigSetter extends JFrame implements ActionListener{

	private JButton setsButton, discoveryButton, finishButton;
	private JLabel setsLabel, discoveryLabel;
	
	
	public ConfigSetter(){
		super("Config");
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel setsPanel = new JPanel();
		setsPanel.setLayout(new FlowLayout());
		this.getContentPane().add(setsPanel);
		setsButton = new JButton("Set root folder for data sets");
		setsButton.addActionListener(this);
		setsPanel.add(setsButton);
		setsLabel = new JLabel();
		if(Config.dataSetsRoot != null) setsLabel.setText(Config.dataSetsRoot.getAbsolutePath());
		else setsLabel.setText("No folder selected");
		setsPanel.add(setsLabel);
		
		JPanel discoveryPanel = new JPanel();
		discoveryPanel.setLayout(new FlowLayout());
		this.getContentPane().add(discoveryPanel);
		discoveryButton = new JButton("Set root folder for pattern discovery");
		discoveryButton.addActionListener(this);
		discoveryPanel.add(discoveryButton);
		discoveryLabel = new JLabel();
		if(Config.discoveryRoot != null) discoveryLabel.setText(Config.discoveryRoot.getAbsolutePath());
		else discoveryLabel.setText("No folder selected");
		discoveryPanel.add(discoveryLabel);

		JPanel finishPanel = new JPanel();
		finishPanel.setLayout(new FlowLayout());
		this.getContentPane().add(finishPanel);
		finishButton = new JButton("Finish configuration");
		finishButton.addActionListener(this);
		finishButton.setEnabled(Config.fieldsFilled());
		finishPanel.add(finishButton);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.pack();
        this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(setsButton)){
			JFileChooser fc = new JFileChooser("Set root folder for data sets");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fc.showOpenDialog(this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                Config.dataSetsRoot = fc.getSelectedFile();
                setsLabel.setText(Config.dataSetsRoot.getAbsolutePath());
            }
		}
		if(e.getSource().equals(discoveryButton)){
			JFileChooser fc = new JFileChooser("Set root folder for pattern discovery");
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnVal = fc.showOpenDialog(this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                Config.discoveryRoot = fc.getSelectedFile();
                discoveryLabel.setText(Config.discoveryRoot.getAbsolutePath());
            }
		}
		if(e.getSource().equals(finishButton)){
			Config.saveConfig();
			Config.initComplete = true;
			this.dispose();
		}
		finishButton.setEnabled(Config.fieldsFilled());
	}
	
		
}
