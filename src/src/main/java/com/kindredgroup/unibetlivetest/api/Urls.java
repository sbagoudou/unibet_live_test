package com.kindredgroup.unibetlivetest.api;

public final class Urls {

    public static final String BASE_PATH = "/api/v1";

    /**
     * events apis
     **/
    public static final String EVENTS = "/events";
    public static final String SELECTIONS = EVENTS + "/{id}/selections";
    public static final String EVENT = EVENTS + "/{id}";
    public static final String EVENT_MARKETS = EVENT + "/market";

    /**
     * bets api
     **/
    public static final String BETS = "bets";
    public static final String ADD_BET = BETS + "/add";


    /**
     * customers apis
     **/
    public static final String CUSTOMERS = "/customers";
    public static final String CURRENT_CUSTOMER = CUSTOMERS + "/current";

    private Urls() {
    }

}
