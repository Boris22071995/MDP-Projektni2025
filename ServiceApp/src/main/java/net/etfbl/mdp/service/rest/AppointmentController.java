package net.etfbl.mdp.service.rest;


import net.etfbl.mdp.model.Appointment;
import net.etfbl.mdp.model.Client;
import net.etfbl.mdp.service.AppointmentService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/appointments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AppointmentController {

	private AppointmentService service = new AppointmentService();
	
	    @GET
	    @Path("/all")
	    public Response getAll() {
	        try {
	            List<Appointment> list = service.getAll();
	            return Response.ok(list).build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"error\"}").build();
	        }
	    }
	 
	    @GET
	    @Path("/user/{username}")
	    public Response getByUser(@PathParam("username") String username) {
	        List<Appointment> list = service.getByUser(username);
	        return Response.ok(list).build();
	    }
	    
	    @POST
	    @Path("/create")
	    public Response create(Appointment a) {
	        if (a == null || a.getOwnerUsername() == null) {
	            return Response.status(Response.Status.BAD_REQUEST).entity("{\"message\":\"invalid\"}").build();
	        }
	        Appointment created = service.create(a);
	        return Response.status(Response.Status.CREATED).entity(created).build();
	    }
	    
	    @PUT
	    @Path("/{id}/status")
	    public Response updateStatus(@PathParam("id") String id, Appointment a) {
	    	try {
	    		Appointment appointment = service.findById(id);
	    		appointment.setStatus(a.getStatus());
	    		boolean ok = service.updateAppointment(appointment);
	    		if (ok) return Response.ok("{\"message\":\"updated\"}").build();
	            else return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"not found\"}").build();
	    	}catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"message\":\"error\"}").build();
	       
	    }
	  }
	    
	    @PUT
	    @Path("/approve/{id}")
	    public Response approveAppointment(@PathParam("id") String id) {
	        try {
	            Appointment a = service.findById(id);
	            if (a == null) {
	                return Response.status(Response.Status.NOT_FOUND)
	                        .entity("{\"message\":\"Appointment not found\"}").build();
	            }
	            a.setStatus("approved");
	            service.updateAppointment(a);
	            return Response.ok("{\"message\":\"approved\"}").build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("{\"message\":\"Server error\"}").build();
	        }
	    }
	    
	    
	    @PUT
	    @Path("/reject/{id}")
	    public Response rejectAppointment(@PathParam("id") String id) {
	        try {
	            Appointment a = service.findById(id);
	            if (a == null) {
	                return Response.status(Response.Status.NOT_FOUND)
	                        .entity("{\"message\":\"Appointment not found\"}").build();
	            }
	            a.setStatus("rejected");
	            service.updateAppointment(a);
	            return Response.ok("{\"message\":\"rejected\"}").build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("{\"message\":\"Server error\"}").build();
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
	                return Response.status(Response.Status.NOT_FOUND)
	                        .entity("{\"message\":\"Appointment not found\"}").build();
	            }
	            a.setComment(comment);
	            service.updateAppointment(a);
	            return Response.ok("{\"message\":\"Comment added\"}").build();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
	                    .entity("{\"message\":\"Server error\"}").build();
	        }
	    }
	    
	    
	    @DELETE
	    @Path("/{id}")
	    public Response delete(@PathParam("id") String id) {
	        boolean ok = service.delete(id);
	        if (ok) return Response.ok("{\"message\":\"deleted\"}").build();
	        else return Response.status(Response.Status.NOT_FOUND).entity("{\"message\":\"not found\"}").build();
	    }
	
}
