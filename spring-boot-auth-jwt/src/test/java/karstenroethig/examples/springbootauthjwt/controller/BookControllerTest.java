package karstenroethig.examples.springbootauthjwt.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import karstenroethig.examples.springbootauthjwt.model.AccessAndRefreshToken;
import karstenroethig.examples.springbootauthjwt.model.Login;

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

	@Test
	@DisplayName("Should be able to access private endpoint with auth and login")
	void shouldBeAbleToAccessPrivateEndpointWithAuthAndLogin() throws Exception
	{
		Login login = new Login();
		login.setUsername("admin");
		login.setPassword("password");

		String loginJson = new ObjectMapper().writeValueAsString(login);

		MvcResult loginResult = mvc.perform(
				post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(loginJson))
			.andExpect(status().isOk()).andReturn();

		String json = loginResult.getResponse().getContentAsString();
		AccessAndRefreshToken tokens = new ObjectMapper().readValue(json, AccessAndRefreshToken.class);

		MockHttpServletResponse response = mvc.perform(
				get("/books/private/admin")
				.header("Authorization", "Bearer " + tokens.getAccessToken()))
			.andExpect(status().isOk())
			.andReturn().getResponse();

		assertThat(response.getContentAsString()).isNotEmpty();
	}
}
