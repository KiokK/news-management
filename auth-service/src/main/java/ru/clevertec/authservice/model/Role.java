package ru.clevertec.authservice.model;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.List;

@Getter
public enum Role {

    ADMIN(Arrays.stream(AuthorityRights.values())
            .map(AuthorityRights::getGrantedAuthority)
            .toList()
    ),
    WRITER(List.of(
            AuthorityRights.NEWS_WRITE.getGrantedAuthority(),
            AuthorityRights.NEWS_MODIFY.getGrantedAuthority(),
            AuthorityRights.NEWS_DELETE.getGrantedAuthority()
    )),
    SUBSCRIBER(List.of(
            AuthorityRights.COMMENTS_WRITE.getGrantedAuthority(),
            AuthorityRights.COMMENTS_MODIFY.getGrantedAuthority(),
            AuthorityRights.COMMENTS_DELETE.getGrantedAuthority()
    ));

    private final List<GrantedAuthority> authorities;

    Role(List<GrantedAuthority> grantedAuthorities) {
        this.authorities = grantedAuthorities;
    }
}