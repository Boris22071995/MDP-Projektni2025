package net.etfbl.mdp.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Invoice implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String invoiceId;
	private String supplierName;
	private String partCode;
	private String partTitle;
	private double priceWithoutVAT;
	private double totalWithVAT;
	private LocalDateTime issueDate;
	
	public Invoice() {
		
	}

	public Invoice(String supplierName, String partCode, String partTitle, double priceWithoutVAT, LocalDateTime time) {
		super();
		this.invoiceId = UUID.randomUUID().toString();;
		this.supplierName = supplierName;
		this.partCode = partCode;
		this.partTitle = partTitle;
		this.priceWithoutVAT = priceWithoutVAT;
		this.totalWithVAT = priceWithoutVAT * 1.17;
		this.issueDate = time;
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getPartCode() {
		return partCode;
	}

	public void setPartCode(String partCode) {
		this.partCode = partCode;
	}

	public String getPartTitle() {
		return partTitle;
	}

	public void setPartTitle(String partTitle) {
		this.partTitle = partTitle;
	}

	public double getPriceWithoutVAT() {
		return priceWithoutVAT;
	}

	public void setPriceWithoutVAT(double priceWithoutVAT) {
		this.priceWithoutVAT = priceWithoutVAT;
	}

	public double getTotalWithVAT() {
		return totalWithVAT;
	}

	public void setTotalWithVAT(double totalWithVAT) {
		this.totalWithVAT = totalWithVAT;
	}

	public LocalDateTime getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDateTime issueDate) {
		this.issueDate = issueDate;
	}

	@Override
	public String toString() {
		return "Invoice [invoiceId=" + invoiceId + ", supplierName=" + supplierName + ", partCode=" + partCode
				+ ", partTitle=" + partTitle + ", priceWithoutVAT=" + priceWithoutVAT + ", totalWithVAT=" + totalWithVAT
				+ ", issueDate=" + issueDate + "]";
	}
	
	
	
	
}
