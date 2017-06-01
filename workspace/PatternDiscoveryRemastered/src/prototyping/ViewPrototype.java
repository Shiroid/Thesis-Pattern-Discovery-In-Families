package prototyping;

import java.io.File;

import nl.tue.cs.patterndiscovery.filemanager.*;
import nl.tue.cs.patterndiscovery.view.util.FileDrop;

public class ViewPrototype {
	
	private static SongPanelPrototype songPanel;

    public static void main( String[] args )
    {
        javax.swing.JFrame frame = new javax.swing.JFrame( "PatternViewer" );
        songPanel = new SongPanelPrototype();
        frame.getContentPane().add( 
            new javax.swing.JScrollPane( songPanel ), 
            java.awt.BorderLayout.CENTER );
     
        new FileDrop( System.out, songPanel, /*dragBorder,*/ new FileDrop.Listener()
        {   public void filesDropped( java.io.File[] files )
            {   for( int i = 0; i < files.length; i++ )
                {   try
                    { 	
                		FileType type = TypeChecker.checkFileType(files[i]);
                		if(type == FileType.COLLINSLISPSONG) songPanel.setSong(files[i]);
                		if(type == FileType.MIREXPATTERNS) songPanel.addPatternSet(files[i]);
                    }   // end try
                    catch( java.io.IOException e ) {}
                }   // end for: through each dropped file
            }   // end filesDropped
        }); // end FileDrop.Listener

        frame.setBounds( 100, 100, 300, 400 );
        frame.setDefaultCloseOperation( frame.EXIT_ON_CLOSE );
        frame.setVisible(true);
    }   // end main
	
}
