package Group1.FaceReco;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import Group1.FaceReco.service.PromotionService;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PromotionResourceIntegrationTest extends JerseyTest {
	
	@Override
    protected Application configure() {
        return new ResourceConfig(PromotionService.class);
    }
	
	@Test
	public void givenGetHiGreeting_whenCorrectRequest_thenResponseIsOkAndContainsHi() {
		System.out.println("******************************************************");
	    Response response = target("/promotion").request().get();
	 
	    Assert.assertEquals("Http Response should be 200: ", Status.OK.getStatusCode(), response.getStatus());
	    Assert.assertEquals("Http Content-Type should be: ", MediaType.APPLICATION_JSON, response.getHeaderString(HttpHeaders.CONTENT_TYPE));
	 
	    String content = response.readEntity(String.class);
	    Assert.assertEquals("Content of ressponse is: ", "hi", content);
	}
}
