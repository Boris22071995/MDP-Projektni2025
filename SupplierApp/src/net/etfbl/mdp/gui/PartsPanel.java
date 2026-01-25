package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.model.Supplier;
import net.etfbl.mdp.parser.PartScraper;
import net.etfbl.mdp.parser.PartXmlUtil;
import net.etfbl.mdp.server.SupplierServer;
import net.etfbl.mdp.util.AppLogger;
import net.etfbl.mdp.util.ConfigurationLoader;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PartsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = AppLogger.getLogger();
	
	private JTable table;
	private DefaultTableModel model;
	private Supplier supplier = new Supplier();
	
	public PartsPanel(Supplier supplier) {
        setLayout(new BorderLayout());
        this.supplier = supplier;
        model = new DefaultTableModel(new Object[]{"Article number", "Title", "Price (KM)", "Image url"}, 0);
        table = new JTable(model){
			private static final long serialVersionUID = 1L;
			
			@Override
			public String getToolTipText(MouseEvent e) {
				Point p =e.getPoint();
				int row = rowAtPoint(p);
				int col = columnAtPoint(p);
				
				if(row >=0 && col >=0) {
					Object value = getValueAt(row, col);
					if(value != null) {
						return value.toString();
					}
				}
				return null;
			}
		};
		
		TableColumnModel columnModel = table.getColumnModel();
		
		columnModel.getColumn(0).setPreferredWidth(75); 
	    columnModel.getColumn(2).setPreferredWidth(75);
	    
	    columnModel.getColumn(1).setPreferredWidth(225); 
	    columnModel.getColumn(3).setPreferredWidth(225); 
		
        add(new JScrollPane(table), BorderLayout.CENTER);
        
        if(!isPortInUse(5001)) {
        	new Thread(new SupplierServer("Supplier1", "5001")).start();
        }
        if(!isPortInUse(5002)) {
        	new Thread(new SupplierServer("Supplier2", "5002")).start();
        }
        if(!isPortInUse(5003)) {
        	new Thread(new SupplierServer("Supplier3", "5003")).start();
        }
 		if(!isPortInUse(5004)) {
 			new Thread(new SupplierServer("Supplier4", "5004")).start();
 		}
              
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
			try {
				loadParts();
			} catch (Exception e1) {
				log.severe("Error while loading parts");
				e1.printStackTrace();
			}
		});
        add(refreshBtn, BorderLayout.SOUTH);

        try {
			loadParts();
		} catch (Exception e1) {
			log.severe("Error while loading parts");
			e1.printStackTrace();
		}
    }
	
	public  boolean isPortInUse(int port) {
	    try {
	        new java.net.ServerSocket(port).close();
	        return false; 
	    } catch (Exception e) {
	        return true;
	    }
	}
	
	private void loadParts() throws Exception {
        model.setRowCount(0);
        List<Part> list = new ArrayList<>();
        try {
        	if(supplier.getInstanceNumber() == 1) {
    			List<Part> list2 = PartScraper.fetchParts(ConfigurationLoader.getString("linkones1.link"));
    			List<Part> list3 = PartScraper.fetchParts(ConfigurationLoader.getString("linktwos1.link"));
    			List<Part> list4 = PartScraper.fetchParts(ConfigurationLoader.getString("linkthrees1.link"));
    			List<Part> list5 = PartScraper.fetchParts(ConfigurationLoader.getString("linkfours1.link"));
    			list.addAll(list2);
    			list.addAll(list3);
    			list.addAll(list4);
    			list.addAll(list5);
    			PartXmlUtil.savePartsToXml(supplier.getName(), list);
        	}else if(supplier.getInstanceNumber() == 2) {
        		List<Part> list2 = PartScraper.fetchParts(ConfigurationLoader.getString("linkones2.link"));
        		List<Part> list3 = PartScraper.fetchParts(ConfigurationLoader.getString("linktwos2.link"));
        		List<Part> list4 = PartScraper.fetchParts(ConfigurationLoader.getString("linkthrees2.link"));
        		List<Part> list5 = PartScraper.fetchParts(ConfigurationLoader.getString("linkfours2.link"));
        		list.addAll(list2);
    			list.addAll(list3);
    			list.addAll(list4);
    			list.addAll(list5);
    			PartXmlUtil.savePartsToXml(supplier.getName(), list);
        	}else if(supplier.getInstanceNumber() == 3) {
        		List<Part> list2 = PartScraper.fetchParts(ConfigurationLoader.getString("linkones3.link"));
        		List<Part> list3 = PartScraper.fetchParts(ConfigurationLoader.getString("linktwos3.link"));
        		List<Part> list4 = PartScraper.fetchParts(ConfigurationLoader.getString("linkthrees3.link"));
        		List<Part> list5 = PartScraper.fetchParts(ConfigurationLoader.getString("linkfours3.link"));
        		list.addAll(list2);
    			list.addAll(list3);
    			list.addAll(list4);
    			list.addAll(list5);
    			PartXmlUtil.savePartsToXml(supplier.getName(), list);
        	}else {
        		List<Part> list2 = PartScraper.fetchParts(ConfigurationLoader.getString("linkones4.link"));
        		List<Part> list3 = PartScraper.fetchParts(ConfigurationLoader.getString("linktwos4.link"));
        		List<Part> list4 = PartScraper.fetchParts(ConfigurationLoader.getString("linkthrees4.link"));
        		List<Part> list5 = PartScraper.fetchParts(ConfigurationLoader.getString("linkfours4.link"));
        		list.addAll(list2);
    			list.addAll(list3);
    			list.addAll(list4);
    			list.addAll(list5);
    			PartXmlUtil.savePartsToXml(supplier.getName(), list);
        	}
        	
		} catch (IOException e) {
			log.severe("Error while loading parts");
			e.printStackTrace();
		}
        
        for (Part p : list) {
            model.addRow(new Object[]{p.getCode(), p.getTitle(), p.getPrice() + " KM", p.getImageURL()});
        }
    }

}
