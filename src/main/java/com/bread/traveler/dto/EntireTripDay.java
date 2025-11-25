package com.bread.traveler.dto;

import com.bread.traveler.entity.TripDayItems;
import com.bread.traveler.entity.TripDays;
import lombok.Data;

import java.util.List;

@Data
public class EntireTripDay{
    private TripDays tripDay;
    private List<TripDayItems> tripDayItems;
}
