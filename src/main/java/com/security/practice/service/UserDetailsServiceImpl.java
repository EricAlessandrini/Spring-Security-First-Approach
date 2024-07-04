package com.security.practice.service;

import com.security.practice.domain.dto.AuthenticationLoginRequest;
import com.security.practice.domain.dto.AuthenticationResponse;
import com.security.practice.domain.dto.AuthenticationSignUpRequest;
import com.security.practice.domain.entity.RoleEntity;
import com.security.practice.domain.entity.UserEntity;
import com.security.practice.persistence.repository.RoleRepository;
import com.security.practice.persistence.repository.UserRepository;
import com.security.practice.utils.JWTUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " doesn't exist"));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles()
                .forEach(role -> authorities.add(
                        new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));

        /*Esta linea es mas para manejar los permisos dentro de los roles pero me parece que si la seguridad esta bien configurada
        * con roles es mas que suficiente para tener una buena seguridad en los endpoints */
        user.getRoles().stream()
                .flatMap(roleEntity -> roleEntity.getPermissionsList().stream())
                .forEach(permissionEntity -> authorities.add(new SimpleGrantedAuthority(permissionEntity.getName())));

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNoExpired(),
                user.isAccountNoLocked(),
                user.isCredentialNoExpired(),
                authorities
        );
    }

    public AuthenticationResponse loginUser(AuthenticationLoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Authentication authentication = this.authenticate(username, password);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        String jwtToken = jwtUtils.createJwtToken(authentication);

        return AuthenticationResponse.builder()
                .username(authentication.getPrincipal().toString())
                .message("User Authenticated Successfully")
                .jwtToken(jwtToken)
                .status(true)
                .build();
    }

    public AuthenticationResponse signUpUser(AuthenticationSignUpRequest signUpRequest) {
        String username = signUpRequest.getUsername();
        String password = signUpRequest.getPassword();

        // Esta parte de la logica tiene que ser sacada a otro metodo...
        Set<RoleEntity> listRoles = new HashSet<>();
        for(String roleName : signUpRequest.getRoles()) {
            RoleEntity role = roleRepository.findRoleEntityByRoleName(roleName);
            if(role == null) {
                throw new EntityNotFoundException("The role doesn't exist");
            } else {
                listRoles.add(role);
            }
        }

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .roles(listRoles)
                .build();

        user = userRepository.save(user);

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole()))));
        user.getRoles().stream().flatMap(role -> role.getPermissionsList().stream())
                .forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission.getName())));

        String token = jwtUtils.createJwtToken(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities));

        return AuthenticationResponse.builder()
                .username(user.getUsername())
                .message("User created successfully")
                .jwtToken(token)
                .status(true)
                .build();
    }

    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if(userDetails == null) {
            throw new BadCredentialsException("Username invalid or non-existent");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

}
