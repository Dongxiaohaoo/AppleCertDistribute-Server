package top.dongxiaohao.common;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Dongxiaohao
 * @Date: 2024/10/30 22:53
 */
@Component
public class JwtTokenUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final String CLAIM_KEY_SUBJECT = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;



    /**
     * 根据负责生成JWT的token
     */
    private String generateToken(Map<String, Object> claims, Long expirationSeconds) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate(expirationSeconds))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 从token中获取JWT中的负载
     */
    public Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LOGGER.info("JWT格式验证失败:{}", token);
            throw new RuntimeException("JWT格式验证失败");
        }
        return claims;
    }

    /**
     * 生成token的过期时间
     */
    private Date generateExpirationDate(Long expirationSeconds) {
        return new Date(System.currentTimeMillis() + expirationSeconds * 1000);
    }


    /**
     * 从token中获取用户id
     */
    public String getSubjectFromToken(String token) {
        String userId;
        try {
            Claims claims = getClaimsFromToken(token);
            userId = claims.getSubject();
        } catch (Exception e) {
            userId = null;
        }
        return userId;
    }

    /**
     * 验证token是否还有效
     *
     * @param token   客户端传入的token
     * @param subject 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, String subject) {
        String userId = getSubjectFromToken(token);
        return userId.equals(subject);
    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    public Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 根据用户信息生成token
     */
    public String generateToken(Integer userId) {
        return generateToken(userId, expiration);
    }

    public String generateToken(Integer subject, Long expirationSeconds) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_SUBJECT, subject);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims, expirationSeconds);
    }

    /**
     * 创建令牌
     * 从数据声明生成令牌
     *
     * @param subject 主题
     * @return 令牌
     */
    public String createToken(Integer subject) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put(CLAIM_KEY_SUBJECT, subject);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(null)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * 创建令牌
     * 从数据声明生成令牌
     *
     * @param uuid uuid
     * @return 令牌
     */
    public String createToken(String uuid) {
        Map<String, Object> claims = new HashMap<>(4);
        claims.put(CLAIM_KEY_SUBJECT, uuid);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(null)
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }


    /**
     * 判断token是否可以被刷新
     */
    public boolean canRefresh(String token) {
        return !isTokenExpired(token);
    }


    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        Claims claims = getClaimsFromToken(token);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims, expiration);
    }


    /**
     * 获取请求token
     *
     * @param request 请求
     * @return {@link String}
     */
    public String getToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        if (StrUtil.isNotBlank(token) && token.startsWith(tokenHead)) {
            token = token.replace(tokenHead, "");
            return token;
        }
        return null;
    }
}