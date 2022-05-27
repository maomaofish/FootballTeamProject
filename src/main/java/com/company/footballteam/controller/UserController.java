package com.company.footballteam.controller;

import java.util.Date;
import java.nio.charset.StandardCharsets;

import com.company.footballteam.model.User;
import com.company.footballteam.responsestruct.ResponseToken;
import com.company.footballteam.responsestruct.ResponseUser;
import com.company.footballteam.service.UserService;
import com.company.footballteam.constants.SecurityConstants;

import javax.crypto.SecretKey;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
// import io.jsonwebtoken.SignatureAlgorithm;
// import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseUser> signUp(@RequestBody User user) {
        //String token = getJWTToken(user.getEmail());
        User resUser = userService.saveUser(user);
        ResponseUser response = new ResponseUser(resUser);
        return new ResponseEntity<ResponseUser>(response,HttpStatus.CREATED);

    }

    // @GetMapping("/user_info")
    // @ResponseBody
    // public ResponseEntity<Team> getAllTeamPlayers() {
    //     ResponseEntity<Team> entity = new ResponseEntity<Team>(userService.getAllTeamPlayersByLoginUser(),
    //             HttpStatus.OK);
    //     return entity;
    // }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<ResponseToken> loginGetToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null != authentication) {
			SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
			String jwt = Jwts.builder().setIssuer("Football Team").setSubject("JWT Token")
						.claim("username", authentication.getName())
					  //.claim("authorities", populateAuthorities(authentication.getAuthorities()))
                      .claim("authorities", SecurityConstants.DEFAULT_ROLE)
					  .setIssuedAt(new Date())
					.setExpiration(new Date((new Date()).getTime() + 3000000))
					.signWith(key).compact();
			//response.setHeader(SecurityConstants.JWT_HEADER, jwt);
            ResponseToken response = new ResponseToken(jwt);
            return new ResponseEntity<ResponseToken>(response, HttpStatus.OK);
		}
        ResponseToken response = new ResponseToken("Fail to get Token");
        return new ResponseEntity<ResponseToken>(response,HttpStatus.OK);
    }


    // private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
	// 	Set<String> authoritiesSet = new HashSet<>();
    //     for (GrantedAuthority authority : collection) {
    //     	authoritiesSet.add(authority.getAuthority());
    //     }
    //     return String.join(",", authoritiesSet);
	// }
    // private String getJWTToken(String userEmail) {

    //     String secretKey = "mySecretKey";
	// 	List<GrantedAuthority> grantedAuthorities = AuthorityUtils
	// 			.commaSeparatedStringToAuthorityList("ROLE_USER");
		
	// 	String token = Jwts
	// 			.builder()
	// 			.setId("softtekJWT")
	// 			.setSubject(userEmail)
	// 			.claim("authorities",
	// 					grantedAuthorities.stream()
	// 							.map(GrantedAuthority::getAuthority)
	// 							.collect(Collectors.toList()))
	// 			.setIssuedAt(new Date(System.currentTimeMillis()))
	// 			.setExpiration(new Date(System.currentTimeMillis() + 600000))
	// 			.signWith(SignatureAlgorithm.HS512,
	// 					secretKey.getBytes()).compact();

	// 	return "Bearer " + token;
    // }
}
