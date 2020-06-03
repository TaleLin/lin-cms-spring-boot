package io.github.talelin.latticy.service.impl;

import io.github.talelin.core.util.EncryptUtil;
import io.github.talelin.latticy.common.constant.IdentityConstant;
import io.github.talelin.latticy.model.UserIdentityDO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@Slf4j
@ActiveProfiles("test")
public class UserIdentityServiceImplTest {

    @Autowired
    private UserIdentityServiceImpl userIdentityService;

    public UserIdentityDO setUp1() {
        UserIdentityDO userIdentity = new UserIdentityDO();
        userIdentity.setUserId(1);
        userIdentity.setIdentityType(IdentityConstant.USERNAME_PASSWORD_IDENTITY);
        userIdentity.setIdentifier("pedro");
        userIdentity.setCredential(EncryptUtil.encrypt("123456"));
        return userIdentity;
    }

    public UserIdentityDO setUp2(Integer userId, String identityType, String identifier, String credential) {
        UserIdentityDO userIdentity = new UserIdentityDO();
        userIdentity.setUserId(userId);
        userIdentity.setIdentityType(identityType);
        userIdentity.setIdentifier(identifier);
        userIdentity.setCredential(EncryptUtil.encrypt(credential));
        return userIdentity;
    }

    @Test
    public void createIdentity() {
        UserIdentityDO userIdentity = userIdentityService.createIdentity(
                1,
                IdentityConstant.USERNAME_PASSWORD_IDENTITY,
                "pedro",
                EncryptUtil.encrypt("123456")
        );
        assertNotNull(userIdentity.getId());
        assertTrue(EncryptUtil.verify(userIdentity.getCredential(), "123456"));
    }

    @Test
    public void createIdentity1() {
        UserIdentityDO userIdentity = setUp1();
        userIdentityService.createIdentity(userIdentity);
        assertNotNull(userIdentity.getId());
        assertTrue(EncryptUtil.verify(userIdentity.getCredential(), "123456"));
    }

    @Test
    public void createUsernamePasswordIdentity() {
        UserIdentityDO userIdentity = userIdentityService.createUsernamePasswordIdentity(
                1,
                "pedro",
                "123456");
        assertNotNull(userIdentity.getId());
        assertTrue(EncryptUtil.verify(userIdentity.getCredential(), "123456"));
    }

    @Test
    public void verifyUsernamePassword() {
        UserIdentityDO userIdentity = setUp1();
        userIdentityService.createIdentity(userIdentity);

        boolean valid = userIdentityService.verifyUsernamePassword(userIdentity.getUserId(), "pedro", "123456");
        assertTrue(valid);
    }

    @Test
    public void changePassword() {
        UserIdentityDO userIdentity = setUp1();
        userIdentityService.createIdentity(userIdentity);

        boolean b = userIdentityService.changePassword(userIdentity.getUserId(), "147258");
        assertTrue(b);

        boolean valid = userIdentityService.verifyUsernamePassword(userIdentity.getUserId(), "pedro", "147258");
        assertTrue(valid);
    }

    @Test
    public void changeUsername() {
        UserIdentityDO userIdentity = setUp1();
        userIdentityService.createIdentity(userIdentity);

        boolean b = userIdentityService.changeUsername(userIdentity.getUserId(), "pedro1");
        assertTrue(b);

        boolean valid = userIdentityService.verifyUsernamePassword(userIdentity.getUserId(), "pedro1", "123456");
        assertTrue(valid);
    }
}