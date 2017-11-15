package prototyping.algo;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import nl.tue.cs.patterndiscovery.config.Config;
import nl.tue.cs.patterndiscovery.model.DataSet;
import nl.tue.cs.patterndiscovery.model.PatternSet;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternOccurrence;
import nl.tue.cs.patterndiscovery.model.TuneFamily;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.view.util.FileCBRenderer;
import nl.tue.cs.patterndiscovery.view.util.PanelFactory;

public class ProtoDiscoveryLauncher extends JFrame implements ActionListener {

	private DataSet ds;
	private TuneFamily tf;

	private JSpinner gsParamSpinner;
	private JSpinner rrParamSpinner;
	private JSpinner ssParamSpinner;
	private JSpinner mrParamSpinner;

	private JSpinner meParamSpinner;
	private JSpinner peParamSpinner;
	private JSpinner psParamSpinner;
	private JCheckBox transpositionBox;

	private JSpinner repsParamSpinner; 
	private JSpinner coverParamSpinner; 

	private JSpinner pbParamSpinner; 
	private JSpinner ibParamSpinner; 
	private JSpinner iterParamSpinner; 
	private JSpinner skipParamSpinner; 
	
	private JButton runButton;
	
	public ProtoDiscoveryLauncher(List<ModelSubTree> msts){
		super("Configure Discovery Algorithm");
		for(ModelSubTree mst: msts){
			mst.forceTruth();
		}
		if(!msts.isEmpty()){
			this.tf = msts.get(0).tuneFamily;
			this.ds = msts.get(0).dataSet;
		}
		JLabel nameLabel = new JLabel(this.tf.getFamilyName());
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(nameLabel);
		
		JLabel pipeParamLabel = new JLabel("Pipeline Parameters");
		pipeParamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(pipeParamLabel);

		this.gsParamSpinner = createSpinner("Maximum Gap Size", 2, 0.5, 8, 0.5); 
		this.rrParamSpinner = createSpinner("Rhytmic Variation", 0.7, 0.1, 1, 0.1); 
		this.ssParamSpinner = createSpinner("Tempo Scale Limit", 0.5, 0.1, 1, 0.1); 
		this.mrParamSpinner = createSpinner("Majority Ratio", 0.6, 0.05, 1, 0.05); 
		
		JLabel taskParamLabel = new JLabel("Task Specific Paramters");
		taskParamLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(taskParamLabel);
		
		JLabel discoveryLabel = new JLabel("Discovery Paramters");
		discoveryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(discoveryLabel);

		this.meParamSpinner = createSpinner("Maximum Embellishment", 1, 0, 20, 1); 
		this.peParamSpinner = createSpinner("Patterns Per Note", 3, 0, 100, 1); 
		this.psParamSpinner = createSpinner("Minimum Discovery Pattern Size", 3, 0, 10, 1);
		this.transpositionBox = createCheckBox("Include Transpositions", true);
		
		JLabel clusterLabel = new JLabel("Clustering Paramters");
		clusterLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(clusterLabel); 

		this.repsParamSpinner = createSpinner("Maximum Occurrences Per Song", 3, 1, 20, 1); 
		this.coverParamSpinner = createSpinner("Cluster Coverage Leeway", 1, 0.1, 1, 0.1); 
		
		JLabel selectionLabel = new JLabel("Selection Paramters");
		selectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(selectionLabel); 

		this.pbParamSpinner = createSpinner("Pair-Wise Selection Bias", 0.05, 0, 20, 0.05); 
		this.ibParamSpinner = createSpinner("Individual Selection Bias", 0.5, 0, 20, 0.05); 
		this.iterParamSpinner = createSpinner("Number Of Selection Cycles", 10, 1, 30, 1); 
		this.skipParamSpinner = createSpinner("Cost Of Non-Covered Notes", 1, -10, 100, 0.5); 

		this.runButton = new JButton("Run Algorithm");
		this.runButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(runButton);
		this.runButton.addActionListener(this);

		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.pack();
        this.setDefaultCloseOperation( this.DISPOSE_ON_CLOSE );
        this.setVisible(true);
	}

	
	protected JSpinner createSpinner(String name, double defaultValue, double min, double max, double stepSize){
		JPanel panel = new JPanel();
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel nameLabel = new JLabel(name);
		panel.add(nameLabel);
		JSpinner spinner = new JSpinner(new SpinnerNumberModel(defaultValue, min, max, stepSize)); 
		panel.add(spinner);
		this.add(panel);
		return spinner;
	}

	
	protected JCheckBox createCheckBox(String name, boolean defaultValue){
		JPanel panel = new JPanel();
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JLabel nameLabel = new JLabel(name);
		panel.add(nameLabel);
		JCheckBox box = new JCheckBox("", defaultValue); 
		panel.add(box);
		this.add(panel);
		return box;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == runButton){
			ProtoDiscoveryAlgorithm algo = new ProtoDiscoveryAlgorithm(tf, (double) gsParamSpinner.getValue(),
					(double) rrParamSpinner.getValue(), (double) ssParamSpinner.getValue(),
					(double) mrParamSpinner.getValue(), (int) Math.round((double) meParamSpinner.getValue()),
					(int) Math.round((double) peParamSpinner.getValue()),
					(int) Math.round((double) psParamSpinner.getValue()),
					transpositionBox.isSelected(),
					(int) Math.round((double) repsParamSpinner.getValue()),
					(double) coverParamSpinner.getValue(),
					(double) pbParamSpinner.getValue(),
					(double) ibParamSpinner.getValue(),
					(int) Math.round((double) iterParamSpinner.getValue()),
					(double) skipParamSpinner.getValue());
			algo.run();
			writeOutput(algo);
			this.dispose();
		}
	}
	
	public void writeOutput(ProtoDiscoveryAlgorithm algo) {
		Collection<PatternSet> patSets = algo.getSelected();
		File root = Config.getDiscoveryRoot();
		String separator = System.getProperty("file.separator");
		String params = algo.getParams();
		File dir = new File(root.getAbsolutePath() + separator + ds.getName() + separator + "InterOpusDiscoveryClassTask" + separator + "PipelineAlgorithm" + separator + params + separator + "discovery");
		if(!dir.exists()){
			dir.mkdirs();
		}
		for(PatternSet ps: patSets){
			File out = new File(dir.getAbsolutePath() + separator + ps.getSong().getSongName() + ".txt");
			try {
				out.createNewFile();
				FileWriter writer = new FileWriter(out);
				writer.write(ps.getFileContent());
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		patSets = algo.getSelectedInclusive();
		dir = new File(root.getAbsolutePath() + separator + ds.getName() + separator + "InterOpusDiscoveryClassTask" + separator + "PipelineAlgorithm" + separator + params + "_inclusive" + separator + "discovery");
		if(!dir.exists()){
			dir.mkdirs();
		}
		for(PatternSet ps: patSets){
			File out = new File(dir.getAbsolutePath() + separator + ps.getSong().getSongName() + ".txt");
			try {
				out.createNewFile();
				FileWriter writer = new FileWriter(out);
				writer.write(ps.getFileContent());
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		patSets = algo.getClustered();
		dir = new File(root.getAbsolutePath() + separator + ds.getName() + separator + "InterOpusDiscoveryClassTask" + separator + "PipelineAlgorithm" + separator + params + "_all" + separator + "discovery");
		if(!dir.exists()){
			dir.mkdirs();
		}
		for(PatternSet ps: patSets){
			File out = new File(dir.getAbsolutePath() + separator + ps.getSong().getSongName() + ".txt");
			try {
				out.createNewFile();
				FileWriter writer = new FileWriter(out);
				writer.write(ps.getFileContent());
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
