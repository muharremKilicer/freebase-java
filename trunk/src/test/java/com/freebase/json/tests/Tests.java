package com.freebase.json.tests;

import static com.freebase.json.JSON.a;
import static com.freebase.json.JSON.o;
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
        // make the json object by using the o/a notation
        JSON j1 = JSON.o("id",null,"limit",1);

        // make the json object parsing the javascript syntax from a string 
        JSON j2 = JSON.parse(query1);

        // make sure they serialize the same
        assertTrue(j1.toString().equals(j2.toString()));
    }
    
    @Test public void testJSON2() throws ParseException {
        // make the json object by using the o/a notation
        JSON j1 = a(o("a",1,"b",true,"c",null,"d",o("a1",a("blah"))));
        
        // make the json object parsing the javascript syntax from a string 
        JSON j2 = JSON.parse(query2);

        // make sure they serialize the same
        assertTrue(j1.toString().equals(j2.toString()));
    }

    @Test public void testJSON3() throws ParseException {
        // make the json object by using the o/a notation
        JSON j1 = a()._(o()._("a",1)._("b",true)._("c",null)._("d",o()._("a1",a()._("blah"))));

        // make the json object parsing the javascript syntax from a string 
        JSON j2 = JSON.parse(query2);

        // make sure they serialize the same
        assertTrue(j1.toString().equals(j2.toString()));
    }

    @Test public void testJSON4() throws ParseException {
        // make the json object by using the o/a notation
        JSON j1 = a(o("a",1,"b",true,"c",null,"d",o("a1",a("blah"))));

        // make the json object by using the _ appenders
        JSON j2 = a()._(o()._("a",1)._("b",true)._("c",null)._("d",o()._("a1",a()._("blah"))));

        // make sure they serialize the same
        assertTrue(j1.toString().equals(j2.toString()));
    }
    
}

