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

    private static final RowMapper<UserView> READ_USER_MAPPER = (resultSet, rowNumber) ->
            new UserView(resultSet.getString("email"),
                    resultSet.getString("login"),
                    resultSet.getString("password"),
                    resultSet.getInt("score"));

    private static final RowMapper<UserView> READ_USER_LOGIN_SCORE_MAPPER = (resultSet, rowNumber) -> {
        System.out.println(resultSet);
        return new UserView(null, resultSet.getString("login"),
                null, resultSet.getInt("score"));
    };



    @Autowired
    public JdbcUserService(JdbcTemplate template) {
        setJdbcTemplate(template);
    }

    @Override
    @SuppressWarnings("all")
    public void addUser(UserView user) {
        getJdbcTemplate().update(UserQueries.addUser(),
                new Object[] {user.getEmail(), user.getLogin(), user.getPassword()});
    }

    @Override
    @SuppressWarnings("all")
    public UserView getByLoginOrEmail(String loginOrEmail) {
        return getJdbcTemplate().queryForObject(UserQueries.getByLoginOrEmail(),
                new Object[]{loginOrEmail, loginOrEmail},
                READ_USER_MAPPER);
    }

    @Override
    @SuppressWarnings("all")
    public UserView changeUser(UserView user) {
        if (getJdbcTemplate().update(UserQueries.changeUser(),
                new Object[] {user.getEmail(), user.getPassword(), user.getLogin()}) != 0) {
            return user;
        }
        return null;
    }

    @Override
    @SuppressWarnings("all")
    public List<UserView> getLeaderboard() {
        return getJdbcTemplate().query(UserQueries.getLeaderboard(), READ_USER_LOGIN_SCORE_MAPPER);
    }
}
