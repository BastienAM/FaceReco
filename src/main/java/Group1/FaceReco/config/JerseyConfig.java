package Group1.FaceReco.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import Group1.FaceReco.service.GroupService;
import Group1.FaceReco.service.PromotionService;
import Group1.FaceReco.service.StudentService;

@Configuration
public class JerseyConfig extends ResourceConfig{

	public JerseyConfig() {
		register(StudentService.class);
		register(PromotionService.class);
		register(GroupService.class);
	}
	
}
