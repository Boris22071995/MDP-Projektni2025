package net.etfbl.mdp.service.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.service.RedisPartService;
import net.etfbl.mdp.util.AppLogger;

@Path("/parts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PartController {

	private RedisPartService service = new RedisPartService();
	private static final Logger log = AppLogger.getLogger();
	
	@GET
	@Path("/all")
	public Response getAll() {
		try {
			List<Part> list = service.getAllParts();
			log.info("Parts retrived.");
			return Response.ok(list).build();
		}catch(Exception e) {
			log.severe("Error while retriving appointments.");
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"error\"}").build();
		}
	}
	
	
	@POST
	@Path("/create")
	public Response create(Part p) {
		if(p == null) {
			log.severe("Error while adding part to database.");
			return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"invalid\"}").build();
		}
		
		service.addPart(p);
		log.info("Part added to database");
		return Response.status(Response.Status.CREATED).build();
	}
	
	@PUT
	@Path("/update")
	public Response update(Part p) {
		System.out.println("Da l smo u kontroleru?");
		if(p == null) {
			log.severe("Error while adding part to database.");
			return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"invalid\"}").build();
		}
		
		service.updatePart(p);
		log.info("Part added to database");
		return Response.status(Response.Status.ACCEPTED).build();
	}
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		service.deletePart(id);
		log.info("Appointment deleted.");
		return Response.ok("{\"message\":\"deleted\"}").build();
	}
}
