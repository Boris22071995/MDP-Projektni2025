package net.etfbl.mdp.service;

import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.util.AppLogger;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.logging.Logger;

public class RedisPartService {

	private final Jedis jedis = new Jedis("localhost", 6379);
	private static final Logger log = AppLogger.getLogger();
	public List<Part> getAllParts() {
		List<Part> parts = new ArrayList<>();
		Set<String> keys = jedis.keys("part:*");
		for (String key : keys) {
			Map<String, String> map = jedis.hgetAll(key);
			Part p = new Part(map.get("id"), map.get("name"), map.get("manufacturer"),
					Double.parseDouble(map.get("price")), Integer.parseInt(map.get("quantity")),
					map.get("description"));
			parts.add(p);
		}
		return parts;
	}

	public void addPart(Part p) {
		String key = "part:" + p.getId();
		Map<String, String> map = new HashMap<>();
		map.put("id", p.getId());
		map.put("name", p.getName());
		map.put("manufacturer", p.getManufacturer());
		map.put("price", String.valueOf(p.getPrice()));
		map.put("quantity", String.valueOf(p.getQuantity()));
		map.put("description", p.getDescription());
		for (Map.Entry<String, String> entry : map.entrySet()) {
			jedis.hset(key, entry.getKey(), entry.getValue());
		}
		log.info("Part:" + p.getName() +" added to database.");
	}

	public void updatePart(Part p) {
		addPart(p);
		log.info("Part:" + p.getName() +" updated.");
	}

	public void deletePart(String id) {
		jedis.del("part:" + id);
		log.info("Part deleted.");
	}

	public Part getPart(String id) {
		Part part = null;
		List<Part> parts = new ArrayList<>();
		parts = getAllParts();

		for (Part tmp : parts) {
			if (id.equals(tmp.getId())) {
				part = tmp;
			}
		}
		return part;
	}

}
