package org.example;
import java.util.HashMap;
import java.util.Map;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class Main {
    public static void main(String[] args) {
        JedisPool pool = new JedisPool("localhost", 6379);

        try (Jedis jedis = pool.getResource()) {
            // Store & Retrieve a simple string
            jedis.set("foo", "bar");
            System.out.println(jedis.get("foo")); // prints bar

            // Store & Retrieve a HashMap
            Map<String, String> hash = new HashMap<>();
            hash.put("name", "John");
            hash.put("surname", "Smith");
            hash.put("company", "Redis");
            hash.put("age", "29");

            String key = "user-session:123";
            for (Map.Entry<String, String> entry : hash.entrySet()) {
                jedis.hset(key, entry.getKey(), entry.getValue());
            }

            System.out.println(jedis.hgetAll(key));
            // Prints: {name=John, surname=Smith, company=Redis, age=29}
        }
    }
}
