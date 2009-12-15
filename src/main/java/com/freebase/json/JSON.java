/*
 * Copyright (c) 2009, Metaweb Technologies, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above
 *     copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided
 *     with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY METAWEB TECHNOLOGIES AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL METAWEB
 * TECHNOLOGIES OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.freebase.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;

/**
 * This class is a telescopic (meaning, jQuery-like) wrapper for all
 * the various classes that can make up a JSON object.
 * 
 * While using JSON in Javascript or Python comes natural, dealing with it
 * in Java is a pain, mostly because of casting issues. Having a single 
 * wrapper object that encapsulates all those casting issues allows us
 * to cascade the method calls. So something that in javascript would be
 * 
 * <pre>   var a = b.c[1].d[3];</pre>
 *    
 * can be translated in Java as:
 * 
 * <pre>   String a = b.get("c").get(1).get("d").get(3).string();</pre>
 *    
 * which a lot more verbose but still better than having to 
 * deal with all the casting between Map and List and String.
 */
public class JSON {

    public enum Type {
        OBJECT, 
        ARRAY, 
        STRING,
        NUMBER,
        BOOLEAN
    }
    
    private Type type;
    
    private JSONObject obj;
    private JSONArray array;
    private String string;
    private Number number;
    private boolean bool;
    
    // --------------------------------------------------
    
    public JSON(Object o) {
        if (o == null) {
            throw new RuntimeException("can't wrap a null object");
        } else if (o instanceof JSONObject) {
            this.obj = (JSONObject) o;
            this.type = Type.OBJECT;
        } else if (o instanceof JSONArray) {
            this.array = (JSONArray) o;
            this.type = Type.ARRAY;
        } else if (o instanceof String) {
            this.string = (String) o;
            this.type = Type.STRING;
        } else if (o instanceof Number) {
            this.number = (Number) o;
            this.type = Type.NUMBER;
        } else {
            throw new RuntimeException("don't how how to deal with this type of object: " + o);
        }
    }
        
    public JSON(boolean bool) {
        this.bool = bool;
        this.type = Type.BOOLEAN;
    }
    
    // --------------------------------------------------
    
    public JSON get(String key) {
        if (key == null) throw new RuntimeException("Can't ask for a null key");
        switch (this.type) {
            case BOOLEAN:
            case STRING:
            case NUMBER:
                throw new RuntimeException("Only objects or arrays contain other values");
            case ARRAY:
                int index = Integer.parseInt(key); 
                return new JSON(this.array.get(index));
            case OBJECT:
                return new JSON(this.obj.get(key));
            default:
                // this should never happen but just in case
                throw new RuntimeException("Don't recognize this object type: " + this.type);
        }
    }

    public JSON get(int index) {
        switch (this.type) {
            case BOOLEAN:
            case STRING:
            case NUMBER:
                throw new RuntimeException("Only objects or arrays contain other values");
            case ARRAY:
                return new JSON(this.array.get(index));
            case OBJECT:
                return new JSON(this.obj.get(Integer.toString(index)));
            default:
                // this should never happen but just in case
                throw new RuntimeException("Don't recognize this object type: " + this.type);
        }
    }
    
    public boolean has(String key) {
        switch (this.type) {
            case BOOLEAN:
            case STRING:
            case NUMBER:
                return false;
            case ARRAY:
                int index = Integer.parseInt(key); 
                return (index > 0 && index < this.array.size());
            case OBJECT:
                return this.obj.containsKey(key);
            default:
                // this should never happen but just in case
                throw new RuntimeException("Don't recognize this object type: " + this.type);
        }
    }

    public boolean has(int index) {
        switch (this.type) {
            case BOOLEAN:
            case STRING:
            case NUMBER:
                return false;
            case ARRAY:
                return (index > 0 && index < this.array.size());
            case OBJECT:
                return this.obj.containsKey(Integer.toString(index));
            default:
                // this should never happen but just in case
                throw new RuntimeException("Don't recognize this object type: " + this.type);
        }
    }
        
    public String string() {
        if (this.type != Type.STRING) {
            throw new RuntimeException("This is not a String, it's a " + this.type);
        }
        return this.string;
    }
    
    public Number number() {
        if (this.type != Type.NUMBER) {
            throw new RuntimeException("This is not a Number, it's a " + this.type);
        }
        return this.number;
    }
    
    public boolean bool() {
        if (this.type != Type.BOOLEAN) {
            throw new RuntimeException("This is not a Boolean, it's a " + this.type);
        }
        return this.bool;
    }
    
    public JSONAware json() {
        if (this.type != Type.OBJECT && this.type != Type.ARRAY) {
            throw new RuntimeException("This is not an Object or an Array, it's a " + this.type);
        }
        return (this.type == Type.OBJECT) ? this.obj : this.array;
    }
    
    public JSONObject obj() {
        if (this.type != Type.OBJECT) {
            throw new RuntimeException("This is not an Object, it's a " + this.type);
        }
        return this.obj;
    }

    public JSONArray array() {
        if (this.type != Type.ARRAY) {
            throw new RuntimeException("This is not an Array, it's a " + this.type);
        }
        return this.array;
    }
    
    public boolean isArray() {
        return (this.type == Type.ARRAY);
    }
    
    public boolean isObject() {
        return (this.type == Type.OBJECT);
    }
    
    public boolean isContainer() {
        return (this.type == Type.OBJECT || this.type == Type.ARRAY);
    }
}
