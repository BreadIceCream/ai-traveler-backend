package com.bread.traveler.dto;

import com.bread.traveler.entity.Trips;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntireTrip {

    private Trips trip;
    private List<EntireTripDay> tripDays;

}
