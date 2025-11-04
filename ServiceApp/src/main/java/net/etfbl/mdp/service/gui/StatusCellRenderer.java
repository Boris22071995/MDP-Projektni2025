package net.etfbl.mdp.service.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StatusCellRenderer extends DefaultTableCellRenderer {

	 @Override
	    public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {

	        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));

	        if (!isSelected) {
	            c.setForeground(Color.BLACK);
	            c.setBackground(Color.WHITE);

	            if (value != null) {
	                String text = value.toString();

	               
	                if (text.equalsIgnoreCase("Approved")) {
	                    c.setForeground(new Color(39, 174, 96)); 
	                } else if (text.equalsIgnoreCase("Not approved")) {
	                    c.setForeground(new Color(243, 156, 18)); 
	                }

	                
	                else if (text.equalsIgnoreCase("Blocked")) {
	                    c.setForeground(new Color(192, 57, 43)); 
	                } else if (text.equalsIgnoreCase("Not blocked")) {
	                    c.setForeground(new Color(39, 174, 96)); 
	                }
	            }
	        }
	        return c;
	    }
	
}
