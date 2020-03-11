package com.android.prm.service.accountdto;

import com.android.prm.service.mapper.UserMapper;
import com.android.prm.service.model.request.AccountRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class AccountDTO implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        AccountRequest accountRequest = userMapper.loadUserByUsername(username);
        if (accountRequest == null) {
            throw new UsernameNotFoundException("Not found user: " + username);
        }
        return new User(accountRequest.getUsername(), accountRequest.getPassword(), emptyList());
    }

}
