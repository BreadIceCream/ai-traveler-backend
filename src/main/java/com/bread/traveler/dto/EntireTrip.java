package com.bread.traveler.dto;

import com.bread.traveler.entity.Trips;
import lombok.Data;

import java.util.List;

@Data
public class EntireTrip {

    private Trips trip;
    private List<EntireTripDay> tripDays;

}
