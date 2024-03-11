package com.awesome.bank.dal.entity;


import com.awesome.bank.domain.model.AccountType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "account")
public class AccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "account")
    private Long id;

    @Column
    public BigDecimal balance;

    @Column
    public AccountType type;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", orphanRemoval = true)
    public List<OperationEntity> operations = new ArrayList<>();

    @Column
    private BigDecimal overdraftAllowed;

    @Column
    private Boolean hasLimit;

    @Column
    private BigDecimal limitAllowed;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant latestUpdateDate;

    @Version
    private Long version;

}