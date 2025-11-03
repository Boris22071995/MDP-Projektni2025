package net.etfbl.mdp.service.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;

import net.etfbl.mdp.service.ClientService;
import net.etfbl.mdp.model.Client;

@Path("/clients")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClientController {
	private ClientService service = new ClientService();

	@GET
    @Path("/all")
    public Response getAllClients() {
        try {
            List<Client> clients = service.getAllClients();
            return Response.ok(clients).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Server error\"}").build();
        }
    }

	@POST
    @Path("/register")
    public Response register(Client client) {
        try {
            if (client == null || client.getUsername() == null || client.getPassword() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\":\"Invalid client data\"}").build();
            }

            boolean ok = service.register(client);
            if (ok) {
                return Response.status(Response.Status.OK).build();
            } else {
                return Response.status(Response.Status.CONFLICT)
                        .entity("{\"message\":\"User already exists\"}").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Server error\"}").build();
        }
    }
	
	public static class LoginRequest {
        public String username;
        public String password;
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest req) {
        try {
            if (req == null || req.username == null || req.password == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"message\":\"Invalid credentials\"}").build();
            }
            
            boolean ok = service.login(req.username, req.password);
            Client c = service.findByUsername(req.username);
            if (!ok) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"message\":\"Wrong password\"}").build();
            }

       
           
            if (c == null) {
                // neočekivano: login prošao ali nema objekta
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(c).build();
            }
            return Response.ok(c).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Server error\"}").build();
        }
    }
    
    @PUT
    @Path("/approve/{username}")
    public Response approveClient(@PathParam("username") String username) {
        try {
            Client c = service.findByUsername(username);
            if (c == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"User not found\"}").build();
            }
            service.approveClient(username);
            return Response.ok("{\"message\":\"approved\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Server error\"}").build();
        }
    }
    
    @PUT
    @Path("/block/{username}")
    public Response blockClient(@PathParam("username") String username) {
        try {
            Client c = service.findByUsername(username);
            if (c == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"User not found\"}").build();
            }
            service.blockClient(username);
            return Response.ok("{\"message\":\"blocked\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Server error\"}").build();
        }
    }
    
    @DELETE
    @Path("/{username}")
    public Response deleteClient(@PathParam("username") String username) {
        try {
            Client c = service.findByUsername(username);
            if (c == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"message\":\"User not found\"}").build();
            }
            service.delete(username);
            return Response.ok("{\"message\":\"deleted\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"message\":\"Server error\"}").build();
        }
    }

}
