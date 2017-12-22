package seabattle.authorization.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import seabattle.authorization.views.LeaderboardView;
import seabattle.authorization.views.UserView;

import java.util.List;


@SuppressWarnings("SqlNoDataSourceInspection")
@Service
public class JdbcUserService implements UserService {

    private static final RowMapper<UserView> READ_USER_MAPPER = (resultSet, rowNumber) ->
            new UserView(resultSet.getString("email"),
                    resultSet.getString("login"),
                    resultSet.getString("password"),
                    resultSet.getInt("score"));

    private static final RowMapper<LeaderboardView> READ_USER_LOGIN_SCORE_MAPPER = (resultSet, rowNumber) ->
            new LeaderboardView(null, resultSet.getString("login"),
                    resultSet.getInt("score"));

    private static final RowMapper<Integer> READ_POSITION_MAPPER = (resultSet, rowNumber) ->
            resultSet.getInt("position");

    private JdbcTemplate template;

    public JdbcUserService(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void addUser(UserView user) {
        String sql = "INSERT INTO users (email, login, password) VALUES (?, ?, ?)";
        template.update(sql, user.getEmail(), user.getLogin(), user.getPassword());
    }

    @Override
        public UserView getByLoginOrEmail(String loginOrEmail) {
        String sql = "SELECT DISTINCT email, login, password, score FROM users WHERE email = ? OR login = ?";
        return template.queryForObject(sql, READ_USER_MAPPER, loginOrEmail, loginOrEmail);
    }

    @Override
    public UserView changeUser(UserView user) {
        String sql = "UPDATE users SET (email, password) = (?, ?) WHERE login = ?";
        if (template.update(sql, user.getEmail(), user.getPassword(), user.getLogin()) != 0) {
            return user;
        }
        return null;
    }

    @Override
    public List<LeaderboardView> getLeaderboard(Integer limit) {
        String sql = "SELECT login, score FROM users ORDER BY score DESC LIMIT ?";
        return template.query(sql, ps -> ps.setInt(1, limit), READ_USER_LOGIN_SCORE_MAPPER);
    }

    @Override
    public UserView setScore(UserView user) {
        String sql = "UPDATE users SET score = ? WHERE login = ?";
        if (template.update(sql, user.getScore(), user.getLogin()) != 0) {
            return user;
        }
        return null;
    }

    @Override
    public Integer getPosition(UserView user) {
        String sql = "SELECT count(*) as position FROM users WHERE score >= ? AND login < ?";
        return template.query(sql, ps -> { ps.setInt(1, user.getScore());
                ps.setString(2, user.getLogin()); },
                READ_POSITION_MAPPER).get(0) + 1;
    }
}
