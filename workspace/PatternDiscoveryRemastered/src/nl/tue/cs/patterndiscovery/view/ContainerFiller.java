package nl.tue.cs.patterndiscovery.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import nl.tue.cs.patterndiscovery.filemanager.DirectoryNavigator;
import nl.tue.cs.patterndiscovery.filemanager.FileExtensionStripper;
import nl.tue.cs.patterndiscovery.filemanager.FileType;
import nl.tue.cs.patterndiscovery.filemanager.PatternSetReader;
import nl.tue.cs.patterndiscovery.filemanager.SongReader;
import nl.tue.cs.patterndiscovery.model.DataSet;
import nl.tue.cs.patterndiscovery.model.Pattern;
import nl.tue.cs.patterndiscovery.model.PatternSet;
import nl.tue.cs.patterndiscovery.model.TuneFamily;
import nl.tue.cs.patterndiscovery.model.Song;
import nl.tue.cs.patterndiscovery.model.util.ModelSubTree;
import nl.tue.cs.patterndiscovery.model.util.SubTreeFactory;
import nl.tue.cs.patterndiscovery.view.util.ContainerTracker;
import nl.tue.cs.patterndiscovery.view.util.FileCBRenderer;
import nl.tue.cs.patterndiscovery.view.util.PanelFactory;
import nl.tue.cs.patterndiscovery.filemanager.TypeChecker;
import nl.tue.cs.patterndiscovery.view.util.FileDrop;

public class ContainerFiller extends JFrame implements ActionListener, ItemListener{

	private PanelContainer panelContainer;
	private ModelSubTree oldMST;
	private JComboBox<DataSet> dsBox;
	private JComboBox<TuneFamily> famBox;
	private JComboBox<Song> songBox;
	private JComboBox<File> scopeBox;
	private JComboBox<File> algoBox;
	private JComboBox<File> paramBox;
	private JComboBox<Pattern> patternBox;
	private PatternSet selectedPatternSet = new PatternSet();
	
	private JButton pianoPatternButton;
	private JButton pianoHeatButton;
	private JButton familyButton;
	private JButton familyHeatButton;
	private JButton parameterButton;
	private JButton parameterHeatButton;
	private JButton singlePatternButton;
	private JButton singlePatternFamilyButton;
	private JButton multiPatternButton;

	private JButton copyToButton;
	private JButton copyFromButton;
	private JButton swapButton;

	private JTextArea songArea;
	private ArrayList<Song> droppedSongs;
	private JTextArea patternArea;
	private ArrayList<PatternSet> droppedPatternSets;
	private JCheckBox useDropsBox;
	
	public ContainerFiller(PanelContainer target, ModelSubTree mst){
		super("Container Filler");
		this.panelContainer = target;
		this.oldMST = mst;
		
		JPanel copyPanel = new JPanel();
		this.add(copyPanel);
		this.copyToButton = createButton("Copy to this", true, copyPanel);
		this.copyFromButton = createButton("Copy from this", true, copyPanel);
		this.swapButton = createButton("Swap", true, copyPanel);
		
		this.dsBox = createComboBox(false, DataSet.class, "Select data set:");
		this.famBox = createComboBox(false, TuneFamily.class, "Select tune family:");
		this.songBox = createComboBox(false, Song.class, "Select song:");
		this.scopeBox = createComboBox(true, File.class, "Select discovery scope:");
		this.algoBox = createComboBox(true, File.class, "Select discovery algorithm:");
		this.paramBox = createComboBox(true, File.class, "Select algorithm parameters:");
		this.patternBox = createComboBox(true, Pattern.class, "Select specific pattern:");

		JPanel fileDropPanel = new JPanel();
		JPanel fdSubPanel = new JPanel();
		fdSubPanel.setLayout(new BoxLayout(fdSubPanel, BoxLayout.Y_AXIS));
		this.add(fileDropPanel);
		this.songArea = new JTextArea();
		this.songArea.setText("Songs dropped:");
		this.droppedSongs = new ArrayList<Song>();
		this.patternArea = new JTextArea();
		this.patternArea.setText("Pattern sets dropped:");
		this.droppedPatternSets = new ArrayList<PatternSet>();
		
		fdSubPanel.add(new JLabel("Or drop files:"));
		fdSubPanel.add(new JLabel("Use the dropped files?"));
		this.useDropsBox = new JCheckBox();
		this.useDropsBox.setSelected(false);
		fdSubPanel.add(this.useDropsBox);
		
		fileDropPanel.add(fdSubPanel);
		fileDropPanel.add(songArea);
		fileDropPanel.add(patternArea);
        
        new FileDrop( this, new FileDrop.Listener() {   
        	public void filesDropped( java.io.File[] files ) { 
    			useDropsBox.setSelected(false);  
        		for( int i = 0; i < files.length; i++ ) {   
        			try {
						FileType type = TypeChecker.checkFileType(files[i]);
						String newLine = System.lineSeparator();
						switch(type){
						case COLLINSLISPSONG:
							Song s = SongReader.readLispSong(files[i]);
							droppedSongs.add(s);
							songArea.append(newLine + s.getSongName());
							finishButtonsEnabled(true);
							break;
						case MIREXPATTERNS:
							PatternSet ps = PatternSetReader.readPatternSet(files[i]);
							ps.setName(FileExtensionStripper.getNameWithoutExt(files[i].getName()));
							droppedPatternSets.add(ps);
							patternArea.append(newLine + ps);
							finishButtonsEnabled(true);
							break;
						default: 
							System.out.println("Could not determine file type.");
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			pack();
            	}   // end for: through each dropped file
            }   // end filesDropped
        }); // end FileDrop.Listener

		JPanel finishSongPanel = new JPanel();
		this.add(finishSongPanel);
		JPanel finishFamilyPanel = new JPanel();
		this.add(finishFamilyPanel);
		JPanel finishParameterPanel = new JPanel();
		this.add(finishParameterPanel);
		JPanel finishPatternPanel = new JPanel();
		this.add(finishPatternPanel);
		this.pianoPatternButton = createButton("Piano Pattern View", false, finishSongPanel);
		this.pianoHeatButton = createButton("Piano Heat Map View", false, finishSongPanel);
		this.familyButton = createButton("Family Pattern View", false, finishFamilyPanel);
		this.familyHeatButton = createButton("Family Heat Map View", false, finishFamilyPanel);
		this.parameterButton = createButton("Parameter Pattern View", false, finishParameterPanel);
		this.parameterHeatButton = createButton("Parameter Heat Map View", false, finishParameterPanel);
		this.singlePatternButton = createButton("Single Pattern View", false, finishPatternPanel);
		this.singlePatternFamilyButton = createButton("Single Pattern Family View", false, finishPatternPanel);
		this.multiPatternButton = createButton("Pattern Strips View", false, finishPatternPanel);
		finishButtonsEnabled(false);

		for(DataSet ds: DirectoryNavigator.findDataSets()){
			this.dsBox.addItem(ds);
		}
		this.dsBox.setEnabled(true);
		
		if(this.oldMST != null){
			for(int i = dsBox.getItemCount() - 1; i >= 0; i--){
				if(this.oldMST.dataSet != null)
					if(this.oldMST.dataSet.equals(dsBox.getItemAt(i)))
						dsBox.setSelectedIndex(i);
			}
		}
		
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.pack();
        this.setDefaultCloseOperation( this.DISPOSE_ON_CLOSE );
        this.setVisible(true);
	}
	
	protected <T> JComboBox<T> createComboBox(boolean isFileBox, Class<T> type, String name){
		JComboBox<T> box = new JComboBox<T>();
		JLabel nameLabel = new JLabel(name);
		nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		this.add(nameLabel);
		this.add(box);
		box.setEnabled(false);
		if(isFileBox)
			box.setRenderer(new FileCBRenderer());
		box.addItemListener(this);
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		return box;
	}
	
	protected JButton createButton(String text, boolean enabled, Container container){
		JButton result = new JButton(text);
		result.setEnabled(enabled);
		container.add(result);
		result.addActionListener(this);
		result.setAlignmentX(Component.CENTER_ALIGNMENT);
		return result;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if(arg0.getSource() == pianoHeatButton){
			ModelSubTree mst = constructMST(!this.useDropsBox.isSelected());
			PanelFactory.putPatternHeatMap(mst,	this.panelContainer);
			this.dispose();
		}

		if(arg0.getSource() == pianoPatternButton){
			ModelSubTree mst = constructMST(!this.useDropsBox.isSelected());
			PanelFactory.putPatternHighlightRoll(mst, this.panelContainer);
			this.dispose();
		}

		if(arg0.getSource() == familyButton){
			List<ModelSubTree> msts = constructMSTFamily(!this.useDropsBox.isSelected());
			PanelFactory.putPatternHighlightFamily(msts, this.panelContainer);
			this.dispose();
		}

		if(arg0.getSource() == familyHeatButton){
			List<ModelSubTree> msts = constructMSTFamily(!this.useDropsBox.isSelected());
			PanelFactory.putFamilyHeatMap(msts, this.panelContainer);
			this.dispose();
		}

		if(arg0.getSource() == parameterButton){
			List<ModelSubTree> msts = constructMSTParam(!this.useDropsBox.isSelected());
			PanelFactory.putPatternHighlightFamily(msts, this.panelContainer);
			this.dispose();
		}

		if(arg0.getSource() == parameterHeatButton){
			List<ModelSubTree> msts = constructMSTParam(!this.useDropsBox.isSelected());
			PanelFactory.putFamilyHeatMap(msts, this.panelContainer);
			this.dispose();
		}

		if(arg0.getSource() == singlePatternButton){
			ModelSubTree mst = constructMST(!this.useDropsBox.isSelected());
			mst.pattern = (Pattern) patternBox.getSelectedItem();
			PanelFactory.putPatternHighlightRoll(mst, this.panelContainer);
			this.dispose();
		}

		if(arg0.getSource() == singlePatternFamilyButton){
			List<ModelSubTree> msts = constructMSTFamily(!this.useDropsBox.isSelected());
			int selectedPatternID = ((Pattern) this.patternBox.getSelectedItem()).getPatternID();
			for(ModelSubTree mst: msts){
				for(Pattern p: mst.patternSet){
					if(p.getPatternID() == selectedPatternID){
						mst.pattern = p;
						break;
					}
				}
			}
			PanelFactory.putPatternHighlightFamily(msts, this.panelContainer);
			this.dispose();
		}

		if(arg0.getSource() == multiPatternButton){
			List<ModelSubTree> msts = new ArrayList<ModelSubTree>();
			ModelSubTree base = constructMST(!this.useDropsBox.isSelected());
			for(Pattern p: base.patternSet){
				ModelSubTree mst = SubTreeFactory.createFromMST(base);
				mst.pattern = p;
				msts.add(mst);
			}
			PanelFactory.putPatternHighlightFamily(msts, this.panelContainer);
			this.dispose();
		}

		if(arg0.getSource() == copyToButton){
			ContainerTracker.setClickMode(ContainerTracker.ClickMode.COPYTO);
			this.dispose();
		}

		if(arg0.getSource() == copyFromButton){
			ContainerTracker.setClickMode(ContainerTracker.ClickMode.COPYFROM);
			this.dispose();
		}

		if(arg0.getSource() == swapButton){
			ContainerTracker.setClickMode(ContainerTracker.ClickMode.SWAP);
			this.dispose();
		}
	}
	
	protected void fillPatternBox(){
		ModelSubTree mst = SubTreeFactory.createDSetToSong(
				(DataSet) dsBox.getSelectedItem(), 
				(TuneFamily) famBox.getSelectedItem(), 
				(Song) songBox.getSelectedItem());
		mst.song.load();
		List<File> patSetFiles = DirectoryNavigator.findPatternSetFiles(mst, (File) paramBox.getSelectedItem());
		if(!patSetFiles.isEmpty())
			this.selectedPatternSet = PatternSetReader.readPatternSet(
				patSetFiles.get(0), 
				mst.song);
		else this.selectedPatternSet = new PatternSet();

		activateComboBox(this.patternBox);
		for(Pattern p: this.selectedPatternSet){
			this.patternBox.addItem(p);
		}
		
		if(this.oldMST != null){
			for(int i = patternBox.getItemCount() - 1; i >= 0; i--){
				if(this.oldMST.pattern != null)
					if(this.oldMST.pattern.equals(patternBox.getItemAt(i)))
						patternBox.setSelectedIndex(i);
			}
		}
		this.singlePatternButton.setEnabled(true);
	}
	
	protected ModelSubTree constructMST(boolean fromFileStructure){
		ModelSubTree mst;
		if(fromFileStructure){
			mst = SubTreeFactory.createDSetToSong(
					(DataSet) dsBox.getSelectedItem(), 
					(TuneFamily) famBox.getSelectedItem(), 
					(Song) songBox.getSelectedItem());
			mst.song.load();
			List<File> patSetFiles = DirectoryNavigator.findPatternSetFiles(mst, (File) paramBox.getSelectedItem());
			if(!patSetFiles.isEmpty())
				mst.patternSet = PatternSetReader.readPatternSet(
					patSetFiles.get(0), 
					mst.song);
			else mst.patternSet = new PatternSet();
		} else{
			mst = SubTreeFactory.createEmpty();
			if(!droppedSongs.isEmpty()) mst.song = droppedSongs.get(droppedSongs.size()-1);
			if(!droppedPatternSets.isEmpty()) mst.patternSet = droppedPatternSets.get(droppedPatternSets.size()-1);
			else mst.patternSet = new PatternSet();
		}
		return mst;
	}
	
	protected List<ModelSubTree> constructMSTFamily(boolean fromFileStructure){
		ArrayList<ModelSubTree> msts = new ArrayList<ModelSubTree>();
		if(fromFileStructure){
			ModelSubTree base = SubTreeFactory.createDSetToFamily((DataSet) dsBox.getSelectedItem(), 
					(TuneFamily) famBox.getSelectedItem());
			
			for(Song song: DirectoryNavigator.findSongs(base)){
				msts.add(SubTreeFactory.createDSetToSong(base.dataSet, base.tuneFamily, song));
				song.load();
			}	
			
			for(ModelSubTree mst: msts){
				List<File> patSetFiles = DirectoryNavigator.findPatternSetFiles(mst, (File) paramBox.getSelectedItem());
				if(!patSetFiles.isEmpty())
					mst.patternSet = PatternSetReader.readPatternSet(
						patSetFiles.get(0), 
						mst.song);
				else mst.patternSet = new PatternSet();
			}
		} 
		else {
			for(int i = 0; i < droppedSongs.size(); i++){
				ModelSubTree mst = SubTreeFactory.createEmpty();
				mst.song = droppedSongs.get(i);
				if(droppedPatternSets.size() > i) mst.patternSet = droppedPatternSets.get(i);
				msts.add(mst);
			}
		}
		return msts;
	}
	
	protected List<ModelSubTree> constructMSTParam(boolean fromFileStructure){
		ArrayList<ModelSubTree> msts = new ArrayList<ModelSubTree>();
		if(fromFileStructure){
			ModelSubTree base = SubTreeFactory.createDSetToSong((DataSet) dsBox.getSelectedItem(), 
					(TuneFamily) famBox.getSelectedItem(), (Song) songBox.getSelectedItem());
			base.song.load();
			
			for(int i = 0; i < paramBox.getItemCount(); i++){
				List<File> patSetFiles = DirectoryNavigator.findPatternSetFiles(base, (File) paramBox.getItemAt(i));
				if(!patSetFiles.isEmpty()){
					ModelSubTree mst = SubTreeFactory.createFromMST(base);
					mst.patternSet = PatternSetReader.readPatternSet(
							patSetFiles.get(0), 
							base.song);
					msts.add(mst);
				}
			}
		} 
		else {
			return constructMSTFamily(fromFileStructure);
		}
		return msts;
	}
	
	protected void activateComboBox(JComboBox box){
		box.setEnabled(true);
		box.removeAllItems();
	}
	
	protected void finishButtonsEnabled(boolean enabled){
		this.pianoPatternButton.setEnabled(enabled);
		this.pianoHeatButton.setEnabled(enabled);
		this.familyButton.setEnabled(enabled);
		this.familyHeatButton.setEnabled(enabled);
		this.parameterButton.setEnabled(enabled);
		this.parameterHeatButton.setEnabled(enabled);
		this.singlePatternButton.setEnabled(enabled);
		this.singlePatternFamilyButton.setEnabled(enabled);
		this.multiPatternButton.setEnabled(enabled);
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		finishButtonsEnabled(!droppedSongs.isEmpty());
		
		if(arg0.getSource() == patternBox && patternBox.getItemCount() > 0){
			finishButtonsEnabled(true);
		}
		
		if(arg0.getSource() == paramBox && paramBox.getItemCount() > 0){
			this.useDropsBox.setSelected(false);
			if(songBox.isEnabled()){
				pianoPatternButton.setEnabled(true);
				pianoHeatButton.setEnabled(true);
				parameterButton.setEnabled(true);
				parameterHeatButton.setEnabled(true);
				multiPatternButton.setEnabled(true);
				fillPatternBox();
			}
			if(famBox.isEnabled()){
				familyButton.setEnabled(true);
				familyHeatButton.setEnabled(true);
			}
		} 
		
		if(arg0.getSource() == algoBox && algoBox.getItemCount() > 0){
			this.useDropsBox.setSelected(false);
			if(songBox.isEnabled()){
				parameterButton.setEnabled(true);
				parameterHeatButton.setEnabled(true);
			}
			activateComboBox(paramBox);
			for(File f: DirectoryNavigator.findParameters((File) algoBox.getSelectedItem())){
				paramBox.addItem(f);
			}

			if(this.oldMST != null){
				for(int i = paramBox.getItemCount() - 1; i >= 0; i--){
					if(this.oldMST.patternSet != null)
						if(this.oldMST.patternSet.getParameters().equals(((File) paramBox.getItemAt(i)).getName()))
							paramBox.setSelectedIndex(i);
				}
			}
		} 

		if(arg0.getSource() == scopeBox && scopeBox.getItemCount() > 0){
			this.useDropsBox.setSelected(false);
			activateComboBox(algoBox);
			for(File f: DirectoryNavigator.findAlgorithms((File) scopeBox.getSelectedItem())){
				algoBox.addItem(f);
			}

			if(this.oldMST != null){
				for(int i = algoBox.getItemCount() - 1; i >= 0; i--){
					if(this.oldMST.patternSet != null)
						if(this.oldMST.patternSet.getAlgorithm().equals(((File) algoBox.getItemAt(i)).getName()))
							algoBox.setSelectedIndex(i);
				}
			}
		} 

		if(arg0.getSource() == songBox && songBox.getItemCount() > 0){
			this.useDropsBox.setSelected(false);
			if(paramBox.isEnabled()){
				this.pianoPatternButton.setEnabled(true);
				this.pianoHeatButton.setEnabled(true);
				this.familyButton.setEnabled(true);
				this.familyHeatButton.setEnabled(true);
				this.multiPatternButton.setEnabled(true);
				fillPatternBox();
			}
			if(algoBox.isEnabled()){
				this.parameterButton.setEnabled(true);
				this.parameterHeatButton.setEnabled(true);
			}
		} 

		if(arg0.getSource() == famBox && famBox.getItemCount() > 0){
			this.useDropsBox.setSelected(false);
			activateComboBox(songBox);
			for(Song song: DirectoryNavigator.findSongs( 
					SubTreeFactory.createDSetToFamily((DataSet) dsBox.getSelectedItem(), 
							(TuneFamily) famBox.getSelectedItem()))){
				songBox.addItem(song);
			}		


			if(this.oldMST != null){
				for(int i = songBox.getItemCount() - 1; i >= 0; i--){
					if(this.oldMST.song != null)
						if(this.oldMST.song.equals(songBox.getItemAt(i)))
							songBox.setSelectedIndex(i);
				}
			}
			
			if(paramBox.isEnabled()){
				this.familyButton.setEnabled(true);
			}	
			
			if(patternBox.isEnabled()){
				this.singlePatternFamilyButton.setEnabled(true);
			}	
		} 

		if(arg0.getSource() == dsBox && dsBox.getItemCount() > 0){
			this.useDropsBox.setSelected(false);
			activateComboBox(famBox);
			for(TuneFamily tf: DirectoryNavigator.findTuneFamilies( 
					SubTreeFactory.createDSet((DataSet) dsBox.getSelectedItem()))){
				famBox.addItem(tf);
			}
			
			activateComboBox(scopeBox);
			for(File f: DirectoryNavigator.findScopes( 
					SubTreeFactory.createDSet((DataSet) dsBox.getSelectedItem()))){
				scopeBox.addItem(f);
			}

			if(this.oldMST != null){
				for(int i = famBox.getItemCount() - 1; i >= 0; i--){
					if(this.oldMST.tuneFamily != null)
						if(this.oldMST.tuneFamily.equals(famBox.getItemAt(i)))
							famBox.setSelectedIndex(i);
				}
				for(int i = scopeBox.getItemCount() - 1; i >= 0; i--){
					if(this.oldMST.patternSet != null)
						if(this.oldMST.patternSet.getScope().equals(((File) scopeBox.getItemAt(i)).getName()))
							scopeBox.setSelectedIndex(i);
				}
			}
		}
	}
}
