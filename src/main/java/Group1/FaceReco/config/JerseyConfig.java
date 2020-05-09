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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig{

	// Ajout des classes de services et de la documentation
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

		String scheme = this.getPropertiesValues("scheme");
		String host = this.getPropertiesValues("host");
		String res = this.getPropertiesValues("resourcePackage");

		BeanConfig bean = new BeanConfig();
		bean.setTitle("Face Reco API ");
		bean.setSchemes(new String[]{scheme});
		bean.setHost(host);
		bean.setBasePath("/api");
		bean.setResourcePackage(res);
		bean.setPrettyPrint(true);
		bean.setScan(true);
	}

	private String getPropertiesValues(String property) {
		Properties properties = new Properties();
		String file = "application.properties";
		String ret = "";

		try {
			InputStream stream = getClass().getClassLoader().getResourceAsStream(file);
			if (stream != null) {
				properties.load(stream);
				ret = properties.getProperty(property);
			}
			stream.close();
		}
		catch (IOException e) {

		}
		return ret;
	}
}
