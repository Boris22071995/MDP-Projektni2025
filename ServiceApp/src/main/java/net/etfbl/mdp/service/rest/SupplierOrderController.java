package net.etfbl.mdp.service.rest;

import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.etfbl.mdp.model.Part;
import net.etfbl.mdp.service.SupplierOrderService;
import net.etfbl.mdp.util.AppLogger;

@Path("/orders")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SupplierOrderController {
	
	SupplierOrderService service = new SupplierOrderService();
	private static final Logger log = AppLogger.getLogger();

	@GET
	@Path("/all/{supplier}")	
	public Response getAll(@PathParam("supplier") String supplier) {
		try {
			List<Part> list = service.loadParts(supplier);
			log.info("Parts retrived.");
			return Response.ok(list).build();
		}catch(Exception e) {
			log.severe("Error while retriving appointments.");
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"error\"}").build();
		}
	}
}
