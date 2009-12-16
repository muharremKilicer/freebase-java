package com.freebase.json.tests;

import static org.junit.Assert.assertTrue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.freebase.json.JSON;

public class Tests {

    String query1 = "{ 'id' : null , 'limit' : 1 }".replace('\'', '"');
    String query2 = "[{ 'a' : 1 , 'b' : true , 'c' : null , 'd' : { 'a1' : [ 'blah' ] }}]".replace('\'', '"');
    
    @Test public void testJSONParser() throws ParseException {
        JSONParser parser = new JSONParser();
        Object json = parser.parse(query1);
        assertTrue(json instanceof JSONObject);
    }
    
    @Test public void testJSON1() throws ParseException {
        JSON j1 = JSON.o("id",null,"limit",1);
        JSONParser parser = new JSONParser();
        JSON j2 = new JSON(parser.parse(query1));
        assertTrue(j1.toString().equals(j2.toString()));
    }
    
    @Test public void testJSON2() throws ParseException {
        JSON j1 = JSON.a(JSON.o("a",1,"b",true,"c",null,"d",JSON.o("a1",JSON.a("blah"))));
        JSONParser parser = new JSONParser();
        JSON j2 = new JSON(parser.parse(query2));
        assertTrue(j1.toString().equals(j2.toString()));
    }
    
}

