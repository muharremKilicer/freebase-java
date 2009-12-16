package com.freebase.api.tests;

import static com.freebase.json.JSON.o;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.freebase.api.Freebase;
import com.freebase.json.JSON;

public class Tests {
        
    @Test public void testMQLRead() {
        Freebase freebase = Freebase.getFreebase();
        JSON query = o("id",null,"limit",1);
        JSON response = freebase.mqlread(query);
        assertTrue("/user/root".equals(response.get("result").get("id").string()));
    }
    
}

