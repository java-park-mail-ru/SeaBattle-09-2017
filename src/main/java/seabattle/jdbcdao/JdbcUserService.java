package seabattle.jdbcdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import seabattle.dao.UserService;
import seabattle.queries.UserQueries;
import seabattle.views.UserView;

@Service
public class JdbcUserService extends JdbcDaoSupport implements UserService {

    @Autowired
    public JdbcUserService(JdbcTemplate template) {
        setJdbcTemplate(template);
    }

    @Override
    public void addUser(UserView user) {
        getJdbcTemplate().update(UserQueries.addUser(),
                new Object[] {user.getEmail(), user.getLogin(), user.getPassword()});
    }

    @Override
    public UserView getByLoginOrEmail(String loginOrEmail) {
        return getJdbcTemplate().queryForObject(UserQueries.getByLoginOrEmail(),
                new Object[] {loginOrEmail, loginOrEmail},
                new BeanPropertyRowMapper<>(UserView.class));
    }

    @Override
    public UserView changeUser(UserView user) {
        getJdbcTemplate().update(UserQueries.changeUser(),
                new Object[] {user.getEmail(), user.getPassword()});
        return user;
    }
}
