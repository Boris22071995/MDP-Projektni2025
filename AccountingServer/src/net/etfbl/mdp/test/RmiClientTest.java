package net.etfbl.mdp.test;


import net.etfbl.mdp.model.Invoice;
import net.etfbl.mdp.rmi.InvoiceService;

import java.rmi.Naming;
import java.util.List;
import java.util.UUID;

public class RmiClientTest {
	public static void main(String[] args) {
        try {
            InvoiceService service = (InvoiceService) Naming.lookup("rmi://localhost:1099/InvoiceService");

            // Dodaj test fakturu
          //  Invoice inv = new Invoice(UUID.randomUUID().toString(), "TestDobavljac", "A123", "Disk pločice", 150.0);
           // service.addInvoice(inv);
           // System.out.println("✅ Faktura poslata.");

            // Ispiši sve fakture
           // List<Invoice> list = service.getAllInvoices();
           // System.out.println("📄 Fakture u sistemu:");
           // list.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
