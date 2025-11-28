package com.bread.traveler.dto;

import com.bread.traveler.entity.TripDayItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntireTripDayItem {

    private TripDayItems item;
    private ItineraryItem entity;

}
