package com.study.spring.library.security;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.acls.domain.ConsoleAuditLogger;
import org.springframework.security.acls.domain.DefaultPermissionGrantingStrategy;
import org.springframework.security.acls.domain.SpringCacheBasedAclCache;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Configuration
public class AclConfig {

  private static final String CACHE_NAME = "cacheName";

  @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
  @Autowired
  private DataSource dataSource;

  @Bean
  public CacheManager cacheManager() {
    Caffeine<Object, Object> caffeineCacheBuilder =
        Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterAccess(1, TimeUnit.MINUTES);

    CaffeineCacheManager cacheManager = new CaffeineCacheManager(CACHE_NAME);
    cacheManager.setCaffeine(caffeineCacheBuilder);
    return cacheManager;
  }

  @Bean
  public AclCache aclCache() {
    return new SpringCacheBasedAclCache(
        cacheManager().getCache(CACHE_NAME),
        permissionGrantingStrategy(),
        aclAuthorizationStrategy()
    );
  }

  @Bean
  public PermissionGrantingStrategy permissionGrantingStrategy() {
    return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
  }

  @Bean
  public AclAuthorizationStrategy aclAuthorizationStrategy() {
    return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_EDITOR"));
  }

  @Bean
  public MethodSecurityExpressionHandler defaultMethodSecurityExpressionHandler() {
    DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
    AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(aclService());
    expressionHandler.setPermissionEvaluator(permissionEvaluator);
    expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(aclService()));
    return expressionHandler;
  }

  @Bean
  public LookupStrategy lookupStrategy() {
    return new CustomLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), new ConsoleAuditLogger());
  }

  @Bean
  public JdbcMutableAclService aclService() {
    return new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
  }
}

