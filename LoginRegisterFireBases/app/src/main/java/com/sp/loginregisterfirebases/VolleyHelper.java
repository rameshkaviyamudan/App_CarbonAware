package com.sp.loginregisterfirebases;

import java.util.HashMap;

public class VolleyHelper {
    static String region = " https://2167caa1-ba0b-40ae-a400-933e4194b001-asia-south1.apps.astra.datastax.com/api/rest";
    public static String loginurl = region +  "/v2/keyspaces/login/users/";
    public static String carburl = region + "/v2/keyspaces/carbdata/carbon_footprint/";

    static String Cassandra_Token = "AstraCS:fZthuZRpyhlmgGzKKsMpTcfb:4701e6d53c6e86b451c23d2e38f874779ea55d76ed802c6a63dbcde5ba3f6bd7";
    static int lastID = 0;
    public static HashMap getHeader() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Cassandra-Token", Cassandra_Token);
        headers.put("Accept", "application/json");
        headers.put("X-Cassandra-User", "carbdata");
        return headers;
    }
    public static String getUserDetailsUrl(String number) {
        return loginurl + number;
    }

}