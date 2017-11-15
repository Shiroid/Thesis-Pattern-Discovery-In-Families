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
		JLabel namLabel = new JLabel(this.tf.getFamilyName());
		namLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(namLabel);

		this.gsParamSpinner = createSpinner("Maximum Gap Size", 2, 0.5, 8, 0.5); 
		this.rrParamSpinner = createSpinner("Rhytmic Variation", 0.5, 0.1, 1, 0.1); 
		this.ssParamSpinner = createSpinner("Tempo Scale Limit", 0.5, 0.1, 1, 0.1); 
		this.mrParamSpinner = createSpinner("Majority Ratio", 0.6, 0.05, 1, 0.05); 

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


	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == runButton){
			ProtoDiscoveryAlgorithm algo = new ProtoDiscoveryAlgorithm(tf, (double) gsParamSpinner.getValue(), (double) rrParamSpinner.getValue(), (double) ssParamSpinner.getValue(), (double) mrParamSpinner.getValue());
			writeOutput(algo.run(), algo);
			this.dispose();
		}
	}
	
	public void writeOutput(Collection<PatternSet> patSets, ProtoDiscoveryAlgorithm algo) {
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
				System.out.println("fail");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
