package Group1.FaceReco.config;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import Group1.FaceReco.service.AccountService;
import Group1.FaceReco.service.GroupService;
import Group1.FaceReco.service.PromotionService;
import Group1.FaceReco.service.RightService;
import Group1.FaceReco.service.RoleService;
import Group1.FaceReco.service.StudentService;
import Group1.FaceReco.service.TimesheetService;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig{

	public JerseyConfig() {
		register(StudentService.class);
		register(PromotionService.class);
		register(GroupService.class);
		register(RoleService.class);
		register(RightService.class);
		register(AccountService.class);
		register(TimesheetService.class);
		this.configureSwagger();
	}

	private void configureSwagger () {
		this.register(ApiListingResource.class);
		this.register(SwaggerSerializers.class);

		BeanConfig bean = new BeanConfig();
		bean.setTitle("Face Reco API ");
		bean.setSchemes(new String[]{"http"});
		bean.setHost("localhost:8080");
		bean.setBasePath("/api");
		bean.setResourcePackage("Group1.FaceReco");
		bean.setPrettyPrint(true);
		bean.setScan(true);
	}
}
