package ru.clevertec.news.model.util;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public enum AuthorityRights {

    ADMIN(new SimpleGrantedAuthority("admin")),
    NEWS_WRITE(new SimpleGrantedAuthority("news:create")),
    NEWS_MODIFY(new SimpleGrantedAuthority("news:modify")),
    NEWS_DELETE(new SimpleGrantedAuthority("news:delete")),
    COMMENTS_WRITE(new SimpleGrantedAuthority("comments:create")),
    COMMENTS_MODIFY(new SimpleGrantedAuthority("comments:modify")),
    COMMENTS_DELETE(new SimpleGrantedAuthority("comments:delete"));

    private final GrantedAuthority grantedAuthority;

    AuthorityRights(GrantedAuthority grantedAuthority) {
        this.grantedAuthority = grantedAuthority;
    }
}
