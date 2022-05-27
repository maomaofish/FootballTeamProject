package com.company.footballteam.service.impl;

import javax.transaction.Transactional;
import com.company.footballteam.exception.PreconditionNotSatified;
import com.company.footballteam.exception.ResourceNotFoundException;
import com.company.footballteam.model.Team;
import com.company.footballteam.model.User;
import com.company.footballteam.repository.TeamRepository;
import com.company.footballteam.repository.UserRepository;
import com.company.footballteam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.company.footballteam.constants.SecurityConstants;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamServiceImpl teamService;

    @Autowired
    private TeamRepository teamRepository;

    private static int TEAMVALUE = 20000000;
    private static int BUDGET = 5000000;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
    public UserServiceImpl(UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    private String getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    @Override
    @Transactional
    public User saveUser(User user) {

        if (user.getEmail().length() == 0 || !user.getEmail().contains("@")){
            throw new PreconditionNotSatified("new", "User email is empty or invalid format");
        }
        String pass = passwordEncoder.encode(user.getPassword());
        user.setPassword(pass);
        /*user.setPassword(
                "{bcrypt}" + passwordEncoder.encode(user.getPassword()));*/// encription
        Team newteam = new Team();

        newteam.setUserId(user.getId());//lina
        newteam.setCountry("Country");
        newteam.setName("Team Name");
        newteam.setTeamValue(TEAMVALUE);
        newteam.setBudget(BUDGET);

        Team team = teamService.saveTeam(newteam);

        user.setTeam(team);//lina new

        //user.setTeamId(team.getId());
        user.setRoleName(SecurityConstants.DEFAULT_ROLE);
        return userRepository.save(user);
    }

    @Override
    public Team getAllTeamPlayersByLoginUser() {

        String userEmail = this.getCurrentUser();
        User user = userRepository.findByEmail(userEmail);
        
        Team team = teamRepository.getbyUserId(user.getId());//lina

        Team resTeam =  teamRepository.findById(team.getId()).orElseThrow(() -> new ResourceNotFoundException("Team", "id", team.getId()));
        return resTeam;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        user.setPassword("{bcrypt}" + user.getPassword());
        return new MyUserDetails(user);
    }
}
