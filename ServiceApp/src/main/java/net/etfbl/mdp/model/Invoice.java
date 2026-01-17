package net.etfbl.mdp.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Invoice implements Serializable {

	private static final long serialVersionUID = 1L;

	private String invoiceId;
	private String orderId;
	private double totalWithVAT;
	private LocalDateTime issuedAt;

	public Invoice() {
	}

	public Invoice(String invoiceId, String orderId, double totalWithVAT) {
		this.invoiceId = invoiceId;
		this.orderId = orderId;
		this.totalWithVAT = totalWithVAT;
		this.issuedAt = LocalDateTime.now();
	}

	public String getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public double getTotalWithVAT() {
		return totalWithVAT;
	}

	public void setTotalWithVAT(double totalWithVAT) {
		this.totalWithVAT = totalWithVAT;
	}

	public LocalDateTime getIssuedAt() {
		return issuedAt;
	}

	public void setIssuedAt(LocalDateTime issuedAt) {
		this.issuedAt = issuedAt;
	}

	@Override
	public String toString() {
		return "Invoice [invoiceId=" + invoiceId + ", orderId=" + orderId + ", totalWithVAT=" + totalWithVAT
				+ ", issuedAt=" + issuedAt + "]";
	}

}
