package com.kindredgroup.unibetlivetest.entity;

import com.kindredgroup.unibetlivetest.types.BetState;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Table(name = "bet")
@Entity
@Data
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name = "selection_id", nullable = false)
    private Selection selection;

    @Column(name = "state")
    private BetState betState;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

}
