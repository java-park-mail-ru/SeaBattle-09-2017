package seabattle.jdbcdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import seabattle.dao.UserService;
import seabattle.queries.UserQueries;
import seabattle.views.UserView;

import java.util.List;


@Service
public class JdbcUserService extends JdbcDaoSupport implements UserService {

    private RowMapper<UserView> readUser = (resultSet, rowNumber) ->
            new UserView(resultSet.getString("email"),
                    resultSet.getString("login"),
                    resultSet.getString("password"),
                    resultSet.getInt("score"));

    private RowMapper<UserView> readUserLoginScore = (resultSet, rowNumber) -> {
        System.out.println(resultSet);
        return new UserView(null, resultSet.getString("login"),
                null, resultSet.getInt("score"));
    };



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
                readUser);
    }

    @Override
    public UserView changeUser(UserView user) {
        getJdbcTemplate().update(UserQueries.changeUser(),
                new Object[] {user.getEmail(), user.getPassword(), user.getLogin()});
        return user;
    }

    @Override
    public List<UserView> getLeaderboard() {
        return getJdbcTemplate().query(UserQueries.getLeaderboard(), readUserLoginScore);
    }
}
