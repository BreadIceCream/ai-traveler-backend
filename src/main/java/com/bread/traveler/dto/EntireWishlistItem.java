package com.bread.traveler.dto;

import com.bread.traveler.entity.WishlistItems;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "完整心愿单项信息")
public class EntireWishlistItem{

    @Schema(description = "心愿单项基本信息", implementation = WishlistItems.class)
    private WishlistItems item;
    
    @Schema(description = "实体信息", implementation = ItineraryItem.class)
    private ItineraryItem entity;

}
