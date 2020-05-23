package com.example.pss.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
//@EqualsAndHashCode(exclude = "user")
@ToString(exclude = "user")
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Delegation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 5)
    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTimeStart;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateTimeStop;

    private double travelDietAmount = 30;

    @Min(0)
    @Max(3)
    private int breakfastNumber = 0;

    @Min(0)
    @Max(3)
    private int dinnerNumber = 0;

    @Min(0)
    @Max(3)
    private int supperNumber = 0;

    private TransportEnum transportEnum;

    private double ticketPrice;

    private AutoCapacityEnum autoCapacityEnum;

    private double km;

    private double accommodationPrice;

    private double otherOutlayDesc;

    private double otherOutlayPrice;

    @JsonIgnore
    @ManyToOne
    private User user;

    public Delegation(@NotNull String description, @NotNull LocalDate dateTimeStart, @NotNull LocalDate dateTimeStop,
                      double travelDietAmount, int breakfastNumber, int dinnerNumber, int supperNumber,
                      TransportEnum transportEnum, double ticketPrice, AutoCapacityEnum autoCapacityEnum, double km,
                      double accommodationPrice, double otherOutlayDesc, double otherOutlayPrice) {
        this.description = description;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeStop = dateTimeStop;
        this.travelDietAmount = travelDietAmount;
        this.breakfastNumber = breakfastNumber;
        this.dinnerNumber = dinnerNumber;
        this.supperNumber = supperNumber;
        this.transportEnum = transportEnum;
        this.ticketPrice = ticketPrice;
        this.autoCapacityEnum = autoCapacityEnum;
        this.km = km;
        this.accommodationPrice = accommodationPrice;
        this.otherOutlayDesc = otherOutlayDesc;
        this.otherOutlayPrice = otherOutlayPrice;

        if (this.transportEnum.equals(TransportEnum.auto)) {
            this.ticketPrice = 0.0;
        } else {
            this.autoCapacityEnum = AutoCapacityEnum.NONE;
            this.km = 0.0;
        }
    }

    public void addUser(User user) {
        this.setUser(user);
        user.getDelegations().add(this);
    }

    public void removeUser(User user) {
        this.setUser(null);
        user.getDelegations().remove(this);
    }
}