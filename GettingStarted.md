# Introduction #

Freebase is an open database of the world's information, built by a global community and free for anyone to query, contribute to, and build applications on top of.

In order to enable that, Freebase provides a set of HTTP web services that your applications can interact with. While you don't need any special software to perform the required HTTP requests, it is often useful to have language-specific libraries that try to simplify the effort required to connect to such web services.

This project provides one such library for the Java language.

# JSON #

One of the characteristics of the Freebase APIs is that they heavily rely on [JSON](JSON.md), both for input and for output. Even the query language used to query information against Freebase (what is known as MQL and pronounced so that it rhymes with "mickel") is a specific form of JSON.

Unfortunately, Java's strongly typed nature and unlike other languages like Javascript and Python where it belongs as a first class citizen, makes it very unnatural to use JSON as a data format with the API.

This is why this library is built around the concept of a telescopic JSON API that is designed to make it easier not only to parse and serialize data in JSON formats, but also to construct, modify and manipulate JSON data directly inside code with as little syntax overhead as possible.

We suggest you to familiarize yourself with our telescopic [JSON](JSON.md) API before reading the examples below.

# MQL #

The most important tool at our disposal when dealing with Freebase is the MQL language. To learn more about it, point your browser as the [very comprehensive documentation on the Freebase web site](http://www.freebase.com/docs/) and follow up from there. You are also highly encouraged to try it out on the [MQL Query Editor](http://www.freebase.com/app/queryeditor), a highly interactive page on the Freebase web site that helps you familiarize yourself with the query language and with the schemas used in the various data domains that Freebase contains.

# Reading Data from Freebase #

Let's start by asking who is the director of the movie "Blade Runner". This is achieved with the following MQL query

```
{
  "id":   null,
  "type": "/film/film",
  "name": "Blade Runner",
  "directed_by": [{
    "id":   null,
    "name": null
  }]
}​
```

how do we execute that query from Java with this library? First, you need to make the jar available to your app. This is beyond the scope of this document, but if you use Maven or Ant+Ivy, we strongly suggest you to read how to [directly integrate this library with your POM](Maven.md).

Once the library is made available to your classloader, we need to import the required classes

```
import com.freebase.api.Freebase;
import com.freebase.json.JSON;

import static com.freebase.json.JSON.o;
import static com.freebase.json.JSON.a;
```

The first import is the class that contains all the Freebase APIs, while the second is the class that represents the telescopic JSON API.

The two following static imports don't change any behavior but add useful syntax sugar to our class and allows us to use the short hand form of object and array creation, respectively o() and a() instead of JSON.o() and JSON.a() saving us a lot of typing and making it easier to read our hand-crafted JSON.

Now that we have all the tools we need, the first action is to retrieve a Freebase instance to work on. This is achieved by calling a static constructor on the Freebase class, like this:

```
     Freebase freebase = Freebase.getFreebase();
```

This connects you with the main Freebase site at http://www.freebase.com/. Alternatively, you can ask to get access to the Freebase Sandbox, a scratchpad replica of the entire Freebase system (along with all the data and its underlying infrastructure) that is very useful to test writes and data loads. That is done like this:

```
      Freebase sandbox = Freebase.getFreebaseSandbox();
```

Both main and sandbox instances present the exact same APIs and react in the exact same way, the only difference is that in sandbox you should not be concerned if you write something bogus because it is wiped out every well. If you're doing read-only activity, it's probably better to use Freebase since it's a faster system, but if you're doing read-write activity and you're not 100% sure of the results, we strongly suggest to use the sandbox.

Now we need to come up with the MQL query. There are two ways to do this: by parsing a string that encodes JSON (but this makes the burden of keeping it well formed on you) or use the telescopic [JSON](JSON.md) API.

Here is how the first would be:

```
String query_str = "{" +
  "'id':   null," +
  "'type': '/film/film'," +
  "'name': 'Blade Runner'," +
  "'directed_by': [{" +
    "'id':   null," +
    "'name': null" +
  "}]" +
"}​".replace('\'', '"');
JSON query = JSON.parse();
```

Note how both Java and JSON use double quotes and instead of doing escaping all over the place and heavily sacrifice readability, we preferred using a runtime approach with the last replace() call. This is by no means required (or safe in case you have escaped single quotes inside), but it makes life a lot easier in practice.

Still, the above is cumbersome and error prone, mostly because it's very easy to destroy the syntax integrity of the JSON object while working it with piece-meal with concatenated strings. A better approach is to use the [JSON](JSON.md) API directly, like this:

```
JSON query = o(
  "id", null,
  "type", "/film/film",
  "name", "Blade Runner",
  "directed_by", a(o(
    "id", null,
    "name", null
  ))
);
```

where o() builds a JSON object and a() builds a JSON array and all the various casting issues have been eliminated by the use of a single polymorphic class that can represent all JSON content.

Now that we have the query, we need to execute it against Freebase, this is done by calling the "mqlread" service, like this:

```
JSON result = freebase.mqlread(query);
```

Like most Freebase web services, they behave as JSON-in/JSON-out services and also wrap the result with an envelope that provides additional information from the HTTP protocol on how the request has been executed. To get to the meat of our result, we need to ask for the "result" object, then dig our way into the JSON result to find the name of the director, like this:

```
String director = result.get("result").get("directed_by").get(0).get("name").string();
```

NOTE: since movies can have more than one director (even if it's not the case on this one), we have encapsulated the 'directed\_by' clause with an array. This is why we have to have the extra "get(0)" in there to get the first director).

## Writing Data to Freebase ##

The other side of the equation is obviously how to write data in Freebase or modify data that is already there. In order to achieve this, first you have to have a valid Freebase account, so go get one before you can continue.

After that the only difference between reading and writing (besides the shape of the query, of course) is that you have to authenticate yourself against the site. This is done by invoking sign-on methods on the Freebase class like this:

```
Freebase sandbox = Freebase.getFreebaseSandbox();
sandbox.sign_in(username, password);
```

which fills your current Freebase instance with authorization credentials that will be used later when invoking the commands that require authorization which are the "mqlwrite" (used to add relational data) and the "upload" (used to add binary and textual data) services.