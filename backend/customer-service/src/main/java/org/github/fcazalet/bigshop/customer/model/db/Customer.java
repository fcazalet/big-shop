package org.github.fcazalet.bigshop.customer.model.db;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String email;
    @OneToOne(mappedBy = "customer")
    @JoinColumn(name = "default_shipping_address_id", referencedColumnName = "id")
    private CustomerAddress defaultShippingAddress;
    @OneToOne(mappedBy = "customer")
    @JoinColumn(name = "default_invoice_address_id", referencedColumnName = "id")
    private CustomerAddress defaultInvoiceAddress;
    @CreatedDate
    @Column(name = "created_at")
    private Date createdAt;
    @LastModifiedDate
    @Column(name = "last_modified_at")
    private Date lastModifiedAt;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public CustomerAddress getDefaultShippingAddress() {
        return defaultShippingAddress;
    }

    public void setDefaultShippingAddress(CustomerAddress defaultShippingAddress) {
        this.defaultShippingAddress = defaultShippingAddress;
    }

    public CustomerAddress getDefaultInvoiceAddress() {
        return defaultInvoiceAddress;
    }

    public void setDefaultInvoiceAddress(CustomerAddress defaultInvoiceAddress) {
        this.defaultInvoiceAddress = defaultInvoiceAddress;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
}
