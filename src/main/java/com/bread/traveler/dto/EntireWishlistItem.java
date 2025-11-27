package com.bread.traveler.dto;

import com.bread.traveler.entity.WishlistItems;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EntireWishlistItem {

    private WishlistItems wishlistItem;
    private ItineraryItem entity;

}
