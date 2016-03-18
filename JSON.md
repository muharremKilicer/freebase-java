# Telescopic JSON API #

While dealing with JSON data in Javascript or Python is simple and natural, dealing with JSON objects in Java is a lot more painful, mostly because all the JSON libraries have different classes for objects and arrays (which are normally considered as Map and List respectively). This differentiation forces the libraries to return Object classes and you to cast them into their respective types in order to do something with that. The increased verbosity makes it very hard to use JSON in Java like one would in other languages where its syntax is more natural.

Since the Freebase API is heavily based on JSON, we tried hard to find a way for developers to write and manipulate JSON object in a way that follows the Java language restrictions as a strongly typed language, yet reduces the casting and verbosity overhead that other JSON libraries force you to use.

# Telescopic API #

A telescopic API is one that can be chained together since an object keeps passing itself along. While it might sound weird, if you've ever used jQuery you already know what a telescopic API is and how it feels to use it.

Our goal in this library was to create a single telescopic wrapper for all JSON classes and thus provide a convenient sugar coating around the various syntactic needs that Java requires to comply with strong typing.

# Creating JSON objects by hand #

There is one class you should worry about and it's called com.freebase.json.JSON, this is the telescopic wrapper and it's all you need to interact with the JSON data. So, start your classes with

```
  import com.freebase.json.JSON 
```

and you're good to go.

Now, let's say that you want to create a json object by hand, in Javascript you would do

```
  var json = { "foo" : "bar" }; 
```

with our JSON class we can do

```
  JSON json = JSON.o("foo","bar"); 
```

where JSON.o() stands for 'create an object'. That call also supports variable arguments, so you can use it for more than one key/value pair like this

```
  JSON json = JSON.o("a",1,"b",2); 
```

which means

```
  var json = { "a" : 1, "b" : 2 }; 
```

JSON offers another static constructor, JSON.a() which works the same as JSON.o() but creates an array instead, so

```
  var json = [ "a" , "b" ]; 
```

would be

```
  JSON json = JSON.a("a","b"); 
```

Note, of course, that they can be nested

```
  JSON json = JSON.a("a",JSON.o("b","c")); 
```

which translates to

```
  var json = [ "a" , { "b" : "c" }]; 
```

# Parsing and Stringifying #

While it's handy to be able to create JSON objects directly, many times you're given JSON objects encoded as a string and you need to parse it out. It's simple to do

```
  try {
    JSON json = JSON.parse(string);
  } catch (Exception e) {
    // to deal with eventual parsing problems
  }
```

To serialize a JSON object, simply call its toString() method or use it whenever a string should be used (java will call toString() for you automatically). So for example,

```
  JSON json = JSON.o("a","b");
  System.out.println(json); 
```

would result in having

```
  {"a":"b"} 
```

printed as output.

# Obtaining values from JSON objects #

The JSON wrapper provides the get() function to obtain values from a JSON object.

```
  JSON json = JSON.o("a","b");
  String value = json.get("a").string(); 
```

Note how you have to use .string() or .number() or .bool() to cast the values into something that you can use directly in Java.

get() can be used for both objects and arrays, the first with string keys and the second with numberic indices like so

```
  JSON json = JSON.o("a",JSON.a("b",JSON.o("c",true)));
  boolean b = json.get("a").get(2).get("c").bool(); 
```

# Modifying JSON objects #

Another useful feature is to be able to modify a JSON object after you've created it or obtained it by parsing.

The JSON wrapper provides you two methods JSON.put() and JSON.del() that you can use to modify an existing JSON object, so for example you can delete parts of a json object like this

```
  JSON json = JSON.o("a","b","c",JSON.a("1","2"));
  String s = json.get("c").del(2).toString(); 
```

that will return s as

```
  {"a":"b","c":["1"]}
```

or you can add new data to it like this

```
  JSON json = JSON.o("a","b");
  String s = json.put("c",JSON.a("1")).toString(); 
```

that will return s as

```
  {"a":"b","c":["1"]}
```

# Using static imports to further compact syntax #

One of the new and relatively unknown feature of Java is the ability to load static symbols from other classes as they were defined in your own. This allows us to use JSON.o() as simply o() and JSON.a() as a().

To do this simply add

```
  import static com.freebase.json.JSON.a;
  import static com.freebase.json.JSON.o; 
```

to the top of your class and now you can write things like this

```
  JSON json = a(o("a",1,"b",true,"c",null,"d",o("a1",a("blah"))));
  String s = json.toString(); 
```

where s will be

```
  [{"a":1,"b":true,"c":null,"d":{"a1":["blah"]}}] 
```