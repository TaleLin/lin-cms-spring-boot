package io.github.talelin.latticy.mapper;

import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.model.UserGroupDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
@ActiveProfiles("test")
public class GroupMapperTest {

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserGroupMapper userGroupMapper;

    @Test
    public void selectGroupsByUserId() {
        String email = "13129982604@qq.com";
        String username = "pedro-test";
        UserDO user = new UserDO();
        user.setEmail(email);
        user.setUsername(username);
        userMapper.insert(user);

        GroupDO group = GroupDO.builder().name("group").info("零零落落").build();
        groupMapper.insert(group);

        userGroupMapper.insert(new UserGroupDO(user.getId(), group.getId()));

        List<GroupDO> groups = groupMapper.selectGroupsByUserId(user.getId());
        boolean anyMatch = groups.stream().anyMatch(it -> it.getName().equals("group"));
        assertTrue(anyMatch);
    }


    @Test
    public void selectUserGroupIDs() {
        String email = "13129982604@qq.com";
        String username = "pedro-test";
        UserDO user = new UserDO();
        user.setEmail(email);
        user.setUsername(username);
        userMapper.insert(user);

        GroupDO group = GroupDO.builder().name("group").info("零零落落").build();
        groupMapper.insert(group);

        userGroupMapper.insert(new UserGroupDO(user.getId(), group.getId()));

        List<Integer> groupIds = groupMapper.selectUserGroupIds(user.getId());
        boolean anyMatch = groupIds.stream().anyMatch(it -> it.equals(group.getId()));
        assertTrue(anyMatch);
    }


    @Test
    public void selectCountById() {
        GroupDO group = GroupDO.builder().name("group").info("零零落落").build();
        groupMapper.insert(group);
        int count = groupMapper.selectCountById(group.getId());
        assertTrue(count > 0);
    }

    @Test
    public void selectCountUserByUserIdAndGroupName() {
        String email = "13129982604@qq.com";
        String username = "pedro-test";
        UserDO user = new UserDO();
        user.setEmail(email);
        user.setUsername(username);
        userMapper.insert(user);

        GroupDO group = GroupDO.builder().name("group").info("零零落落").build();
        groupMapper.insert(group);

        userGroupMapper.insert(new UserGroupDO(user.getId(), group.getId()));

        int count = groupMapper.selectCountUserByUserIdAndGroupName(user.getId(), "group");
        assertTrue(count > 0);
    }
}