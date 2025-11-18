package net.etfbl.mdp.gui;

import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.model.Supplier;
import net.etfbl.mdp.parser.PartScraper;
import net.etfbl.mdp.parser.PartXmlUtil;
import net.etfbl.mdp.server.SupplierServer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PartsPanel extends JPanel {
	
	private JTable table;
	private DefaultTableModel model;
	private Supplier supplier = new Supplier();
	
	public PartsPanel(Supplier supplier) {
        setLayout(new BorderLayout());
        this.supplier = supplier;
        model = new DefaultTableModel(new Object[]{"Article number", "Title", "Price (€)", "Image url"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
        new Thread(new SupplierServer("Supplier1", "5001")).start();
        new Thread(new SupplierServer("Supplier2", "5002")).start();
        new Thread(new SupplierServer("Supplier3", "5003")).start();
        new Thread(new SupplierServer("Supplier4", "5004")).start();
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
			try {
				loadParts();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
        add(refreshBtn, BorderLayout.SOUTH);

        try {
			loadParts();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
	
	private void loadParts() throws Exception {
        model.setRowCount(0);
        List<Part> list = new ArrayList<>();
        try {
        	if(supplier.getInstanceNumber() == 1) {
    			List<Part> list2 = PartScraper.fetchParts("https://www.autohub.ba/ts/senzor-temperature-rashladne-tecnosti-2/bmw-3-e21-3206-90kw?q=");
    			list = list2;
    			PartXmlUtil.savePartsToXml(supplier.getName(), list2);
    			//new Thread(new SupplierServer(supplier.getName(), supplier.getPort())).start();
    			System.out.println("Saved parts to " + supplier.getName() + "_parts.xml");
        	}else if(supplier.getInstanceNumber() == 2) {
        		List<Part> list2 = PartScraper.fetchParts("https://www.autohub.ba/ts/nosac-motora-2/bmw-3-e21-3206-90kw?q=");
    			list = list2;
    			PartXmlUtil.savePartsToXml(supplier.getName(), list2);
    			//new Thread(new SupplierServer(supplier.getName(), supplier.getPort())).start();
    			System.out.println("Saved parts to " + supplier.getName() + "_parts.xml");
        	}else if(supplier.getInstanceNumber() == 3) {
        		List<Part> list2 = PartScraper.fetchParts("https://www.autohub.ba/ts/filter-vazduha-2/bmw-3-e21-3206-90kw?q=");
    			list = list2;
    			PartXmlUtil.savePartsToXml(supplier.getName(), list2);
    			//new Thread(new SupplierServer(supplier.getName(), supplier.getPort())).start();
    			System.out.println("Saved parts to " + supplier.getName() + "_parts.xml");
        	}else {
        		List<Part> list2 = PartScraper.fetchParts("https://www.autohub.ba/ts/pumpa-za-vodu-2/bmw-3-e21-3206-90kw?q=");
    			list = list2;
    			PartXmlUtil.savePartsToXml(supplier.getName(), list2);
    			//new Thread(new SupplierServer(supplier.getName(), supplier.getPort())).start();
    			System.out.println("Saved parts to " + supplier.getName() + "_parts.xml");
        	}
        	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        for (Part p : list) {
            model.addRow(new Object[]{p.getCode(), p.getTitle(), p.getPrice(), p.getImageURL()});
        }
    }

}
