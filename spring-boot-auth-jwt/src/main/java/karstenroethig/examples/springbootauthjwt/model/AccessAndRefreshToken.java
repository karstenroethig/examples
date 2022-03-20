package karstenroethig.examples.springbootauthjwt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessAndRefreshToken
{
	private String accessToken;
	private String refreshToken;
}
