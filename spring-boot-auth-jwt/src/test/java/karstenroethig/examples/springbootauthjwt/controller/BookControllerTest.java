package karstenroethig.examples.springbootauthjwt.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class BookControllerTest
{
	private @Autowired MockMvc mvc;

	@Test
	@DisplayName("Should be able to access public endpoint without auth")
	public void shouldBeAbleToAccessPublicEndpointWithoutAuth() throws Exception
	{
		MockHttpServletResponse response = mvc.perform(get("/books/public"))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		assertThat(response.getContentAsString()).isNotEmpty();
	}

	@Test
	@DisplayName("Should get forbidden on private endpoint without auth")
	public void shouldGetForbiddenOnPrivateEndpointWithoutAuth() throws Exception
	{
		mvc.perform(get("/books/private/user"))
			.andExpect(status().isForbidden())
			.andReturn();
	}

	@Test
	@DisplayName("Should be able to access private endpoint with auth")
	@WithMockUser(username = "user")
	void shouldBeAbleToAccessPrivateEndpointWithAuth() throws Exception
	{
		MockHttpServletResponse response = mvc.perform(get("/books/private/user"))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		assertThat(response.getContentAsString()).isNotEmpty();
	}
}
