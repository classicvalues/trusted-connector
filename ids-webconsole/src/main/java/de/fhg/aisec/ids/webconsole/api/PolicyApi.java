/*-
 * ========================LICENSE_START=================================
 * IDS Core Platform Webconsole
 * %%
 * Copyright (C) 2017 Fraunhofer AISEC
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */
package de.fhg.aisec.ids.webconsole.api;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.GsonBuilder;

import de.fhg.aisec.ids.api.policy.PAP;
import de.fhg.aisec.ids.webconsole.WebConsoleComponent;

/**
 * REST API interface for managing "apps" in the connector.
 * 
 * In this implementation, apps are either docker or trustX containers. 
 * 
 * The API will be available at http://localhost:8181/cxf/api/v1/policies/<method>.
 * 
 * @author Julian Schuette (julian.schuette@aisec.fraunhofer.de)
 *
 */
@Path("/policies")
public class PolicyApi {
	private static final Logger LOG = LoggerFactory.getLogger(PolicyApi.class);
	
	@GET
	@Path("list")
	@Produces("application/json")
	public String list() {
		LOG.info("policy list");
		List<String> result = new ArrayList<>();
		
		Optional<PAP> pap = WebConsoleComponent.getPolicyAdministrationPoint();
		if (!pap.isPresent()) {
			return "[]";
		}		
		result = pap.get().listRules();
		return new GsonBuilder().create().toJson(result);
	}
	
	@POST
	@OPTIONS
	@GET
	@Path("install")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public String install(@Multipart(value="policy_file", required=true) InputStream is) {
		LOG.info("Received policy file");
		Optional<PAP> pap = WebConsoleComponent.getPolicyAdministrationPoint();
		
		// if pap service is not available at runtime, return error TODO return proper HTTP error code
		if (!pap.isPresent()) {
			return "no PAP";
		}
		pap.get().loadPolicy(is);
		return new GsonBuilder().create().toJson("OK");
	}	
}
