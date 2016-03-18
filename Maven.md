To use freebase-java with Maven, you will need to define the freebase-java maven repository in the repositories section of your pom.xml:

```
  <repositories>
    <repository>
      <id>freebase-java</id>
      <url>http://freebase-java.googlecode.com/svn/repository</url>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <releases>
        <enabled>true</enabled>
      </releases>
    </repository>
  </repositories>
```

Now, you should define the maven dependency for freebase-java:

```
<dependencies>
    <dependency>
      <groupId>com.freebase</groupId>
      <artifactId>freebase-java</artifactId>
      <version>1.0.0</version>
    </dependency>
</dependencies>
```

That's it! Now your maven project is freebase enabled.