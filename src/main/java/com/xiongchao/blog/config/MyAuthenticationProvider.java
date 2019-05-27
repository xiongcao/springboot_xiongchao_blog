package com.xiongchao.blog.config;

import org.springframework.stereotype.Component;

@Component
public class MyAuthenticationProvider {
//    public class MyAuthenticationProvider implements AuthenticationProvider {

//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    private final Logger loggerAdmin = LoggerFactory.getLogger("ADMIN");
//
//    @Autowired
//    private AdminCacheService adminCacheService;
//
//    @Autowired
//    private RoleCacheService roleCacheService;
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        // 获取用户登录时输入的用户名
//        String username = authentication.getName();
//        // 根据用户名查询系统中的用户信息
//        Admin admin = adminCacheService.findByUsername(username);
//        // 如果用户列表为 null，说明查找用户功能出现异常，抛出 AuthenticationServiceException
//        if (null == admin) {
//            logger.warn("管理员:{} 登录失败:用户名错误", username);
//            throw new RuntimeException("用户名错误");
//        }
//
//        // 锁定状态
//        if (Admin.LOCKED_BY_PASSWORD.equals(admin.getStatus())) {
//            if (new Date().after(DateUtils.addHours(admin.getLockedDate(), 24))) {//超过24小时自动解锁
//                admin.setStatus(Admin.NORMAL_STATUS);
//                admin.setPasswordAttemptCount(0);
//                admin.setLockedDate(null);
//                adminCacheService.save(admin);
//            } else {
//                logger.warn("管理员:{} 登录 登录失败:用户密码尝试次数过多 ", username);
//                throw new RuntimeException("用户密码尝试次数过多,请24小时后再尝试,或找领导解锁");
//            }
//        }
//        if (Admin.LOCKED_BY_LEADER.equals(admin.getStatus())) {
//            logger.warn("管理员:{} 登录 登录失败:用户已被上级锁定", username);
//            throw new RuntimeException("用户已被上级锁定");
//        }
//
//        // 密码对比
//        String password = (String) authentication.getCredentials();
//        if (!admin.passwordMatches(password)) {
//            admin.setPasswordAttemptCount(admin.getPasswordAttemptCount() + 1);
//            if (admin.getPasswordAttemptCount() > Admin.PASSWORD_ATTEMPT_MAX_COUNT) {//密码尝试超过上限
//                admin.setStatus(Admin.LOCKED_BY_PASSWORD);
//                admin.setLockedDate(new Date());
//                adminCacheService.save(admin);
//                logger.warn("管理员:{} 登录 登录失败:用户密码尝试次数过多", username);
//                throw new RuntimeException("用户密码尝试次数过多,请24小时后再尝试,或找领导解锁");
//            }
//
//            adminCacheService.save(admin);
//            logger.warn("管理员:{} 登录失败:密码错误", username);
//            throw new RuntimeException("密码错误");
//        }
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        List<Role> roles = roleCacheService.findRolesByAdminId(admin.getId());
//        for (Role role : roles) {
//            authorities.add(new SimpleGrantedAuthority(role.getName()));
//        }
//
//        return new UsernamePasswordAuthenticationToken(authentication, authentication.getCredentials(), authorities);
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return true;
//    }
}
