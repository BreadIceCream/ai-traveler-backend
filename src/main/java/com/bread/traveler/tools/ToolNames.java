package com.bread.traveler.tools;

import java.util.Set;

public class ToolNames {

    public static final String WEB_SEARCH_TOOL_NAME = "webSearch";
    public static final String POI_SEARCH_TOOL_NAME = "poiSearch";
    public static final String FETCH_TOOL_NAME = "fetch";

    public static final String[] GAODE_MCP_TOOLS = {
            "maps_direction_bicycling",
            "maps_direction_driving",
            "maps_direction_transit_integrated",
            "maps_direction_walking",
            "maps_distance",
            "maps_geo",
            "maps_regeocode",
            "maps_ip_location",
            "maps_schema_personal_map",
            "maps_around_search",
            "maps_search_detail",
            "maps_text_search",
            "maps_schema_navi",
            "maps_schema_take_taxi",
            "maps_weather"
    };
    public static final Set<String> GAODE_MCP_TOOLS_ALLOWED = Set.of(
            "maps_direction_bicycling",
            "maps_direction_driving",
            "maps_direction_transit_integrated",
            "maps_direction_walking",
            "maps_geo",
            "maps_regeocode",
            "maps_schema_personal_map",
            "maps_around_search",
            "maps_schema_navi",
            "maps_schema_take_taxi",
            "maps_weather"
    );
    public static final Set<String> ROUTE_PLAN_CLIENT_GAODE_MCP_TOOLS = Set.of(
            "maps_direction_bicycling",
            "maps_direction_driving",
            "maps_direction_transit_integrated",
            "maps_direction_walking",
            "maps_distance",
            "maps_geo",
            "maps_regeocode",
            "maps_schema_navi",
            "maps_schema_take_taxi"
    );

}
