package net.etfbl.mdp.service.rest;


import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.Produces;

import net.etfbl.mdp.service.ClientService;
import net.etfbl.mdp.model.Client;

@Path("/clients")
public class ClientController {
	private ClientService service = new ClientService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Client> getAll(){
		return service.getAllClients();
	}
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response register(Client c) {
		boolean success = service.register(c);
		return success ? Response.ok().build() : Response.status(Response.Status.CONFLICT).build();
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response login(HashMap<String, String> credentials) {
		boolean ok = service.login(credentials.get("username"), credentials.get("password"));
		return ok ? Response.ok().build() : Response.status(Response.Status.UNAUTHORIZED).build();
	}

}
