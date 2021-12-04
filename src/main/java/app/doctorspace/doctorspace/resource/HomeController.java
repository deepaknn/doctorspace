package app.doctorspace.doctorspace.resource;

import app.doctorspace.doctorspace.entity.AuthenticationRequest;
import app.doctorspace.doctorspace.entity.AuthenticationResponse;
import app.doctorspace.doctorspace.service.AppUserDetailsService;
import app.doctorspace.doctorspace.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AppUserDetailsService appUserDetailsService;

    @GetMapping("/")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok("Welcome to DoctorSpace");
    }

    @PostMapping("/authenticate/token")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = appUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtUtil.generateToken(userDetails, 1000 * 60 * 60 * 24);
        final String refreshToken = jwtUtil.generateToken(userDetails, 1000 * 60 * 60 * 24 * 24);
        return ResponseEntity.ok(new AuthenticationResponse(token, refreshToken));
    }

    @PostMapping("/authenticate/refreshToken")
    public ResponseEntity<AuthenticationResponse> refreshAuthentication(@RequestBody AuthenticationResponse authenticationResponse) throws Exception {
        final UserDetails userDetails = appUserDetailsService.loadUserByUsername(
                jwtUtil.extractUsername(authenticationResponse.getRefreshToken()));

        if (jwtUtil.validateToken(authenticationResponse.getRefreshToken(), userDetails)) {
            return ResponseEntity.ok(new AuthenticationResponse(jwtUtil.generateToken(
                    userDetails, 1000 * 60 * 60 * 24), authenticationResponse.getRefreshToken()));
        } else {
            throw new BadCredentialsException("Refresh token is invalid or expired");
        }
    }
}
