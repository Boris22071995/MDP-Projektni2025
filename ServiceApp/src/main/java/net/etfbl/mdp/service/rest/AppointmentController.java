package net.etfbl.mdp.service.rest;

import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.service.AppointmentService;
import net.etfbl.mdp.util.AppLogger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

@Path("/appointments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AppointmentController {

	private AppointmentService service = new AppointmentService();
	private static final Logger log = AppLogger.getLogger();

	@GET
	@Path("/all")
	public Response getAll() {
		try {
			List<Appointment> list = service.getAll();
			log.info("Appointments retrived.");
			return Response.ok(list).build();
		} catch (Exception e) {
			log.severe("Error while getting appointments.");
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"error\"}").build();
		}
	}

	@GET
	@Path("/user/{username}")
	public Response getByUser(@PathParam("username") String username) {
		List<Appointment> list = service.getByUser(username);
		log.info("User is retrived.");
		return Response.ok(list).build();
	}

	@POST
	@Path("/create")
	public Response create(Appointment a) {
		if (a == null || a.getOwnerUsername() == null) {
			log.info("Appointment created.");
			return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"invalid\"}").build();
		}
		Appointment created = service.create(a);
		log.severe("Error while creating appointment.");
		return Response.status(Response.Status.CREATED).entity(created).build();
	}

	@PUT
	@Path("/{id}/status")
	public Response updateStatus(@PathParam("id") String id, Appointment a) {
		try {
			Appointment appointment = service.findById(id);
			appointment.setStatus(a.getStatus());
			boolean ok = service.updateAppointment(appointment);
			if (ok)
				return Response.ok("{\"message\":\"updated\"}").build();
			else
				return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"not found\"}").build();
		} catch (Exception e) {
			e.printStackTrace();
			log.severe("Error while updating status.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"error\"}").build();

		}
	}

	@PUT
	@Path("/approve/{id}")
	public Response approveAppointment(@PathParam("id") String id) {
		try {
			Appointment a = service.findById(id);
			if (a == null) {
				log.severe("Error, appointment not found");
				return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Appointment not found\"}")
						.build();
			}
			a.setStatus("approved");
			service.updateAppointment(a);
			log.info("Appointment approved");
			return Response.ok("{\"message\":\"approved\"}").build();
		} catch (Exception e) {
			log.severe("Server error");
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"Server error\"}")
					.build();
		}
	}

	@PUT
	@Path("/reject/{id}")
	public Response rejectAppointment(@PathParam("id") String id) {
		try {
			Appointment a = service.findById(id);
			if (a == null) {
				log.severe("Appointment not found.");
				return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Appointment not found\"}")
						.build();
			}
			a.setStatus("rejected");
			service.updateAppointment(a);
			log.info("Appointment rejected.");
			return Response.ok("{\"message\":\"rejected\"}").build();
		} catch (Exception e) {
			e.printStackTrace();
			log.severe("Server error.");
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"Server error\"}")
					.build();
		}
	}

	@PUT
	@Path("/comment/{id}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addComment(@PathParam("id") String id, String comment) {
		try {
			Appointment a = service.findById(id);
			if (a == null) {
				log.severe("Appointment not found.");
				return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"Appointment not found\"}")
						.build();
			}
			a.setComment(comment);
			service.updateAppointment(a);
			log.info("Commented added");
			return Response.ok("{\"message\":\"Comment added\"}").build();
		} catch (Exception e) {
			log.severe("Server error");
			e.printStackTrace();
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"Server error\"}")
					.build();
		}
	}

	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") String id) {
		boolean ok = service.delete(id);
		if (ok) {
			log.info("Appointment deleted.");
			return Response.ok("{\"message\":\"deleted\"}").build();
		}
		else {
			log.severe("Error while deleting appointment.");
			return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"not found\"}").build();
		}
	}

}
