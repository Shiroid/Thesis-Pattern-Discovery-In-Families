package nl.tue.cs.patterndiscovery.view.util;

import java.awt.Color;
import java.awt.Component;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class FileCBRenderer extends JLabel implements ListCellRenderer {

	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		this.setOpaque(true);
		if(value instanceof File){
			this.setText(((File) value).getName());
		} else{
			if(value != null){
				this.setText(value.toString());
			}
		}
        if (isSelected) {
        	this.setBackground(list.getSelectionBackground());
        	this.setForeground(list.getSelectionForeground());
        } else {
        	this.setBackground(list.getBackground());
        	this.setForeground(list.getForeground());
        }
        this.setFont(list.getFont());
		return this;
	}

}
