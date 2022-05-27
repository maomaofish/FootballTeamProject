package com.company.footballteam.model;

//import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity anotation specifies that the class is an entity
//@Table anotation specifies the table in the database with which this entity is mapped
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "country")
    private String country;

    @Column(name = "age")
    private int age;

    @Column(name = "market_value")
    private long marketValue;

    @Column(name = "asking_price")
    private long askingPrice;

    // 1 means in market; 0: not in market
    @Column(name = "in_market")
    private int inMarket;

    @Column(name = "player_type")
    private int playerType;

    @Column(name = "team_id")
    private long teamId;

}
