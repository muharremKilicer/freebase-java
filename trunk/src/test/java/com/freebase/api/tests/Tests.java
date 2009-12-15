package com.freebase.api.tests;

import static org.junit.Assert.assertTrue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.freebase.api.Freebase;
import com.freebase.json.JSON;

public class Tests {

    String id_query = "{ 'id' : null, 'limit' : 1 }";
    
    Logger logger = LoggerFactory.getLogger(Tests.class);
    
    public String normalize(String json) {
        return json.replace("'", "\"");
    }
    
    @Test public void testJSONParser() throws ParseException {
        JSONParser parser = new JSONParser();
        Object json = parser.parse(normalize(id_query));
        assertTrue(json instanceof JSONObject);
    }
    
    @Test public void testMQLRead() {
        Freebase freebase = Freebase.getFreebase();
        JSON response = freebase.mqlread(normalize(id_query));
        assertTrue("/user/root".equals(response.get("result").get("id").string()));
    }
}

