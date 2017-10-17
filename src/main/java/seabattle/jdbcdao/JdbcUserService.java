package seabattle.jdbcdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import seabattle.dao.UserService;
import seabattle.queries.UserQueries;
import seabattle.views.UserView;

import java.sql.ResultSet;
import java.sql.SQLException;

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
                new Object[]{loginOrEmail, loginOrEmail},
                new RowMapper<UserView>() {
                    @Nullable
                    @Override
                    public UserView mapRow(ResultSet resultSet, int numberOfRows) throws SQLException {
                        return new UserView(resultSet.getString("email"),
                                resultSet.getString("login"),
                                resultSet.getString("password"),
                                resultSet.getInt("score"));
                    }
                });
    }

    @Override
    public UserView changeUser(UserView user) {
        getJdbcTemplate().update(UserQueries.changeUser(),
                new Object[] {user.getEmail(), user.getPassword(), user.getLogin()});
        return user;
    }
}
