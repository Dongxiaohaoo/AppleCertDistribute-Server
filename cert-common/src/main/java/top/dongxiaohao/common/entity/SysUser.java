package top.dongxiaohao.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Collection;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import top.dongxiaohao.common.enums.SysUserEnum;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Dongxiaohao
 * @since 2024-10-30
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SysUser implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 姓名
     */
    private String name;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 0-启用,1-禁用
     */
    private Integer disable;

    /**
     * 角色Id
     */
    private Integer roleId;

    private Date createdTime;

    private Integer createdBy;

    private Date deletedTime;

    private Integer deletedBy;

    private Integer isDeleted;

    private Date updatedTime;

    private Integer updatedBy;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return Objects.equals(this.disable, SysUserEnum.Status.ENABLE.getCode());
    }
}
