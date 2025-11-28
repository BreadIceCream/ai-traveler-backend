package com.bread.traveler.dto;

import com.bread.traveler.entity.WishlistItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntireWishlistItem{

    private WishlistItems item;
    private ItineraryItem entity;

}
